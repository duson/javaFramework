package com.facewnd.ad.front;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.facewnd.ad.common.model.RestResult;
import com.facewnd.ad.modules.communication.MqttSender;
import com.facewnd.ad.modules.device.service.DeviceService;
import com.facewnd.ad.utils.ValidateCodeHandle;
import com.facewnd.core.draw.RandomValidateCodeUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@CrossOrigin
@RestController
public class MainController extends BaseFrontController {

	@Autowired
	private MqttSender mqttSender;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@RequestMapping("/")
    public ModelAndView index() {
		
		ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
	}

	@RequestMapping(value="/test/{name}")
    @ResponseBody
    @HystrixCommand(fallbackMethod = "fallback")
    public RestResult test(@PathVariable("name") String name) {
		RestResult ret = new RestResult();
		ret.setSuccess(true);
		ret.setReturnValue(name);

		return ret;
	}
	
	public RestResult fallback(String name) {
		RestResult ret = new RestResult();
		
		return ret;
    }
	
	@GetMapping("getCode")
	@ResponseBody
	public void getCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		String refer = request.getHeader("referer");
		/*if (null == refer) {
			return;
		}*/

		String validateCode = RandomValidateCodeUtils.getRandcode(response.getOutputStream());
		ValidateCodeHandle.save(request.getSession().getId(), validateCode);
		//request.getSession().setAttribute("randomCode", validateCode);
	}
}
