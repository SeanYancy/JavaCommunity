package com.sean.community.community.Controller;


import com.sean.community.community.entity.Comment;
import com.sean.community.community.entity.DiscussPost;
import com.sean.community.community.entity.Page;
import com.sean.community.community.entity.User;
import com.sean.community.community.service.CommentService;
import com.sean.community.community.service.DiscussPostService;
import com.sean.community.community.service.LikeService;
import com.sean.community.community.service.UserService;
import com.sean.community.community.util.CommunityConstant;
import com.sean.community.community.util.CommunityUtil;
import com.sean.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

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

    @RequestMapping(path="/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int postId, Model model, Page page) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //点赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        model.addAttribute("likeCount", likeCount);
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, postId);
        model.addAttribute("likeStatus", likeStatus);

        // 设置分页
        page.setLimit(5);
        page.setPath("/discuss/detail/" + postId);
        page.setRows(post.getCommentCount());


        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论的列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论的VO列表（VO显示对象）
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment: commentList) {
                // 单个评论的VO
                Map<String, Object> commentVo = new HashMap<>();
                // 添加评论
                commentVo.put("comment", comment);
                // 添加评论的作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));


                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 查询回复
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                // 回复的VO列表
                List<Map<String, Object>> replayVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply: replyList){
                        Map<String, Object> replayVo = new HashMap<>();
                        replayVo.put("reply", reply);
                        replayVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复的目标
                        User target = reply.getTargetId() == 0? null: userService.findUserById(reply.getUserId());
                        replayVo.put("target", target);

                        // 点赞
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replayVo.put("likeCount", likeCount);
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replayVo.put("likeStatus", likeStatus);

                        replayVoList.add(replayVo);
                    }
                }
                // 把回复放入评论VO
                commentVo.put("replays", replayVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }

    //置顶
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody //异步
    public String setHot(int id) {
        discussPostService.updateType(id, 1);

        return CommunityUtil.getJSONString(0);
    }

    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody //异步
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);

        return CommunityUtil.getJSONString(0);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody //异步
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        return CommunityUtil.getJSONString(0);
    }
}
