package com.loser.backend.club.pojo;


import com.loser.backend.club.domain.ClubLevel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.StringJoiner;

@Data @Accessors
public class loserClub {
    private static final long serialVersionUID = -6201064273078927674L;

    private ClubLevel clubLevel;

    /**
     * 30日日均资产
     */
    private BigDecimal avgAsset;


    @Override
    public String toString() {
        return new StringJoiner(", ", loserClub.class.getSimpleName() + "[", "]")
                .add("clubLevel=" + clubLevel)
                .add("avgAsset=" + avgAsset)
                .toString();
    }
}
