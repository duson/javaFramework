
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "data", type = "data")
public class ESModel implements Serializable {

	@Id
	@Field(type=FieldType.Long, store=true, index=FieldIndex.not_analyzed)
	private Long id;
		
	@Field(type = FieldType.String, store = true, index=FieldIndex.analyzed, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
	private String question;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
}
