package uk.ac.leeds.comp2913.api.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  String jwkSetUri;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .csrf().disable()
        .cors().and()
        .authorizeRequests(authorizeRequests ->
        authorizeRequests
//          TODO: Public endpoints for timetable
//          .antMatchers(HttpMethod.POST, "/test/permission/**").hasAuthority("SCOPE_message:write")
          .anyRequest().authenticated()
      )
      .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    // @formatter:on
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
  }
}

