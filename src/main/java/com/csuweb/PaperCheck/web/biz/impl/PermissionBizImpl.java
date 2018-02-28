package com.csuweb.PaperCheck.web.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.web.biz.PermissionBiz;
import com.csuweb.PaperCheck.web.dao.PermissionMapper;
import com.csuweb.PaperCheck.web.model.Permission;
@Service
public class PermissionBizImpl implements PermissionBiz {
	
	@Resource
	private PermissionMapper permissionmapper;
	@Override
	public Permission selPermissionByPrimaryKey(String id) {
		return permissionmapper.selectByPrimaryKey(id);
	}

}
