package io.jandy.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author JCooky
 * @since 2016-04-05
 */
@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public void init(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/webjars/**", "/**/favicon.ico");
    super.init(web);
  }

  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests()
        .anyRequest().permitAll();

    http.logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .permitAll();
  }

}
