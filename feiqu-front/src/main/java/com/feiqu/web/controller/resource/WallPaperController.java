package com.feiqu.web.controller.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.feiqu.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@RequestMapping("wallPaper")
@Controller
public class WallPaperController {
    private static Logger logger = LoggerFactory.getLogger(WallPaperController.class);
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RequestMapping("")
    public String index(Model model){
        String movieLocation = Global.getConfig("feiqu.fileLocation");
        File movieLocationFile = new File(movieLocation);
        String movieLocationJson = "";
        String key = "wallpaper:Categories";
        if(stringRedisTemplate.hasKey(key)){
            movieLocationJson = stringRedisTemplate.opsForValue().get(key);
        }else {
            if(!movieLocationFile.exists()){
                movieLocationFile.mkdirs();
            }
            File file = FileUtil.file(movieLocationFile,"wallpaperCategories.json");
            if(!file.exists()){
                String result = HttpUtil.createGet("http://service.picasso.adesk.com/v1/wallpaper/category")
                        .execute().body();
                FileUtil.writeBytes(result.getBytes(),file);
            }
            movieLocationJson = FileUtil.readString(file, Charset.defaultCharset());
            stringRedisTemplate.opsForValue().set(key,movieLocationJson,30, TimeUnit.SECONDS);
        }
        JSONObject jsonObject = new JSONObject(movieLocationJson);
        JSONArray categories = jsonObject.getJSONObject("res").getJSONArray("category");
        model.addAttribute("categories",categories);

        String result = HttpUtil.createGet("http://service.picasso.adesk.com/v1/wallpaper/album?limit=30&adult=false&first=1&order=hot")
                .execute().body();
        jsonObject = new JSONObject(result);
        JSONArray albums = jsonObject.getJSONObject("res").getJSONArray("album");
        model.addAttribute("albums",albums);
        return "/front/wallpaper/index";
    }

    @RequestMapping("/category/{categoryId}")
    public String category(@PathVariable String categoryId, Model model,@RequestParam(required = false,defaultValue = "1") Integer page,
                           @RequestParam(required = false,defaultValue = "new") String order){
        //获取某类别下的壁纸
        String url = "http://service.picasso.adesk.com/v1/wallpaper/category/"+ categoryId +"/wallpaper?first="+page+"&order="+order+"&limit=100";
        String wallPapers = HttpUtil.get(url);
        JSONObject wallPapersJson = new JSONObject(wallPapers);
//        logger.info("请求url：{}，返回结果：{}",url,wallPapers);
        model.addAttribute("wallPapers",wallPapersJson.getJSONObject("res").getJSONArray("wallpaper"));
        model.addAttribute("categoryId",categoryId);
        return "/front/wallpaper/categoryPapers";
    }

   /* @RequestMapping("/album/{albumId}")
    public String albumId(@PathVariable String albumId, Model model,@RequestParam(required = false,defaultValue = "1") Integer page,
                           @RequestParam(required = false,defaultValue = "new") String order){
        //获取某类别下的壁纸
        String url = "http://service.picasso.adesk.com/v1/wallpaper/album/"+albumId+"/wallpaper?limit=30&adult=false&first=1&order="+order;
        String wallPapers = HttpUtil.get(url);
        JSONObject wallPapersJson = new JSONObject(wallPapers);
        logger.info("请求url：{}，返回结果：{}",url,wallPapers);
        model.addAttribute("album",wallPapersJson.getJSONObject("res").getJSONObject("album"));
        return "/front/wallpaper/categoryPapers";
    }*/
}
