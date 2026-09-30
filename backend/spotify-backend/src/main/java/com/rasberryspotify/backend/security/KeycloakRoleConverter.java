package com.rasberryspotify.backend.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.defaultAuthoritiesConverter.convert(jwt);


        Map<String, Object> accesses = jwt.getClaim("realm_access");
        if (accesses != null && accesses.containsKey("roles")) {
            var roles = ((Collection<?>) accesses.get("roles")).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());

            authorities = Stream.concat(authorities.stream(), roles.stream())
                    .collect(Collectors.toSet());
        }

        String principalName = jwt.getClaimAsString("preferred_username");
        return new JwtAuthenticationToken(jwt, authorities, principalName != null ? principalName : Objects.requireNonNull(jwt.getSubject()));
    }
}
