package com.csuweb.PaperCheck.web.biz.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.web.biz.UserBiz;
import com.csuweb.PaperCheck.web.dao.UserMapper;
import com.csuweb.PaperCheck.web.model.User;
@Service
public class UserBizImpl implements UserBiz {
	
	@Resource
	private UserMapper usermapper;

	public String makeMD5(String password) {
		MessageDigest md;
		try {
			//// 生成一个MD5加密计算摘要
			md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(password.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			 // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String pwd = new BigInteger(1, md.digest()).toString(16);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	public User authentication(User user) {
		
		return usermapper.authentication(user);
	}

	/**
	 * 增加用户，用户注册
	 */
	@Override
	public int addUser(User user) {
		return usermapper.insertSelective(user);
	}

	@Override
	public User selUserByPrimaryKey(String userid) {
		return usermapper.selectByPrimaryKey(userid);
	}

	@Override
	public int upUser(User user) {
		
		return usermapper.updateByPrimaryKey(user);
	}

	@Override
	public int delUser(String userid) {
		// TODO Auto-generated method stub
		return usermapper.deleteByPrimaryKey(userid);
	}

	@Override
	public List<User> selAllowedUser(String roleids) {
		// TODO Auto-generated method stub
		return usermapper.selAllowedUser(roleids.split(","));
	}

	@Override
	public List<User> selUserCheck() {
		// TODO Auto-generated method stub
		return usermapper.selUserCheck();
	}
}
