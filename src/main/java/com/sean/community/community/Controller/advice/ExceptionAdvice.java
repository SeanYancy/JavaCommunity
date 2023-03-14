package com.sean.community.community.Controller.advice;


import com.sean.community.community.util.CommunityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        logger.error("服务器发生异常：" + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        // 如果是异步请求要返回json
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            try (
                    PrintWriter writer = response.getWriter();
                    ) {
                writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
            } catch (IOException ex) {
                throw new RuntimeException("异常处理发生错误:" + e.getMessage());
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/error");
            } catch (IOException ex) {
                throw new RuntimeException("异常处理发生错误" + e.getMessage());
            }
        }


    }
}
