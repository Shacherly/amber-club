package com.loser.backend.club.api;


import com.loser.backend.club.api.fallback.ClubApiFallbackFactory;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.api.http.request.InternalMultiParamUid;
import com.loser.backend.club.api.http.request.InternalParamUid;
import com.loser.backend.club.api.http.response.BriefClubVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        url = "${trading.domain}",
        name = "trading-club-service",
        path = "/club/internal/v1",
        contextId = "club",
        fallbackFactory = ClubApiFallbackFactory.class
)
public interface ClubServiceApi {


    @PostMapping("/brief/me")
    Response<BriefClubVO> getPersonalClub(@Validated @RequestBody InternalParamUid param);


    @PostMapping("/brief/multi")
    Response<List<BriefClubVO>> getMultiClubs(@Validated @RequestBody InternalMultiParamUid param);
}
