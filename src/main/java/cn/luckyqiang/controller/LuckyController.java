package cn.luckyqiang.controller;

import cn.luckyqiang.annotation.Autowired;
import cn.luckyqiang.annotation.Controller;
import cn.luckyqiang.annotation.RequestMapping;
import cn.luckyqiang.annotation.RequestParam;
import cn.luckyqiang.service.api.LuckyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: Controller
 * @Description: TODO
 * @Author: zhangzhiqiang
 * @Date: 2020-03-16 17:53
 * @Company: www.luckyqiang.cn
 */

@Controller
@RequestMapping("/lucky")
public class LuckyController {

    @Autowired("luckyServiceImpl")
    private LuckyService luckyService;

    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name, @RequestParam("age") String age) {
        try {
            PrintWriter pw = response.getWriter();
            String result = luckyService.query(name, age);
            pw.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
