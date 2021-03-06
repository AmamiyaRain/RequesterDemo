package com.web.controllers.user;

import com.alibaba.fastjson.JSONObject;
import com.web.base.common.CommonResponse;
import com.web.base.entity.CaptchaRequestEntity;
import com.web.base.entity.CaptchaResultEntity;
import com.web.base.entity.PageResult;
import com.web.pojo.DAO.user.UserDAO;
import com.web.pojo.DTO.page.PageDTO;
import com.web.pojo.DTO.sms.SendMessageDTO;
import com.web.pojo.DTO.user.UserDeleteDTO;
import com.web.pojo.DTO.user.UserLoginDTO;
import com.web.pojo.DTO.user.UserModifyPasswordDTO;
import com.web.pojo.DTO.user.UserRegisterDTO;
import com.web.pojo.VO.user.UserAvatarVO;
import com.web.pojo.VO.user.UserTokenVO;
import com.web.pojo.VO.user.UserVO;
import com.web.services.captcha.CaptchaService;
import com.web.services.sms.SMSService;
import com.web.services.user.UserService;
import com.web.util.security.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 * @author ybw
 * @date 2022/5/9
 */

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService, CaptchaService captchaService) {
		this.userService = userService;
		this.captchaService = captchaService;
	}

	private final CaptchaService captchaService;

	/**
	 * 根据用户id查询用户信息
	 *
	 * @param id 用户id
	 * @return {@link CommonResponse< UserDAO >}
	 */

	@GetMapping("/getUserById")
	@ApiOperation(value = "根据用户id查询用户信息", notes = "根据用户id查询用户信息")
	public CommonResponse<UserDAO> getUser(@RequestParam(value = "id", required = true) int id) {
		UserDAO userDAO = userService.selectByPrimaryKey(id);
		return CommonResponse.create(userDAO, null);
	}

	/**
	 * 注册用户
	 *
	 * @param userRegisterDTO 用户注册信息
	 * @return {@link CommonResponse< UserDAO >}
	 */

	@PostMapping("/register")
	@ApiOperation(value = "注册用户", notes = "注册用户")
	public CommonResponse<UserAvatarVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
		userService.register(userRegisterDTO);
		return CommonResponse.create(null, "注册成功");
	}

	@PostMapping("/modifyAvatar")
	@ApiOperation(value = "修改头像", notes = "修改头像")
	public CommonResponse<UserTokenVO> modifyAvatar(@RequestPart() MultipartFile userAvatar, HttpServletRequest request) {
		UserVO userVO = TokenUtil.getUserInfoFromHttpServletRequest(request, UserVO.class);
		UserTokenVO userTokenVO = userService.modifyAvatar(userAvatar, userVO);
		return CommonResponse.create(userTokenVO, "修改成功");
	}

	@PostMapping("/login")
	@ApiOperation(value = "登录用户", notes = "登录用户")
	public CommonResponse<UserTokenVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
		CaptchaRequestEntity captchaRequestEntity = new CaptchaRequestEntity();
		captchaRequestEntity.setIp(request.getRemoteAddr());
		captchaRequestEntity.setToken(userLoginDTO.getToken());
		JSONObject response = captchaService.getCaptchaValidationResult(userLoginDTO.getServer(), captchaRequestEntity);
		CaptchaResultEntity captchaResultEntity = JSONObject.parseObject(response.toJSONString(), CaptchaResultEntity.class);
		System.out.println(captchaResultEntity);
		if (captchaResultEntity.getSuccess() != 1 || captchaResultEntity.getScore() < 60) {
			return CommonResponse.create(null, "验证失败");
		}
		UserTokenVO userTokenVO = userService.login(userLoginDTO);
		return CommonResponse.create(userTokenVO, "登录成功");
	}

	@PostMapping("/modifyPassword")
	@ApiOperation(value = "修改密码", notes = "修改密码")
	public CommonResponse<String> modifyPassword(@RequestBody UserModifyPasswordDTO userModifyPasswordDTO, HttpServletRequest request) {
		UserVO userVO = TokenUtil.getUserInfoFromHttpServletRequest(request, UserVO.class);
		userService.modifyPassword(userModifyPasswordDTO, userVO);
		return CommonResponse.create(null, "修改成功");
	}

	@PostMapping("/deleteUser")
	@ApiOperation(value = "删除用户", notes = "删除用户")
	public CommonResponse<String> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO, HttpServletRequest request) {
		UserVO userVO = TokenUtil.getUserInfoFromHttpServletRequest(request, UserVO.class);
		userService.deleteUser(userDeleteDTO, userVO);
		return CommonResponse.create(null, "删除成功");
	}

	@PostMapping("/getAllUserInfo")
	@ApiOperation(value = "获取所有用户信息", notes = "获取所有用户信息")
	public CommonResponse<PageResult<UserVO>> getAllUserInfo(@RequestBody PageDTO pageDTO, HttpServletRequest request) {
		UserVO userVO = TokenUtil.getUserInfoFromHttpServletRequest(request, UserVO.class);
		PageResult<UserVO> pageInfo = userService.getUserList(pageDTO, userVO);
		return CommonResponse.create(pageInfo, "获取成功");
	}

	@PostMapping("/sendMessage")
	@ApiOperation(value="发送短信",notes = "发送短信")
	public CommonResponse<String> sendMessage(@RequestBody SendMessageDTO sendMessageDTO,HttpServletRequest request){
		UserVO userVO = TokenUtil.getUserInfoFromHttpServletRequest(request, UserVO.class);
		userService.userBindingTel(sendMessageDTO,userVO);
		return CommonResponse.create(null, "获取成功");
	}

}
