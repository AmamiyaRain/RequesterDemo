package com.javaweb.service.impl;

import com.javaweb.base.enums.BusinessErrorEnum;
import com.javaweb.base.exceptions.BusinessException;
import com.javaweb.mapper.FinalUserAccountMapper;
import com.javaweb.pojo.DAO.FinalUserAccountDAO;
import com.javaweb.pojo.DTO.UserRegisterDTO;
import com.javaweb.service.UserService;
import com.javaweb.util.security.SecurityUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

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
        if (userRegisterDTO.getUserTel() == null || Strings.isEmpty(userRegisterDTO.getUserPassword()) || Strings.isEmpty(userRegisterDTO.getUserName())
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


}

