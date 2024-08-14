package io.github.jwdeveloper.tiktok.server.schedule;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.IpUtil;
import io.github.jwdeveloper.tiktok.server.data.LiveClientConnect;
import io.github.jwdeveloper.tiktok.server.service.LiveClientService;
import io.github.jwdeveloper.tiktok.server.service.LiveRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Slf4j
@AllArgsConstructor
@Component
public class JobHandle {

    private final LiveClientService liveClientService;
    private final LiveRoomService liveRoomService;

    @XxlJob("checkLiveClient")
    public void checkLiveClient() {
        log.info("Checking clients");
        liveClientService.getClientConnectList(null).stream()
                .filter(x -> IpUtil.getIp().equals(x.getServerIp()))
                .filter(x -> !x.isDeleted())
                .forEach(this::createClientConnect);
    }

    @XxlJob("checkLiveClientFast")
    public void checkLiveClientFast() {
        String param = XxlJobHelper.getJobParam();
        log.info("Checking clients fast: {}", param);
        Arrays.asList(param.split(",")).forEach(hostName -> {
            var x = liveClientService.getClientConnect(hostName);
            if (x != null && IpUtil.getIp().equals(x.getServerIp()) && !x.isDeleted()) {
                createClientConnect(x);
            }
        });
    }

    private void createClientConnect(LiveClientConnect x) {
        try {
            var liveUserData = liveClientService.getLiveUserData(x.getHostName());
            if (liveUserData != null && liveUserData.isLiveOnline()) {
                log.info("hostName:{} 正在直播中", x.getHostName());
                var liveData = liveClientService.getLiveData(liveUserData.getRoomId());
                liveRoomService.liveUpdateByRoomId(liveData, liveUserData.getRoomId());
                liveClientService.createClientConnect(x.getHostName(), liveUserData.getRoomId());
            } else if (liveUserData != null && !liveUserData.isHostNameValid()) {
                liveClientService.setHostNameValid(x.getHostName(), liveUserData.getUserStatus().name());
            } else {
                log.info("hostName:{} 不在直播中", x.getHostName());
                liveClientService.disconnect(x.getHostName());
            }
        } catch (Exception e) {
            log.error("开播监控启动失败 hostName:{}", x.getHostName(), e);
        }
    }
}
