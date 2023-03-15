package com.sean.community.community.dao;

import org.apache.ibatis.annotations.Mapper;
import com.sean.community.community.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}