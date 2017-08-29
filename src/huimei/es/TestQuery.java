package huimei.es;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.rescore.RescoreBuilder;


/**
 * Description: Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月13日 author：huangzhenjie
 * 
 * @version 1.0
 */
public class TestQuery {

    public static void test() {
        try {
            TransportClient client = EsClientFactory.getTransportClient();
            Script script;
            Map<String, Object> params = new HashMap<>();
            params.put("gender", 1);
            params.put("userSymptom", Arrays.asList(6729, 2777, 136, 46));
            script = new Script("symptom_check", ScriptService.ScriptType.FILE, "painless", params);
            String[] includeSources = { "department", "diseaseAlias", "diseaseName", "id", "freq",
                    "symptomListStr", "gender" };
            String[] exculdeSources = {};
            String queryString = "6729|2777|136|46";
            String defaultFiled = "symptomListStr";
            SearchResponse response = client.prepareSearch("disease").setTypes("disease").setFrom(0)
                    .setSize(10)
                    .setSource(SearchSourceBuilder.searchSource().fetchSource(includeSources, exculdeSources))
                    .setQuery(
                            QueryBuilders.queryStringQuery(queryString)
                                    .defaultField(defaultFiled))
                    .setRescorer(RescoreBuilder
                            .queryRescorer(QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery(),
                                    ScoreFunctionBuilders.scriptFunction(script)))
                            .setQueryWeight(0).setRescoreQueryWeight(1).windowSize(200))
                    .execute().actionGet();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            TransportClient client = EsClientFactory.getTransportClient();

            SearchRequestBuilder request = client.prepareSearch("customer");
            // request.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
