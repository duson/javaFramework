package com.facewnd.ad.api.modules.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facewnd.ad.api.model.RestRequest;
import com.facewnd.ad.api.modules.device.reponseparams.AppUpgradeReponse;
import com.facewnd.ad.api.modules.device.requestparams.AppUpgradeRequest;
import com.facewnd.ad.common.model.RestResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/device")
@Api(value="终端接口", tags="终端")
public class DeviceApi {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ApiOperation(value = "终端App升级", notes="返回最新的一个版本信息", httpMethod="POST", response=AppUpgradeRequest.class)
    @PostMapping("/upgrade")
    public RestResult<AppUpgradeReponse> upgrade(@RequestBody @Validated RestRequest<AppUpgradeRequest> params) {
		RestResult<AppUpgradeReponse> ret = new RestResult<AppUpgradeReponse>();
		ret.setSuccess(true);
		return ret;
	}
}
