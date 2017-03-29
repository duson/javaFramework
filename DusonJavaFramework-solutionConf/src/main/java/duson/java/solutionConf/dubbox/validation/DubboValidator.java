package com.xxxdubboEx.validation;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.validation.support.jvalidation.JValidator;
import com.protruly.pay.api.exception.PayException;

public class DubboValidator extends JValidator {
	
	/**
	 * 创建一个新的实例 DubboValidator.
	 *
	 * @param url
	 */
	public DubboValidator(URL url) {
		super(url);
	}

	public void validate(String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Exception {
        try{
            super.validate(methodName, parameterTypes, arguments);
        }catch (Exception e){
            throw new PayException("DUBBO参数校验失败", e);
        }
    }
}
