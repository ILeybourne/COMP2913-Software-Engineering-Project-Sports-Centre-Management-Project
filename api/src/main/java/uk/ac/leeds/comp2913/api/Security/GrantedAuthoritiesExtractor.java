package uk.ac.leeds.comp2913.api.Security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<?> permissions = (Collection<?>) jwt.getClaims()
                .getOrDefault("permissions", Collections.emptyList());

        final List<GrantedAuthority> coll = permissions.stream()
                .map((string) -> "SCOPE_" + string)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return coll;
    }

}
