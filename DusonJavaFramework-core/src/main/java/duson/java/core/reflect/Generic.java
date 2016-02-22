package duson.java.core.reflect;

public class Generic {
	public static <T> T getSetting(Class<T> c, String json) throws InstantiationException, IllegalAccessException {
		T setting = c.newInstance();

		return setting;
	}
}
