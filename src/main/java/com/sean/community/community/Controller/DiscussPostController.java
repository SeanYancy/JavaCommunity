package com.sean.community.community.Controller;


import com.sean.community.community.entity.DiscussPost;
import com.sean.community.community.entity.User;
import com.sean.community.community.service.DiscussPostService;
import com.sean.community.community.service.UserService;
import com.sean.community.community.util.CommunityUtil;
import com.sean.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) { // 没登录
            return CommunityUtil.getJSONString(403, "没登陆呢！");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);


        // 报错要统一处理
        return CommunityUtil.getJSONString(0, "发布成功");
    }
}