package com.loser.backend.club.service;

import com.loser.backend.club.domain.AverageMonthlyAsset;
import com.loser.backend.club.domain.UserMonthAverageAsset;

import java.util.List;

public interface IAvgMonthService {

    List<AverageMonthlyAsset> get4Evaluate(int page, int pageSize);

    List<UserMonthAverageAsset> getloserAsset4Evaluate(int page, int pageSize);

    int syncAssetSource(List<AverageMonthlyAsset> source);

    int syncloserAssetSource(List<UserMonthAverageAsset> source);

    int batchUpsert(List<AverageMonthlyAsset> records);
}
