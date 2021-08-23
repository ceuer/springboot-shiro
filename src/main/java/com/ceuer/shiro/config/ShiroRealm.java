package com.ceuer.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroRealm extends AuthorizingRealm {
	
	//part2	Shiro授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		System.out.println("执行Shiro【授权】方法-> Authorization");
		return null;
	}
	
	//part1	Shiro认证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		System.out.println("执行Shiro【授权】方法-> Authentication");
		
		//强转AuthenticationToken对象中为UsernamePasswordToken对象，获取用户token
		UsernamePasswordToken token=(UsernamePasswordToken)authenticationToken;
		
		/*
			模拟 根据从token获取登录账号，然后根据登录账号从数据库查询是否存在账号信息
			username：admin
			password：111
		 */
		String dataBaseUserPassWord="111";//此处模拟从数据库中获取admin此账号对应的密码
		String username=token.getUsername();//获取登录账号
		return new SimpleAuthenticationInfo(username,dataBaseUserPassWord,"");
	}
}
