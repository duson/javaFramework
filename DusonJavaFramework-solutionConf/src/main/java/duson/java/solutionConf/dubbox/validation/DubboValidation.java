package com.xxx.dubboEx.validation;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.validation.Validator;
import com.alibaba.dubbo.validation.support.jvalidation.JValidation;

public class DubboValidation extends JValidation {
	protected Validator createValidator(URL url) {
        return new DubboValidator(url);
    }
}
