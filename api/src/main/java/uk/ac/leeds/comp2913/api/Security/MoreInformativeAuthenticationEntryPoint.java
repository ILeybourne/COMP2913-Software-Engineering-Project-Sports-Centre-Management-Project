package uk.ac.leeds.comp2913.api.Security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MoreInformativeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        this.delegate.commence(request, response, authException);

        if (authException.getCause() instanceof JwtValidationException) {
            JwtValidationException exception = (JwtValidationException) authException.getCause();
            Collection<OAuth2Error> errors = exception.getErrors();
            this.mapper.writeValue(response.getWriter(), errors);
        }
    }
}
