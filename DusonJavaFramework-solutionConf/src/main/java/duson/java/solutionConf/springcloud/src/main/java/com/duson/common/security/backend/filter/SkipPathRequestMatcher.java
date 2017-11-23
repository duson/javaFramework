/**
 * com.facewnd.ad.common.security.backend.filter
 * SkipPathRequestMatcher.java
 * 
 * 2017年10月16日-下午3:00:35
 * 2017-版权所有
 * 
 */
package com.facewnd.ad.common.security.backend.filter;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 * SkipPathRequestMatcher
 * 
 * 创建人：pengcaiyun
 * 最后修改人：pengcaiyun
 * 2017年10月16日 下午3:00:35
 * 
 * @version 1.0.0
 * 
 */
public class SkipPathRequestMatcher implements RequestMatcher {

	private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request) ? true : false;
    }

}
