package com.csuweb.PaperCheck.web.biz;

import java.util.List;

import com.csuweb.PaperCheck.web.model.User;


public interface UserBiz {

	String makeMD5(String password);//对密码进行md5加密

	User authentication(User user);//用于shiro的登录认证

	int addUser(User user);//增加用户，用户注册

	User selUserByPrimaryKey(String userid);
	
	List<User> selAllowedUser(String roleids);

	int upUser(User user);//修改密码，完善资料的方法

	int delUser(String userid);//删除用户的方法
}
