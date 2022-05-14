package com.web.services.permission;

import com.web.pojo.VO.user.UserVO;

import java.math.BigInteger;

public interface PermissionService {
	boolean checkUserPermissionExists(UserVO userVO, BigInteger permissionCode);
}
