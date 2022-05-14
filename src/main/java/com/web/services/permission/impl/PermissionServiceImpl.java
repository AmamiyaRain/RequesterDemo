package com.web.services.permission.impl;

import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.mapper.permission.PermissionMapper;
import com.web.mapper.role.RoleMapper;
import com.web.pojo.DAO.role.RoleDAO;
import com.web.pojo.VO.user.UserVO;
import com.web.services.permission.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private PermissionMapper permissionMapper;

	@Override
	public boolean checkUserPermissionExists(UserVO userVO, BigInteger permissionCode) {
		RoleDAO roleDAO;
		try {
			roleDAO = roleMapper.selectByPrimaryKey((userVO.getUserRole()));
		} catch (Exception e) {
			throw new BusinessException(BusinessErrorEnum.ROLE_NOT_EXISTS);
		}
		if (roleDAO == null) {
			throw new BusinessException(BusinessErrorEnum.ROLE_NOT_EXISTS);
		}
		BigInteger userPermissions = new BigInteger(String.valueOf(roleDAO.getUserRolePermissions()), 2);
		return userPermissions.and(permissionCode).equals(permissionCode);
	}
}
