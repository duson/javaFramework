package duson.java.utils.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JRedisUtils {
	private static JedisPool pool = null;
	private static String redis_servers = "";
	 
	private static JedisPool getPool() throws Exception {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(100);
			config.setMaxIdle(20);
			config.setMaxWaitMillis(10000 * 1000);
			config.setTestOnBorrow(true);
			
			List<HostAndPort> hostAndPorts = getHostAndPort();
			if(hostAndPorts == null || hostAndPorts.size() == 0)
				throw new Exception("not set redis host and port");
			pool = new JedisPool(config, hostAndPorts.get(0).getHost(), hostAndPorts.get(0).getPort());
		}
		return pool;
	}
	 
	public static Jedis getJedis() throws Exception {
		JedisPool pool = getPool();
		
		return pool.getResource();
	}
	public static void returnResource(Jedis redis) throws Exception {
       if (redis != null) {
       	JedisPool pool = getPool();
           pool.returnResource(redis);
       }
   }
	
	//public static JedisCluster jc;
	public static JedisCluster getJedisCluster() {
		//if(jc == null) {
	        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
	        
	        List<HostAndPort> hostAndPorts = getHostAndPort();
	        for(HostAndPort kv : hostAndPorts) {
		        jedisClusterNodes.add(new HostAndPort(kv.getHost(), kv.getPort()));
	        }
	        
	        JedisCluster jc = new JedisCluster(jedisClusterNodes);
		//}
		
       return jc;
	}
	
	private static List<HostAndPort> getHostAndPort() {
		List<HostAndPort>  result = new ArrayList<HostAndPort>();
		
		String[] servers = redis_servers.split("\\|");
		
		String host = null;
		int port = 6379;
		String[] hostAndPorts;
		for(String server : servers) {
			hostAndPorts = server.split(":");
			if(hostAndPorts.length > 1 && hostAndPorts[1] != null)
				port = Integer.parseInt(hostAndPorts[1]);
			if(hostAndPorts.length > 0 && hostAndPorts[0] != null && !hostAndPorts[0].isEmpty()) {
				host = hostAndPorts[0];
				result.add(new HostAndPort(host, port));
			}
		}
		
		return result;
	}

}
