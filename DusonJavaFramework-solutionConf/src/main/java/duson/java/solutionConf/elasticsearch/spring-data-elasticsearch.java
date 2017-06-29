
import java.io.IOException;
import java.net.InetAddress;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class IndexCreator {
	
	public static void main(String[] args) throws IOException, SQLException {
		
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		
		ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);
				
		 SearchQuery searchQuery = new NativeSearchQueryBuilder()
			        .withQuery(QueryBuilders.matchAllQuery())
			        .build();
		 Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {

			@Override
			public Aggregations extract(SearchResponse response) {
				/*SearchHits searchHits = response.getHits();
		        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
		        //遍历结果
		        for(SearchHit hit:searchHits){
		            System.out.println("String方式打印文档搜索内容:");
		            System.out.println(hit.getSourceAsString());
		            System.out.println("Map方式打印高亮内容");
		            System.out.println(hit.getHighlightFields());
		        }*/
				return response.getAggregations();
			}
			
		});
		
        //关闭client
        client.close();
	}
	
}
