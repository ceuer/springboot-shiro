package com.ceuer.shiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Shiro角色权限自定义资源过滤器 支持一个资源对应多个角色或的关系
 */
@Component
public class ShiroRoleAndOrFilter extends RolesAuthorizationFilter {
	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		
		final Subject subject = getSubject(request, response);
		final String[] rolesArray = (String[]) mappedValue;
		
		if (rolesArray == null || rolesArray.length == 0) {
			// 无指定角色时，无需检查，允许访问
			return true;
		}
		
		for (String roleName : rolesArray) {
			if (subject.hasRole(roleName)) {
				return true;
			}
		}
		
		return false;
	}
}