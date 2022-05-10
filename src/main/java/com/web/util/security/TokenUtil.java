package com.web.util.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author NXY666
 */

public class TokenUtil {
	/**
	 * 私钥
	 */
	private static final String SECRET = "noteBook";

	/**
	 * 过期时间 24h
	 */
	private static final long TTL = (72 * 60 * 60L);

	/**
	 * 请求头参数名
	 */
	private static final String TOKEN_HEADER = "Authorization";

	/**
	 * 传入一个 String 的 subject ，返回他的加密token
	 *
	 * @param subject 一般是 userName
	 * @return token
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	public static String createJwt(Object object) throws JsonProcessingException {
		String subject = OBJECT_MAPPER.writeValueAsString(object);
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + TTL * 1000);
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(subject)
				.setIssuedAt(now)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, SECRET);
		return builder.compact();
	}


	/**
	 * 解析 token
	 *
	 * @param token token
	 * @return token信息
	 */
	public static Claims parseToken(String token) {
		try {
			return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessErrorEnum.LOGIN_OUT_DATED);
		}
	}

	/**
	 * 判断是否过期
	 *
	 * @param token token
	 * @return 是否过期
	 */
	public static boolean isExpired(String token) {
		try {
			if (token != null) {
				Claims claims = parseToken(token);
				if (claims != null) {
					Date expiration = claims.getExpiration();
					return expiration.before(new Date());
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}


	/**
	 * 判断是否过期
	 *
	 * @param expireDate 过期时间
	 * @return 是否过期
	 */
	public static boolean isExpired(Date expireDate) {
		try {
			return expireDate.before(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 获取 token 参数名
	 *
	 * @return token 参数名
	 */
	public static String getTokenHeader() {
		return TOKEN_HEADER;
	}

	/**
	 * 从 token 中获取用户名
	 *
	 * @param token token
	 * @return 用户名
	 */
	public static <T> T getUserInfoFromToken(String token, Class<T> cls) {
		try {
			Claims claims = parseToken(token);
			if (claims != null) {
				String subject = claims.getSubject();
				if (subject != null) {
					return OBJECT_MAPPER.readValue(subject, cls);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new BusinessException(BusinessErrorEnum.LOGIN_OUT_DATED);
	}

	public static <T> T getUserInfoFromHttpServletRequest(HttpServletRequest request, Class<T> cls)  {
		String token = request.getHeader(getTokenHeader());
		if (token == null) {
			throw new BusinessException(BusinessErrorEnum.NOT_LOGGED_IN);
		}
		if (isExpired(token)) {
			throw new BusinessException(BusinessErrorEnum.LOGIN_OUT_DATED);
		}
		return getUserInfoFromToken(token, cls);
	}
}
