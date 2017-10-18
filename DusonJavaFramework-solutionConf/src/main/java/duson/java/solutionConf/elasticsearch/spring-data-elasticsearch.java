
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

		BoolQueryBuilder query = QueryBuilders.boolQuery();
		
		if(filter.getHotelId() != null)
			query.filter(QueryBuilders.termQuery("hotelId", filter.getHotelId()));
		if(filter.getSceneIds() != null)
			query.filter(QueryBuilders.termsQuery("sceneId", filter.getSceneIds()));
		if(StringUtils.isNotBlank(filter.getQuestion()))
			query.must(QueryBuilders.matchQuery("question", filter.getQuestion()));
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withMinScore((float) 0.1).withQuery(query)
				.withSort(SortBuilders.scoreSort().order(SortOrder.DESC)).build();
		logger.debug("DSL" + searchQuery.getQuery().toString());
		List<ESModel> list = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<List<ESModel>>() {

			@Override
			public List<ESModel> extract(SearchResponse response) {
				List<ESModel> result = Lists.newArrayList();
				
				EntityMapper entityMapper = new DefaultEntityMapper();
				
				SearchHits searchHits = response.getHits();
				logger.debug("共搜到:" + searchHits.getTotalHits() + "条结果");
		        //遍历结果
		        ESModel idx;
		        for(SearchHit hit : searchHits){
		        	String sourceAsString = hit.getSourceAsString();
		        	if (StringUtils.isNotBlank(sourceAsString)) {
						try {
							idx = entityMapper.mapToObject(sourceAsString, ESModel.class);
							idx.setScore(hit.getScore());
						} catch (IOException e) {
							throw new ElasticsearchException("failed to map source [ " + sourceAsString + "] to class " + ESModel.class.getSimpleName(), e);
						}
						result.add(idx);
					}
		        }
				return result;
			}
			
		});
		
        //关闭client
        client.close();
	}
	
}
