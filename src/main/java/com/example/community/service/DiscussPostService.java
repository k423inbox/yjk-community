package com.example.community.service;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.util.SensitiveFiliter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.IllformedLocaleException;
import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFiliter sensitiveFiliter;
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostsRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    public int addDiscussPost(DiscussPost post){
        if (post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFiliter.filter(post.getTitle()));
        post.setContent(sensitiveFiliter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPostRows(post);
    }
    public DiscussPost finDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
}
