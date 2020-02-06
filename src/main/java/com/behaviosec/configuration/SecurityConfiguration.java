package com.behaviosec.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

public class SecurityConfiguration {
	
} /*
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.sessionManagement().maximumSessions(2);
//		BehavioSecAuthenticationFilter customFilter = new BehavioSecAuthenticationFilter();
//		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

		http.authorizeRequests()
			.antMatchers("/").permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
*/