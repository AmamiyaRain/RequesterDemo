package com.web.controller;

import com.web.base.common.CommonResponse;
import com.web.pojo.DAO.FinalUserAccountDAO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 * @author ybw
 * @date 2022/5/9
 */

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
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
    @ApiOperation(value = "根据用户id查询用户信息", notes = "根据用户id查询用户信息")
    public CommonResponse<FinalUserAccountDAO> getUser(@RequestParam(value = "id",required = true) int id) {
        FinalUserAccountDAO finalUserAccountDAO = userService.selectByPrimaryKey(id);
        return CommonResponse.create(finalUserAccountDAO, null);
    }

    /**
     * 注册用户
     * @param userRegisterDTO 用户注册信息
     * @return {@link CommonResponse<FinalUserAccountDAO>}
     */

    @PostMapping("/register")
    @ApiOperation(value = "注册用户", notes = "注册用户")
    public CommonResponse<String> register(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        userService.register(userRegisterDTO);
        return CommonResponse.create(null,"注册成功");
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录用户", notes = "登录用户")
    public CommonResponse<String> login(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        return CommonResponse.create(null,"登录成功");
    }
}
