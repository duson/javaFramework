package duson.java.solutionConf.swagger;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="返回结果")
public class RestResult<T> {
	@ApiModelProperty(value="是否成功")
	private boolean success;
	@ApiModelProperty(value="错误代码")
	private String errorCode;
	@ApiModelProperty(value="错误信息")
	private String errorMsg;
	@ApiModelProperty(value="返回的业务结果")
	private T returnValue;
	
	public RestResult() {}
	
	public RestResult(boolean success, T returnValue) {
		this.success = success;
		this.returnValue = returnValue;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public T getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(T returnValue) {
		this.returnValue = returnValue;
	}
	
}
