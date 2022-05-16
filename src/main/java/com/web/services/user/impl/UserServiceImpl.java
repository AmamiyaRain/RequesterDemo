package com.web.services.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.base.constants.PermissionConstant;
import com.web.base.entity.PageResult;
import com.web.base.entity.SMSEntity;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.mapper.permission.PermissionMapper;
import com.web.mapper.role.RoleMapper;
import com.web.mapper.user.UserMapper;
import com.web.pojo.DAO.user.UserDAO;
import com.web.pojo.DTO.page.PageDTO;
import com.web.pojo.DTO.sms.SendMessageDTO;
import com.web.pojo.DTO.user.UserDeleteDTO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserVO;
import com.web.pojo.VO.user.UserTokenVO;
import com.web.services.oss.OssService;
import com.web.services.permission.PermissionService;
import com.web.services.sms.SMSService;
import com.web.services.user.UserService;
import com.web.util.security.SecurityUtil;
import com.web.util.security.SyncUtil;
import com.web.util.security.TokenUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private OssService ossService;

	@Resource
	private UserMapper userMapper;

	@Resource
	private PermissionMapper permissionMapper;

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private PermissionService permissionService;

	@Resource
	private SMSService smsService;

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
				userDAO.setUserAvatar(null);
				userMapper.insert(userDAO);
			} catch (Exception e) {
				throw new BusinessException(BusinessErrorEnum.REGISTER_FAILED);
			}
			SyncUtil.finish(userRegisterDTO);
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}

	@Override
	public UserTokenVO modifyAvatar(MultipartFile userAvatar, UserVO userVO) {
		if (SyncUtil.start(userAvatar + "-" + userVO)) {
			String userAvatarUrl;
			if (userAvatar != null) {
				userAvatarUrl = ossService.uploadImage(userAvatar);
				UserDAO userDAO = userMapper.selectByUserName(userVO.getUserName());
				userDAO.setUserAvatar(userAvatarUrl);
				BeanUtils.copyProperties(userDAO, userVO);
				userMapper.updateByPrimaryKeySelective(userDAO);
				SyncUtil.finish(userAvatar + "-" + userVO);
				return getLoginSuccessUserVO(userDAO);
			}
			SyncUtil.finish(null + "-" + userVO);
			throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);

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
		UserVO userVO = new UserVO();
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
	public void modifyPassword(UserModifyPasswordDTO userModifyPasswordDTO, UserVO userVO) {
		if (SyncUtil.start(userModifyPasswordDTO)) {
			try {
				UserDAO userDAO = userMapper.selectByUserName(userVO.getUserName());
				if (userDAO == null) {
					throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
				}
				if (Strings.isEmpty(userModifyPasswordDTO.getOldPassword()) || Strings.isEmpty(userModifyPasswordDTO.getNewPassword())) {
					throw new BusinessException(BusinessErrorEnum.MISSING_REQUIRED_PARAMETERS);
				}
				String oldPassword;
				try {
					oldPassword = SecurityUtil.getMd5(userModifyPasswordDTO.getOldPassword(), userDAO.getUserSalt());
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.CHECK_OLD_PASSWORD_FAILED);
				}
				if (!oldPassword.equals(userDAO.getUserPassword())) {
					throw new BusinessException(BusinessErrorEnum.CHECK_OLD_PASSWORD_FAILED);
				}
				if (userModifyPasswordDTO.getOldPassword().equals(userModifyPasswordDTO.getNewPassword())) {
					throw new BusinessException(BusinessErrorEnum.PASSWORD_IS_SAME);
				}
				try {
					userDAO.setUserPassword(SecurityUtil.getMd5(userModifyPasswordDTO.getNewPassword(), userDAO.getUserSalt()));
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
	public void deleteUser(UserDeleteDTO userDeleteDTO, UserVO userVO) {
		if (SyncUtil.start(userDeleteDTO)) {
			try {
				if (!permissionService.checkUserPermissionExists(userVO, PermissionConstant.USER_MANAGEMENT)) {
					throw new BusinessException(BusinessErrorEnum.PERMISSION_DENIED);
				}
				if (userDeleteDTO.getUserId().equals(userVO.getId())) {
					throw new BusinessException(BusinessErrorEnum.CANNOT_DELETE_OWN_ACCOUNT);
				}
				UserDAO userDAO = null;
				try {
					userDAO = userMapper.selectByPrimaryKey(userDeleteDTO.getUserId());
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.USER_NOT_EXISTS);
				}
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

	@Override
	public PageResult<UserVO> getUserList(PageDTO pageDTO, UserVO userVO) {
		if (SyncUtil.start(pageDTO)) {
			try {
				if (!permissionService.checkUserPermissionExists(userVO, PermissionConstant.USER_MANAGEMENT)) {
					throw new BusinessException(BusinessErrorEnum.PERMISSION_DENIED);
				}
				try {
					PageHelper.startPage(pageDTO.getPageIndex(), pageDTO.getPageSize());
				} catch (Exception e) {
					throw new BusinessException(BusinessErrorEnum.PAGE_PARAMETER_ERROR);
				}
				List<UserDAO> userDAOList = userMapper.selectAll();
				PageInfo<UserDAO> pageInfo = new PageInfo<>(userDAOList);
				List<UserVO> userVOList = new ArrayList<>();
				for (UserDAO userDAO : userDAOList) {
					UserVO userVO1 = new UserVO();
					BeanUtils.copyProperties(userDAO, userVO1);
					userVO1.setUserRoleName(roleMapper.selectByPrimaryKey(userDAO.getUserRole()).getUserRoleName());
					userVOList.add(userVO1);
				}
				return new PageResult<>(pageInfo.getTotal(), userVOList);
			} finally {
				SyncUtil.finish(pageDTO);
			}
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}

	@Override
	public void userBindingTel(SendMessageDTO sendMessageDTO, UserVO userVO) {
		SMSEntity smsEntity = new SMSEntity();
		smsEntity.setToken(sendMessageDTO.getToken());
		smsEntity.setPhone(sendMessageDTO.getUserTel());
		smsService.sendSMS(smsEntity);
	}
}

