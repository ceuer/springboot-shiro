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
		
		this.shiroFiltersMapTest1(filtersMap);//Shiro过滤器内容过滤测试1：简单测试/user/add可以不受限访问，/user/update需要认证才能访问
		
		//part3	存储shiro过滤器链内容
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filtersMap);
		
		return shiroFilterFactoryBean;
	}
	
	/**
	 * Shiro过滤器内容过滤测试1：简单测试/user/add可以不受限访问，/user/update需要认证才能访问
	 * @param filtersMap 过滤器map对象
	 */
	private void shiroFiltersMapTest1(Map<String, String> filtersMap){
		filtersMap.put("/user/add", "anon");
		filtersMap.put("/user/update", "authc");
	}
	
}
