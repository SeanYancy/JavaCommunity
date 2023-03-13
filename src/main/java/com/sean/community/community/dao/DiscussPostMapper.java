package com.sean.community.community.dao;

import org.apache.ibatis.annotations.Mapper;
import com.sean.community.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param 用于给参数取别名，但如果方法只有一个参数，并且是动态的那么必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

}
