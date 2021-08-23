package com.ceuer.shiro.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	
	//part1 初始化ShiroRealm对象
	@Bean
	public ShiroRealm shiroRealm(){
		System.out.println("@Bean -> ShiroRealm");
		return new ShiroRealm();
	}
	
	//part2 初始化DefaultWebSecurityManager Bean对象
	@Bean(name = "ShiroWebSecurityManager")
	public DefaultWebSecurityManager shiroWebSecurityManager(ShiroRealm shiroRealm){
		System.out.println("@Bean -> ShiroWebSecurityManager");
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(shiroRealm);
		return defaultWebSecurityManager;
	}
	
	//part3
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("ShiroWebSecurityManager") DefaultWebSecurityManager shiroWebSecurityManager){
		System.out.println("@Bean -> ShiroFilterFactoryBean");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//part1 设置Shiro安全管理器
		shiroFilterFactoryBean.setSecurityManager(shiroWebSecurityManager);
		
		//part2 设置shiro过滤器链
		/*
			anon：无需认证就可以访问
			authc：必须认证才能访问:一般就是登陆成功就算认证过了
			perms：拥有对某个资源的权限才能访问perms[user:add] 多个为:perms["user:add,user:update"]
			roles：拥有某个角色权限才能访问roles[1] 多个为 roles[1,2,3]
			user：必须拥有 记住我 功能才能访问
		 */
		Map<String, String> filtersMap = new LinkedHashMap();
		
		/* Shiro默认为anon，即不需要认证和授权就可以访问 */
		this.shiroFiltersMapTest1(filtersMap);
		
		/* Shiro过滤内容测试2：只需要认证的资源：需要认证才能访问 */
		this.shiroFiltersMapTest2(filtersMap);
		
		/* Shiro过滤内容测试3：需要认证并且需要【用户资源权限】授权的资源才能访问 */
		this.shiroFiltersMapTest3(filtersMap);
		
		/* Shiro过滤内容测试4：需要认证并且需要【角色资源权限】授权的资源才能访问 */
		this.shiroFiltersMapTest4(filtersMap);
		
		//part3	存储shiro过滤器链内容
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filtersMap);
		
		//part4	设置遇到认证错误的跳转页面
		//设置遇到未认证或者没有权限时跳转的页面，此处指定到登录页面
		shiroFilterFactoryBean.setLoginUrl("/login.html");
		//设置遇到未认证或者没有权限时跳转的页面，此处指定到登录页面
		//shiroFilterFactoryBean.setUnauthorizedUrl("/login.html");
		
		return shiroFilterFactoryBean;
	}
	
	/**
	 * Shiro过滤内容测试1：Shiro默认为anon，即不需要认证和授权就可以访问
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapTest1(Map<String, String> filtersMap){
		// filtersMap.put("/welcome", "anon");
		// filtersMap.put("/login.html", "anon");
	}
	
	/**
	 * Shiro过滤内容测试2：只需要认证的资源：需要认证才能访问
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapTest2(Map<String, String> filtersMap){
		// filtersMap.put("/session/userlist", "authc");
		// filtersMap.put("/session/deptlist", "authc");
		// /session/* 表示 /session/a 、/session/b、/session/c
		// /session/** 表示 /session/a 、/session/a/b、/session/a/b/c
		filtersMap.put("/session/*", "authc");
	}
	
	/**
	 * Shiro过滤内容测试3：需要认证并且需要【用户资源权限】授权的资源才能访问
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapTest3(Map<String, String> filtersMap){
		filtersMap.put("/user/add", "perms[user:add]");
		filtersMap.put("/user/update", "perms[user:update]");
		
		//filtersMap.put("/user/*", "perms[user:*]");
	}
	
	/**
	 * Shiro过滤内容测试4：需要认证并且需要【角色资源权限】授权的资源才能访问
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapTest4(Map<String, String> filtersMap){
		filtersMap.put("/dept/add", "roles[1]");
		filtersMap.put("/dept/update", "roles[2]");
	}
	
}
