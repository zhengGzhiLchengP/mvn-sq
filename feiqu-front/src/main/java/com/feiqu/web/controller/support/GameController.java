package com.feiqu.web.controller.support;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/11/6.
 */
@Controller
@RequestMapping("game")
public class GameController {


    @GetMapping("study")
    public String study(){
        return "/game/study";
    }
}
