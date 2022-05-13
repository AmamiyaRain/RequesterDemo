package com.web.services.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.pojo.DAO.user.UserDAO;
import com.web.pojo.DTO.user.UserDeleteDTO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserLoginVO;
import com.web.pojo.VO.user.UserTokenVO;

public interface UserService {


	int deleteByPrimaryKey(Integer id);

	int insert(UserDAO record);

	int insertSelective(UserDAO record);

	UserDAO selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(UserDAO record);

	int updateByPrimaryKey(UserDAO record);

	void register(UserRegisterDTO userRegisterDTO);

	UserTokenVO login(UserLoginDTO userLoginDTO);

	UserTokenVO getLoginSuccessUserVO(UserDAO userDAO) throws JsonProcessingException;

	void modifyPassword(UserModifyPasswordDTO userModifyPasswordDTO, UserLoginVO userLoginVO);

	void deleteUser(UserDeleteDTO userDeleteDTO, UserLoginVO userLoginVO);

}
