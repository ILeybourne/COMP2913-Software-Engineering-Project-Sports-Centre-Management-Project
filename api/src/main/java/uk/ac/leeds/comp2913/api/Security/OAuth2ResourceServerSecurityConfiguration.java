package uk.ac.leeds.comp2913.api.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
    http
      .cors().and()
      .csrf().disable()
      .authorizeRequests()
//        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .antMatchers("/public/**").permitAll()
        .anyRequest().authenticated()
      .and()
      .oauth2ResourceServer()
            .authenticationEntryPoint(authEntryPoint())
            .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationDecoder());
    // @formatter:on
    }

    private MoreInformativeAuthenticationEntryPoint authEntryPoint() {
        return new MoreInformativeAuthenticationEntryPoint();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationDecoder() {
        return new GrantedAuthoritiesExtractor();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoderJwkSupport decoder = (NimbusJwtDecoderJwkSupport)
                JwtDecoders.fromOidcIssuerLocation(this.issuerUri);

        OAuth2TokenValidator<Jwt> springDefaults = JwtValidators.createDefaultWithIssuer(this.issuerUri);

        decoder.setJwtValidator(springDefaults);
        return decoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

