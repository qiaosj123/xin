package com.jk.controller;
import com.alibaba.fastjson.JSON;
import com.jk.model.User1;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

@Controller
public class ElasticsearchController {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @RequestMapping("elaslist")
    @ResponseBody
    public HashMap<String,Object> text(Integer page, Integer rows, String name) {
        System.out.println(name);
        HashMap<String, Object> hash = new HashMap<>();
        //获取 Elasticsearch 客户端
        Client client = elasticsearchTemplate.getClient();
        //创建搜索请求对象 参数为索引名称
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("book-002")
                .setTypes("jk-book")
                //设置查询条件 boolQuery()多条件查询 matchQuery()单个条件查询
                .setQuery(QueryBuilders.multiMatchQuery(name,"name"));
    /*if(cakeprice!=null&&price%2==0){
        searchRequestBuilder.addSort("orderPrice", SortOrder.DESC);
    }else if(price!=null&&price%2!=0){
        searchRequestBuilder.addSort("orderPrice", SortOrder.ASC);
    }*/
        //创建高亮搜索 设置高亮搜索条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        //将高亮搜索条件放入到搜索请求对象中
        searchRequestBuilder.highlighter(highlightBuilder);
        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);
        //setFrom，从哪一个Score开始查
        //setSize，需要查询出多少条结果
        searchRequestBuilder.setFrom((page-1)*rows).setSize(rows);
        //获取查询结果返回结果集
        SearchResponse searchResponse = searchRequestBuilder.get();
        //获取命中条数
        SearchHits hits = searchResponse.getHits();
        //获取总条数，用于分页
        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);
        //获取返回结果集迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        List<User1>  list  = new ArrayList<>();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            //获取到源码内容 以json字符串的格式获取
            String sourceAsString = next.getSourceAsString();
            //获取对应的高亮域
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            //获取高亮字段
            HighlightField cakeAll = highlightFields.get("name");
            //将json字符串转换成实体Bean
            User1 userBean1 = JSON.parseObject(sourceAsString, User1.class);
            //取得定义的高亮标签
            userBean1.setName(cakeAll.fragments()[0].toString());
            list.add(userBean1);
            System.out.println(sourceAsString);
            //为thinkName（相应字段）增加自定义的高亮标签
        }
        hash.put("rows",list);
        hash.put("total",totalHits);
        return hash;
    }

}
