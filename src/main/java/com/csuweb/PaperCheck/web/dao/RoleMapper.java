package com.csuweb.PaperCheck.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.csuweb.PaperCheck.web.model.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKeyWithBLOBs(Role record);

    int updateByPrimaryKey(Role record);

	List<Role> selAllRole();

	Role selRoleByRoleName(@Param("rolename")String rolename);
}