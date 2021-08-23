package com.ceuer.shiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

public class ShiroRealm extends AuthorizingRealm {
	
	//part2	Shiro授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		System.out.println("执行Shiro【授权】方法-> Authorization");
		//获取当前用户Subject对象
		Subject subject= SecurityUtils.getSubject();
		
		//此处从当前用户中获取认证过称中传递的参数，此处是用户对象（下行代码中SimpleAuthenticationInfo参数中的对一个参数username）
		//new SimpleAuthenticationInfo(username, dataBaseUserPassWord, "");
		String username=(String)subject.getPrincipal();
		
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermission("user:add");
		//simpleAuthorizationInfo.addStringPermission("admin:add");
		simpleAuthorizationInfo.addStringPermission("user:update");
		//simpleAuthorizationInfo.addRole("1");
		
		return simpleAuthorizationInfo;
	}
	
	//part1	Shiro认证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		System.out.println("执行Shiro【认证】方法-> Authentication");
		
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
