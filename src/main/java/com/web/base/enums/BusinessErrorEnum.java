package com.web.base.enums;


import com.web.base.common.CommonError;

/**
 * @author YBW&NXY666
 */
public enum BusinessErrorEnum implements CommonError {
	/**
	 * 一般错误 （1xx）
	 */
	UNKNOWN_ERROR(10001, "未知错误"),
	FAILED_TO_PARSE_JSON(10002, "反序列化失败"),
	ENCODE_BASE64_FAILED(10003, "转码Base64失败"),
	DECODE_BASE64_PICTURE_FAILED(10004, "解码Base64图片失败"),
	ABNORMAL_DATA(10005, "数据异常"),
	FAILED_TO_CREATE_EXCEL_OBJECT(10006, "创建Excel映射对象失败"),
	FAILED_TO_PARSE_EXCEL_VALUE(10007, "解析Excel值失败"),
	REQUEST_IS_HANDLING(10008, "已有相似的请求正在处理，请稍后再试"),
	MISSING_REQUIRED_PARAMETERS(10009, "缺少必要的参数"),
	INSUFFICIENT_NUMBER_OF_PARAMETERS(10010, "参数数量不足"),


	/**
	 * 用户相关 （2xx）
	 */
	USER_ALREADY_EXISTS(20001, "该帐号已被注册"),
	USER_NOT_EXISTS(20002, "用户不存在"),
	LOGIN_FAILED(20003, "帐号或密码错误，请重试"),
	LOGIN_OUT_DATED(20004, "登录状态已失效，请重新登录"),
	NOT_LOGGED_IN(20005, "未登录，请先登录"),
	PASSWORD_IS_SAME(20006, "密码未发生改变"),
	CHECK_OLD_PASSWORD_FAILED(20007, "旧密码不正确"),
	FAILED_TO_MODIFY_PASSWORD(20008, "修改密码失败"),
	COMPANY_ABBR_IS_MISSING(20009, "系统管理员注册帐号必须包含公司简称"),
	REGISTER_FAILED(20010, "注册失败"),
	CANNOT_DELETE_OWN_ACCOUNT(20011, "不能删除自己的帐号"),
	TOKEN_GENERATE_FAILED(20012, "生成Token失败"),
	TOKEN_VALIDATION_FAILED(20013, "Token验证失败"),
	ROLE_NAME_CANNOT_BE_FIND(20014, "无法获取角色组名"),

	/**
	 * 验证相关 （3xx）
	 */
	FAILED_TO_CREATE_VERIFICATION_CODE(30001, "生成验证码失败"),
	CAPTCHA_CODE_ERROR(30002, "验证码错误"),

	/**
	 * 权限相关 （5xx）
	 */
	CREATE_PERMISSION_FAILED(50001, "创建权限失败"),
	PERMISSION_ALREADY_EXISTS(50002, "权限已存在"),
	ROLE_ALREADY_EXISTS(50003, "角色已存在"),
	PERMISSION_DENIED(50004, "权限不足"),
	ROLE_NOT_EXISTS(50005, "角色不存在"),
	FAILED_TO_DELETED_PERMISSION(50006, "权限删除失败"),
	UNABLE_TO_MODIFY_OWN_ROLE(50007, "无法编辑自己的角色"),
	ROLE_IS_BOUND(50008, "还有用户绑定该角色，无法删除"),
	PROHIBITED_TO_MODIFY(50009, "该角色禁止编辑"),
	PROHIBITED_TO_DELETE(50010, "该权限禁止删除"),

	/**
	 * 公司相关 （6xx）
	 */
	INSTITUTIONAL_CODING_FORMAT_ERROR(60001, "公司编码格式错误"),
	COMPANY_NOT_EXISTS(60002, "公司不存在"),
	THE_USER_HAS_NOT_JOINED_THE_COMPANY(60003, "该用户未加入该公司"),
	COMPANY_ALREADY_EXISTS(60004, "公司缩写已存在"),
	FAILED_TO_DELETE_COMPANY(60005, "删除公司失败"),

	/**
	 * 文件相关 （7xx）
	 */
	THE_FILE_IS_DAMAGED(70001, "文件损坏"),
	UNRECOGNIZED_FILE_TYPE(70002, "文件类型无法识别"),

	/**
	 * 敏感操作相关 （8xx）
	 */
	OPERATION_REQUEST_NOT_EXISTS(80001, "目标操作请求不存在"),
	VALID_OPERATION_REQUEST_NOT_EXISTS(80002, "无有效操作请求，请先申请");


	private final int errCode;

	private String errMsg;

	BusinessErrorEnum(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	@Override
	public int getErrCode() {
		return this.errCode;
	}

	@Override
	public String getErrMsg() {
		return this.errMsg;
	}

	@Override
	public void setErrMsg(String msg) {
		this.errMsg = msg;
	}
}
