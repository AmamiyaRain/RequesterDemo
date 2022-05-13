package com.web.mapper.role;

import com.web.pojo.DAO.role.RoleDAO;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleDAO record);

    int insertSelective(RoleDAO record);

    RoleDAO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleDAO record);

    int updateByPrimaryKey(RoleDAO record);
}