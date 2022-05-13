package com.web.mapper.user;

import com.web.pojo.DAO.user.UserDAO;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDAO record);

    int insertSelective(UserDAO record);

    UserDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDAO record);

    int updateByPrimaryKey(UserDAO record);

    UserDAO selectByUserName(String userName);
}