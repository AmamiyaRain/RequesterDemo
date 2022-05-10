package com.web.service;

import com.web.pojo.DAO.FinalUserAccountDAO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserLoginVO;

public interface UserService {


    int deleteByPrimaryKey(Integer id);

    int insert(FinalUserAccountDAO record);

    int insertSelective(FinalUserAccountDAO record);

    FinalUserAccountDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinalUserAccountDAO record);

    int updateByPrimaryKey(FinalUserAccountDAO record);

    void register(UserRegisterDTO userRegisterDTO);

    UserLoginVO login(UserLoginDTO userLoginDTO);

    UserLoginVO loginSuccessUserVO(FinalUserAccountDAO finalUserAccountDAO);

}
