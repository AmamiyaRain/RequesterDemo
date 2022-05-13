package com.web.services.user.impl;

import com.web.base.constants.PermissionConstant;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.mapper.permission.PermissionMapper;
import com.web.mapper.role.RoleMapper;
import com.web.mapper.user.UserMapper;
import com.web.pojo.DAO.user.UserDAO;
import com.web.pojo.DTO.user.UserDeleteDTO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserLoginVO;
import com.web.pojo.VO.user.UserTokenVO;
import com.web.services.permission.PermissionService;
import com.web.services.user.UserService;
import com.web.util.security.SecurityUtil;
import com.web.util.security.SyncUtil;
import com.web.util.security.TokenUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {


	@Resource
	private UserMapper userMapper;

	@Resource
	private PermissionMapper permissionMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private PermissionService permissionService;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserDAO record) {
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(UserDAO record) {
		return userMapper.insertSelective(record);
	}

	@Override
	public UserDAO selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserDAO record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserDAO record) {
		return userMapper.updateByPrimaryKey(record);
	}

	@Override
	public void register(UserRegisterDTO userRegisterDTO) {
		if (SyncUtil.start(userRegisterDTO)) {
			try {
				if (userRegisterDTO.getUserTel() == null || Strings.isEmpty(userRegisterDTO.getUserPassword()) || Strings.isEmpty(userRegisterDTO.getUserName())
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
				UserDAO userDAO = new UserDAO();
				try {
					userDAO.setUserName(userRegisterDTO.getUserName());
					userDAO.setUserPassword(encPassword);
					userDAO.setUserTel(userRegisterDTO.getUserTel());
					userDAO.setUserSalt(salt);
					userDAO.setUserRole(1);
					userDAO.setUserAvatar("我头像呢?我头像呢?我头像呢?");
					userMapper.insert(userDAO);
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.REGISTER_FAILED);
				}
			} finally {
				SyncUtil.finish(userRegisterDTO);
			}
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}

	@Override
	public UserTokenVO login(UserLoginDTO userLoginDTO) {
		if (SyncUtil.start(userLoginDTO)) {
			if (Strings.isEmpty(userLoginDTO.getUserName()) || Strings.isEmpty(userLoginDTO.getUserPassword())) {
				throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
			}
			UserDAO userDAO = userMapper.selectByUserName(userLoginDTO.getUserName());
			if (userDAO == null) {
				throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
			}
			String encPassword;
			try {
				encPassword = SecurityUtil.getMd5(userLoginDTO.getUserPassword(), userDAO.getUserSalt());
			} catch (Exception e) {
				throw new BusinessException(BusinessErrorEnum.ABNORMAL_DATA);
			}
			if (!encPassword.equals(userDAO.getUserPassword())) {
				throw new BusinessException(BusinessErrorEnum.LOGIN_FAILED);
			}
			SyncUtil.finish(userLoginDTO);
			return getLoginSuccessUserVO(userDAO);
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}


	@Override
	public UserTokenVO getLoginSuccessUserVO(UserDAO userDAO) {
		// 创建用户视图模型
		UserLoginVO userVO = new UserLoginVO();
		BeanUtils.copyProperties(userDAO, userVO);
		// 创建用户登录成功视图模型
		try {
			userVO.setUserRoleName(roleMapper.selectByPrimaryKey(userDAO.getUserRole()).getUserRoleName());
		} catch (Exception e) {
			throw new BusinessException(BusinessErrorEnum.ROLE_NOT_EXISTS);
		}
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
				UserDAO userDAO = userMapper.selectByUserName(userLoginVO.getUserName());
				if (userDAO == null) {
					throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
				}
				if (Strings.isEmpty(userModifyPasswordDTO.getUserName()) || Strings.isEmpty(userModifyPasswordDTO.getUserPassword()) || Strings.isEmpty(userModifyPasswordDTO.getUpdatedUserPassword())) {
					throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
				}
				String oldPassword;
				try {
					oldPassword = SecurityUtil.getMd5(userDAO.getUserPassword(), userDAO.getUserSalt());
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
					userDAO.setUserPassword(SecurityUtil.getMd5(userModifyPasswordDTO.getUpdatedUserPassword(), userDAO.getUserSalt()));
					userMapper.updateByPrimaryKey(userDAO);
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

	@Override
	public void deleteUser(UserDeleteDTO userDeleteDTO, UserLoginVO userLoginVO) {
		if (SyncUtil.start(userDeleteDTO)) {
			try {
				if (!permissionService.checkUserPermissionExists(userLoginVO, PermissionConstant.USER_MANAGEMENT)) {
					throw new BusinessException(BusinessErrorEnum.PERMISSION_DENIED);
				}
				if (userDeleteDTO.getUserId().equals(userLoginVO.getId())) {
					throw new BusinessException(BusinessErrorEnum.CANNOT_DELETE_OWN_ACCOUNT);
				}
				UserDAO userDAO = userMapper.selectByPrimaryKey(userDeleteDTO.getUserId());
				if (userDAO == null) {
					throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
				}
				userMapper.deleteByPrimaryKey(userDeleteDTO.getUserId());
			} finally {
				SyncUtil.finish(userDeleteDTO);
			}
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}


}

