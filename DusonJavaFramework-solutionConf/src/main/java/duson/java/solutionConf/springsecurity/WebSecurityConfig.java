package duson.java.solutionConf.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import duson.java.solutionConf.springsecurity.SecurityUserService;
import duson.java.solutionConf.springsecurity.authentication.CustomAuthenticationFilter;
import duson.java.solutionConf.springsecurity.authentication.CustomAuthenticationProvider;
import duson.java.solutionConf.springsecurity.authentication.JwtAuthenticationTokenFilter;
import duson.java.solutionConf.springsecurity.authorize.SecurityAuthorizeFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityUserService userService;
	@Autowired
	private SecurityAuthorizeFilter authorizeFilter;
	@Autowired
	private CustomAuthenticationFilter authenticationFilter;

	@Override  
    public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
    }
	
	@Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(authorizeFilter, FilterSecurityInterceptor.class)
		.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
        //.csrf().disable() // 跨站请求
		.sessionManagement()
			//.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 基于token，所以不需要session
			.and()
        .authorizeRequests()
            .antMatchers("/", "/error", "/test/**").permitAll() // 允许匿名访问
            // 除上面外的所有请求全部需要鉴权认证
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
        .logout()
        	.logoutSuccessUrl("/")
            .permitAll()
		;
		
		// 禁用缓存
		http.headers().cacheControl();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider()); // 方式1：设置身份认证提供者
		//auth.userDetailsService(userService)/*.passwordEncoder(new BCryptPasswordEncoder())*/; // 方式2：设置userDetailsService
		
		// auth.inMemoryAuthentication().withUser("abc").password("123456").roles("USER"); // 预置用户
	}
	
	@Override  
    public void configure(WebSecurity web) throws Exception {  
        web.ignoring().antMatchers("/static/**");  
    }
	
	@Bean
    public CustomAuthenticationProvider authenticationProvider() {
		CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        //authenticationProvider.setUserDetailsService(userService); // 自定义的用户和角色数据提供者
        //authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder()); // 设置密码加密对象
        return authenticationProvider;
    }
}
