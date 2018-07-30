package com.csuweb.PaperCheck.web.dao;

import java.util.List;

import com.csuweb.PaperCheck.web.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User authentication(User user);//通过用户的登录名loginname获取用户数据

	List<User> selAllowedUser(String[] roleids);//通过获取到的可操作roleids查询出其权限内可操作的user对象

	int updateBydeleteRoleId(String roleid);//在删除某个角色时同时修改user对象的roleid
	
	int selectCountUserByRoleId(String roleid);//查询该角色id所对应的用户个数

	List<User> selUserCheck();
}