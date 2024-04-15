package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

//    @RequestMapping("/test")
//    public String test(Model model)
//    {
//        model.addAttribute("msg","testText");
//        return "test";
//    }

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostsRows(0));
        page.setPath("index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (list!=null){
            for (DiscussPost post:list){
                Map<String,Object>map =new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPost",discussPosts);
        return "index";
    }
    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
