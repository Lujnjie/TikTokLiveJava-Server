package io.github.lulajax.tiktok.server.data.pk;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Builder;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name="pk_round_scores")
public class PkRoundScores implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "round_id",nullable = false)
    private Integer roundId;

    private String roomId;

    @Column(name = "player")
    private String player;

    @Column(name = "gift_id")
    private Integer giftId;

    @Column(name = "gift_name")
    private String giftName;

    @Column(name = "diamond_cost")
    private Integer diamondCost;

    @Column(name = "gift_picture_link")
    private String giftPictureLink;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_profile_name")
    private String userProfileName;

    @Column(name = "user_picture_link")
    private String userPictureLink;

    @Column(name = "combo")
    private Integer combo;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "time_stamp")
    private Long timeStamp;

    public void copy(PkRoundScores source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}