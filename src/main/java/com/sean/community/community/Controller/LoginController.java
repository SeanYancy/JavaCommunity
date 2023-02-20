package com.sean.community.community.Controller;

import com.sean.community.community.entity.User;
import com.sean.community.community.service.UserService;
import com.sean.community.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = null;
        try {
            map = userService.register(user);
        } catch (IllegalAccessException e) {
            logger.error("Username is null" + e.getMessage());
        }
        if (map == null || map.isEmpty()) { // 返回无错误
            model.addAttribute("msg", "注册成功，我们已经向您得邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int returnVal = userService.activation(userId, code);
        if (returnVal == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号可以使用了！");
            model.addAttribute("target", "/login");
        } else if (returnVal == ACTIVATION_REPREAT) {
            model.addAttribute("msg", "无效操作，该账号已经激活！");
            model.addAttribute("target", "/index");
        } else if (returnVal == ACTIVATION_FAILURE) {
            model.addAttribute("msg", "激活失败，激活码不正确！");
            model.addAttribute("target", "/index");
        }

        return "/site/operate-result";
    }
}
