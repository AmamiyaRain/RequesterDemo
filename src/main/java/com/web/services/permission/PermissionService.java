package com.web.services.permission;

import com.web.pojo.VO.user.UserLoginVO;

import java.math.BigInteger;

public interface PermissionService {
	boolean checkUserPermissionExists(UserLoginVO userLoginVO, BigInteger permissionCode);
}
