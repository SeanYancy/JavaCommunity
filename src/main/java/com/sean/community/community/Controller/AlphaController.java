package com.sean.community.community.Controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello Spring!";
    }

    //默认get请求
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String elementKey = enumeration.nextElement();
            String elementVal = request.getHeader(elementKey);
            System.out.println("key:" + elementKey + " val:" + elementVal);
        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");

        try(
                PrintWriter pw = response.getWriter();
                )
        {
            pw.write("<h1>仿牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get
    // /student?current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit)
    {
        System.out.println("cu"+current +"li"+ limit);
        return "current:" + current + " limit:" + limit;
    }

    @RequestMapping(path = "/students/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a";
    }

    //POST 请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String studentName, int studentAge) {
        System.out.println(studentName);
        System.out.println(studentAge);
        return "success";
    }

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "1");
        mav.addObject("age", "10");
        mav.setViewName("/demo/view");
        return mav;
    }

    // 相应JSON数据（异步）
    // Java Object -> JSON字符串 -> JS Object
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> kv = new HashMap<>();
        kv.put("name", "张三");
        kv.put("age", "23");
        kv.put("salary", "100000");
        return kv;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> kv = new HashMap<>();
        kv.put("name", "张三");
        kv.put("age", "23");
        kv.put("salary", "100000");
        list.add(kv);
        Map<String, Object> kv1= new HashMap<>();
        kv1.put("name", "李四");
        kv1.put("age", "99");
        kv1.put("salary", "9900000");
        list.add(kv1);
        Map<String, Object> kv2= new HashMap<>();
        kv2.put("name", "王5");
        kv2.put("age", "919");
        kv2.put("salary", "99010000");
        list.add(kv2);
        return list;
    }
}
