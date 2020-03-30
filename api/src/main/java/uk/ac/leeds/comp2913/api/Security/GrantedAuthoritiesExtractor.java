package uk.ac.leeds.comp2913.api.Security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

    public Collection<GrantedAuthority> convert(Jwt jwt) {
//            Collection<String> scopes = (Collection<String>) jwt.getClaims().get("scope");
//            return scopes.stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());
        Collection<?> authorities = (Collection<?>)
                jwt.getClaims().getOrDefault("new_claim", Collections.emptyList());

        return authorities.stream()
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
