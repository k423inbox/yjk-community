package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {


    /**
     * 分页查询
     * @param userId
     * @param offset
     * @param limit
     * @return
     */

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    /**
     * 查询表中数据总数
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);
    int insertDiscussPostRows(DiscussPost discussPost);
    DiscussPost selectDiscussPostById(int id);
    int updateCommentCount(int id,int commentCount);
    int insertDiscussPost(DiscussPost discussPost);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
