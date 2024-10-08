package io.github.lulajax.tiktok.server.data;

import com.xxl.job.core.util.IpUtil;
import io.github.jwdeveloper.tiktok.data.requests.LiveUserData;
import io.github.jwdeveloper.tiktok.live.LiveRoomInfo;
import io.github.jwdeveloper.tiktok.models.ConnectionState;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "live_client_connect", indexes = {
        @Index(name = "idx_live_client_connect_roomId", columnList = "roomId"),
        @Index(name = "idx_live_client_connect_hostId", columnList = "hostId"),
        @Index(name = "idx_live_client_connect_hostName", columnList = "hostName")
})
public class LiveClientConnect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private int likesCount;
    private int viewersCount;
    private int totalViewersCount;
    private long startTime;
    private boolean ageRestricted;
    private Long hostId;
    private String hostName;
    private String hostProfileName;
   
    @Column(name = "host_picture_link")
    private String hostPictureLink;

    private long hostFollowing;
    private long hostFollowers;

    private String title;
    private String language;
    private String connectionState;
    private String status;

    private String serverIp;

    private boolean deleted;

    /**
     * 开播监控优先级，1：重点监控 1分钟扫描一次，2：普通监控 5分钟扫描一次，3：PK 监控 1分钟扫描一次
     */
    private Integer priorityMonitor = 2;


    public LiveClientConnect buildFrom(LiveRoomInfo roomInfo) {
        this.roomId = roomInfo.getRoomId();
        this.likesCount = roomInfo.getLikesCount();
        this.viewersCount = roomInfo.getViewersCount();
        this.totalViewersCount = roomInfo.getTotalViewersCount();
        this.startTime = roomInfo.getStartTime();
        this.ageRestricted = roomInfo.isAgeRestricted();
        this.hostId = roomInfo.getHost().getId();
        this.hostName = roomInfo.getHost().getName();
        this.hostProfileName = roomInfo.getHost().getProfileName();
        this.hostPictureLink = roomInfo.getHost().getPicture().getLink();
        this.hostFollowing = roomInfo.getHost().getFollowing();
        this.hostFollowers = roomInfo.getHost().getFollowers();
        this.title = roomInfo.getTitle();
        this.connectionState = roomInfo.getConnectionState().name();
        this.serverIp = IpUtil.getIp();
        return this;
    }

    public LiveClientConnect buildFrom(LiveUserData.Response liveUserData) {
        this.roomId = liveUserData.getRoomId();
        this.startTime = liveUserData.getStartTime();
        this.hostId = liveUserData.getUser().getId();
        this.hostName = liveUserData.getUser().getName();
        this.hostProfileName = liveUserData.getUser().getProfileName();
        this.hostPictureLink = liveUserData.getUser().getPicture().getLink();
        this.hostFollowing = liveUserData.getUser().getFollowing();
        this.hostFollowers = liveUserData.getUser().getFollowers();
        this.connectionState =  liveUserData.isLiveOnline() ? ConnectionState.CONNECTED.name() : ConnectionState.DISCONNECTED.name();
        this.serverIp = IpUtil.getIp();
        return this;
    }
}
