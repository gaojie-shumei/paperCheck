package com.csuweb.PaperCheck.web.dao;

import com.csuweb.PaperCheck.web.model.QA;
import com.csuweb.PaperCheck.web.model.QAWithBLOBs;

public interface QAMapper {
    int deleteByPrimaryKey(String id);

    int insert(QAWithBLOBs record);

    int insertSelective(QAWithBLOBs record);

    QAWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QAWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(QAWithBLOBs record);

    int updateByPrimaryKey(QA record);
}