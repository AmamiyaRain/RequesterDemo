package com.web.mapper;

import com.web.pojo.DAO.FinalUserAccountDAO;

public interface FinalUserAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinalUserAccountDAO record);

    int insertSelective(FinalUserAccountDAO record);

    FinalUserAccountDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinalUserAccountDAO record);

    int updateByPrimaryKey(FinalUserAccountDAO record);

    FinalUserAccountDAO selectByUserName(String userName);
}