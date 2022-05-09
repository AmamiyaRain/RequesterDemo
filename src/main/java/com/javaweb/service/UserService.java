package com.javaweb.service;

import com.javaweb.pojo.DAO.FinalUserAccountDAO;
import com.javaweb.pojo.DTO.UserRegisterDTO;

public interface UserService {


    int deleteByPrimaryKey(Integer id);

    int insert(FinalUserAccountDAO record);

    int insertSelective(FinalUserAccountDAO record);

    FinalUserAccountDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinalUserAccountDAO record);

    int updateByPrimaryKey(FinalUserAccountDAO record);

    void register(UserRegisterDTO userRegisterDTO);

}
