package com.feiqu.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.*;
import cn.hutool.extra.emoji.EmojiUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.system.model.FqGoodPic;
import com.feiqu.system.model.FqGoodPicExample;
import com.feiqu.system.model.collectData.*;
import com.feiqu.system.service.collectData.*;
import com.feiqu.system.service.mainData.FqGoodPicService;
import com.jeesuite.filesystem.FileSystemClient;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Component("collectDataTask")
public class CollectDataTask {

    private static Logger logger = LoggerFactory.getLogger(CollectDataTask.class);

    //股票黄历
    @Resource
    private StockHlService stockHlService;
    @Resource
    private HotTopicService hotTopicService;
    @Resource
    private FinanceNewsService financeNewsService;

    //同花顺的
    public void financeNews() {
        try {
            String s = HttpUtil.get("http://comment.10jqka.com.cn/api/realtimegetnews/");
            s = s.substring(16, s.length() - 1);
            JSONArray jsonArray = new JSONArray(s);
            jsonArray.forEach(e -> {
                JSONObject jsonObject = (JSONObject) e;
                String title = jsonObject.getStr("title");
                String content = jsonObject.getStr("content");
                String url = jsonObject.getStr("url");
                FinanceNews financeNews = new FinanceNews();
                financeNews.setUrl(url);
                financeNews.setTitle(title);
                financeNews.setContent(content);
                financeNews.setGmtCreate(new Date());
                financeNewsService.insert(financeNews);
            });
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    public void stockHl() {
        String body = HttpUtil.get("https://itatrd.gjzq.com.cn/jg/data/cal_indexData.json?v=" + System.currentTimeMillis());
//        Console.log(body);
        JSONObject jsonObject = JSONUtil.parseObj(body);
        String yinli = jsonObject.getJSONObject("attach").getStr("lunar");
        JSONObject stkCalendar = jsonObject.getJSONObject("attach").getJSONObject("stkCalendar");
        String fundcontent = stkCalendar.getStr("fundcontent");
        String suitable = stkCalendar.getStr("suitable");
        String summarycontent = stkCalendar.getStr("summarycontent");
        String tecCon = stkCalendar.getStr("tecCon");
        StockHl stockHl = new StockHl();
        stockHl.setGmtCreate(new Date());
        stockHl.setFundContent(HtmlUtil.cleanHtmlTag(fundcontent));
        stockHl.setSuit(suitable);
        stockHl.setSummary(HtmlUtil.cleanHtmlTag(summarycontent));
        stockHl.setTechData(tecCon);
        stockHl.setYinli(yinli);
        stockHlService.insert(stockHl);
    }

    @Resource
    private FqGoodPicService fqGoodPicService;

    //"http://z1.krsncjdnr.info/pw/,F:\\1024imgs\\http,5,3"
    //"http://z1.krsncjdnr.info/pw/,F:\\1024imgs\\http,5,3"
    public void paqu1024(String param) {
        try {
            logger.info("开始爬取1024数据");
            String[] params = param.split(",");
            String prefix = params[0];
            String filePreFix = params[1];
            Integer limitNum = Integer.valueOf(params[2]);
            Integer imgNum = Integer.valueOf(params[3]);
            String url = prefix + "/thread.php?fid=14&page=1";
            String html = HttpUtil.get(url);
//        Console.log(html);
            Document document = Jsoup.parse(html);
            Element element = document.getElementById("ajaxtable");
            Elements elements = element.getElementsByTag("a");
            String pattern = "html_data/\\d+/\\d+/\\d+\\.html";
            int index = 1;
            File fileParent = FileUtil.file(filePreFix);
            Date now = new Date();
            String format = DateUtil.format(now, "yyyy/MM/dd/HH");
            String fileName = "";
            FqGoodPicExample fqGoodPicExample = new FqGoodPicExample();
            for (Element urlEle : elements) {
                String href = urlEle.attr("href");
                if (ReUtil.isMatch(pattern, href)) {
                    String realUrl = prefix + href;
                    String meituHtml = HttpUtil.get(realUrl);
                    Document meituDocument = Jsoup.parse(meituHtml);
                    Element breadCrumb = meituDocument.getElementById("breadCrumb");
                    Element read_tpc = meituDocument.getElementById("read_tpc");
                    String name = breadCrumb.getElementsByTag("a").get(3).text();
                    fqGoodPicExample.clear();
                    fqGoodPicExample.createCriteria().andTitleEqualTo(name);
                    int count = fqGoodPicService.countByExample(fqGoodPicExample);
                    if (count > 0) {
                        continue;
                    }
                    List<String> imgSrcs = read_tpc.getElementsByTag("img").eachAttr("src");
//                Console.log(name);
                    String fileNameFormat = format + index;
                    StringJoiner stringJoiner = new StringJoiner(",");
                    int i = 1;
                    for (String imgSrc : imgSrcs) {
                        fileName = fileNameFormat + i + ".jpg";
                        File imgFile = FileUtil.file(fileParent, fileName);
                        long num = HttpUtil.downloadFile(imgSrc, imgFile);
                        String imgUrl = FileSystemClient.getClient(BizConstant.FILE_SYSTEM_ALIYUN).upload(fileName, imgFile);
                        stringJoiner.add(imgUrl);
                        imgFile.delete();
                        i++;
                        if (i > imgNum) {
                            break;
                        }
                        Thread.sleep(1000);
                    }
                    FqGoodPic fqGoodPic = new FqGoodPic();
                    fqGoodPic.setGmtCreate(now);
                    fqGoodPic.setTitle(name);
                    fqGoodPic.setPicUrlList(stringJoiner.toString());
                    fqGoodPicService.insert(fqGoodPic);
                    logger.info("goodpic完成数据插入：{}", JSON.toJSONString(fqGoodPic));
                    index++;
                }
                if (index > limitNum) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("爬取1024失败！", e);
        }

    }

    @Resource
    private RunLogMessageService runLogMessageService;

    //5000
    public void runLogMessageCollect(String maxnum){
        int num = Integer.valueOf(maxnum);
        logger.debug("开始处理，处理数量：{}",num);
        for(int i=0;i<num;i++){
            try {
                String v = stringRedisTemplate.opsForList().leftPop(BizConstant.LOG_KEY);
                if(v ==null){
                    break;
                }
                RunLogMessage runLogMessage = JSONUtil.toBean(v,RunLogMessage.class);
                runLogMessageService.insert(runLogMessage);
            }catch (Exception e){
                logger.error("插入日志失败！",e);
            }
        }
    }

    @Resource
    private LogDataService logDataService;

    //prefixAll = /home/chenweidong/feiqu/logs,/home/chenweidong/feiqu-admin/logs
//prefixAll = /home/chenweidong/feiqu/logs,/home/chenweidong/feiqu-admin/logs
    public void logCollect(String prefixAll) {
        String[] prefixs = StrUtil.split(prefixAll, ",");
        String prefix = prefixs[0];
        String adminprefix = prefixs[1];
        try {
            String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            File logFile = new File(prefix + "/front-info.log");
            File errorFile = new File(prefix + "/front-error.log");
            File gcLogFile = new File(prefix + "/gc.log");

            File adminlogFile = new File(adminprefix + "/sys-info.log");
            File adminerrorFile = new File(adminprefix + "/sys-error.log");
            File admingcLogFile = new File(adminprefix + "/gc.log");
            if (logFile.exists()) {
                logFile.setWritable(true);
                String logfileStr = FileUtil.readString(logFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(logfileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(logfileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("front-info");
                    logDataService.insert(logData);
                    FileUtil.writeBytes("/r/n".getBytes(), logFile);
                }
            }
            if (errorFile.exists()) {
                errorFile.setWritable(true);
                String errorFileStr = FileUtil.readString(errorFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(errorFileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(errorFileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("front-error");
                    logDataService.insert(logData);
                    FileUtil.writeBytes("/r/n".getBytes(), errorFile);
                }
            }

            if (gcLogFile.exists()) {
                String gcLogFileStr = FileUtil.readString(gcLogFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(gcLogFileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(gcLogFileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("front-gc");
                    logDataService.insert(logData);
                    FileUtil.writeBytes(("/r/n").getBytes(), gcLogFile);
                }

            }
            if (adminlogFile.exists()) {

                adminlogFile.setWritable(true);
                String adminlogFileStr = FileUtil.readString(adminlogFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(adminlogFileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(adminlogFileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("admin-info");
                    logDataService.insert(logData);
                    FileUtil.writeBytes("/r/n".getBytes(), adminlogFile);
                }
            }
            if (adminerrorFile.exists()) {
                adminerrorFile.setWritable(true);
                String adminerrorFileStr = FileUtil.readString(adminerrorFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(adminerrorFileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(adminerrorFileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("admin-error");
                    logDataService.insert(logData);
                    FileUtil.writeBytes("/r/n".getBytes(), adminerrorFile);
                }
            }

            if (admingcLogFile.exists()) {
                String admingcLogFileStr = FileUtil.readString(admingcLogFile, CharsetUtil.CHARSET_UTF_8);
                if (!ObjectUtil.equal(admingcLogFileStr, "/r/n")) {
                    LogData logData = new LogData();
                    logData.setContent(admingcLogFileStr);
                    logData.setGenerateDate(format);
                    logData.setLogType("admin-gc");
                    logDataService.insert(logData);
                    FileUtil.writeBytes(("/r/n").getBytes(), admingcLogFile);
                }
            }
        } catch (Exception e) {
            logger.error("生成日志失败", e);
        } finally {

        }
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static List<String> refers = Lists.newArrayList("n/WnBe01o371", "n/KqndgxeLl9", "n/mproPpoq6O", "n/74Kvx59dkx");
    private String proxy_redisKey = "proxies:ips";

    /**
     * 抓取66ip的免费代理地址
     * 抓取快代理IP(https://www.kuaidaili.com/free/inha/)
     * 抓取西刺代理的HTTP免费代理https://www.xicidaili.com/wt
     */
    public void ipPool() {
        int page = 1;
        List<String> results = new LinkedList<>();
        try {
            while (page <= 100) {
                String html = HttpUtil.get("http://www.66ip.cn/" + page + ".html");
                Document document = Jsoup.parse(html);
                Elements elements = document.select(".containerbox table tr:gt(0)");

                if (elements != null && elements.size() > 0) {
                    for (Element element : elements) {
                        String ip = element.select("td:nth-child(1)").text();
                        String port = element.select("td:nth-child(2)").text();
                        String proxy = ip + ":" + port;
                        results.add(proxy);
                    }
                }
                page++;
            }
            for (String proxy : results) {
                stringRedisTemplate.opsForSet().add(proxy_redisKey, proxy);
            }
        } catch (Exception e) {
            logger.error("获取ip代理失败", e);
        } finally {
        }
    }

    /**
     * node-7 V2EX
     * node-6 知乎
     * node-5 微信
     * node-3 百度贴吧
     * node-2 百度
     * node-1 微博
     * param = 1, 5, 6, 7, 11, 19, 53, 72, 167, 2404
     */
    public void hotTopic(String param) {
        String key = BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX + DateUtil.formatDate(new Date());
        Set<String> set = stringRedisTemplate.opsForZSet().reverseRange(key, 0, 100);
        if (CollectionUtil.isEmpty(set)) {
            return;
        }
        String ip = RandomUtil.randomEle(Lists.newArrayList(set));
        logger.info("获得代理ip：{}", ip);
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", ip);
        System.setProperty("http.proxyPort", "9999");
        System.setProperty("https.proxyHost", ip);
        System.setProperty("https.proxyPort", "9999");
        Document document = null;
        try {

            String refer = RandomUtil.randomEle(refers);
            String url = "https://tophub.today/";
            String body = HttpUtil.createGet(url)
                    .header("referer", url + refer)
                    .header("user-agent", RandomUtil.randomEle(BizConstant.userAgentArray)).execute().body();
            if (StrUtil.isEmpty(body)) {
                return;
            }

            document = Jsoup.parse(body);
            if (document == null) {
                return;
            }
        } catch (Exception e) {
            logger.error("爬取热点失败", e);
            return;
        }

        Date now = new Date();
        Element elementEnd;
        String source = "";
        Integer[] nodes = null;
        if (StrUtil.isNotEmpty(param)) {
            String[] nodesnum = param.split(",");
            nodes = Convert.toIntArray(nodesnum);
        }

        for (int nodeIndex : nodes) {
            switch (nodeIndex) {
                case 1:
                    source = "微博";
                    break;
                case 5:
                    source = "微信";
                    break;
                case 6:
                    source = "知乎";
                    break;
                case 7:
                    source = "V2EX";
                    break;
                case 11:
                    source = "36氪";
                    break;
                case 19:
                    source = "哔哩哔哩";
                    break;
                case 53:
                    source = "好奇心日报";
                    break;
                case 72:
                    source = "煎蛋";
                    break;
                case 119:
                    source = "IT之家";
                    break;
                case 32:
                    source = "虎嗅网";
                    break;
                case 167:
                    source = "值得买";
                    break;
                case 2404:
                    source = "雪球";
                    break;
                default:
                    source = "未知";
                    break;
            }
            try {
                elementEnd = document.getElementById("node-" + nodeIndex);
                if (elementEnd == null) {
                    continue;
                }
                Elements aElements = elementEnd.getElementsByTag("a");
                HotTopicExample hotTopicExample = new HotTopicExample();
                int index = 0;
                for (Element element : aElements) {

                    String href = element.attr("href");
                    if (!href.startsWith("http")) {
                        continue;
                    }
                    Elements te = element.getElementsByClass("t");
                    String text = te.get(0).text();
                    text = EmojiUtil.removeAllEmojis(text);
                    hotTopicExample.clear();
                    hotTopicExample.createCriteria().andTitleEqualTo(text).andSourceEqualTo(source);
                    int count = hotTopicService.countByExample(hotTopicExample);
                    if (count > 0) {
                        continue;
                    }
                    HotTopic hotTopic = new HotTopic();
                    hotTopic.setCreateTime(now);
                    hotTopic.setSource(source);
                    hotTopic.setTitle(text);
                    hotTopic.setUrl(href);
                    hotTopicService.insert(hotTopic);
                    index++;
                    //如果一个source下面超过了10条 就跳出循环 b站数据太多
                    if (index >= 5) {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("处理出错", e);
            }
        }
    }
}
