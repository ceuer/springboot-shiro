package com.ceuer.shiro.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	
	//part1 初始化ShiroRealm对象
	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		return shiroRealm;
	}
	
	//part2 初始化DefaultWebSecurityManager Bean对象
	@Bean(name = "ShiroWebSecurityManager")
	public DefaultWebSecurityManager shiroWebSecurityManager() {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(shiroRealm());
		
		// //設置自定義realm.
		// securityManager.setRealm(shiroRealm());
		// //配置記住我
		// securityManager.setRememberMeManager(rememberMeManager());
		// //配置redis緩存
		// securityManager.setCacheManager(cacheManager());
		// //配置自定義session管理，使用redis
		// securityManager.setSessionManager(sessionManager());
		return defaultWebSecurityManager;
	}
	
	//part3
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("ShiroWebSecurityManager") DefaultWebSecurityManager shiroWebSecurityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//part1 设置Shiro安全管理器
		shiroFilterFactoryBean.setSecurityManager(shiroWebSecurityManager);
		
		//part2 设置资源权限和角色自定义过滤管理器
		Map<String, Filter> myFilterMap = new HashMap();
		myFilterMap.put("my-perms", new ShiroPermitAndOrFilter());//可以配置ShiroPermitAndOrFilter的Bean
		myFilterMap.put("my-roles", new ShiroRoleAndOrFilter());//可以配置ShiroRoleAndOrFilter的Bean
		//使用自定义拦截器
		shiroFilterFactoryBean.setFilters(myFilterMap);
		
		//part3 设置shiro过滤器链
		/*
		 *  Shiro内置过滤器，可以实现权限相关的拦截器
		 *  注意！！！ 有顺序要求
		 *      常见的过滤器：
		 *          anon: 无需认证（登录）就可以访问
		 *          authc: 必须登录认证才可以访问
		 *          user: 如果使用remember功能可以直接访问
		 *          perms: 该资源必须获得资源权限才可以访问perms[user:add] 多个为:perms[user:add,user:update]
		 *          roles: 该资源必须获得角色权限才可以访问roles[1] 多个为 roles[1,2,3]
		 */
		Map<String, String> filtersMap = new LinkedHashMap();
		//设置资源过滤器
		this.shiroFiltersMapAll(filtersMap);
		
		//part4	存储shiro过滤器链内容
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filtersMap);
		
		//part5	设置遇到认证错误的跳转页面
		//在未认证之前用户访问了启用认证或者需要授权的资源时跳转的页面，此处指定到登录页面
		shiroFilterFactoryBean.setLoginUrl("/login.html");
		//在认证了之后用户访问了需要授权的资源时跳转的页面，此处指定到登录页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/login.html");
		//登录成功之后跳转到的页面
		shiroFilterFactoryBean.setSuccessUrl("/");
		
		return shiroFilterFactoryBean;
	}
	
	/**
	 * Shiro过滤内容
	 *
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapAll(Map<String, String> filtersMap) {
		/* Shiro默认为anon，即不需要认证和授权就可以访问 */
		this.shiroFiltersMapAnon(filtersMap);
		
		/* Shiro过滤内容测试2：只需要认证的资源：需要认证才能访问 */
		this.shiroFiltersMapAuthc(filtersMap);
		
		/* Shiro过滤内容测试3：需要认证并且需要【用户资源权限】授权的资源才能访问 */
		this.shiroFiltersMapPerms(filtersMap);
		
		/* Shiro过滤内容测试4：需要认证并且需要【角色资源权限】授权的资源才能访问 */
		this.shiroFiltersMapRoles(filtersMap);
	}
	
	/**
	 * Shiro过滤内容测试1：Shiro默认为anon，即不需要认证和授权就可以访问
	 *
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapAnon(Map<String, String> filtersMap) {
		// filtersMap.put("/welcome", "anon");
		// filtersMap.put("/login.html", "anon");
	}
	
	/**
	 * Shiro过滤内容测试2：只需要认证的资源：需要认证才能访问
	 *
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapAuthc(Map<String, String> filtersMap) {
		// filtersMap.put("/session/userlist", "authc");
		// filtersMap.put("/session/deptlist", "authc");
		// /session/* 表示 /session/a 、/session/b、/session/c
		// /session/** 表示 /session/a 、/session/a/b、/session/a/b/c
		filtersMap.put("/session/*", "authc");
	}
	
	/**
	 * Shiro过滤内容测试3：需要认证并且需要【用户资源权限】授权的资源才能访问
	 *
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapPerms(Map<String, String> filtersMap) {
		/*
			保持“，”表示多个权限的并列关系，每个“，”分割的字符串可能是多个“或”权限的集合，再用“|”分割字符串，“|”需要转义。
			filtersMap.put("/admin/channel/update", "authc,perms[channel:update]");
			/admins/user/**=perms["user:add:*,user:modify:*"]
			filtersMap.put("/user/add","perms[user:add|root]");
			一个资源对应一个用户权限：filtersMap.put("/user/add", "perms[user:add]");
			一个资源对应多个用户权限：filtersMap.put("/user/add", "perms[]");
		 */
		// filtersMap.put("/user/add", "perms[user:add|admin:add]");
		// filtersMap.put("/user/update", "perms[user:update]");
		
		filtersMap.put("/user/add", "my-perms[user:add|admin:add]");
		filtersMap.put("/user/update", "perms[user:update]");
		filtersMap.put("/user/delete", "perms[user:delete]");
	}
	
	/**
	 * Shiro过滤内容测试4：需要认证并且需要【角色资源权限】授权的资源才能访问
	 *
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapRoles(Map<String, String> filtersMap) {
		filtersMap.put("/dept/update", "roles[user]");
		filtersMap.put("/dept/delete", "roles[delete]");
		filtersMap.put("/dept/add", "my-roles[admin,user1,user2]");
	}
	
}