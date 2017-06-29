
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
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class IndexCreator {

	
	public static void main(String[] args) throws IOException, SQLException {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		
		//搜索数据
        GetResponse response = client.prepareGet("a", "b", "1").execute().actionGet();
        //输出结果
        System.out.println(response.getSourceAsString());
		
		QueryBuilder matchQuery = QueryBuilders.matchQuery("title", "编程");
        HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<h2>");
        hiBuilder.postTags("</h2>");
        hiBuilder.field("title");
        // 搜索数据
        SearchResponse response = client.prepareSearch("blog")
                .setQuery(matchQuery)
                .highlighter(hiBuilder)
                .execute().actionGet();
        //获取查询结果集
        SearchHits searchHits = response.getHits();
        System.out.println("共搜到:"+searchHits.getTotalHits()+"条结果!");
        //遍历结果
        for(SearchHit hit:searchHits){
            System.out.println("String方式打印文档搜索内容:");
            System.out.println(hit.getSourceAsString());
            System.out.println("Map方式打印高亮内容");
            System.out.println(hit.getHighlightFields());

            System.out.println("遍历高亮集合，打印高亮片段:");
            Text[] text = hit.getHighlightFields().get("title").getFragments();
            for (Text str : text) {
                System.out.println(str.string());
            }
        }
		
		/* 指建索引 */

		BulkRequestBuilder bulkRequest = client.prepareBulk();
		
		Connection conn = IndexCreator.getConnection();
		conn.setAutoCommit(false);
		QueryRunner runner = new QueryRunner();
		
		List<Map<String, Object>> question = null;
		for (Map<String, Object> item : question) {
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject()
				        .field("questionId", item.get("questionId"))
				        .field("sceneId", item.get("sceneId"))
				        .field("hotelId", item.get("hotelId"))
				        .field("queType", item.get("queType"))
				        .field("queContent", item.get("queContent"))
				        .field("business", item.get("business"))
				        .field("subBusiness", item.get("subBusiness"))
				        .field("parentId", item.get("parentId"))
				        .field("sequence", item.get("sequence"))
				    .endObject();
			bulkRequest.add(client.prepareIndex("robot", "speech", item.get("questionId").toString()).setSource(builder));
		}
		
		bulkRequest.execute().actionGet();
		System.out.println("finished");
		
        //关闭client
        client.close();*/
	}
	
	public static void createMapping(TransportClient client, String indices, String mappingType) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder()
		.startObject()
			.startObject(indices)
				.startObject("properties")
					.startObject("queContent").field("type", "string").field("store", "yes").field("analyzer", "ik_smart").field("search_analyzer", "ik_smart").endObject()
				.endObject()
			.endObject()
		.endObject();
		
		PutMappingRequest mapping = Requests.putMappingRequest(indices).type(mappingType).source(builder);
		client.admin().indices().putMapping(mapping).actionGet();
		client.close();
	}
	
}
