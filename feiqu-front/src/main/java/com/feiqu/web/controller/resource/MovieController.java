package com.feiqu.web.controller.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.feiqu.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.nio.charset.Charset;

/**
 * https://github.com/jokermonn/-Api
 */
@Controller
@RequestMapping("movie")
public class MovieController {
    private static Logger logger = LoggerFactory.getLogger(MovieController.class);

    @RequestMapping("")
    public String index(Model model) {
        String movieLocation = Global.getConfig("feiqu.fileLocation");
        File movieLocationFile = new File(movieLocation);
        String movieLocationJson = "";
        if (!movieLocationFile.exists()) {
            movieLocationFile.mkdirs();
        }
        File file = FileUtil.file(movieLocationFile, "TimeApp-Area-id.json");
        if (!file.exists()) {
            String result = HttpUtil.createGet("https://api-m.mtime.cn/Showtime/HotCitiesByCinema.api").header("Host", "api-m.mtime.cn")
                    .execute().body();
            FileUtil.writeBytes(result.getBytes(), file);
        }
        movieLocationJson = FileUtil.readString(file, Charset.defaultCharset());
        JSONObject jsonObject = new JSONObject(movieLocationJson);
        JSONArray locations = jsonObject.getJSONArray("p");
        model.addAttribute("locations", locations);
        return "/front/movie/index";
    }

    @RequestMapping("/location/{locationId}")
    public String locationMovies(@PathVariable String locationId, Model model) {
        //正在售票(包括正在热映和即将上映)
        String movies = HttpUtil.get("https://api-m.mtime.cn/PageSubArea/HotPlayMovies.api?locationId=" + locationId);
        JSONObject moviesJson = new JSONObject(movies);
        model.addAttribute("movies", moviesJson);
        //正在热映

        movies = HttpUtil.get("https://api-m.mtime.cn/Showtime/LocationMovies.api?locationId=" + locationId);
        JSONObject moviesHot = new JSONObject(movies);
        model.addAttribute("moviesHot", moviesHot);

        model.addAttribute("locationId", locationId);
        return "/front/movie/locationMovies";
    }

    @RequestMapping("/detail/{locationId}/{movieId}")
    public String detail(@PathVariable String locationId, @PathVariable String movieId, Model model) {
        String movieJson = HttpUtil.get("https://ticket-api-m.mtime.cn/movie/detail.api?locationId=" + locationId + "&movieId=" + movieId);
        JSONObject movieAll = new JSONObject(movieJson);
        JSONObject movie = movieAll.getJSONObject("data");

//        logger.debug(movie.toString());
        model.addAttribute("movie", movie);
        return "/front/movie/detail";
    }
}
