package duson.java.solutionConf.log4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.Logger;

public class Usage {
	private static final Logger classLogger = LoggerFactory.getLogger(Usage.class); // slf4j
	private static final Log logger = LogFactory.getLog("rest"); // 自定义日志

	static final Log logger = LogFactory.getLog("custom"); // log4j
}
