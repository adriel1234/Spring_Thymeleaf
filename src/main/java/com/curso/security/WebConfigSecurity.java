package com.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




@Configuration
@EnableWebSecurity
public class WebConfigSecurity  extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementacaoUserDetailService  implementacaoUserDetailService;


	@Override // configura as solicitações de acesso por Http
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf()
		.disable()// desativa as configurações padrao de memoria.
		.authorizeRequests() // Permiti restringir acessos
		.antMatchers(HttpMethod.GET,"/").permitAll() // Qualquer usuário acessa a pagina
		.antMatchers("/materialize/**").permitAll()
		.antMatchers(HttpMethod.GET,"/cadastropessoa").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll()//permite qualquer usuário
		.loginPage("/login")
		.defaultSuccessUrl("/cadastropessoa")
		.failureUrl("/login?error=true")
		.and()
		.logout().logoutSuccessUrl("/login") // Mapeia URL de Logout e invalida o usuário autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	@Override // Cria autenticação do usuário com banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementacaoUserDetailService)
		.passwordEncoder(new BCryptPasswordEncoder());

	}

	@Override //Ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
	


}
