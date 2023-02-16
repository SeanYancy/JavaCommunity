package com.sean.community.community;

import com.sean.community.community.dao.DiscussPostMapper;
import com.sean.community.community.dao.UserMapper;
import com.sean.community.community.entity.DiscussPost;
import com.sean.community.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user1);

        User user2 = userMapper.selectByName("liubei");
        System.out.println(user2);
    }

    @Test
    public void testInsertUser() {
        User tester = new User();
        tester.setUsername("tester");
        tester.setPassword("123456");
        tester.setSalt("abc");
        tester.setEmail("tester@test.com");
        tester.setType(0);
        tester.setStatus(0);
        tester.setHeaderUrl("http://www.nowcoder.com/101.png");
        tester.setCreateTime(new Date());

        int rows = userMapper.insertUser(tester);
        System.out.println(rows);
        System.out.println(userMapper.selectByName("tester"));
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updatePassword(150, "abcdefg");
        System.out.println(rows);
        System.out.println(userMapper.selectById(150));
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 20);
        for (DiscussPost a : list) {
            System.out.println(a);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
