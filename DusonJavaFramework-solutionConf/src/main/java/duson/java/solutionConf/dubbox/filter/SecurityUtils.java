package duson.java.solutionConf.dubbox.filter;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SecurityUtils {
	
	public static String generateSign(String secretKey, String method, String url, Map<String, Object> params)
	{
		List<Map.Entry<String, Object>> paramList = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
		Collections.sort(paramList, new Comparator<Map.Entry<String, Object>>() {   
		    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
		        return (o1.getKey()).toString().compareTo(o2.getKey());
		    }
		});
		String paramStr = ""; String md5Str = "";
		for (Map.Entry<String, Object> entry : paramList) {
			paramStr += entry.getKey() + "=" + entry.getValue().toString();
		}

		/* 或
		SortedMap<String, Object> smap = new TreeMap<String, Object>(params);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            sb.append(m.getKey()).append("=").append(m.getValue()).append("&");
        }

		String paramStr = sb.append("key=").append(secretKey).toString();
		 */
		
		try {
			URL u = new URL(url);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String s = method + u.getHost() + u.getPath() + paramStr + secretKey;
			byte[] bArr = s.getBytes("UTF-8");
			byte[] md5Value = md5.digest(bArr);
			BigInteger bigInt = new BigInteger(1,md5Value);
			md5Str = bigInt.toString(16);
			while(md5Str.length() < 32 ){
				md5Str = "0" + md5Str;
			}
		}
		catch(Exception e) {
			return "";
		}
		
		return md5Str;
	}
	
}
