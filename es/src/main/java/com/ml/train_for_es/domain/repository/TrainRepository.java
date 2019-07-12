package com.ml.train_for_es.domain.repository;

import com.ml.train_for_es.domain.entity.TrainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TrainRepository extends ElasticsearchRepository<TrainEntity,Integer> {

    TrainEntity findByTrainId(Integer trainId);

    List<TrainEntity> findByRandomNumGreaterThanAndRandomNumLessThan(Integer randomNumGeater,Integer randomNumLess);

    List<TrainEntity> findByRandomNumIn(List<Integer> randomNumIdList);

    Page<TrainEntity> findByRandomNum(Integer randomNum, PageRequest pageRequest);

}
