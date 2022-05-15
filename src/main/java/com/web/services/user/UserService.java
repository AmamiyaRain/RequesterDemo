package com.web.services.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.base.entity.PageResult;
import com.web.pojo.DAO.user.UserDAO;
import com.web.pojo.DTO.page.PageDTO;
import com.web.pojo.DTO.user.UserDeleteDTO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserAvatarVO;
import com.web.pojo.VO.user.UserVO;
import com.web.pojo.VO.user.UserTokenVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {


	int deleteByPrimaryKey(Integer id);

	int insert(UserDAO record);

	int insertSelective(UserDAO record);

	UserDAO selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(UserDAO record);

	int updateByPrimaryKey(UserDAO record);

	void register( UserRegisterDTO userRegisterDTO);

	UserTokenVO login(UserLoginDTO userLoginDTO);

	UserTokenVO getLoginSuccessUserVO(UserDAO userDAO) throws JsonProcessingException;

	void modifyPassword(UserModifyPasswordDTO userModifyPasswordDTO, UserVO userVO);

	void deleteUser(UserDeleteDTO userDeleteDTO, UserVO userVO);

	UserTokenVO modifyAvatar(MultipartFile userAvatar, UserVO userVO);

	PageResult<UserVO> getUserList(PageDTO pageDTO, UserVO userVO);
}
