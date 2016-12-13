package duson.java.solutionConf.swagger;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="请求实体")
public class RestRequest<T> {

	@NotBlank(message="apiKey不能为null")
	@ApiModelProperty(value="apiKey", required=true)
	private String apiKey;
	
	/**
	 * 业务参数是否启用AES加密
	 *
	 * @since 1.0.0
	 */
	@ApiModelProperty(value="aesEncryty")	
	private Boolean aesEncryty;
	
	@NotBlank(message="sign不能为null")
	@ApiModelProperty(value="签名", required=true)
	private String sign;
	
	@NotBlank(message="timestamp不能为null")
	@ApiModelProperty(value="时间戳", required=true)
	private String timestamp;
	
	@ApiModelProperty(value="业务参数")
	@Valid
	private T bizContent;
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	/**
	 * aesEncryty
	 *
	 * @return  the aesEncryty
	 * @since   1.0.0
	*/
	
	public Boolean isAesEncryty() {
		return aesEncryty;
	}
	/**
	 * @param aesEncryty the aesEncryty to set
	 */
	public void setAesEncryty(Boolean aesEncryty) {
		this.aesEncryty = aesEncryty;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * bizContent
	 *
	 * @return  the bizContent
	 * @since   1.0.0
	*/
	
	public T getBizContent() {
		return bizContent;
	}
	/**
	 * @param bizContent the bizContent to set
	 */
	public void setBizContent(T bizContent) {
		this.bizContent = bizContent;
	}
	
}
