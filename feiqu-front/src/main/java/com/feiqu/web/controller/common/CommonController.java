package com.feiqu.web.controller.common;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.base.FqResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.constant.IconUrlConfig;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.h5.FqUserDTO;
import com.feiqu.system.pojo.h5.FqUserH5;
import com.feiqu.system.pojo.response.KeyValue;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.basicData.HolidayService;
import com.feiqu.system.service.mainData.FqUserService;
import com.feiqu.system.service.mainData.UploadImgRecordService;
import com.github.pagehelper.PageHelper;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/23.
 */
@Controller
@RequestMapping("api")
public class CommonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Resource
    private FqUserService fqUserService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private UploadImgRecordService uploadImgRecordService;

    @Resource
    private HolidayService holidayService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查看节日名称
     * @param dateStr
     * @return
     */
    @ResponseBody
    @PostMapping("holidayName")
    public Object holidayName(@RequestParam(required = false) String dateStr){
        if(StrUtil.isEmpty(dateStr)){
            String name = holidayService.holidayName(new Date());
            return success(name);
        }
        Date date = DateUtil.parse(dateStr);
        String name = holidayService.holidayName(date);
        return success(name);
    }

    @GetMapping("/userDetail/{userId}")
    @ResponseBody
    public Object userCard(@PathVariable Integer userId){
        FqUserCache fqUserCache = CacheManager.getUserCacheByUid(userId);
        if(fqUserCache != null){
            FqUserDTO fqUserDTO = new FqUserDTO(fqUserCache);
            return success(fqUserDTO);
        }
        return error("未查到相关用户");
    }

    @PostMapping("register")
    @ResponseBody
    @RepeatSubmit
    public Object registerDo(HttpServletRequest request, @RequestBody FqUserH5 user) {
        FqResult result = new FqResult();
        logger.info("registerDo:用户详情：{}",user.toString());
        try {
            if(!Validator.isEmail(user.getUsername())){
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
            if(StringUtils.isEmpty(user.getVerifyCode())){
                return result.fail("验证码不能为空!");
            }
            String ip = WebUtil.getIP(request);
            String key = "captcha:"+ip;
            String code =  stringRedisTemplate.opsForValue().get(key);
            if(!user.getVerifyCode().equals(code)){
                result.setResult(ResultEnum.VERIFY_CODE_NOT_CORRECT);
                return result;
            }

            FqUserExample FqUserExample = new FqUserExample();
            FqUserExample.createCriteria().andUsernameEqualTo(user.getUsername().trim());
            FqUser userDBUsername = fqUserService.selectFirstByExample(FqUserExample);
            if (userDBUsername != null) {
                result.setResult(ResultEnum.USERNAME_EXIST);
                return result;
            }
            FqUser toRegister = new FqUser();
            toRegister.init();
            toRegister.setNickname("飞趣"+RandomUtil.randomNumbers(6));
            toRegister.setUsername(user.getUsername().trim());
            toRegister.setPassword(user.getPassword().trim());
            Date now = new Date();
            toRegister.setCreateIp(ip);
            toRegister.setCreateTime(now);
            toRegister.setCity(CommonUtils.getRegionByIp(ip));
            String salt = CommonUtils.getSalt();
            toRegister.setSalt(salt);
            toRegister.setPassword(CommonUtils.encryptPassword(user.getPassword(),salt));
            if (StringUtils.isBlank(user.getIcon())) {
                toRegister.setIcon(RandomUtil.randomEle(IconUrlConfig.icons));
            }
            fqUserService.insert(toRegister);
            logger.info("注册成功，用户：{}", JSON.toJSONString(toRegister));
        } catch (MailException e) {
            logger.error("注册失败{} ",user.getUsername());
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public BaseResult dologin(HttpServletRequest request,HttpServletResponse response, @RequestBody FqUser user) {
        FqResult result = new FqResult();
        try {
            String ip = WebUtil.getIP(request);
            logger.info("h5 login:登陆用户详情：{},ip:{}",user.toString(),ip);
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
                result.fail("用户名和密码不能为空");
                return result;
            }
            FqUserExample FqUserExample = new FqUserExample();
            FqUserExample.createCriteria().andUsernameEqualTo(user.getUsername());
            FqUser userDB = fqUserService.selectFirstByExample(FqUserExample);

            if (userDB != null) {
                String ps = CommonUtils.encryptPassword(user.getPassword(),userDB.getSalt());
                if(!ps.equals(userDB.getPassword())){
                    result.fail("密码不正确");
                    return result;
                }
                if(UserStatusEnum.FROZEN.getValue().equals(userDB.getStatus())){
                    result.fail("用户被冻结");
                    return result;
                }
                WebUtil.loginUser(request, response, userDB, true);
                String token = CommonUtils.genToken(userDB.getId());
                FqUserSim fqUserSim = new FqUserSim(userDB);
                fqUserSim.setToken(token);
                result.setData(fqUserSim);
                return result;
            } else {
                result.fail("用户未查到");
                logger.info("用户未查到");
                return result;
            }
        } catch (Exception e) {
            logger.error("login error", e);
            result.fail("系统错误");
        }
        return result;
    }

    /**
     * 移动端上传普通的文件 图片 文本之类
     * @param request
     * @param file
     * @param picNum 已经上传的图片数量
     * @return
     */
    @PostMapping("/h5Upload")
    @ResponseBody
    public Object h5Upload(HttpServletRequest request, MultipartFile file,
                         @RequestParam(required = false) Integer picNum,String token){
        FqResult result = new FqResult();
        File localFile = null;
        String fileName = "";
        try {
            if(picNum != null && picNum >= 9){
                return result.fail("上传图片数量不能超过9张！");
            }
            if(StringUtils.isEmpty(token)){
                return result.fail("token不能为空");
            }
            long size = file.getSize();
            if(size >  10000 * 1024){
                return result.fail("上传文件大小不要超过10M！");
            }
            Integer userId = CommonUtils.getUIdFromToken(token);
            if(userId == null || userId <= 0){
                return result.fail("用户未登录");
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            if(fqUser == null){
                return result.fail("用户未查到");
            }
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyy-MM-dd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH+File.separator+time;
            File fileParent = new File(path);
            if(!fileParent.exists()){
                fileParent.mkdir();
            }
            String picUrl;
            String extName = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
            List<String> picExtList = CommonConstant.picExtList;
            if(!picExtList.contains(extName)){
                result.setResult(ResultEnum.PIC_URL_NOT_RIGHT);
                return result;
            }
            String fileNameFormat = DateUtil.format(now,"yyyyMMddHHmmss")+RandomUtil.randomNumbers(2);
            fileName = BizConstant.FILE_NAME_PREFIX + fileNameFormat + "." + extName;
            localFile = new File(fileParent,fileName);
            FileUtil.writeBytes(file.getBytes(),localFile);
            String picMd5 = DigestUtil.md5Hex(localFile);
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                picUrl = uploadImgRecord.getPicUrl();
            }else {
                picUrl = FileSystemClient.getClient(CommonConstant.FILE_SYSTEM_ALIYUN).upload(fileName, localFile);
//                picUrl+=CommonConstant.QINIU_PIC_STYLE_NAME;
                uploadImgRecord = new UploadImgRecord(picUrl,picMd5, now, WebUtil.getIP(request),fqUser.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(picUrl);
            logger.info("h5 用户{}:上传了一张照片：{}",fqUser.getNickname(),picUrl);
        } catch (Exception e) {
            logger.error("上传图片失败",e);
            result.fail("系统错误");
        }finally {
            if (localFile != null) {
                boolean delete = localFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
        }
        return result;
    }

    @ResponseBody
    @GetMapping("getRegionByIp")
    public String getRegionByIp(String ip){

        return CommonUtils.getRegionByIp(ip)+"-------"+CommonUtils.getFullRegionByIp(ip);
    }

    /**
     * 上传普通的文件 图片 文本之类
     * @param request
     * @param file
     * @param picNum 已经上传的图片数量
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, MultipartFile file,
                         @RequestParam(required = false) Integer picNum){
        BaseResult result = new BaseResult();
        File localFile = null;
        String fileName = "";
        File toFile = null;
        try {
            if(picNum != null && picNum >= 9){
                result.setResult(ResultEnum.SYSTEM_ERROR);
                result.setMessage("上传图片数量不能超过9张！");
                return result;
            }
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            long size = file.getSize();
            if(size >  10000 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传文件大小不要超过10M");
                return result;
            }
            String picUrl = "";
            String picMd5 = DigestUtil.md5Hex(file.getInputStream());
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                picUrl = uploadImgRecord.getPicUrl();
            }else {
                Date now = new Date();
                String extName = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
                List<String> picExtList = CommonConstant.picExtList;
                if(!picExtList.contains(extName)){
                    result.setResult(ResultEnum.PIC_URL_NOT_RIGHT);
                    return result;
                }
                String format = DateUtil.format(now,"yyyyMMddHHmmss");
                String fileNameFormat =format +RandomUtil.randomNumbers(2);
                fileName = BizConstant.FILE_NAME_PREFIX + fileNameFormat + "." + extName;
                //只要大于300kb 就给它压缩 哼 算了 不压缩了吧
                picUrl = FileSystemClient.getPublicClient().upload(fileName, file.getInputStream(),extName);
                uploadImgRecord = new UploadImgRecord(picUrl,picMd5, now, WebUtil.getIP(request),fqUser.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(picUrl);
            logger.info("用户{}:上传了一张照片：{}",fqUser.getNickname(),picUrl);
        } catch (Exception e) {
            logger.error("上传图片失败",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }finally {
            if (localFile != null) {
                boolean delete = localFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
            if (toFile != null) {
                boolean delete = toFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
        }
        return result;
    }

    @PostMapping("uploadVideo")
    @ResponseBody
    public Object uploadVideo(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
        BaseResult result = new BaseResult();
        String fileName = "";
        File localFile = null;
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if(fqUser.getLevel() < 3){
                result.setResult(ResultEnum.PARAM_ERROR);
                result.setMessage("用户等级不足3级！");
                return result;
            }
            long size = file.getSize();
            if(size >  20 * 1024 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传视频文件大小不要超过20M");
                return result;
            }
            Date now = new Date();
            String time = DateFormatUtils.format(now,"yyyyMMdd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH+File.separator+time;
            String videoUrl;
            String extName = FileUtil.extName(file.getOriginalFilename());
            fileName = BizConstant.FILE_NAME_PREFIX + DateUtil.format(now, "yyyyMMddHHmmss") + "." + extName;
            localFile = new File(path,fileName);
            if(!localFile.exists()){
                localFile.mkdirs();
            }
            //MultipartFile自带的解析方法
            file.transferTo(localFile);

            String videoMd5 = DigestUtil.md5Hex(file.getBytes());
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(videoMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                videoUrl = uploadImgRecord.getPicUrl();
            }else {
                videoUrl = FileSystemClient.getClient(CommonConstant.FILE_SYSTEM_ALIYUN).upload("video/"+fileName,localFile);
//                aliyunOssClient.putObject(CommonConstant.ALIYUN_OSS_BUCKET_NAME,"video/"+fileName,file.getInputStream());
//                videoUrl = CommonConstant.ALIOSS_URL_PREFIX+"/video/"+fileName;
                uploadImgRecord = new UploadImgRecord(videoUrl,videoMd5,new Date(), WebUtil.getIP(request),fqUser.getId(), size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(videoUrl);
        } catch (IOException e) {
            logger.error("上传视频失败",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("findActiveUserNames")
    @ResponseBody
    public Object findActiveUserNames(){
        BaseResult result = new BaseResult();
        try {int month = DateUtil.thisMonth()+1;
            String key = "activeUserNames"+month;
            Set<String> activeNames = stringRedisTemplate.opsForSet().members(key);
            if(CollectionUtil.isEmpty(activeNames)){
                Set<String> userIds =stringRedisTemplate.opsForZSet().reverseRangeByScore(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,9);
                if(CollectionUtils.isNotEmpty(userIds)){
                    List<Integer> userIdList = Lists.newArrayList();
                    for(String userId : userIds){
                        userIdList.add(Integer.valueOf(userId));
                    }
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andIdIn(userIdList);
                    List<FqUser> fqUsers = fqUserService.selectByExample(example);
                    List<String> names = fqUsers.stream().map(FqUser::getNickname).collect(Collectors.toList());
                    result.setData(names);
                    stringRedisTemplate.opsForSet().add(key,names.toArray(new String[names.size()]));
                    stringRedisTemplate.expire(key,7, TimeUnit.DAYS);
                }
            }else {
                result.setData(activeNames);
            }
        } catch (Exception e) {
            logger.error("获取活跃用户信息报错",e);
        }
        return result;
    }

    @PostMapping("findUsersByName")
    @ResponseBody
    public Object findUsersByName(String username){
        BaseResult result = new BaseResult();
        try {
            if(StringUtils.isEmpty(username)){
                return result;
            }
            List<KeyValue> simUser = Lists.newArrayList();
            FqUserExample example = new FqUserExample();
            FqUserExample.Criteria criteria1=  example.createCriteria();
            criteria1.andUsernameLike("%"+username+"%");
            FqUserExample.Criteria criteria2=  example.createCriteria();
            criteria2.andNicknameLike("%"+username+"%");
            example.or(criteria2);
            PageHelper.startPage(1,10,false);
            List<FqUser> fqUsers = fqUserService.selectByExample(example);
            if(CollectionUtils.isNotEmpty(fqUsers)){
                fqUsers.forEach(fqUser -> {
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(fqUser.getId().toString());
                    keyValue.setValue(fqUser.getUsername()+"("+fqUser.getNickname()+")");
                    simUser.add(keyValue);
                });
            }
            result.setData(simUser);
        } catch (Exception e) {
            logger.error("获取用户信息报错",e);
        }
        return result;
    }
}
