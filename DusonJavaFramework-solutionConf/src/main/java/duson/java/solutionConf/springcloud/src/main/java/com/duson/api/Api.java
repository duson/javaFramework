import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.facewnd.ad.api.model.RestRequest;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/")
@Api(value="", tags="")
public class Api {
	@Autowired
	private StringRedisTemplate redisTemplate;
		
	private final Logger logger = LogManager.getLogger(getClass());
	
	@ApiOperation(value = "测试通讯地址", notes="测试通讯地址", httpMethod="GET", response=String.class)
    @GetMapping("/test")
    public RestResult test() {
		RestResult result=new RestResult();
		result.setSuccess(true);
		result.setErrorCode(ErrorCode.Success.toString());
		result.setErrorMsg(ErrorCode.Success.getDesc());
		return result;
    }
	
}

