package com.feiqu.web.controller.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.common.base.BaseResult;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.store.AliyunOssService;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.pojo.cache.FqUserCache;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by ZhangShuzheng on 2017/5/15.
 */
@RestController
@RequestMapping("/aliyun/oss")
public class AliyunOssController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOssController.class);

	@Resource
	private AliyunOssService aliyunOssService;


	/**
	 * 签名生成
	 * @param callback 跨域请求
	 * @return jsonp
	 */
	@GetMapping("/policy")
	@CrossOrigin(origins = "*", methods = RequestMethod.GET) // 该注解不支持JDK1.7
	public Object policy(@RequestParam(required = false) String callback) {
		FqUserCache fqUserCache = getCurrentUser();
		if(fqUserCache == null){
			return error("用户未登陆，拒绝签名！");
		}
		LOGGER.info("AliyunOssController-policy:"+callback);
		JSONObject result = aliyunOssService.policy();
		if (StringUtils.isBlank(callback)) {
			return success(result);
		}
		MappingJacksonValue jsonp = new MappingJacksonValue(result);
		jsonp.setValue(callback);
		return success(jsonp);
	}

	/**
	 * 上传成功回调方法
	 * @param request
	 * @return
	 */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.POST}) //
	@PostMapping("callback")
	public Object callback(HttpServletRequest request) {
		LOGGER.info("callback:{}",JSON.toJSONString(request.getParameterMap()));
		BaseResult result = new BaseResult();
		JSONObject data = new JSONObject();
		String filename = request.getParameter("filename");
		filename = CommonConstant.ALIOSS_URL_PREFIX.concat("/").concat(filename);
		data.put("filename", filename);
		data.put("size", request.getParameter("size"));
		data.put("mimeType", request.getParameter("mimeType"));
		data.put("width", request.getParameter("width"));
		data.put("height", request.getParameter("height"));
		result.setData(data);
        LOGGER.info("callback:data{}",JSON.toJSONString(result));
		return result;
	}

}
