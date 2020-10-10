package com.feiqu.web.controller.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.TopicTypeEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FqMusicResponse;
import com.feiqu.system.service.mainData.FqMusicService;
import com.feiqu.system.service.mainData.GeneralLikeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/24.
 */
@Controller
@RequestMapping("music")
public class MusicController extends BaseController{

    private final static Logger logger = LoggerFactory.getLogger(MusicController.class);

    @Resource
    private FqMusicService musicService;

    @Resource
    private WebUtil webUtil;
    @Resource
    private GeneralLikeService likeService;

    /*@Resource
    private OSSClient aliyunOssClient;*/

    @ResponseBody
    @RequestMapping("delete")
    public Object delete(Integer id){
        Assert.notNull(getCurrentUser());
        FqMusic toUpdate = new FqMusic();
        toUpdate.setId(id);
        toUpdate.setDelFlag(YesNoEnum.YES.getValue());
        musicService.updateByPrimaryKeySelective(toUpdate);
        return success();
    }

    /*
    我的文章
     */
    @GetMapping(value = "/my")
    public String myArticles(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(defaultValue = "1") Integer page,
                             Model model) {
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                return USER_LOGIN_REDIRECT_URL;
            }
            PageHelper.startPage(page, 20);
            FqMusicExample example = new FqMusicExample();
            example.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<FqMusicResponse> musics = musicService.selectWithUserByExample(example);

            PageInfo pageInfo = new PageInfo(musics);
            FqLabelExample labelExample = new FqLabelExample();
            labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());

            model.addAttribute("musics", musics);
            model.addAttribute("count", pageInfo.getTotal());
            model.addAttribute("p", page);
            model.addAttribute("pageSize", 20);
        } catch (Exception e) {
            logger.error("获取我的音乐失败！", e);
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/music/musics";
    }

    @GetMapping({"","index"})
    public String index(HttpServletRequest request, HttpServletResponse response, Integer page){
        FqMusicExample example = new FqMusicExample();
        page = page == null? 1: page;
        PageHelper.startPage(page,CommonConstant.DEAULT_PAGE_SIZE);
        example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        example.setOrderByClause("create_time desc");
        List<FqMusicResponse> list= musicService.selectWithUserByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        request.setAttribute("list",list);
        request.setAttribute("count",pageInfo.getTotal());
        request.setAttribute("p",pageInfo.getPageNum());
        request.setAttribute("pageSize",CommonConstant.DEAULT_PAGE_SIZE);
        //拿前三 todo
        return "/music/index";
    }

    @ResponseBody
    @PostMapping("upload")
    public Object upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
        BaseResult result = new BaseResult();
        try{
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            long size = file.getSize();
            if(size >  10*1000 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传文件大小不要超过10M");
                return result;
            }
            String musicUrl = "";
            String extName = FileUtil.extName(file.getOriginalFilename());
            String time = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String path = CommonConstant.FILE_UPLOAD_TEMP_PATH + File.separator + time;
            String fileName = BizConstant.FILE_NAME_PREFIX + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "." + extName;
            File localFile = new File(path, fileName);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            //MultipartFile自带的解析方法
            file.transferTo(localFile);
            musicUrl = FileSystemClient.getClient("aliyun").upload("music/"+fileName,localFile);
            result.setData(musicUrl);
        }catch (Exception e){
            logger.error("upload music error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "uploadMusic")
    public Object uploadMusic(FqMusic music, HttpServletRequest request, HttpServletResponse response){
        BaseResult baseResult = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request,response);
            if(fqUser == null){
                baseResult.setResult(ResultEnum.USER_NOT_LOGIN);
                return baseResult;
            }
            String musicUrl = music.getMusicUrl();
            if(StringUtils.isBlank(musicUrl)|| StringUtils.isBlank(music.getMusicName()) || StringUtils.isBlank(music.getSinger())){
                baseResult.setResult(ResultEnum.PARAM_NULL);
                return baseResult;
            }
            if(music.getMusicName().length() > 250){
                baseResult.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                return baseResult;
            }
            if(music.getLyric()!= null && music.getLyric().length() > 6000){
                baseResult.setResult(ResultEnum.Lyric_TOO_LONG);
                return baseResult;
            }
            String musicExt = FileUtil.extName(musicUrl);
            List<String> musicExtList = Arrays.asList("mp3","wav","mid");
            if(!musicExtList.contains(musicExt)){
                baseResult.setResult(ResultEnum.SONG_URL_NOT_RIGHT);
                return baseResult;
            }
            FqMusicExample example = new FqMusicExample();
            example.createCriteria().andMusicUrlEqualTo(musicUrl).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqMusic musicDB = musicService.selectFirstByExample(example);
            if(musicDB != null){
                baseResult.setResult(ResultEnum.MP3_URL_ALREADY_EXIST);
                return baseResult;
            }
            music.setCreateTime(new Date());
            music.setUserId(fqUser.getId());
            music.setDelFlag(YesNoEnum.NO.getValue());
            musicService.insert(music);
            baseResult.setResult(ResultEnum.SUCCESS);
            baseResult.setData(music);
        } catch (Exception e) {
            logger.error("uploadMP3 error",e);
            baseResult.setResult(ResultEnum.FAIL);
        }
        return baseResult;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer musicId, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqMusic music = musicService.selectByPrimaryKey(musicId);
            if(music == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(musicId).andTopicTypeEqualTo(TopicTypeEnum.MUSIC_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(musicId,TopicTypeEnum.MUSIC_TYPE.getValue(),1,user.getId(),new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);
            music.setLikeCount(music.getLikeCount() == null? 1: music.getLikeCount()+1);
            musicService.updateByPrimaryKey(music);
            result.setData(music.getLikeCount());
        } catch (Exception e) {
            logger.error("music like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @GetMapping("{musicId}")
    public Object detail(@PathVariable Integer musicId, HttpServletRequest request, HttpServletResponse response){
        try {


            FqMusic music = musicService.selectByPrimaryKey(musicId);
            FqMusic nextMusic = musicService.selectByPrimaryKey(musicId-1);
            FqMusic lastMusic = musicService.selectByPrimaryKey(musicId+1);
            //下一首
            //上一首

            if(music != null){
                request.setAttribute("music",music);
                request.setAttribute("nextMusic",nextMusic);
                request.setAttribute("lastMusic",lastMusic);
                request.setAttribute("oUser", CacheManager.getUserCacheByUid(music.getUserId()));
            }else {
                return "/music/404";
            }
        } catch (Exception e) {
            logger.error("music detail error",e);
        }
        return "/music/detail";
    }

    @PostMapping("play")
    @ResponseBody
    public Object playCount(Integer musicId){
        BaseResult result = new BaseResult();
        FqMusic music = musicService.selectByPrimaryKey(musicId);
        if(music == null){
            logger.error("music为空 音乐播放记录失败");
            return result;
        }
        music.setPlayCount(music.getPlayCount() == null? 1 : music.getPlayCount() + 1);
        musicService.updateByPrimaryKey(music);
        return result;
    }
}
