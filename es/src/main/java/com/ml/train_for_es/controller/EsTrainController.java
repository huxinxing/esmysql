package com.ml.train_for_es.controller;

import com.alibaba.fastjson.JSONObject;
import com.ml.train_for_es.domain.entity.TrainEntity;
import com.ml.train_for_es.domain.repository.TrainRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api("es 培训相关的接口实现")
@RestController
@RequestMapping(value = "/train")
public class EsTrainController {

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @ApiOperation(value = "批量添加数据")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/batchAdd", method = GET)
    public String batchAddMsg(

    ) {
        try {
            /**
             * 插入一条数据
             */
            TrainEntity trainEntity = new TrainEntity();
            trainEntity.setTrainId(0);
            trainEntity.setRandomNum(new Random().nextInt(1000));
            trainEntity.setCreateTime(System.currentTimeMillis());
            trainRepository.save(trainEntity);

            /**
             * 批量插入数据
             * 批量插入数据建议在10000到15000之间，参数调整1、防止gc 2、es内存占用
             */
            List<TrainEntity> trainEntityList = new ArrayList<>();
            for (int i = 1; i < 1001; i++) {
                TrainEntity trainEntityTmp = new TrainEntity();
                trainEntityTmp.setTrainId(i);
                trainEntityTmp.setRandomNum(new Random().nextInt(10000));
                trainEntityTmp.setCreateTime(System.currentTimeMillis());
                trainEntityList.add(trainEntityTmp);
            }
            if (!CollectionUtils.isEmpty(trainEntityList)) {
                trainRepository.saveAll(trainEntityList);
            }

            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }


    @ApiOperation(value = "单条数据查询")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryOne", method = GET)
    public String queryOne(
            @RequestParam(value = "trainId", required = true) Integer trainId
    ) {
        try {

            TrainEntity trainEntity = trainRepository.findByTrainId(trainId);
            if (!ObjectUtils.isEmpty(trainEntity)) {
                return JSONObject.toJSONString(trainEntity);
            } else {
                throw new Exception("数据不存在");
            }
            //select * from zhifan_train_index where trainId = ${trainId}
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }


    @ApiOperation(value = "区间查询")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryRange", method = GET)
    public String queryOne(
            @RequestParam(value = "randomNumGeater", required = true) Integer randomNumGeater,
            @RequestParam(value = "randomNumLess", required = true) Integer randomNumLess
    ) {
        try {

            List<TrainEntity> trainEntityList = trainRepository.findByRandomNumGreaterThanAndRandomNumLessThan(randomNumGeater, randomNumLess);
            if (!CollectionUtils.isEmpty(trainEntityList)) {
                return JSONObject.toJSONString(trainEntityList);
            } else {
                throw new Exception("数据不存在");
            }
            //select * from zhifan_train_index where randomNum > ${randomNumGeater} and randomNum < ${randomNumLess}
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }

    @ApiOperation(value = "in查询")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryIn", method = GET)  //1024
    public String queryOne(

    ) {
        try {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                ids.add(new Random().nextInt(10000));
            }
            List<TrainEntity> trainEntityList = trainRepository.findByRandomNumIn(ids);
            if (!CollectionUtils.isEmpty(trainEntityList)) {
                return JSONObject.toJSONString(trainEntityList);
            } else {
                throw new Exception("数据不存在");
            }
            //select * from zhifan_train_index where randomNum in (1,2,3) limit 0,100
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }


    @ApiOperation(value = "分页查询")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryPage", method = GET)
    public String queryPage(
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize
    ) {
        try {


            Page<TrainEntity> trainEntityPage = trainRepository.findAll(PageRequest.of(pageNum, pageSize, new Sort(Sort.Direction.DESC, "trainId")));


            if (!CollectionUtils.isEmpty(trainEntityPage.getContent())) {
                return JSONObject.toJSONString(trainEntityPage);
            } else {
                throw new Exception("数据为空！");
            }
            //select * from zhifan_train_index limit 0,20
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }


    @ApiOperation(value = "动态查询")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryTrends", method = GET)  //1024
    public String queryPage(

    ) {
        try {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                ids.add(new Random().nextInt(10000));
            }
            if (CollectionUtils.isEmpty(ids)) {
                return null;
            }
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("randomNum", ids);//in查询
            boolQueryBuilder.must(queryBuilder);


            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
            nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 100));
            NativeSearchQuery query = nativeSearchQueryBuilder.build();


            Page<TrainEntity> trainEntityPage = trainRepository.search(query);
            if (!CollectionUtils.isEmpty(trainEntityPage.getContent())) {
                return JSONObject.toJSONString(trainEntityPage);
            } else {
                throw new Exception("数据为空！");
            }

            //select * from zhifan_train_index where randomNum in (1,2,3) limit 0,100
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }

    @ApiOperation(value = "聚合查询aggregation")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/queryAggregation", method = GET)
    public String queryAggregation(

    ) {
        try {
            List<TrainEntity> trainEntityList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                TrainEntity trainEntity = new TrainEntity();
                trainEntity.setTrainId(2000 + i);
                trainEntity.setRandomNum(2000);
                trainEntity.setCreateTime(System.currentTimeMillis());
                trainEntityList.add(trainEntity);
            }
            if (!CollectionUtils.isEmpty(trainEntityList)) {
                trainRepository.saveAll(trainEntityList);
            }

            /**
             * 构建查询条件，以及确定查询的索引和类型
             */
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("randomNum", 2000));
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
            nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);

            nativeSearchQueryBuilder.withIndices("zhifan_train_index").withTypes("zhifan_train_type");

            /**
             * 构建聚合查询语句
             */
            MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("trainId").field("trainId");
            nativeSearchQueryBuilder.addAggregation(maxAggregationBuilder);
            NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

            Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, new ResultsExtractor<Aggregations>() {
                @Override
                public Aggregations extract(SearchResponse response) {
                    return response.getAggregations();
                }
            });
            /**
             * 获取查询的值
             */
            HashMap<Long, Long> map = new HashMap<>();
            Max max = aggregations.get("trainId");
            String msg = max.getName() + "_" + max.getValue();
            return msg;
            //select max(trainId) as trainId from zhifan_train_index where randomNum = 2000
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }

}
