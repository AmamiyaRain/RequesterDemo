package com.web.service.impl;

import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.mapper.UserMapper;
import com.web.pojo.DAO.FinalUserAccountDAO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserLoginVO;
import com.web.pojo.VO.user.UserTokenVO;
import com.web.service.UserService;
import com.web.util.security.SecurityUtil;
import com.web.util.security.SyncUtil;
import com.web.util.security.TokenUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {


	@Resource
	private UserMapper userMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(FinalUserAccountDAO record) {
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(FinalUserAccountDAO record) {
		return userMapper.insertSelective(record);
	}

	@Override
	public FinalUserAccountDAO selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(FinalUserAccountDAO record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(FinalUserAccountDAO record) {
		return userMapper.updateByPrimaryKey(record);
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		if (userRegisterDTO.getUserTel() == null || StringUtils.isEmpty(userRegisterDTO.getUserPassword()) || StringUtils.isEmpty(userRegisterDTO.getUserName())
		) {
			throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
		}

		if (userMapper.selectByUserName(userRegisterDTO.getUserName()) != null) {
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
		try {
			finalUserAccountDAO.setUserName(userRegisterDTO.getUserName());
			finalUserAccountDAO.setUserPassword(encPassword);
			finalUserAccountDAO.setUserTel(userRegisterDTO.getUserTel());
			finalUserAccountDAO.setUserSalt(salt);
			finalUserAccountDAO.setUserRole(1);
			userMapper.insert(finalUserAccountDAO);
		} catch (Exception e) {
			throw new BusinessException(BusinessErrorEnum.REGISTER_FAILED);
		}
	}

	@Override
	public UserTokenVO login(UserLoginDTO userLoginDTO) {
		if (Strings.isEmpty(userLoginDTO.getUserName()) || Strings.isEmpty(userLoginDTO.getUserPassword())) {
			throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
		}
		FinalUserAccountDAO finalUserAccountDAO = userMapper.selectByUserName(userLoginDTO.getUserName());
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

		return getLoginSuccessUserVO(finalUserAccountDAO);
	}


	@Override
	public UserTokenVO getLoginSuccessUserVO(FinalUserAccountDAO finalUserAccountDAO) {
		// 创建用户视图模型
		UserLoginVO userVO = new UserLoginVO();
		BeanUtils.copyProperties(finalUserAccountDAO, userVO);

		// 创建用户登录成功视图模型
		UserTokenVO userTokenVO = new UserTokenVO();
		try {
			userTokenVO.setUserToken(TokenUtil.createJwt(userVO));
		} catch (Exception e) {
			throw new BusinessException(BusinessErrorEnum.TOKEN_GENERATE_FAILED);
		}

		return userTokenVO;
	}

	@Override
	public void modifyPassword(UserModifyPasswordDTO userModifyPasswordDTO, UserLoginVO userLoginVO) {
		if (SyncUtil.start(userModifyPasswordDTO)) {
			try {
				FinalUserAccountDAO finalUserAccountDAO = userMapper.selectByUserName(userLoginVO.getUserName());
				if (finalUserAccountDAO == null) {
					throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
				}
				if (Strings.isEmpty(userModifyPasswordDTO.getUserName()) || Strings.isEmpty(userModifyPasswordDTO.getUserPassword()) || Strings.isEmpty(userModifyPasswordDTO.getUpdatedUserPassword())) {
					throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
				}
				String oldPassword;
				try {
					oldPassword = SecurityUtil.getMd5(finalUserAccountDAO.getUserPassword(), finalUserAccountDAO.getUserSalt());
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.CHECK_OLD_PASSWORD_FAILED);
				}
				if (!oldPassword.equals(userModifyPasswordDTO.getUserPassword())) {
					throw new BusinessException(BusinessErrorEnum.CHECK_OLD_PASSWORD_FAILED);
				}
				if (userModifyPasswordDTO.getUserPassword().equals(userModifyPasswordDTO.getUpdatedUserPassword())) {
					throw new BusinessException(BusinessErrorEnum.PASSWORD_IS_SAME);
				}
				try {
					finalUserAccountDAO.setUserPassword(SecurityUtil.getMd5(userModifyPasswordDTO.getUpdatedUserPassword(), finalUserAccountDAO.getUserSalt()));
					userMapper.updateByPrimaryKey(finalUserAccountDAO);
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.FAILED_TO_MODIFY_PASSWORD);
				}
			} finally {
				SyncUtil.finish(userModifyPasswordDTO);
			}
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}
}

