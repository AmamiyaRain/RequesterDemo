package com.javaweb.controller;

import com.javaweb.base.common.CommonResponse;
import com.javaweb.pojo.DAO.FinalUserAccountDAO;
import com.javaweb.pojo.DTO.UserRegisterDTO;
import com.javaweb.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 * @author ybw
 * @date 2022/5/9
 */

@RestController
@RequestMapping("/user")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户id查询用户信息
     * @param id 用户id
     *
     * @return {@link CommonResponse<FinalUserAccountDAO>}
     */

    @GetMapping("/getUserById")
    public CommonResponse<FinalUserAccountDAO> getUser(@RequestParam(value = "id",required = false) int id) {
        FinalUserAccountDAO finalUserAccountDAO = userService.selectByPrimaryKey(id);
        return CommonResponse.create(finalUserAccountDAO, null);
    }

    /**
     * 注册用户
     * @param userRegisterDTO 用户注册信息
     * @return {@link CommonResponse<FinalUserAccountDAO>}
     */

    @PostMapping("/register")
    public CommonResponse<String> register(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        userService.register(userRegisterDTO);
        return CommonResponse.create(null,"注册成功");
    }
}
