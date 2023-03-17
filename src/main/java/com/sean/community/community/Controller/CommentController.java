package com.sean.community.community.Controller;

import com.sean.community.community.Event.EventProducer;
import com.sean.community.community.entity.Comment;
import com.sean.community.community.entity.DiscussPost;
import com.sean.community.community.entity.Event;
import com.sean.community.community.service.CommentService;
import com.sean.community.community.service.DiscussPostService;
import com.sean.community.community.util.CommunityConstant;
import com.sean.community.community.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;


    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论时间
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target =  discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment targer = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(targer.getUserId());
        }

        eventProducer.fireEvent(event);

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
