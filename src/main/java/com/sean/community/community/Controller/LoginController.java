package com.sean.community.community.Controller;

import com.google.code.kaptcha.Producer;
import com.sean.community.community.entity.User;
import com.sean.community.community.service.UserService;
import com.sean.community.community.util.CommunityConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    Producer kaptchaProducer;

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
        if (map == null || map.isEmpty()) { // ???????????????
            model.addAttribute("msg", "??????????????????????????????????????????????????????????????????????????????????????????");
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
            model.addAttribute("msg", "?????????????????????????????????????????????");
            model.addAttribute("target", "/login");
        } else if (returnVal == ACTIVATION_REPREAT) {
            model.addAttribute("msg", "???????????????????????????????????????");
            model.addAttribute("target", "/index");
        } else if (returnVal == ACTIVATION_FAILURE) {
            model.addAttribute("msg", "????????????????????????????????????");
            model.addAttribute("target", "/index");
        }

        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //???????????????
        String text = kaptchaProducer.createText();
        BufferedImage kaptcha = kaptchaProducer.createImage(text);
        // ??????????????????session
        session.setAttribute("kaptcha", text);

        // ???????????????????????????
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(kaptcha, "png", os);
        } catch (IOException e) {
            logger.error("???????????????:" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme, Model model,
                        HttpSession session, HttpServletResponse response) {

        //1. ???????????????
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "??????????????????");
            return "/site/login";
        }

        // ??????????????????
        int expiredSeconds = rememberme ? REMEMBERME_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
