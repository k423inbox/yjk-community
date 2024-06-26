package com.example.community.controller;

import com.example.community.entity.Comment;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.CommentService;
import com.example.community.service.DiscussPostService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityUtils;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.example.community.util.CommunityConstant.ENTITY_TYPE_COMMENT;
import static com.example.community.util.CommunityConstant.ENTITY_TYPE_POST;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPOst(String title,String content){
        User user = hostHolder.getUsers();
        if (user == null){
            return CommunityUtils.getJSONString(403,"你还没有登录");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况，将来统一处理
        return CommunityUtils.getJSONString(0,"发布成功");
    }
    @RequestMapping(value = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeCount",likeCount);
        //点赞状态
        int likeStatus =
                hostHolder.getUsers()==null ? 0:likeService.findEntityLikeStatus(hostHolder.getUsers().getId(),ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
ENTITY_TYPE_POST,post.getId(), page.getOffset(), page.getLimit());
        // 评论Vo列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if (commentList != null){
            for (Comment comment: commentList){
                // 评论Vo
                Map<String,Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment",comment);
                // 作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",likeCount);
                //点赞状态
                likeStatus = hostHolder.getUsers()==null ? 0:
                        likeService.findEntityLikeStatus(hostHolder.getUsers().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus",likeStatus);
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表


                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        //点赞状态
                        likeStatus = hostHolder.getUsers()==null ? 0:
                                likeService.findEntityLikeStatus(hostHolder.getUsers().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",likeStatus);
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }

                }
                commentVo.put("replys",replyVoList);


                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("replyCount",replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }

}
