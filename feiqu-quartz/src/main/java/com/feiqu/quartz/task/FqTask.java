package com.feiqu.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.GirlCategoryEnum;
import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.exception.job.TaskException;
import com.feiqu.common.utils.HttpClientUtil;
import com.feiqu.quartz.spider.TopicInfoPipeline;
import com.feiqu.quartz.spider.V2exDTO;
import com.feiqu.quartz.util.CommonUtils;
import com.feiqu.system.model.*;
import com.feiqu.system.model.collectData.LogData;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.pojo.response.wangyi.NewsResponse;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.collectData.LogDataService;
import com.feiqu.system.service.mainData.*;
import com.github.pagehelper.PageHelper;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("fqTask")
public class FqTask {
    private static Logger logger = LoggerFactory.getLogger(FqTask.class);

    @Resource
    private NginxLogService logService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private FqFriendLinkService fqFriendLinkService;
    @Resource
    private FqBackgroundImgService fqBackgroundImgService;
    @Resource
    private SuperBeautyService superBeautyService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private FqUserActiveNumService fqUserActiveNumService;
    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private TopicInfoPipeline topicInfoPipeline;
    @Resource
    private FqTopicService fqTopicService;
    @Resource
    private FqNewsService fqNewsService;
    @Resource
    private FqGoodPicService fqGoodPicService;
    @Resource
    private FqNoticeService fqNoticeService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";

    //每天凌晨 移除所有的黑名单
    public void clean(){
        Boolean ok = stringRedisTemplate.delete(BizConstant.FQ_BLACK_LIST_REDIS_KEY);
    }

    /**
     * https://www.xiaoheihe.cn/community/7216/list（源数据地址）
     * 绝地求生生成的数据 爬虫
     */
    public void jdqs(String page) {
        if (StringUtils.isEmpty(page)) {
            page = "0";
        }
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("limit", "20");
        map.put("offset", page);
        map.put("topic_id", "7216");
        map.put("sort_filter", "reply");
        map.put("type_filter", "all");
        map.put("os_type", "web");
        map.put("version", "999.0.0");
        map.put("hkey", "d40441b8ab455080e17d64ab01c578ab");
        map.put("_time", new Date().getTime() / 1000 + "");
        String s = HttpUtil.createGet("https://api.xiaoheihe.cn/bbs/web/link/list")
                .header("User-Agent", USER_AGENT).header("Referer", "https://www.xiaoheihe.cn/community/7216/list").form(map).execute().body();
        JSONObject jsonObject = new JSONObject(s);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray links = result.getJSONArray("links");
        FqTopicExample topicExample = new FqTopicExample();
        Date now = new Date();
        for (Object link : links) {
            try {
                topicExample.clear();
                JSONObject ljson = (JSONObject) link;
                String title = ljson.getStr("title");
                int comment_num = ljson.getInt("comment_num");
                topicExample.createCriteria().andTitleEqualTo(title);
                int count = fqTopicService.countByExample(topicExample);
                if (count > 0) {
                    continue;
                }
                Integer linkid = ljson.getInt("linkid");
                map.put("link_id", linkid);
                String detail = HttpUtil.createGet("https://api.xiaoheihe.cn/bbs/web/link/detail").header("User-Agent", USER_AGENT).header("Referer", "https://www.xiaoheihe.cn/community/7216/list").form(map).execute().body();

                JSONObject detailjson = new JSONObject(detail);
                String text = detailjson.getJSONObject("result").getJSONObject("link").getStr("text");

                JSONObject textJson = (JSONObject) JSONUtil.parseArray(text).get(0);
                String textreal = textJson.getStr("text");
                String description = ljson.getStr("description");
                FqTopic fqTopic = new FqTopic();
                fqTopic.setAuthor("");
                if (description.length() > 100) {
                    description = description.substring(0, 99);
                }
                fqTopic.setAuthorIcon(EmojiUtil.removeAllEmojis(description));
                fqTopic.setCommentCount(comment_num);
                fqTopic.setContent(textreal);
                fqTopic.setSource("小黑盒");
                fqTopic.setType("绝地求生");
                fqTopic.setTitle(title);
                fqTopic.setGmtCreate(now);
                fqTopicService.insert(fqTopic);
                map.put("owner_only", 0);
                map.put("sort_filter", "");
                String tree = HttpUtil.createGet("https://api.xiaoheihe.cn/bbs/app/link/tree")
                        .header("User-Agent", USER_AGENT).header("Referer", "https://www.xiaoheihe.cn/community/7216/list").form(map).execute().body();
                JSONObject treeJson = JSONUtil.parseObj(tree);
                JSONArray comments = treeJson.getJSONObject("result").getJSONArray("comments");
                if (!comments.isEmpty()) {
                    comments.forEach(comment -> {
                        JSONObject cj = (JSONObject) comment;
                        JSONObject jj = (JSONObject) cj.getJSONArray("comment").get(0);
                        //                    Console.log(jj.getStr("text"));
                        FqTopicReply fqTopicReply = new FqTopicReply();
                        fqTopicReply.setGmtCreate(now);
                        fqTopicReply.setContent(jj.getStr("text"));
                        fqTopicReply.setTopicId(fqTopic.getId());
                        fqTopicService.insertFqTopicReply(fqTopicReply);
                    });
                }
                //模拟睡眠 防止被发现
                ThreadUtil.sleep(3, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.error("绝地求生爬虫失败！", e);
            }
        }
    }


    /**
     * 通知
     *
     * @param msg
     */
    public void notifyOrder(String msg) {
        try {
            int userIdInt = 22;
            Date now = new Date();
            CommonUtils.sendOfficialMsg(userIdInt, now, msg);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailSender.getUsername());
            simpleMailMessage.setTo("573047307@qq.com");
            simpleMailMessage.setText(msg);
            simpleMailMessage.setSubject("自动通知");
            mailSender.send(simpleMailMessage);
        } catch (Exception e) {
            logger.error("自动通知失败", e);
        }
    }

    public void goodpic(String path) {
//        File file = new File("F:\\1024imgs");
        try {
            path = StringUtils.isEmpty(path) ? "/home/chenweidong/1024imgs" : path;
            File file = new File(path);
            if (!file.exists()) {
                throw new TaskException("文件夹不存在", TaskException.Code.CONFIG_ERROR);
            }
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            File randomImgFile = RandomUtil.randomEle(files);
            logger.info(randomImgFile.getName());
            File[] imgFiles = randomImgFile.listFiles();
            logger.info("总共多少文件夹：{}，该文件夹下面多少图片：{}", files.length, imgFiles.length);
            /*int randomImgNum = imgFiles.length > 20?20:10;
            List<File> randomImgFiles = RandomUtil.randomEles(Arrays.asList(imgFiles),randomImgNum);*/

            Date now = new Date();
            String format = DateUtil.format(now, "yyyy/MM/dd/HHmmss");
            StringJoiner stringJoiner = new StringJoiner(",");
            String fileName = "";
            int i = 1;
            //            File destFile = new File("F:\\1024imgs\\test.jpg");
//            File destFile = new File("/home/chenweidong/test/test.jpg");
            for (File randomImgFile1 : imgFiles) {
                String extName = FileUtil.extName(randomImgFile1);
//                BufferedImage bufferedImage = ImageUtil.read(randomImgFile1);
//                Rectangle rectangle = new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() - 40);
//                ImageUtil.cut(bufferedImage, destFile, rectangle);
//                logger.info("randomImgFile1 大小：{}", FileUtil.size(randomImgFile1));
                String fileNameFormat = format + i;
                fileName = fileNameFormat + "." + extName;
                String imgUrl = FileSystemClient.getClient(BizConstant.FILE_SYSTEM_ALIYUN).upload(fileName, randomImgFile1);
                stringJoiner.add(imgUrl);
                i++;
                randomImgFile1.delete();
            }

            FqGoodPic fqGoodPic = new FqGoodPic();
            fqGoodPic.setGmtCreate(now);
            fqGoodPic.setTitle(randomImgFile.getName());
            fqGoodPic.setPicUrlList(stringJoiner.toString());
            fqGoodPicService.insert(fqGoodPic);
            logger.info(JSON.toJSONString(fqGoodPic));
            boolean delete = FileUtil.del(randomImgFile);
            logger.info("是否删除成功{}", delete);
        } catch (Exception e) {
            logger.error("自动生成图片出错", e);
        }
    }

    //    @Scheduled(cron = "0 0/30 9-18 * * ? ")//从9点到18点 每半个小时更新一次
//    @Scheduled(cron = "0 51 15 * * ? ")
    public void contentUpdateWork() {
        StopWatch stopwatch = StopWatch.createStarted();
        try {
            PageHelper.startPage(1, 10, false);
            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("create_time desc");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<ThoughtWithUser> newThoughts = thoughtService.getThoughtWithUser(thoughtExample);
            if (CollectionUtil.isNotEmpty(newThoughts)) {
                newThoughts.forEach(t -> {
                    if (StrUtil.isNotEmpty(t.getPicList())) {
                        t.setPictures(Arrays.asList(t.getPicList().split(",")));
                    }
                });
            }
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(BizConstant.CACHE_NEW_THOUGHT_LIST, JSON.toJSONString(newThoughts));
//            BizConstant.CACHE_NEW_THOUGHT_LIST = newThoughts;

        } catch (Exception e) {
            logger.error("", e);
        }


        stopwatch.stop();
        String time = stopwatch.toSplitString();
        logger.info("热门文章以及通知以及热门图片更新完毕,耗时:{}", time);
    }

    //每3个小时更新一次 3-》2
//    @Scheduled(cron = "1 0 */2 * * ? ")
    public void newsWork() {
        String result = null;
        int loopSize = 10, loopCount = 10, loopIndex = 0;
        String url = "https://3g.163.com/touch/reconstruct/article/list/BBM54PGAwangning/";
        String suffix = "-10.html";
        int filterNum = 0;
        int sameTitleNum = 0;
        int insertNum = 0;
        try {
            Date now = new Date();
            FqNewsExample fqNewsExample = new FqNewsExample();

            while (loopIndex <= loopCount) {
                String realUrl = url + loopSize * loopIndex + suffix;
                logger.info(realUrl);
                result = HttpClientUtil.getWebPage(realUrl);
                int index = result.indexOf(":");
                result = result.substring(index + 1, result.lastIndexOf("]") + 1);

                List<NewsResponse> newsResponseList = JSON.parseArray(result, NewsResponse.class);
                if (CollectionUtils.isEmpty(newsResponseList)) {
                    logger.info(result);
                    filterNum += 10;
                    continue;
                }
                for (NewsResponse newsResponse : newsResponseList) {
                    if (newsResponse.getCommentCount() < 1000) {
                        filterNum += 1;
                        continue;
                    }
                    if (StrUtil.isEmpty(newsResponse.getUrl())) {
                        filterNum += 1;
                        continue;
                    }
                    if (newsResponse.getUrl().startsWith("00")) {
                        filterNum += 1;
                        continue;
                    }
                    if (newsResponse.getUrl().contains("mp.weixin.qq.com") || newsResponse.getUrl().contains("photoview") || newsResponse.getUrl().contains("ithome")) {
                        filterNum += 1;
                        continue;
                    }
                    fqNewsExample.clear();
                    fqNewsExample.createCriteria().andTitleEqualTo(newsResponse.getTitle());
                    int count = fqNewsService.countByExample(fqNewsExample);
                    if (count > 0) {
                        filterNum += 1;
                        sameTitleNum += 1;
                        continue;
                    }
                    String result2 = null;
                    try {
                        result2 = HttpClientUtil.getWebPage(newsResponse.getUrl());
                    } catch (Exception e) {
                        logger.error("详情报错：" + newsResponse.getUrl(), e);
                        filterNum += 1;
                        continue;
                    }
                    result2 = result2.substring(result2.indexOf("<article"), result2.lastIndexOf("</article>") + 10);
//                    result2 = result2.substring(0, result2.lastIndexOf("<div class=\"footer\">"));
//                    result2 += "</article>";
                    result2 = result2.replaceAll("data-src", "src");

                    FqNews fqNews = new FqNews(newsResponse);
                    fqNews.setGmtCreate(now);
                    fqNews.setCommentCount(0);
                    fqNews.setContent(result2);
                    fqNewsService.insert(fqNews);
                    //https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/E1HA59FP0001899N/comments/hotList?offset=0&limit=2&headLimit=3&tailLimit=1&callback=hotList&ibc=newswap
                    //https://comment.api.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/E1HE234A0001899N/comments/hotList?offset=0&limit=2&headLimit=3&tailLimit=1&callback=hotList&ibc=newswap
                    //抓取热门评论
                    //hotList(
                    //{"commentIds":["336761335","336764402"],"comments":{"336764402":{"against":1,"anonymous":false,"buildLevel":1,"commentId":336764402,"content":"饿狗","createTime":"2018-11-26 10:11:18","ext":{},"favCount":0,"ip":"39.181.*.*","isDel":false,"postId":"E1HE234A0001899N_336764402","productKey":"a2869674571f77b5a0867c3d71db5856","shareCount":0,"siteName":"网易","source":"ph","unionState":false,"user":{"avatar":"http://mobilepics.nosdn.127.net/netease_subject/ee4b25e1484e81d0a57a64ee886903701541929401968170","incentiveInfoList":[],"location":"浙江省台州市","nickname":"秦淮颂金陵美","redNameInfo":[],"userId":105372094,"userType":1,"vipInfo":"vipy"},"vote":895},"336761335":{"against":0,"anonymous":false,"buildLevel":1,"commentId":336761335,"content":"不得不佩服饿渣就是强","createTime":"2018-11-26 10:04:37","favCount":0,"ip":"101.233.*.*","isDel":false,"postId":"E1HE234A0001899N_336761335","productKey":"a2869674571f77b5a0867c3d71db5856","shareCount":0,"siteName":"网易","source":"ph","unionState":false,"user":{"avatar":"http://mobilepics.nosdn.127.net/netease_subject/1cd65ab0c6ec53f43ef80ab572b7420a1525585493339128","incentiveInfoList":[],"location":"广东省深圳","nickname":"身上带有X","redNameInfo":[],"userId":640066,"userType":1,"vipInfo":""},"vote":685}}});
                    insertNum++;
                }
                loopIndex++;
            }
        } catch (Exception e) {
            logger.error("新闻更新报错", e);
        } finally {
        }
        logger.info("过滤掉的新闻条数：{}，过滤掉相同标题的新闻条数：{}，插入新闻条数：{}，新闻更新完毕,耗时{}秒", filterNum, sameTitleNum, insertNum);
    }

    //爬虫
//    @Scheduled(cron = "0 3 */6 * * ?")
    public void spider() {
        int result = 0;
        try {
            OOSpider ooSpider = OOSpider.create(Site.me()
                            .setUserAgent(BizConstant.userAgentArray[new Random().nextInt(BizConstant.userAgentArray.length)])
                            .addHeader("Referer", "https://www.v2ex.com/").setSleepTime(10000).setDomain("v2ex.com"),
                    topicInfoPipeline, V2exDTO.class);
            ooSpider.addUrl("https://www.v2ex.com/?tab=hot")
                    .run();
            FqTopicExample example = new FqTopicExample();
            //少于10条评论的 全部删除
            example.createCriteria().andCommentCountLessThan(10);
            result = fqTopicService.deleteByExample(example);
            fqTopicService.deleteSameData();
        } catch (Exception e) {
            logger.error("爬虫出错", e);
        } finally {
        }
        logger.info("爬虫数据更新完毕,删除数据：{}", result);
    }


    public void sendLogEmail() {

        Date now = new Date();
//        DateUtil.offsetDay(now, -1).toString("yyyyMMdd");

        Date begin = DateUtils.addHours(now, -8);
        Date end = DateUtils.addHours(now, -6);

        NginxLogExample example = new NginxLogExample();
        example.createCriteria().andCreateTimeBetween(begin, end).andSpiderTypeEqualTo(0);
        Integer pv = logService.countByExample(example);
        long uv = logService.countUvByExample(example);

        //1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(1);
        long baiduzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(2);
        long googlezhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(3);
        long bingzhizhu = logService.countByExample(example);
        example.clear();
        example.createCriteria().andCreateTimeBetween(begin, end)
                .andSpiderTypeEqualTo(4);
        long sougouzhizhu = logService.countByExample(example);

        String clickkey = BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX + DateUtil.offsetDay(now, -1).toString("yyyy-MM-dd");
        Long count = stringRedisTemplate.opsForZSet().count(clickkey,1,1000);

        String msg = StrUtil.format("昨天的访问统计：pv:{},uv:{},ip:{}",pv,uv,count);
        CommonUtils.sendOfficialMsg(22, now, msg);

        String htmlContent = getEmailHtml(DateUtil.offsetDay(now, -1).toString("yyyy-MM-dd") + "的PV UV", pv, uv, baiduzhizhu,
                googlezhizhu, bingzhizhu, sougouzhizhu,count);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");
            helper.setFrom(mailSender.getUsername());
            //邮件主题
            helper.setSubject("社区前一天的PVUV");
            String[] tos = {"573047307@qq.com", "610589771@qq.com", "758991933@qq.com", "571148352@qq.com"};
            //邮件接收者的邮箱地址
            helper.setTo(tos);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("发送邮件失败 ", e);
        } finally {
        }
    }

    private String getEmailHtml(String time, long pv, long uv, long baiduzhizhu, long googlezhizhu, long bingzhizhu,
                                long sougouzhizhu, Long count) {
        String html = "<html><head>" +
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
                "你好，<em style=\"font-weight: 700;\">飞趣社区开发者</em>： </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  " + time + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  PV:" + pv + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  UV:" + uv + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  ip数量:" + count + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  百度蜘蛛爬取次数:" + baiduzhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  google蜘蛛爬取次数:" + googlezhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  bing蜘蛛爬取次数:" + bingzhizhu + " </p> " +
                "<p style=\"line-height: 2; margin: 0; margin-bottom: 10px; padding: 0;\">  搜狗蜘蛛爬取次数:" + sougouzhizhu + " </p> " +
                "<div style=\"\"></div>  </td> </tr> <tr style=\"background-color: #fafafa; color: #999; height: 35px; margin: 0; padding: 0; text-align: center;\"> <td style=\"margin: 0; padding: 0;\">系统邮件，请勿直接回复。</td> </tr> </tbody> </table>" +
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
        return html;
    }

    //活跃度计算
    public void activeCount() {
        try {
            Date now = new Date();
            int day = DateUtil.dayOfMonth(now);
            DateTime tbegin = DateUtil.beginOfDay(now);
//            DateTime ybegin = DateUtil.beginOfDay(DateUtil.yesterday());
            boolean isfirstDay = day == 1;
            int month = DateUtil.thisMonth() + 1;
            Set<String> userIds = redisTemplate.opsForZSet().reverseRange(BizConstant.FQ_ACTIVE_USER_SORT + month, 0, -1);
            if (CollectionUtil.isNotEmpty(userIds)) {
                List<Integer> userIdList = Lists.newArrayList();
                for (String userId : userIds) {
                    userIdList.add(Integer.valueOf(userId));
                }
                FqUserExample fqUserExample = new FqUserExample();
                fqUserExample.createCriteria().andIdIn(userIdList);
                List<FqUser> fqUsers = fqUserService.selectByExample(fqUserExample);
                Map<Integer, FqUser> userMap = MapUtil.newHashMap();
                fqUsers.forEach(fqUser -> {
                    userMap.put(fqUser.getId(), fqUser);
                });
                FqUserActiveNumExample example = new FqUserActiveNumExample();
                for (int i = 0; i < userIdList.size(); i++) {
                    Double score = redisTemplate.opsForZSet().score(BizConstant.FQ_ACTIVE_USER_SORT + month, userIdList.get(i).toString());
                    if (isfirstDay) {
                        FqUserActiveNum fqUserActiveNum = new FqUserActiveNum();
                        fqUserActiveNum.setGmtCreate(now);
                        fqUserActiveNum.setActiveNum(score == null ? 0 : score.intValue());
                        fqUserActiveNum.setUserId(userIdList.get(i));
                        fqUserActiveNum.setMark(userMap.get(userIdList.get(i)).getNickname() + ":当天活跃度：" + fqUserActiveNum.getActiveNum());
                        fqUserActiveNumService.insert(fqUserActiveNum);
                    } else {
                        example.clear();
                        example.setOrderByClause("ID desc");
                        example.createCriteria().andUserIdEqualTo(userIdList.get(i)).andGmtCreateBetween(DateUtil.beginOfMonth(now).toJdkDate(), tbegin.toJdkDate());
                        FqUserActiveNum yesactive = fqUserActiveNumService.selectFirstByExample(example);
                        if (yesactive == null) {
                            logger.info("没查到数据,用户：{}", userIdList.get(i));
                            yesactive = new FqUserActiveNum();
                            yesactive.setGmtCreate(now);
                            yesactive.setActiveNum(score == null ? 0 : score.intValue());
                            yesactive.setUserId(userIdList.get(i));
                            yesactive.setMark(userMap.get(userIdList.get(i)).getNickname() + ":昨天活跃度：" + yesactive.getActiveNum());
                            fqUserActiveNumService.insert(yesactive);
                        } else {
                            if (yesactive.getActiveNum() != score.intValue()) {
                                //如果昨天的和今天的数据不一样 进行插入
                                logger.info("昨天之前的和今天的数据不一样,用户：{}", userIdList.get(i));
                                yesactive = new FqUserActiveNum();
                                yesactive.setGmtCreate(now);
                                yesactive.setActiveNum(score.intValue());
                                yesactive.setUserId(userIdList.get(i));
                                yesactive.setMark(userMap.get(userIdList.get(i)).getNickname() + ":昨天活跃度：" +
                                        (score.intValue() - (yesactive.getActiveNum() == null ? 0 : yesactive.getActiveNum())));
                                fqUserActiveNumService.insert(yesactive);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("活跃度计算出错！", e);
        } finally {
        }
    }

    @Resource
    private LogDataService logDataService;

    //nginx日志 filePath /usr/local/nginx/logs/host.access.log
    public void nginxLogWork(String filePath) {
        File file = null;
        File errorfile = null;
        try {
            Date now = new Date();
            String format = DateUtil.formatDate(now);
            BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT = "fq_ip_data_" + format;
            file = FileUtil.file(filePath);
            errorfile = FileUtil.file("/usr/local/nginx/logs/error.log");
//        File file = new File("d://logs//host.access.log");
            if (!file.exists()) {
                throw new TaskException("nginx的log日志文件不存在", TaskException.Code.UNKNOWN);
            }
            LogData logData = new LogData();
            logData.setLogType("nginx-log");
            logData.setGenerateDate(DateUtil.formatDate(now));
            logData.setContent(FileUtil.readString(file, "utf-8"));
            logDataService.insert(logData);
            LogData logDataError = new LogData();
            logDataError.setLogType("nginx-error");
            logDataError.setGenerateDate(DateUtil.formatDate(now));
            logDataError.setContent(FileUtil.readString(errorfile, "utf-8"));
            logDataService.insert(logDataError);
            List<String> lines = FileUtil.readLines(file, "utf-8");
            String ip;
            String localTime;
            String method;
            String url;
            String http;
            String status;
            String bytes;
            String referer;
            StringBuilder userAgent;
            String xforward;
            String requestTime;
            for (String line : lines) {
                int spiderType = 0;//0代表没有爬虫 1 百度爬虫 2 google爬虫 3 bing爬虫 4 搜狗
                if (line.contains("Baiduspider")) {
                    spiderType = 1;
                }
                if (line.contains("Googlebot")) {
                    spiderType = 2;
                }
                if (line.contains("Sogouwebspider") || line.contains("www.sogou.com")) {
                    spiderType = 4;
                }
                if (line.contains("bingbot")) {
                    spiderType = 3;
                }
                //来自俄罗斯的搜索引擎蜘蛛
                if (line.contains("YandexBot")) {
                    spiderType = 5;
                }
                if (line.contains("AhrefsBot")) {
                    spiderType = 6;
                }
                if (line.contains("DotBot/1.1")) {
                    spiderType = 6;
                }
                if (line.contains("Alibaba")) {
                    continue;
                }
                line = line.replaceAll("-", "");
                String[] dataList = line.split("\\s");
                ip = dataList[0];
                localTime = dataList[3];
                method = dataList[5];
                url = dataList[6];
                http = dataList[7];
                status = dataList[8];
                bytes = dataList[9];
                referer = dataList[10];
                xforward = dataList[dataList.length - 2];
                requestTime = dataList[dataList.length - 1];
                if (url.contains("layui") || url.contains(".css") || url.contains(".js")
                        || url.contains("wplogin.php") || url.contains("favicon.ico") || url.contains("phpmyadmin")
                        || url.contains("/sign/status") || url.contains("/u/msgsCount") || url.contains("message/read")
                        || url.contains("/visit/records") || url.contains(".png") || url.contains("collection/find")
                        || url.contains("robots.txt") || url.contains("blackList/denyService")) {
                    continue;
                }
                if (status.contains("404")) {
                    continue;
                }
                userAgent = new StringBuilder();
                for (int k = 11; k < dataList.length - 2; k++) {
                    userAgent.append(dataList[k]);
                }
                method = method.replace("\"", "");
                requestTime = requestTime.replaceAll("\"", "");
                http = http.replaceAll("\"", "");
                referer = referer.replaceAll("\"", "");
                if (referer.length() > 250) {
                    logger.info("refer:{} 长度太长 无法存入数据库,准备进行截取", referer);
                    referer = StringUtils.substring(referer, 0, 250);
                }
                if (url.length() > 250) {
                    logger.info("url:{} 长度太长 无法存入数据库,准备进行截取", url);
                    url = StringUtils.substring(url, 0, 250);
                }
                if (method.length() > 250) {
                    logger.info("method:{} 长度太长 无法存入数据库,准备进行截取", method);
                    method = StringUtils.substring(method, 0, 250);
                }
                Double requestTimeDouble = 0d;
                try {
                    requestTimeDouble = Double.valueOf(requestTime);
                } catch (NumberFormatException e) {
                }
                if (userAgent.length() > 254) {
                    logger.info("userAgent:{} 长度太长 无法存入数据库,准备进行截取", userAgent.toString());
                    userAgent.delete(254, userAgent.length());
                }
                NginxLog log = new NginxLog(ip, localTime, method, url, http, status, bytes, referer, xforward,
                        requestTimeDouble, userAgent.toString().replaceAll("\"", ""), spiderType);
                log.setCreateTime(now);
                logService.insert(log);
            }
            /*NginxLogExample nginxLogExample = new NginxLogExample();
            nginxLogExample.createCriteria().andCreateTimeLessThan(DateUtil.offsetMonth(new Date(), -12));//删除90天之前的记录
            logService.deleteByExample(nginxLogExample);*/
        } catch (Exception e) {
            logger.error("nginx日志分析报错", e);
        } finally {
            if (file != null) {
                FileUtil.writeBytes("\\r\\n".getBytes(), file);
            }
            if (errorfile != null) {
                FileUtil.writeBytes("\\r\\n".getBytes(), errorfile);
            }
        }
    }

    public void superBeauty() throws TaskException {
        String picRootPath = "/home/chenweidong/meinv";
        File rootFile = new File(picRootPath);
        if (!rootFile.exists()) {
            throw new TaskException("文件夹不存在,本机ip：" + SystemUtil.getHostInfo().getAddress(), TaskException.Code.UNKNOWN);
        }
        logger.info("rootFile文件名：{},能否被读：{}", rootFile.getName(), rootFile.canRead());
        File[] categoryFiles = rootFile.listFiles();
        long currentTimeMillis = System.currentTimeMillis();
        String picLog = "";
        if (categoryFiles != null && categoryFiles.length > 0) {
            Date now = new Date();
            for (File categoryFile : categoryFiles) {
                boolean insertPic = false;
                logger.info("文件名：{},能否被读：{}", categoryFile.getName(), categoryFile.canRead());
                File[] files = categoryFile.listFiles();
                if (files == null || files.length == 0) {
                    categoryFile.delete();
                    continue;
                }
                for (File file : files) {
                    logger.info("3文件名：{},能否被读：{}", file.getName(), file.canRead());
                    File[] imgs = file.listFiles();
                    if (imgs != null && imgs.length > 0) {
                        String imgUrl = "";
                        String title = "";
                        StringJoiner picUrls = new StringJoiner(",");
                        StringJoiner picDescs = new StringJoiner(",");
                        for (File img : imgs) {
                            String imgName = img.getName();
                            if (imgName.contains("(")) {
                                imgName = imgName.substring(0, imgName.indexOf("("));
                            }
                            String picUrl = null;
                            try {
                                picUrl = FileSystemClient.getPublicClient().upload(BizConstant.FILE_NAME_PREFIX + currentTimeMillis + ".jpg", img);
                            } catch (Exception e) {
                                logger.error("上传图片出错，图片名：" + imgName, e);
                                boolean delete = img.delete();
                                continue;
                            }
                            picUrls.add(picUrl);
                            picDescs.add(imgName);
                            imgUrl = picUrl;
                            title = imgName;
                            boolean delete = img.delete();
                            logger.info("img文件名：{}，是否删除成功：{}", imgName, delete);
                            currentTimeMillis++;
                        }
                        if (StringUtils.isEmpty(title)) {
                            continue;
                        }
                        title = StrUtil.str(title, Charset.forName("utf-8"));
                        SuperBeauty superBeauty = new SuperBeauty();
                        superBeauty.setImgUrl(imgUrl);
                        superBeauty.setTitle(title);
                        superBeauty.setLikeCount(0);
                        superBeauty.setCreateTime(now);
                        superBeauty.setUploadUserId(22);
                        superBeauty.setCategory(GirlCategoryEnum.NV_SHENG.getCode());
                        superBeauty.setDelFlag(YesNoEnum.NO.getValue());
                        superBeauty.setPicList(picUrls.toString());
                        superBeauty.setPicDescList(picDescs.toString());
                        superBeautyService.insert(superBeauty);
                        picLog = superBeauty.toString();
                        insertPic = true;
                        FileUtil.del(file);
                    }
                    if (insertPic) {
                        break;
                    }
                }
            }
        }
        logger.info(picLog);
    }

    //新用户记录
    public void newUserRcord() throws TaskException {
        String picLog = "";
        try {
            FqUserExample userExample = new FqUserExample();
            userExample.setOrderByClause("id desc");
            userExample.createCriteria().andStatusEqualTo(UserStatusEnum.NORMAL.getValue());
//        userExample.createCriteria().andCreateTimeBetween(DateUtil.beginOfDay(DateUtil.yesterday()),DateUtil.endOfDay(DateUtil.yesterday()));
            PageHelper.startPage(0, 10, false);
            List<FqUser> newUsers = fqUserService.selectByExample(userExample);//昨天的新用户
            List<FqUserSim> sims = Lists.newArrayList();
            for (FqUser fqUser : newUsers) {
                FqUserSim sim = new FqUserSim(fqUser);
                sims.add(sim);
            }

            redisTemplate.opsForValue().set(BizConstant.CACHE_NEW_SIMPLE_USERS, JSON.toJSONString(sims));
            //计算用户数量是
            userExample.clear();
            redisTemplate.opsForValue().set(BizConstant.FQ_USER_TOTAL_COUNT, fqUserService.countByExample(userExample) + "");
        } catch (Exception e) {
            logger.error("新用户以及友链更新、插入图片报错", e);
        }
        logger.info("新用户以及友链更新完毕,完成插入图片，图片详情：{}", picLog);
    }

}
