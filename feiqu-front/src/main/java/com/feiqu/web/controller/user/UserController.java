package com.feiqu.web.controller.user;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.ServletUtils;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.dto.LoginCondition;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.constant.IconUrlConfig;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.CookieUtil;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.model.sysData.SysLogininfor;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.MessageUserDetail;
import com.feiqu.system.service.mainData.*;
import com.feiqu.system.service.sysData.SysLogininforService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author cwd
 * @create 2017-09-16:19
 **/
@Controller
@RequestMapping("u")
public class UserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private ThoughtService thoughtService;
    @Resource
    private ArticleService articleService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private CMessageService messageService;
    @Resource
    private UploadImgRecordService uploadImgRecordService;
    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private UserActivateService userActivateService;
    @Resource
    private UserFollowService userFollowService;
    @Resource
    private FqUserService userService;
    @Resource
    private FqThirdPartyService fqThirdPartyService;
    @Resource
    private FqVisitRecordService visitRecordService;
    //支付方式
    @Resource
    private FqUserPayWayService fqUserPayWayService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/myInfo")
    @ResponseBody
    public Object myInfo(){
        return success(getCurrentUser());
    }

    @GetMapping("/card/{userId}")
    public String userCard(@PathVariable Integer userId,Model model){
        FqUserCache fqUserCache = CacheManager.getUserCacheByUid(userId);
        if(fqUserCache == null) return StringUtils.EMPTY;
        model.addAttribute("fqUser",fqUserCache);
        return "/user/userCard";
    }

    @GetMapping("upLevel")
    public String upLevel(Model model) {
        try {
            FqUserCache fqUserCache = getCurrentUser();
            int month = DateUtil.thisMonth() + 1;
            int lastMonth = month == 1 ? 12 : month - 1;
            Double score = stringRedisTemplate.opsForZSet().score(CommonConstant.FQ_ACTIVE_USER_SORT + month, fqUserCache.getId().toString());
            Double lastMonthScore = stringRedisTemplate.opsForZSet().score(CommonConstant.FQ_ACTIVE_USER_SORT + lastMonth, fqUserCache.getId().toString());
            Double totolScore = (score == null ? 0 : score) + (lastMonthScore == null ? 0 : lastMonthScore);
            model.addAttribute("totolScore", totolScore);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "/user/upLevel";
    }

    /**
     * 升级
     * @return
     */
    @PostMapping("upLevel")
    @ResponseBody
    public BaseResult upLevelGo() {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            int originLevel = fqUserCache.getLevel() == null ? 1 : fqUserCache.getLevel();
            int needQudou;
            switch (originLevel) {
                case 1:
                    needQudou = 300;
                    break;
                case 2:
                    needQudou = 1000;
                    break;
                case 3:
                    needQudou = 3000;
                    break;
                case 4:
                    needQudou = 5000;
                    break;
                default:
                    needQudou = 10000;
                    break;
            }
            if (fqUserCache.getQudouNum() < needQudou) {
                result.setResult(ResultEnum.PARAM_ERROR);
                result.setMessage("趣豆不足");
                return result;
            }
            FqUser toUpdate = new FqUser();
            toUpdate.setId(fqUserCache.getId());
            toUpdate.setLevel(originLevel + 1);
            toUpdate.setQudouNum(fqUserCache.getQudouNum()-needQudou);
            userService.updateByPrimaryKeySelective(toUpdate);
            CacheManager.refreshUserCacheByUid(fqUserCache.getId());
            result.setData(originLevel + 1);
        } catch (Exception e) {
            result.setResult(ResultEnum.FAIL);
            logger.error("升级报错", e);
        }
        return result;
    }

    @GetMapping("/set")
    public String set(HttpServletRequest request, HttpServletResponse response, Model model) {
        FqUserCache fqUser = webUtil.currentUser(request, response);
        if (fqUser == null) {
            return loginRedirect("/u/set");
        }
        boolean qqBind = false, sinaBind = false,githubBind = false;
        FqThirdPartyExample example = new FqThirdPartyExample();
        example.createCriteria().andUserIdEqualTo(fqUser.getId());
        List<FqThirdParty> thirdParties = fqThirdPartyService.selectByExample(example);
        for (FqThirdParty p : thirdParties) {
            if ("QQ".equals(p.getProvider())) {
                qqBind = true;
            } else if ("SINA".equals(p.getProvider())) {
                sinaBind = true;
            }else if(LoginProviderEnum.github.name().equals(p.getProvider())){
                githubBind = true;
            }
        }
        FqUserPayWayExample fqUserPayWayExample = new FqUserPayWayExample();
        fqUserPayWayExample.createCriteria().andUserIdEqualTo(fqUser.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<FqUserPayWay> fqUserPayWays = fqUserPayWayService.selectByExample(fqUserPayWayExample);
        if (CollectionUtil.isNotEmpty(fqUserPayWays)) {
            fqUserPayWays.forEach(fqUserPayWay -> {
                switch (fqUserPayWay.getPayWay()) {
                    case 1://PayWayEnum.ALIPAY
                        model.addAttribute("alipayImgUrl", fqUserPayWay.getPayImgUrl());
                        break;
                    case 2:
                        model.addAttribute("wechatPayImgUrl", fqUserPayWay.getPayImgUrl());
                        break;
                }
            });
        }
        request.setAttribute("qqBind", qqBind);
        request.setAttribute("sinaBind", sinaBind);
        request.setAttribute("githubBind", githubBind);
        return "/user/set";
    }

    /**
     * 上传头像
     * @param request
     * @param response
     * @param file
     * @return
     */
    @PostMapping("uploadIcon")
    @ResponseBody
    public Object uploadIcon(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        BaseResult result = new BaseResult();
        String fileName = "";
        try {
            long size = file.getSize();
            int limit = 300 * 1024;//头像不超过50kb=>100kb=>300
            if (size > limit) {
                return error("头像图片大小不能超过300KB");
            }
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            String picUrl;
            Date now = new Date();
            String extName = FileUtil.extName(file.getOriginalFilename());
            List<String> picExtList = CommonConstant.picExtList;
            if (!picExtList.contains(extName)) {
                result.setResult(ResultEnum.PIC_EXTNAME_NOT_RIGHT);
                return result;
            }
            fileName = BizConstant.FILE_NAME_PREFIX + DateUtil.format(now, "yyyyMMddHHmmss") + "." + extName;
            //记录图片md5
            String picMd5 = DigestUtil.md5Hex(file.getInputStream());
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if (uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())) {
                picUrl = uploadImgRecord.getPicUrl();
            } else {
                picUrl = FileSystemClient.getClient("aliyun").upload("icon/" + fileName, file.getInputStream(), extName);
                uploadImgRecord = new UploadImgRecord(picUrl, picMd5, new Date(), WebUtil.getIP(request), user.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            FqUser toUpdateUser = new FqUser();
            toUpdateUser.setIcon(picUrl);
            toUpdateUser.setId(user.getId());
            userService.updateByPrimaryKeySelective(toUpdateUser);
            user.setIcon(picUrl);
            CacheManager.refreshUserCacheByUser(user);
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.UPDATE_ICON.getValue());
            result.setData(picUrl);
        } catch (Exception e) {
            logger.error("上传icon失败", e);
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
            return result;
        }
        return result;
    }

    @GetMapping("/login")
    public String login(String rsUrl, HttpServletRequest request) {
        request.setAttribute("rsUrl", rsUrl);
        return "/login";
    }

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private SysLogininforService sysLogininforService;

    @ResponseBody
    @PostMapping(value = "/login")
    public Object dologin(HttpServletRequest request, @RequestBody LoginCondition condition, HttpServletResponse response) {
        try {
            String ip = getIP();
            if (StringUtils.isBlank(condition.getUsername()) || StringUtils.isBlank(condition.getPassword())) {
                recordLogininfor(condition.getUsername(),BizConstant.FAIL,ip,"前端用户：用户名或密码为空，登录失败");
                return error("参数不能为空");
            }
            String captchaCode = CacheManager.getCaptchaCode(ip);
            if (StringUtils.isNotEmpty(condition.getVerifyCode()) && !condition.getVerifyCode().equals(captchaCode)) {
                recordLogininfor(condition.getUsername(),BizConstant.FAIL,ip,"前端用户：验证码错误，登录失败");
                return error("验证码错误");
            }
            FqUserExample FqUserExample = new FqUserExample();
            FqUserExample.createCriteria().andUsernameEqualTo(condition.getUsername());
            FqUser userDB = userService.selectFirstByExample(FqUserExample);
            if (userDB != null) {
                if (UserStatusEnum.FROZEN.getValue().equals(userDB.getStatus()) || UserStatusEnum.DEL.getValue().equals(userDB.getStatus())) {
                    recordLogininfor(condition.getUsername(),BizConstant.FAIL,ip,"前端用户：用户被冻结，登录失败");
                    return error("用户被冻结，登录失败");
                }
                if(!userDB.getPassword().equals(encryptPassword(condition.getPassword(),userDB.getSalt()))){
                    recordLogininfor(condition.getUsername(),BizConstant.FAIL,ip,"前端用户：密码错误，登录失败");
                    return error("密码错误！");
                }
                WebUtil.loginUser(request, response, userDB, "on".equals(condition.getRemember()));
                recordLogininfor(condition.getUsername(),BizConstant.SUCCESS,ip,"前端用户：登录成功");
                if (StringUtils.isNotBlank(condition.getRsUrl())) {
                    return success(condition.getRsUrl());
                } else {
                    return success(CommonConstant.DOMAIN_URL);
                }
            } else {
                return error("用户名不存在！");
            }
        } catch (Exception e) {
            logger.error("login error", e);
            return error("发生系统错误！");
        }
    }

    private void recordLogininfor(String username, String code, String ip, String msg){
        final UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getHeader(ServletUtils.getRequest(),"User-Agent", CharsetUtil.CHARSET_UTF_8.name()));
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setLoginName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(CommonUtils.getFullRegionByIp(ip));
        // 获取客户端操作系统
        String os = userAgent.getPlatform().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(msg);
        logininfor.setStatus(code);
        // 插入数据
        sysLogininforService.insert(logininfor);
    }

    @GetMapping("logout")
    public String logout(HttpServletResponse response) {
        FqUserCache fqUserCache = getCurrentUser();
        logger.info("用户：{},用户id：{}， 登出飞趣社区", fqUserCache.getNickname(), fqUserCache.getId());
        CacheManager.removeUserCacheByUid(fqUserCache.getId());
        CookieUtil.removeCookie(response, CommonConstant.USER_ID_COOKIE);
        return "redirect:/u/login";
    }

    @GetMapping("register")
    public String register(Model model) {
        try {
            String ip = getIP();
            String address = CommonUtils.getRegionByIp(ip);
            model.addAttribute("address", address);
        } catch (Exception e) {
            logger.error("跳转注册页面出错", e);
        }
        return "/register";
    }

    @PostMapping("register")
    @ResponseBody
    @RepeatSubmit
    public Object registerDo(HttpServletRequest request, FqUser user, HttpServletResponse response, String verifyCode) {
        BaseResult result = new BaseResult();
        logger.info("registerDo:用户详情：{}", user.toString());
        try {
            if (!Validator.isEmail(user.getUsername())) {
                result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
                return result;
            }
            if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
                result.setResult(ResultEnum.PASSWORD_LENGTH_ERROR);
                return result;
            }
            if ("123456".equals(user.getPassword())) {
                result.setResult(ResultEnum.PASSWORD_TOO_SIMPLE);
                return result;
            }
            String ip = getIP();
            String captchaCode = CacheManager.getCaptchaCode(ip);
            if (StringUtils.isNotEmpty(verifyCode) && !verifyCode.equals(captchaCode)) {
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }
            FqUserExample fqUserExample = new FqUserExample();
            fqUserExample.createCriteria().andUsernameEqualTo(user.getUsername().trim());
            FqUser userDBUsername = userService.selectFirstByExample(fqUserExample);
            if (userDBUsername != null) {
                result.setResult(ResultEnum.USERNAME_EXIST);
                return result;
            }
            FqUser toRegister = new FqUser();
            toRegister.init();
            toRegister.setNickname(StrUtil.cleanBlank(user.getNickname()));
            toRegister.setUsername(user.getUsername().trim());
            toRegister.setPassword(user.getPassword().trim());
            toRegister.setSalt(getSalt());
            if (StringUtils.isBlank(user.getNickname())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            fqUserExample.clear();
            fqUserExample.createCriteria().andNicknameEqualTo(user.getNickname());
            FqUser userDBNickname = userService.selectFirstByExample(fqUserExample);
            if (userDBNickname != null) {
                result.setResult(ResultEnum.NICKNAME_EXIST);
                return result;
            }
            Date now = new Date();
            toRegister.setCreateIp(WebUtil.getIP(request));
            toRegister.setCreateTime(now);
            toRegister.setPassword(encryptPassword(user.getPassword(),toRegister.getSalt()));
            if (StringUtils.isBlank(user.getIcon())) {
                toRegister.setIcon(IconUrlConfig.icons.get(new Random().nextInt(IconUrlConfig.size())));
            }
            toRegister.setCity(CommonUtils.getRegionByIp(ip));
            userService.insert(toRegister);
            String token = UUID.fastUUID().toString(true);
            UserActivate userActivate = new UserActivate(toRegister.getId(), token, now);
            userActivateService.insert(userActivate);

            //todo delete
            //MailMessage mailMessage = new SimpleMailMessage();
            ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");
            executor.execute(() -> {
                        String htmlContent = getEmailHtml(toRegister.getNickname(), token);
                        final MimeMessage mimeMessage = mailSender.createMimeMessage();
                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
                            helper.setFrom(mailSender.getUsername());
                            //邮件主题
                            helper.setSubject("飞趣社区邮箱绑定");
                            //邮件接收者的邮箱地址
                            helper.setTo(toRegister.getUsername());
                            helper.setText(htmlContent, true);

                        } catch (MessagingException e) {
                            logger.error("用户{}发送邮件失败 ", toRegister.getUsername());
                        }
                        mailSender.send(mimeMessage);
                        CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(), toRegister.getId(), now,
                                "系统消息通知：欢迎你来到飞趣社区，希望你在这体验愉快！另外，你可以加入官方qq交流群：632118669,一起讨论。 " + DateUtil.formatDateTime(now));
                        //销毁验证码 防止外人利用这个验证码重复注册
                        CacheManager.removeCaptchaCode(ip);
                    }
            );
//发送邮件，参数可以是数组
            WebUtil.loginUser(request, response, toRegister, true);
        } catch (Exception e) {
            logger.error("注册失败{} ", user.getUsername());
            result.setResult(ResultEnum.FAIL);
        }

        return result;
    }

    private String encryptPassword(String password, String salt) {
        return DigestUtil.md5Hex(password+salt);
    }

    private String getSalt() {
        return RandomUtil.randomString(6);
    }

    @GetMapping("activate")
    public String activate(String token) {
        FqUserCache currUser = getCurrentUser();
        if (currUser == null) {
            return USER_LOGIN_REDIRECT_URL;
        }
        if (StringUtils.isNotBlank(token)) {
            UserActivateExample example = new UserActivateExample();
            example.createCriteria().andTokenEqualTo(token);
            UserActivate userActivate = userActivateService.selectFirstByExample(example);
            if (userActivate != null && userActivate.getUserId().equals(currUser.getId())) {
                FqUser toUpdate = new FqUser();
                toUpdate.setId(currUser.getId());
                toUpdate.setIsMailBind(YesNoEnum.YES.getValue());
                userService.updateByPrimaryKeySelective(toUpdate);
                logger.info("{} 邮箱绑定成功,token:{}", currUser.getUsername(),token);
                CacheManager.refreshUserCacheByUser(currUser);
            }else {
                logger.info("{} 邮箱绑定失败，token:{}", currUser.getUsername(),token);
            }
        }
        return "/user/activate";
    }

    @GetMapping("reSendEmail")
    @ResponseBody
    public Object reSendEmail(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request, response);
        if (!Validator.isEmail(currUser.getUsername())) {
            result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
            return result;
        }
        UserActivateExample example = new UserActivateExample();
        example.createCriteria().andUserIdEqualTo(currUser.getId());
        UserActivate userActivate = userActivateService.selectFirstByExample(example);
        String htmlContent = "";
        if (userActivate == null) {
            String token = RandomUtil.randomUUID();
            userActivate = new UserActivate(currUser.getId(), token, new Date());
            userActivateService.insert(userActivate);
            htmlContent = getEmailHtml(currUser.getNickname(), token);
        } else {
            htmlContent = getEmailHtml(currUser.getNickname(), userActivate.getToken());
        }

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
            helper.setFrom(mailSender.getUsername());
            //邮件主题
            helper.setSubject("邮箱绑定");
            //邮件接收者的邮箱地址
            helper.setTo(currUser.getUsername());
            helper.setText(htmlContent, true);

        } catch (MessagingException e) {
            logger.error("用户{}发送邮件失败 ", currUser.getUsername());
            result.setResult(ResultEnum.FAIL);
        }

        ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");
        executor.execute(new Runnable() {
            public void run() {
                mailSender.send(mimeMessage);
            }
        });
        return result;
    }

    @ResponseBody
    @PostMapping("updatePass")
    public Object updatePass(FqUser queryUser, HttpServletRequest request, HttpServletResponse response) {
        BaseResult baseResult = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request, response);
        if (currUser == null) {
            baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
            return baseResult;
        }
        if (!currUser.getId().equals(queryUser.getId())) {
            baseResult.setResult(ResultEnum.USER_NOT_SAME);
            return baseResult;
        }
        if (queryUser.getPassword().length() < 6) {
            baseResult.setResult(ResultEnum.PASSWORD_LENGTH_ERROR);
            return baseResult;
        }
        FqUser toUpdate = new FqUser();
        toUpdate.setId(queryUser.getId());
        toUpdate.setPassword(encryptPassword(queryUser.getPassword(),currUser.getSalt()));
        userService.updateByPrimaryKeySelective(toUpdate);
        return baseResult;
    }

    @GetMapping("forget")
    public String forget(HttpServletRequest request, String key) {
        if (StringUtils.isNotBlank(key)) {
            request.setAttribute("key", key);
            return "/user/passReset";
        }
        return "/user/passForget";
    }

    @PostMapping("resetPass")
    @ResponseBody
    public Object resetPass(HttpServletRequest request, String key, String password, String verifyCode) {
        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(key) || StringUtils.isBlank(verifyCode)) {
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }
        String username = "";
        try {
            String captchaCode = CacheManager.getCaptchaCode(WebUtil.getIP(request));
            if (!verifyCode.equals(captchaCode)) {
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }

            // 2.解密key
            String info = null;
            // cookie 私钥
            String secret = CommonConstant.FORGET_PASSWORD_SECRET;
            try {
                info = SecureUtil.des(secret.getBytes()).decryptStr(Base64.decode(key));
            } catch (RuntimeException e) {
                // ignore
            }
            String[] userInfo = info.split("~");
            username = userInfo[0];
            String beginTime = userInfo[1];
            long lastTime = System.currentTimeMillis() - Long.parseLong(beginTime);
            logger.info("密码重置过去了{}秒", lastTime / 1000);
            if (lastTime / 1000 > 30 * 60) {
                result.setResult(ResultEnum.TIME_EXPIRED);
                return result;
            }
            FqUserExample example = new FqUserExample();
            example.createCriteria().andUsernameEqualTo(username);
            FqUser fqUserDB = userService.selectFirstByExample(example);

            FqUser fqUser = new FqUser();
            fqUser.setPassword(encryptPassword(password,fqUserDB.getSalt()));
            example.clear();
            example.createCriteria().andUsernameEqualTo(username);
            userService.updateByExampleSelective(fqUser, example);
        } catch (Exception e) {
            logger.error("resetPass 密码重置失败，" + username, e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("forget")
    @ResponseBody
    public Object doForget(HttpServletRequest request, HttpServletResponse response, String username, String verifyCode) {
        BaseResult result = new BaseResult();
        try {
            if (!Validator.isEmail(username)) {
                result.setResult(ResultEnum.EMAIL_NOT_CORRECT);
                return result;
            }
            String captchaCode = CacheManager.getCaptchaCode(WebUtil.getIP(request));
            if (!verifyCode.equals(captchaCode)) {
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }
            FqUserExample example = new FqUserExample();
            example.createCriteria().andUsernameEqualTo(username);
            FqUser fqUser = userService.selectFirstByExample(example);
            if (fqUser == null) {
                result.setResult(ResultEnum.USER_NOT_FOUND);
                return result;
            }

            long now = System.currentTimeMillis();

            StringBuilder passForgetBuilder = new StringBuilder()
                    .append(username).append("~")
                    .append(now);

            // cookie 私钥
            String secret = CommonConstant.FORGET_PASSWORD_SECRET;
            // 加密cookie
            String key = SecureUtil.des(secret.getBytes()).encryptBase64(passForgetBuilder.toString());
            String htmlContent = getForgetEmailHtml(fqUser.getNickname(), key);
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
                helper.setFrom(mailSender.getUsername());
                //邮件主题
                helper.setSubject("密码重置");
                //邮件接收者的邮箱地址
                helper.setTo(username);
                helper.setText(htmlContent, true);
            } catch (MessagingException e) {
                logger.error("forget，用户{}发送邮件失败 ", username);
            }

            ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");
            executor.execute(() -> mailSender.send(mimeMessage));
        } catch (Exception e) {
            logger.error("重置密码邮件发送失败，username:{}", username);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
     * 更新个人信息
     * @param queryUser
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @PostMapping("updateUserInfo")
    public Object updateUserInfo(FqUser queryUser, HttpServletRequest request, HttpServletResponse response) {
        BaseResult baseResult = new BaseResult();
        FqUserCache currUser = webUtil.currentUser(request, response);
        if (currUser == null) {
            baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
            return baseResult;
        }
        if (!currUser.getId().equals(queryUser.getId())) {
            baseResult.setResult(ResultEnum.USER_NOT_SAME);
            return baseResult;
        }
        if (!Validator.isEmail(queryUser.getUsername())) {
            baseResult.setResult(ResultEnum.EMAIL_NOT_CORRECT);
            return baseResult;
        }
        if (StringUtils.isBlank(queryUser.getNickname().trim())) {
            baseResult.setResult(ResultEnum.PARAM_NULL);
            return baseResult;
        }
        if (StringUtils.length(queryUser.getCity()) > 20) {
            baseResult.setResult(ResultEnum.PARAM_ERROR);
            baseResult.setMessage("城市不能超过20个字符");
            return baseResult;
        }
        FqUserExample example = new FqUserExample();
        example.createCriteria().andNicknameEqualTo(queryUser.getNickname().trim()).andIdNotEqualTo(queryUser.getId());
        FqUser userDB = userService.selectFirstByExample(example);
        if (null != userDB) {
            baseResult.setResult(ResultEnum.NICKNAME_EXIST);
            return baseResult;
        }
        example.clear();
        example.createCriteria().andUsernameEqualTo(queryUser.getUsername().trim()).andIdNotEqualTo(queryUser.getId());
        userDB = userService.selectFirstByExample(example);
        if (null != userDB) {
            baseResult.setResult(ResultEnum.USERNAME_EXIST);
            return baseResult;
        }
        if (!queryUser.getUsername().equals(currUser.getUsername())) {
            queryUser.setIsMailBind(YesNoEnum.NO.getValue());
        }
        //bug 漏洞
        FqUser toUpdate = new FqUser();
        toUpdate.setId(queryUser.getId());
        toUpdate.setUsername(queryUser.getUsername());
        toUpdate.setNickname(queryUser.getNickname());
        toUpdate.setBirth(queryUser.getBirth());
        toUpdate.setCity(queryUser.getCity());
        toUpdate.setEducation(queryUser.getEducation());
        toUpdate.setSchool(queryUser.getSchool());
        toUpdate.setSex(queryUser.getSex());
        toUpdate.setIsSingle(queryUser.getIsSingle());
        toUpdate.setSign(queryUser.getSign());
        userService.updateByPrimaryKeySelective(toUpdate);
        FqUser fqUserNew = userService.selectByPrimaryKey(queryUser.getId());
        CacheManager.refreshUserCacheByUser(new FqUserCache(fqUserNew));
        return baseResult;
    }

    //私信
    @PostMapping("postMsg")
    @ResponseBody
    public Object postMsg(CMessage message, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        logger.info("私信:消息详情：{}", message.toString());
        try {
            Assert.notNull(message, "私信不能为空！");
            FqUserCache currUser = getCurrentUser();
            if (currUser == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (StringUtils.isBlank(message.getContent())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            message.setPostUserId(currUser.getId());
            message.setCreateTime(new Date());
            message.setType(MsgEnum.FRIEND_MSG.getValue());
            message.setDelFlag(YesNoEnum.NO.getValue());
            messageService.insert(message);
        } catch (Exception e) {
            logger.error("postMsg error", e);
            result.setMessage(e.getMessage());
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
        }
        return result;
    }

    @PostMapping("/clearMsgs")
    @ResponseBody
    public Object clearMsgs(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = null;
        try {
            result = new BaseResult();
            FqUserCache currUser = webUtil.currentUser(request, response);
            if (currUser == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("clearMsgs:清空消息列表，用户名：{}", currUser.getNickname());
            CMessageExample example = new CMessageExample();
            example.createCriteria().andReceivedUserIdEqualTo(currUser.getId());
            CMessage message = new CMessage();
            message.setDelFlag(YesNoEnum.YES.getValue());
            int count = messageService.updateByExampleSelective(message, example);
            result.setData(count);
        } catch (Exception e) {
            logger.error("清空信息失败", e);
        }
        return result;
    }

    @GetMapping("msgs")
    public String msgs(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(defaultValue = "0") Integer pageIndex,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        FqUserCache user = webUtil.currentUser(request, response);
        if (user == null) {
            return login(CommonConstant.DOMAIN_URL + request.getRequestURI(), request);
        }
        CMessageExample messageExample = new CMessageExample();
        messageExample.setOrderByClause("create_time desc");
        messageExample.createCriteria().andReceivedUserIdEqualTo(user.getId())
                .andDelFlagEqualTo(YesNoEnum.NO.getValue());
        PageHelper.startPage(pageIndex, pageSize);
        List<MessageUserDetail> messages = messageService.selectMyMsgsByMessage(messageExample);
        PageInfo page = new PageInfo(messages);
        request.setAttribute("count", page.getTotal());
        request.setAttribute("messages", messages);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("pageSize", pageSize);
        return "/user/msgs";
    }

    @PostMapping("msgsCount")
    @ResponseBody
    public Object msgsCount(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        FqUserCache user = webUtil.currentUser(request, response);
        if (user == null) {
            result.setData(0);
            return result;
        }
        CMessageExample example = new CMessageExample();
        example.createCriteria().andReceivedUserIdEqualTo(user.getId())
                .andDelFlagEqualTo(YesNoEnum.NO.getValue()).andIsReadNotEqualTo(YesNoEnum.YES.getValue());
        Integer count = messageService.countByExample(example);
        result.setData(count);
        return result;
    }

    /**
     * 主页
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/{userId}")
    public String myIndex(HttpServletRequest request, Model model,@PathVariable Integer userId) {
        FqUserCache user = getCurrentUser();
        if(userId <= 0){
            return GENERAL_NOT_FOUNF_404_URL;
        }
        if (null != user && user.getId().equals(userId)) {
            List<SuperBeauty> superBeauties = beautyService.selectRandom(CommonConstant.INDEX_IMG_RANDOM_NUM);
            model.addAttribute("beautySims", superBeauties);
            PageHelper.startPage(0, 10,false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("id desc");
            thoughtExample.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
            model.addAttribute("thoughts", thoughts);
            PageHelper.startPage(0, 10,false);
            ArticleExample articleExample = new ArticleExample();
            articleExample.setOrderByClause("create_time desc");
            articleExample.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<Article> articles = articleService.selectByExample(articleExample);
            model.addAttribute("articles", articles);
            return "/user/myIndex";
        }else {
            FqUser oUser = userService.selectByPrimaryKey(userId);
            if (oUser == null) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            request.setAttribute("oUser", oUser);
            request.setAttribute("user", user);
            if(user != null){
                FqVisitRecord visitRecord = new FqVisitRecord();
                visitRecord.setVisitedUserId(userId);
                visitRecord.setVisitUserId(user.getId());
                visitRecord.setVisitTime(new Date());
                visitRecord.setDelFlag(YesNoEnum.NO.getValue());
                visitRecordService.insert(visitRecord);
            }
            PageHelper.startPage(0, 10,false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("create_time desc");
            thoughtExample.createCriteria().andUserIdEqualTo(userId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
            request.setAttribute("thoughts", thoughts);
            PageHelper.startPage(0, 10,false);
            ArticleExample articleExample = new ArticleExample();
            articleExample.setOrderByClause("create_time desc");
            articleExample.createCriteria().andUserIdEqualTo(userId).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<Article> articles = articleService.selectByExample(articleExample);
            request.setAttribute("articles", articles);
            if (user == null) {
                request.setAttribute("follow", false);
            } else {
                UserFollowExample followExample = new UserFollowExample();
                followExample.createCriteria().andFollowerUserIdEqualTo(user.getId()).andFollowedUserIdEqualTo(oUser.getId())
                        .andDelFlagEqualTo(YesNoEnum.NO.getValue());
                UserFollow userFollow = userFollowService.selectFirstByExample(followExample);
                request.setAttribute("follow", userFollow != null);
            }
            return "/user/peopleIndex";
        }
    }

    @GetMapping("/center")
    public String center(Model model) {
        FqUserCache user = getCurrentUser();
        if (user == null) {
            return loginRedirect("/u/center");
        }
        try{
            String followKey = CommonConstant.FQ_FOLLOW_PREFIX + user.getId();
            String followStr = stringRedisTemplate.opsForValue().get(followKey);
            int fansCount;
            int followCount;
            if (StringUtils.isEmpty(followStr)) {
                UserFollowExample followExample = new UserFollowExample();
                followExample.createCriteria().andFollowedUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                fansCount = userFollowService.countByExample(followExample);
                followExample.clear();
                followExample.createCriteria().andFollowerUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                followCount = userFollowService.countByExample(followExample);
                stringRedisTemplate.opsForValue().set(followKey, fansCount + "|" + followCount,1, TimeUnit.DAYS);
            } else {
                String[] ff = followStr.split("\\|");
                fansCount = Integer.valueOf(ff[0]);
                followCount = Integer.valueOf(ff[1]);
            }
            model.addAttribute("fansCount", fansCount);
            model.addAttribute("followCount", followCount);

        }catch (Exception e){
            logger.error("用户中心失败",e);
            model.addAttribute(GENERAL_CUSTOM_ERROR_CODE,"加载失败");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/user/center";
    }

    @Resource
    private SuperBeautyService beautyService;


    private String getEmailHtml(String nickname, String token) {
        return "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.f2qu.com/\" target=\"_blank\">flyfun社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\">" + nickname + "</em>： </p> <p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\"> 欢迎你加入<em>flyfun社区</em>。请点击下面的按钮激活邮箱。 </p> " +
                "<div style=\"\"> <a href=\"" + CommonConstant.DOMAIN_URL + "/u/activate?token=" + token + "\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">立即激活邮箱</a> </div> <p style=\"line-height: 20px; margin-top: 20px; padding: 10px; background-color: #f2f2f2; font-size: 12px;\"> 如果该邮件不是由你本人操作，请勿进行激活！否则你的邮箱将会被他人绑定。 </p> </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";
    }

    private String getForgetEmailHtml(String nickname, String key) {
        return "<html><head>" +
                "<base target=\"_blank\">" +
                "<style type=\"text/css\">" +
                "::-webkit-scrollbar{ display: none; }" +
                "</style>" +
                "<style id=\"cloudAttachStyle\" type=\"text/css\">" +
                "#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}" +
                "</style>" +
                "</head>" +
                "<body tabindex=\"0\" role=\"listitem\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 600px; border: 1px solid #ddd; border-radius: 3px; color: #555; font-family: 'Helvetica Neue Regular',Helvetica,Arial,Tahoma,'Microsoft YaHei','San Francisco','微软雅黑','Hiragino Sans GB',STHeitiSC-Light; font-size: 12px; height: auto; margin: auto; overflow: hidden; text-align: left; word-break: break-all; word-wrap: break-word;\"> <tbody style=\"margin: 0; padding: 0;\"> <tr style=\"background-color: #393D49; height: 60px; margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 0;\"> <div style=\"color: #5EB576; margin: 0; margin-left: 30px; padding: 0;\"><a style=\"font-size: 14px; margin: 0; padding: 0; color: #5EB576; " +
                "text-decoration: none;\" href=\"http://www.flyfun.site/\" target=\"_blank\">flyfun社区</a></div> </td> </tr> " +
                "<tr style=\"margin: 0; padding: 0;\"> <td style=\"margin: 0; padding: 30px;\"> <p style=\"line-height: 20px; margin: 0; margin-bottom: 10px; padding: 0;\"> " +
                "你好，<em style=\"font-weight: 700;\">" + nickname + "</em>： </p> <p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\"> 请在30分钟内重置您的密码： </p> " +
                "<div style=\"\"> <a href=\"" + CommonConstant.DOMAIN_URL + "/u/forget?key=" + key + "\" style=\"background-color: #009E94; color: #fff; display: inline-block; height: 32px; line-height: 32px; margin: 0 15px 0 0; padding: 0 15px; text-decoration: none;\" target=\"_blank\">立即重置密码</a> </div> <p style=\"line-height: 20px; margin-top: 20px; padding: 10px; background-color: #f2f2f2; font-size: 12px;\"> 如果该邮件不是由你本人操作，请勿进行操作！</p> </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
                "<style type=\"text/css\">" +
                "body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}" +
                "td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}" +
                "pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}" +
                "th,td{font-family:arial,verdana,sans-serif;line-height:1.666}" +
                "img{ border:0}" +
                "header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}" +
                "blockquote{margin-right:0px}" +
                "</style>" +
                "<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>" +
                "</body></html>";
    }
}
