package com.csuweb.PaperCheck.web.biz;

import java.util.List;

import com.csuweb.PaperCheck.web.model.Role;

public interface RoleBiz {

	Role selRoleByPrimaryKey(String id);

	List<Role> selAllowedRole(String permissionids);//通过权限查找此权限集合及其子集所对应的角色集合
	
	int addRole(Role role);
	
	int upRole(Role role);
	
	int delRole(String roleid);

	Role selRoleByRoleName(String rolename);
}
