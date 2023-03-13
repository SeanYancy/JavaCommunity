package com.sean.community.community;

import com.sean.community.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {
    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以赌博，可以嫖娼，可以开票，哈哈哈！！";
        System.out.println(sensitiveFilter.filter(text));

        text = "这里可以赌※博，可以嫖※娼，可以开※票，哈哈哈！！";
        System.out.println(sensitiveFilter.filter(text));
    }
}
