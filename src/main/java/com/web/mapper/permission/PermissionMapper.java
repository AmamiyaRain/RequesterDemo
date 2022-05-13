package com.web.mapper.permission;

import com.web.pojo.DAO.permission.PermissionDAO;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PermissionDAO record);

    int insertSelective(PermissionDAO record);

    PermissionDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PermissionDAO record);

    int updateByPrimaryKey(PermissionDAO record);
}