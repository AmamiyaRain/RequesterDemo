package com.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.mapper.FinalUserAccountMapper;
import com.web.pojo.DAO.FinalUserAccountDAO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserLoginVO;
import com.web.service.UserService;
import com.web.util.security.SecurityUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private FinalUserAccountMapper finalUserAccountMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return finalUserAccountMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(FinalUserAccountDAO record) {
		return finalUserAccountMapper.insert(record);
	}

	@Override
	public int insertSelective(FinalUserAccountDAO record) {
		return finalUserAccountMapper.insertSelective(record);
	}

	@Override
	public FinalUserAccountDAO selectByPrimaryKey(Integer id) {
		return finalUserAccountMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(FinalUserAccountDAO record) {
		return finalUserAccountMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(FinalUserAccountDAO record) {
		return finalUserAccountMapper.updateByPrimaryKey(record);
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		if (userRegisterDTO.getUserTel() == null || StringUtils.isEmpty(userRegisterDTO.getUserPassword()) || StringUtils.isEmpty(userRegisterDTO.getUserName())
		) {
			throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
		}

		if (finalUserAccountMapper.selectByUserName(userRegisterDTO.getUserName()) != null) {
			throw new BusinessException(BusinessErrorEnum.USER_ALREADY_EXISTS);
		}
		String salt = SecurityUtil.getDefaultLengthSalt();
		String encPassword;
		try {
			encPassword = SecurityUtil.getMd5(userRegisterDTO.getUserPassword(), salt);
		} catch (Exception e) {
			throw new BusinessException(BusinessErrorEnum.REGISTER_FAILED);
		}
		FinalUserAccountDAO finalUserAccountDAO = new FinalUserAccountDAO();
		finalUserAccountDAO.setUserName(userRegisterDTO.getUserName());
		finalUserAccountDAO.setUserPassword(encPassword);
		finalUserAccountDAO.setUserTel(userRegisterDTO.getUserTel());
		finalUserAccountDAO.setUserSalt(salt);
		finalUserAccountMapper.insert(finalUserAccountDAO);
	}

	@Override
	public UserLoginVO login(UserLoginDTO userLoginDTO) {
		if (Strings.isEmpty(userLoginDTO.getUserName()) || Strings.isEmpty(userLoginDTO.getUserPassword())) {
			throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
		}
		FinalUserAccountDAO finalUserAccountDAO = finalUserAccountMapper.selectByUserName(userLoginDTO.getUserName());
		if (finalUserAccountDAO == null) {
			throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
		}
		String encPassword;
		try {
			encPassword = SecurityUtil.getMd5(userLoginDTO.getUserPassword(), finalUserAccountDAO.getUserSalt());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!encPassword.equals(finalUserAccountDAO.getUserPassword())) {
			throw new BusinessException(BusinessErrorEnum.LOGIN_FAILED);
		}

	}

	private UserLoginVO getLoginSuccessUserVO(FinalUserAccountDAO finalUserAccountDAO) throws JsonProcessingException {
		// 创建用户视图模型
		UserLoginVO userVO = new UserLoginVO();
		BeanUtils.copyProperties(finalUserAccountDAO, userVO);

//		// 创建用户登录成功视图模型
//		LoginSuccessUserVO loginSuccessUserVO = new LoginSuccessUserVO();
//		BeanUtils.copyProperties(userVO, loginSuccessUserVO);
//		loginSuccessUserVO.setToken(TokenUtil.createJwt(userVO));

//		return loginSuccessUserVO;
	}
}

