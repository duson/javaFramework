package duson.java.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 依赖：commons-collections
 *
 */
public class Demo {
	public static void main(String[] args) {
		List<Long> a = new ArrayList<Long>();
		List<Long> b = new ArrayList<Long>();
		
		// 只存在于a：a-b
		Collection<Long> c1 = CollectionUtils.subtract(a, b);
	}
}
