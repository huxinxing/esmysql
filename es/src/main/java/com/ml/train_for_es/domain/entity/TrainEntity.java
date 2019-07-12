package com.ml.train_for_es.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "zhifan_train_index",type = "zhifan_train_type", replicas = 2, shards = 3)
public class TrainEntity {

    @Id
    private Integer trainId;

    private Integer randomNum;

    private Long createTime;

}
