package com.ecommerce_micro_service.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(exchange->exchange
//                                .pathMatchers("/admin/**").permitAll()

                                .pathMatchers("/api/products/**").hasRole("PRODUCT")
                                .pathMatchers("/api/orders/**").hasRole("ORDER")
                                .pathMatchers("/api/users/**").permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2->
                        oauth2.jwt(jwt->jwt
                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor(){
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter=
                new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt->{
//            List<String>roles=jwt.getClaimAsMap("resource_access")
//                    .entrySet().stream()
//                    .filter(entry->entry.getKey().equals("oauth2-pkce"))
//                    .flatMap( entry->((Map<String,List<String>>)entry.getValue())
//                                                            .get("roles").stream())
//                    .toList();
            //{oauth2-pkce={roles=[USER]}, account={roles=[manage-account, manage-account-links, view-profile]}}
            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            List<String> roles = null;
            for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
                if (entry.getKey().equals("oauth2-pkce")) {
                    Map<String, List<String>> inner = (Map<String, List<String>>) entry.getValue();
                    roles = inner.get("roles");
                    System.out.println(roles);
                }
            }
            System.out.println("Extracted Roles: "+roles);
            return Flux.fromIterable(roles)
                    .map(role->new SimpleGrantedAuthority("ROLE_"+role));
        });
        return jwtAuthenticationConverter;
    }
}
