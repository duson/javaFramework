package duson.java.solutionConf.cxf.dto;

import java.io.Serializable;

/**
 * 
 * @author pmchen
 *
 */
public class WSResult implements Serializable {
    private String data;
    
    private boolean success;
    private int resultCode;
    private String msg;
    
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
