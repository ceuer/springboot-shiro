package com.ceuer.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ShiroController {
	
	@RequestMapping({"/", "index", "index.html"})
	public String index(HttpServletRequest request) {
		request.setAttribute("msg", "Hello Shiro!");
		return "index";
	}
	
	@RequestMapping("/user/add")
	public String add() {
		return "add";
	}
	
	@RequestMapping("/user/update")
	public String update() {
		return "update";
	}
	
	@RequestMapping("/login.html")
	public String gologin() {
		return "login";
	}
	
	@RequestMapping("/login")
	public String login(String username, String password, HttpServletRequest request) {
		//获取当前的用户
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.isAuthenticated()) {//当前用户已认证
			return "redirect:/";
		}
		
		//封装用户的登录数据
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		
		try {
			//执行登录操作，如果没有异常说明登录成功了
			subject.login(usernamePasswordToken);
			return "redirect:/";
		} catch (UnknownAccountException e1) {
			request.setAttribute("msg", "用户名不存在！");
			return "login";
		} catch (IncorrectCredentialsException e2) {
			request.setAttribute("msg", "密码不正确！");
			return "login";
		} catch (AuthenticationException e) {
			request.setAttribute("msg", "用户名或密码错误！");
			e.printStackTrace();
			return "login";
		}
		
	}
	
	@RequestMapping("/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/";
	}
	
}