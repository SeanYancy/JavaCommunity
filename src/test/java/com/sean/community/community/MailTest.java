package com.sean.community.community;

import com.sean.community.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("yanzongxun@icloud.com", "Hello world", "Welcome to my house!");
    }

    @Test
    public void testHTMLMail() {
        Context context = new Context();
        context.setVariable("username", "yanzongxun");
        String content = templateEngine.process("/mail/demo", context);

        mailClient.sendMail("yanzongxun@icloud.com", "Hello World", content);
    }
}
