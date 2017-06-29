
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaMain {
	private static Logger logger = LoggerFactory.getLogger(JavaMain.class);
			
	public static Connection getConnection() {  
        Connection conn = null;  
        try {  
            Class.forName(PropertiesLoader.Instance.getProperty("jdbc.driver"));  
            conn = DriverManager.getConnection(  
            		PropertiesLoader.Instance.getProperty("jdbc.url"), 
            		PropertiesLoader.Instance.getProperty("jdbc.username"),  
            		PropertiesLoader.Instance.getProperty("jdbc.password"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return conn;  
    } 

	public static void main(final String[] args) throws Exception {
		
		Connection conn = JavaMain.getConnection();
		conn.setAutoCommit(false);
		QueryRunner runner = new QueryRunner();
		
		// 查询
		long count = runner.query(conn, "select count(1) from table where UserMobile = ?", new ScalarHandler<Long>(), "");

		List<Map<String, Object>> deviceGroups = runner.query(conn, "select * from tmp_grp where StatusCode = ?", new MapListHandler(), 0);
		for (Map<String, Object> groupMap : deviceGroups) {
			long groupId = Long.parseLong(groupMap.get("groupId").toString());
		}

		// 函数
		Map<String, Object> pkMap = runner.query(conn, "select show_func_get_index_bigint(?) idx", new MapHandler(), "params");
		long userId = Long.parseLong(pkMap.get("idx").toString());

		// 更新
		int affectRows = runner.update(conn, "Sql", params);
		
		conn.commit();
    }
	
}
