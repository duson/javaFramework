package com.facewnd.ad.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.ReflectUtils;

public class BeanCopierWrapper {
	private static final Map<String, BeanCopier> beanCopierCache = new ConcurrentHashMap<String, BeanCopier>();

	public static void copy(Object source, Object target, boolean useConverter) {
		Class<? extends Object> sourceClazz = source.getClass();
		Class<? extends Object> targetClazz = target.getClass();
		String copierKey = generateKey(sourceClazz, targetClazz);
		BeanCopier beanCopier = beanCopierCache.get(copierKey);
		if (beanCopier == null) {
			beanCopier = BeanCopier.create(sourceClazz, targetClazz, useConverter);
			beanCopierCache.put(copierKey, beanCopier);
		}
		beanCopier.copy(source, target, null);
	}

	public static void copy(Object source, Object target) {
		copy(source, target, false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T copy(Object source, Class<T> targetClazz, boolean useConverter) {
		T target = (T) ReflectUtils.newInstance(targetClazz);
		copy(source, target, useConverter);
		return target;
	}
	
	public static <T> T copy(Object source, Class<T> targetClazz) {
		return copy(source, targetClazz, false);
	}

	public static <T> List<T> copyList(List<? extends Object> sourceList, Class<T> targetClazz, boolean useConverter) {
		List<T> targetList = new ArrayList<T>();
		for (Iterator<? extends Object> iterator = sourceList.iterator(); iterator.hasNext();) {
			Object source = iterator.next();
			T target = copy(source, targetClazz, useConverter);
			targetList.add(target);
		}
		return targetList;
	}

	public static <T> List<T> copyList(List<? extends Object> sourceList, Class<T> targetClazz) {
		return copyList(sourceList, targetClazz, false);
	}

	private static String generateKey(Class<?> sourceClazz, Class<?> targetClazz) {
		return sourceClazz.toString() + targetClazz.toString();
	}
}
