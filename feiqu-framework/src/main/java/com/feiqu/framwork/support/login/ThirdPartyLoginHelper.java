package com.feiqu.framwork.support.login;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.common.config.Global;
import com.feiqu.system.pojo.ThirdPartyUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方登录辅助类
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:44:45
 */
public final class ThirdPartyLoginHelper {
	private static final Logger logger = LoggerFactory.getLogger(ThirdPartyLoginHelper.class);
	/**
	 * 获取QQ用户信息
	 * 
	 * @param token
	 * @param openid
	 */
	public static final ThirdPartyUser getQQUserinfo(String token, String openid) throws Exception {
		ThirdPartyUser user = new ThirdPartyUser();
		String url = Global.getPropertiesConfig("getUserInfoURL_qq");
		url = url + "?format=json&access_token=" + token + "&oauth_consumer_key="
				+ Global.getPropertiesConfig("app_id_qq") + "&openid=" + openid;
		logger.info("get qquser info url:{}",url);
		String res = HttpUtil.get(url);
		logger.info("qq info:"+res);
		JSONObject json = JSONObject.parseObject(res);
		if (json.getIntValue("ret") == 0) {
			user.setUserName(json.getString("nickname"));
			String img = json.getString("figureurl_qq_2");
			if (img == null || "".equals(img)) {
				img = json.getString("figureurl_qq_1");
			}
			user.setAvatarUrl(img);
			String sex = json.getString("gender");
			if ("女".equals(sex)) {
				user.setGender("2");
			} else {
				user.setGender("1");
			}
			String city = json.getString("city");
			if(StringUtils.isNotBlank(city)){
				user.setLocation(city);
			}
			String year = json.getString("year");
			if(StringUtils.isNotBlank(year)){
				user.setBirth(year);
			}
			user.setToken(token);
			user.setOpenid(openid);

		} else {
			throw new IllegalArgumentException(json.getString("msg"));
		}
		return user;
	}

	/** 获取微信用户信息 */
	public static final ThirdPartyUser getWxUserinfo(String token, String openid) throws Exception {
		ThirdPartyUser user = new ThirdPartyUser();
		String url = Global.getPropertiesConfig("getUserInfoURL_wx");
		url = url + "?access_token=" + token + "&openid=" + openid;
		String res = HttpUtil.post(url,"");

		JSONObject json = JSONObject.parseObject(res);
		if (json.getString("errcode") == null) {
			user.setUserName(json.getString("nickname"));
			String img = json.getString("headimgurl");
			if (img != null && !"".equals(img)) {
				user.setAvatarUrl(img);
			}
			String sex = json.getString("sex");
			if ("0".equals(sex)) {
				user.setGender("0");
			} else {
				user.setGender("1");
			}
			user.setToken(token);
			user.setOpenid(openid);
		} else {
			throw new IllegalArgumentException(json.getString("errmsg"));
		}
		return user;
	}

	/**
	 * 获取新浪用户信息
	 * 
	 * @param token
	 * @param uid
	 * @return
	 */
	public static final ThirdPartyUser getSinaUserinfo(String token, String uid) throws Exception {
		ThirdPartyUser user = new ThirdPartyUser();
		String url = Global.getPropertiesConfig("getUserInfoURL_sina");
		url = url + "?access_token=" + token + "&uid=" + uid;
		String res = HttpUtil.get(url);
		logger.info("url:"+url+",获取到新浪用户信息："+res);
		JSONObject json = JSONObject.parseObject(res);
		String name = json.getString("name");
		String nickName = StringUtils.isBlank(json.getString("screen_name")) ? name : json.getString("screen_name");
		user.setAvatarUrl(json.getString("avatar_large"));
		user.setUserName(nickName);
		if ("f".equals(json.getString("gender"))) {
			user.setGender("2");
		} else {
			user.setGender("1");
		}
		user.setDescription(json.getString("description"));
		user.setLocation(json.getString("location"));
		user.setToken(token);
		user.setOpenid(uid);
		return user;
	}


	/**
	 * 到github用户信息：{
	 *   "login": "chen87548081",
	 *   "id": 14307466,
	 *   "node_id": "MDQ6VXNlcjE0MzA3NDY2",
	 *   "avatar_url": "https://avatars1.githubusercontent.com/u/14307466?v=4",
	 *   "gravatar_id": "",
	 *   "url": "https://api.github.com/users/chen87548081",
	 *   "html_url": "https://github.com/chen87548081",
	 *   "followers_url": "https://api.github.com/users/chen87548081/followers",
	 *   "following_url": "https://api.github.com/users/chen87548081/following{/other_user}",
	 *   "gists_url": "https://api.github.com/users/chen87548081/gists{/gist_id}",
	 *   "starred_url": "https://api.github.com/users/chen87548081/starred{/owner}{/repo}",
	 *   "subscriptions_url": "https://api.github.com/users/chen87548081/subscriptions",
	 *   "organizations_url": "https://api.github.com/users/chen87548081/orgs",
	 *   "repos_url": "https://api.github.com/users/chen87548081/repos",
	 *   "events_url": "https://api.github.com/users/chen87548081/events{/privacy}",
	 *   "received_events_url": "https://api.github.com/users/chen87548081/received_events",
	 *   "type": "User",
	 *   "site_admin": false,
	 *   "name": "陈卫东",
	 *   "company": "苏州同程",
	 *   "blog": "www.flyfun.site",
	 *   "location": "苏州-园区",
	 *   "email": "573047307@qq.com",
	 *   "hireable": null,
	 *   "bio": "java developer 兼 前端开发 构建只用过 vue",
	 *   "public_repos": 7,
	 *   "public_gists": 0,
	 *   "followers": 2,
	 *   "following": 8,
	 *   "created_at": "2015-09-16T06:24:05Z",
	 *   "updated_at": "2019-12-20T09:30:44Z"
	 * }
	 *
	 * @param access_token
	 * @return
	 */
	public static ThirdPartyUser getGithubUserinfo(String access_token) {
		ThirdPartyUser user = new ThirdPartyUser();
		String url = Global.getPropertiesConfig("getUserInfoURL_github");
		url = url + "?access_token=" + access_token;
		String res = HttpUtil.get(url);
		logger.info("url:"+url+",获取到github用户信息："+res);
		JSONObject json = JSONObject.parseObject(res);
		String login = json.getString("login");
		String name = json.getString("name");
		String nickName = StringUtils.isBlank(name) ? ("github" + RandomUtil.randomString(4)): name;
		user.setAvatarUrl(json.getString("avatar_url"));
		user.setUserName(nickName);
		if ("f".equals(json.getString("gender"))) {
			user.setGender("2");
		} else {
			user.setGender("1");
		}
		String bio = json.getString("bio");
		user.setDescription(StringUtils.trimToEmpty(bio));
		user.setLocation(json.getString("location"));
		user.setToken(access_token);
		user.setOpenid(login);
		String email = json.getString("email");
		user.setAccount(StringUtils.trimToEmpty(email));
		return user;
	}

	/**
	 * 获取QQ的认证token和用户OpenID
	 * 
	 * @param code
	 * @return
	 */
	public static final Map<String, String> getQQTokenAndOpenid(String code, String host) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 获取令牌
		String tokenUrl = Global.getPropertiesConfig("accessTokenURL_qq");
		tokenUrl = tokenUrl + "?grant_type=authorization_code&client_id=" + Global.getPropertiesConfig("app_id_qq")
				+ "&client_secret=" + Global.getPropertiesConfig("app_key_qq") + "&code=" + code
				+ "&redirect_uri=http://" + host + Global.getPropertiesConfig("redirect_url_qq");
		logger.info("tokenUrl：{}",tokenUrl);
		String tokenRes = HttpUtil.get(tokenUrl);
		logger.info("qq res:"+tokenRes);
		if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
			Map<String, String> tokenMap = toMap(tokenRes);
			map.put("access_token", tokenMap.get("access_token"));
			// 获取QQ用户的唯一标识openID
			String openIdUrl = Global.getPropertiesConfig("getOpenIDURL_qq");
			openIdUrl = openIdUrl + "?access_token=" + tokenMap.get("access_token");
			String openIdRes = HttpUtil.get(openIdUrl);
			logger.info("qq:openIdRes:"+openIdRes);
			int i = openIdRes.indexOf("(");
			int j = openIdRes.indexOf(")");
			openIdRes = openIdRes.substring(i + 1, j);
			JSONObject openidObj = JSONObject.parseObject(openIdRes);
			map.put("openId", openidObj.getString("openid"));
		} else {
			throw new IllegalArgumentException("qq 获取tokens参数错误");
		}
		return map;
	}

	/**
	 * 获取微信的认证token和用户OpenID
	 * 
	 * @param code
	 * @return
	 */
	public static final Map<String, String> getWxTokenAndOpenid(String code, String host) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 获取令牌
		String tokenUrl = Global.getPropertiesConfig("accessTokenURL_wx");
		tokenUrl = tokenUrl + "?appid=" + Global.getPropertiesConfig("app_id_wx") + "&secret="
				+ Global.getPropertiesConfig("app_key_wx") + "&code=" + code + "&grant_type=authorization_code";
		String tokenRes = HttpUtil.post(tokenUrl,"");
		if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
			Map<String, String> tokenMap = toMap(tokenRes);
			map.put("access_token", tokenMap.get("access_token"));
			// 获取微信用户的唯一标识openid
			map.put("openId", tokenMap.get("openid"));
		} else {
			throw new IllegalArgumentException(("THIRDPARTY.LOGIN.NOTOKEN weixin"));
		}
		return map;
	}

	/**
	 * 获取github的认证token和用户OpenID
	 *
	 * @param code
	 * @return
	 */
	public static final Map<String, String> getGithubTokenAndOpenid(String code, String state) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 获取令牌
		String tokenUrl = Global.getPropertiesConfig("accessTokenURL_github");
		tokenUrl = tokenUrl + "?client_id=" + Global.getPropertiesConfig("app_id_github") + "&client_secret="
				+ Global.getPropertiesConfig("app_key_github") + "&code=" + code + "&state="+state;
		String tokenRes = HttpUtil.post(tokenUrl,"");
		logger.info("getGithubTokenAndOpenid:{}",tokenRes);
		if (tokenRes != null && tokenRes.contains("access_token")) {
			String[] tokenArray = tokenRes.split("&");
			String tokenand = tokenArray[0];
			map.put("access_token", tokenand.split("=")[1]);
		} else {
			throw new IllegalArgumentException(("THIRDPARTY.LOGIN.NOTOKEN github"));
		}
		return map;
	}

	/**
	 * 获取新浪登录认证token和用户id
	 * 
	 * @param code
	 * @return
	 */
	public static final JSONObject getSinaTokenAndUid(String code, String host) {
		JSONObject json = null;
		try {
			// 获取令牌
			String tokenUrl = Global.getPropertiesConfig("accessTokenURL_sina");
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("client_id",Global.getPropertiesConfig("app_id_sina"));
			map.put("client_secret",Global.getPropertiesConfig("app_key_sina"));
//			map.put("grant_type",Global.getPropertiesConfig("authorization_code"));
			map.put("grant_type","authorization_code");
			map.put("redirect_uri","http://" + host + Global.getPropertiesConfig("redirect_url_sina"));
			map.put("code",code);
			String tokenRes = HttpUtil.post(tokenUrl, map);
			// String tokenRes = httpClient(tokenUrl);
			// {"access_token":"2.00AvYzKGWraycB344b3eb242NUbiQB","remind_in":"157679999","expires_in":157679999,"uid":"5659232590"}
			if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
				json = JSONObject.parseObject(tokenRes);
			} else {
				throw new IllegalArgumentException("THIRDPARTY.LOGIN.NOTOKEN sina");
			}
		} catch (Exception e) {
			logger.error("getSinaTokenAndUid",e);
		}
		return json;
	}

	/**
	 * 将格式为s1&s2&s3...的字符串转化成Map集合
	 * 
	 * @param str
	 * @return
	 */
	private static final Map<String, String> toMap(String str) {
		Map<String, String> map = new HashMap<String, String>();
		String[] strs = str.split("&");
		for (int i = 0; i < strs.length; i++) {
			String[] ss = strs[i].split("=");
			map.put(ss[0], ss[1]);
		}
		return map;
	}

}
