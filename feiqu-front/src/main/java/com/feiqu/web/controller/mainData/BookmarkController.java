package com.feiqu.web.controller.mainData;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.FileUploadUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.mainData.Bookmark;
import com.feiqu.system.model.mainData.BookmarkExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.BookmarkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * Bookmarkcontroller 书签
 * Created by cwd on 2020/1/16.
 */
@Controller
@RequestMapping("/bookmark")
public class BookmarkController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @Resource
    private BookmarkService bookmarkService;

    /**
     * 跳转到Bookmark首页
     */
    @RequestMapping("")
    public String index(Model model,String q) {
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return loginRedirect("/bookmark");
        }

        BookmarkExample bookmarkExample = new BookmarkExample();
        BookmarkExample.Criteria criteria = bookmarkExample.createCriteria();
        criteria.andUserIdEqualTo(fqUserCache.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        if(StrUtil.isNotEmpty(q)){
            criteria.andNameLike("%"+q+"%");
        }
        bookmarkExample.setOrderByClause("b_order desc");
        List<Bookmark> bookmarks = bookmarkService.selectByExample(bookmarkExample);
        model.addAttribute("bookmarks",bookmarks);
        return "/front/bookmark/index";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, MultipartFile file){
        BaseResult result = new BaseResult();
        String fileName = file.getOriginalFilename();
        File uploadFile = null;
        try {
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            long size = file.getSize();
            if(size >  1000 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传文件大小不要超过1M");
                return result;
            }
            String uploadPath = FileUploadUtils.upload(Global.getUploadPath(), file,FileUtil.extName(file.getOriginalFilename()));
            uploadFile = new File(uploadPath);
            String s = FileUtil.readString(uploadFile, CharsetUtil.UTF_8);
            Document document = Jsoup.parse(s);
            if(document == null){
                return error("未查到相关书签");
            }
            Elements elements = document.getElementsByTag("A");
            if(elements.isEmpty()){
                return error("未查到相关书签");
            }
            Date now = new Date();
            List<Bookmark> bookmarks = CollectionUtil.newLinkedList();
            for(Element element : elements){
                Bookmark bookmark = new Bookmark();
                bookmark.setUserId(fqUser.getId());
                bookmark.setGmtCreate(now);
                bookmark.setGmtUpdate(now);
                bookmark.setName(EmojiUtil.removeAllEmojis(element.text()));
                bookmark.setUrl(element.attr("HREF"));
                bookmarks.add(bookmark);
            }
            bookmarkService.saveBatch(bookmarks,fqUser.getId());
            logger.info("用户{}:上传书签，书签数量：{}",fqUser.getNickname(),bookmarks.size());
        } catch (Exception e) {
            logger.error("上传书签失败",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }finally {
            if (uploadFile != null) {
                boolean delete = uploadFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
        }
        return result;
    }


    /**
     * ajax删除Bookmark
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            bookmarkService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * ajax更新Bookmark
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(Bookmark bookmark) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (bookmark.getId() == null) {
                bookmark.setDelFlag(YesNoEnum.NO.getValue());
                bookmark.setGmtCreate(new Date());
                bookmark.setGmtUpdate(new Date());
                bookmark.setUserId(fqUserCache.getId());
                bookmark.setPid(0);
                bookmark.setLevel(1);
                bookmarkService.insert(bookmark);
            } else {
                bookmark.setGmtUpdate(new Date());
                bookmarkService.updateByPrimaryKeySelective(bookmark);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询Bookmark首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            BookmarkExample example = new BookmarkExample();
            example.setOrderByClause("gmt_create desc");
            List<Bookmark> list = bookmarkService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}