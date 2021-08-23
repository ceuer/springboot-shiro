package com.ceuer.shiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Shiro资源权限自定义资源过滤器 支持一个资源对应多个权限或的关系
 */
@Component
public class ShiroPermitAndOrFilter extends AuthorizationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = this.getSubject(request, response);
		String[] perms = (String[]) ((String[]) mappedValue);
		boolean isPermitted = true;
		if (perms != null && perms.length > 0) {
			if (perms.length == 1) {
				if (!isOneOfPermitted(perms[0], subject)) {
					isPermitted = false;
				}
			} else if (!isAllPermitted(perms, subject)) {
				isPermitted = false;
			}
		}
		return isPermitted;
	}
	
	/**
	 * 以“，”分割的权限为并列关系的权限控制，分别对每个权限字符串进行“|”分割解析
	 * 若并列关系的权限有一个不满足则返回false
	 *
	 * @param permStrArray 以","分割的权限集合
	 * @param subject      当前用户的登录信息
	 * @return 是否拥有该权限
	 */
	private boolean isAllPermitted(String[] permStrArray, Subject subject) {
		boolean isPermitted = true;
		for (int index = 0, len = permStrArray.length; index < len; index++) {
			if (!isOneOfPermitted(permStrArray[index], subject)) {
				isPermitted = false;
			}
		}
		return isPermitted;
	}
	
	/**
	 * 判断以“|”分割的权限有一个满足的就返回true，表示权限的或者关系
	 *
	 * @param permStr 权限数组种中的一个字符串
	 * @param subject 当前用户信息
	 * @return 是否有权限
	 */
	private boolean isOneOfPermitted(String permStr, Subject subject) {
		boolean isPermitted = false;
		String[] permArr = permStr.split("\\|");
		if (permArr.length > 0) {
			for (int index = 0, len = permArr.length; index < len; index++) {
				if (subject.isPermitted(permArr[index])) {
					isPermitted = true;
				}
			}
		}
		return isPermitted;
	}
}