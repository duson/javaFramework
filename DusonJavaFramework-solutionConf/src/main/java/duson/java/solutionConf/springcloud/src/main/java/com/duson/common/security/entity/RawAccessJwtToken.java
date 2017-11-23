/**
 * com.facewnd.ad.common.security.entity
 * RawAccessJwtToken.java
 * 
 * 2017年10月16日-下午2:35:19
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import com.facewnd.ad.common.security.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * RawAccessJwtToken
 * 
 * 创建人：pengcaiyun 最后修改人：pengcaiyun 2017年10月16日 下午2:35:19
 * 
 * @version 1.0.0
 * 
 */
public class RawAccessJwtToken implements JwtToken {

	private final static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

	private String token;

	public RawAccessJwtToken(String token) {
		this.token = token;
	}

	/**
	 * Parses and validates JWT Token signature.
	 * 
	 * @throws BadCredentialsException
	 * @throws JwtExpiredTokenException
	 * 
	 */
	public Jws<Claims> parseClaims(String signingKey) {
		try {
			return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
			logger.error("Invalid JWT Token", ex);
			throw new BadCredentialsException("Invalid JWT token: ", ex);
		} catch (ExpiredJwtException expiredEx) {
			logger.info("JWT Token is expired", expiredEx);
			throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
		}
	}

	@Override
	public String getToken() {
		return token;
	}
}
