package com.loser.backend.club.kafka.message;


import com.loser.backend.club.kafka.publisher.AbstractProducerModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * @author ~~ trading.s
 * @date 15:01 01/08/22
 */
@Getter @Setter @Accessors(chain = true)
public class ClubUpgradeModel extends AbstractProducerModel {
    private static final long serialVersionUID = -917712197714846062L;

    private String uid;

    private Integer oldLevel;

    private Integer latestLevel;

    private Long updateTime;

    private String updateOrigin;
}
