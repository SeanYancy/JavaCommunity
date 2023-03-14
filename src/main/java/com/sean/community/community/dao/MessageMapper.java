package com.sean.community.community.dao;

import com.sean.community.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    // 查询当前用户的会话列表， 针对每个会话只返回最新的私信
    // 为综合消息界面而准备的
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的绘画数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message massage);

    // 修改消息得状态
    int updateStatus(List<Integer> ids, int status);
}
