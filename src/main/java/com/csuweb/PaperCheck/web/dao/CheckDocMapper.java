package com.csuweb.PaperCheck.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.csuweb.PaperCheck.web.model.CheckDoc;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;

public interface CheckDocMapper {
    int deleteByPrimaryKey(String guid);

    int insert(CheckDocWithBLOBs record);

    int insertSelective(CheckDocWithBLOBs record);

    CheckDocWithBLOBs selectByPrimaryKey(String guid);

    int updateByPrimaryKeySelective(CheckDocWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CheckDocWithBLOBs record);

    int updateByPrimaryKey(CheckDoc record);
    
    int selectCountByState(@Param("state") String state,@Param("state2") String state2);//查待查重文章的状态是这两个状态的个数

	List<CheckDocWithBLOBs> selectCheckDoc(@Param("state") String state,@Param("state2") String state2);//查待查重文章的状态是这两个状态的·所有·文章

	List<CheckDocWithBLOBs> selectCheckDocByUserId(@Param("userid")String userid);
}