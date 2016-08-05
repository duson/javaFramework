package duson.java.solutionConf.oauth2;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Service {
	public Object authorize(HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			
			String appKey = oauthRequest.getClientId();
			// 根据appKey判断是否存在，不存在，返回异常；存在，则找到关联的商户
			boolean valid = true;
			if (!valid) {
	            OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
	                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
	                            .setErrorDescription("非法访问")
	                            .buildJSONMessage();
	            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
	        }
			
			String authorizationCode = null;
			
			//responseType目前仅支持CODE，另外还有TOKEN  
		    String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);  
		    if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				// 得到用户信息
				// 记录商户与授权码的关联关系，放缓存cache.put(authCode, username);
		    }
		    
			//进行OAuth响应构建
	        OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
	        //设置授权码
	        builder.setCode(authorizationCode);
	        //得到到客户端重定向地址
	        String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
	
	        //构建响应
	        final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
	
	        //根据OAuthResponse返回ResponseEntity响应
	        HttpHeaders headers = new HttpHeaders();
	        headers.setLocation(new URI(response.getLocationUri()));
	        return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {

            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity("OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }

            //返回错误消息（如?error=）
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
	}
	
	public HttpEntity accessToken(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            //检查提交的客户端id是否正确
            //oauthRequest.getClientId()
            boolean valid = true;
            if (!valid) {
                OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription("非法访问")
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }

            // 检查客户端安全KEY是否正确
            //oauthRequest.getClientSecret()
            if (!valid) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                                .setErrorDescription("非法访问")
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }

            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (!valid) { // 判断授权码是否正确
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("错误的授权码")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                }
            }

            //生成Access Token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            // getUsernameByAuthCode(authCode)
            // 记录商户与授权码的关联关系，放缓存 cache.put(accessToken, username);

            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(3600L)) // 过期时间戳
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            //构建错误响应
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
        }
    }
	
	public HttpEntity validAccessToken(HttpServletRequest request) throws OAuthSystemException {
		String serviceName = "";
		
		try {
			// 构建OAuth资源请求
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
			// 获取Access Token
			String accessToken = oauthRequest.getAccessToken();
	
			// 验证Access Token
			// 如果不存在/过期了，返回未验证错误，需重新验证
			boolean valid = false;
			if (!valid) {
				OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setRealm(serviceName).setError(OAuthError.ResourceResponse.INVALID_TOKEN)
						.buildHeaderMessage();
	
				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
			}
			// 根据AccessToken获取关联用户的信息或操作
			
			return new ResponseEntity(valid, HttpStatus.OK);
		}catch(OAuthProblemException e) {  
	      //检查是否设置了错误码  
	      String errorCode = e.getError();  
	      if (OAuthUtils.isEmpty(errorCode)) {  
	        OAuthResponse oauthResponse = OAuthRSResponse  
	               .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)  
	               .setRealm(serviceName)  
	               .buildHeaderMessage();  
	  
	        HttpHeaders headers = new HttpHeaders();  
	        headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,   
	          oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));  
	        return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);  
	      } 
	  
	      OAuthResponse oauthResponse = OAuthRSResponse  
	               .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)  
	               .setRealm(serviceName)  
	               .setError(e.getError())  
	               .setErrorDescription(e.getDescription())  
	               .setErrorUri(e.getUri())  
	               .buildHeaderMessage();  
	  
	      HttpHeaders headers = new HttpHeaders();  
	      headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
	        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));  
	      return new ResponseEntity(HttpStatus.BAD_REQUEST);  
	    }
	}
	
	private String getAccessToken(String code, String appKey, String securtKey, String accessTokenUrl, String redirectUrl) throws OAuthSystemException, OAuthProblemException {  
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());  
        OAuthClientRequest accessTokenRequest = OAuthClientRequest  
                .tokenLocation(accessTokenUrl)  
                .setGrantType(GrantType.AUTHORIZATION_CODE)  
                .setClientId(appKey).setClientSecret(securtKey)  
                .setCode(code).setRedirectURI(redirectUrl)  
                .buildQueryMessage();
        
        //获取access token  
        OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);  
        String accessToken = oAuthResponse.getAccessToken();  
        Long expiresIn = oAuthResponse.getExpiresIn();  
        
        return accessToken;
    }
}
