package com.facewnd.ad.common.exception;

import com.facewnd.ad.common.enumeration.ErrorCode;

public class AdException extends RuntimeException {
	
	private String errorCode;
	
	public AdException() {}
	
	public AdException(String message) {
		super(message);
	}
	
	public AdException(ErrorCode errorCode) {
		this(errorCode.toString(), errorCode.getDesc());
	}
	
	public AdException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public AdException(Throwable cause) {
		super(cause);
	}

	public AdException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/*@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}*/
	
	@Override
	public String toString() {
		if(this.errorCode != null && !this.errorCode.isEmpty())
			return errorCode + ":" + super.getMessage();
		
		return super.getMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}

}
