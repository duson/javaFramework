/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package duson.java.core.mapper;

import java.util.Collection;
import java.util.List;

import net.sf.cglib.beans.BeanCopier;

public class BeanCopier {

	public static <T> T map(Object source, Class<T> destinationClass) throws InstantiationException, IllegalAccessException {
		BeanCopier coper = BeanCopier.create(source.class, ServiceFeeInvoiceDTO.class, false);

		destinationClass desc = destinationClass.newInstance();
		coper.copy(source, desc, null);
		return desc;
	}
}