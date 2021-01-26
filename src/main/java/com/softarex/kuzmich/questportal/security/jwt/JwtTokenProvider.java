package com.softarex.kuzmich.questportal.security.jwt;

import com.softarex.kuzmich.questportal.entity.Role;
import com.softarex.kuzmich.questportal.entity.Token;
import com.softarex.kuzmich.questportal.repository.RedisRepository;
import com.softarex.kuzmich.questportal.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Component
@Log
public class JwtTokenProvider {

    @Value("$(jwt.secret)")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long expiredPeriodMills;

    private UserDetailsImpl userDetailsService;
    private RedisRepository redisRepository;

    @Autowired
    public JwtTokenProvider(UserDetailsImpl userDetailsService,
                            RedisRepository redisRepository) {
        this.userDetailsService = userDetailsService;
        this.redisRepository = redisRepository;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, Set<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date nowDate = new Date();
        Date expiredDate = new Date(nowDate.getTime() + expiredPeriodMills);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        Token invalidToken = redisRepository.findByToken(token);
        if (invalidToken.getStatus() == Token.Status.INVALID) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    public String retrieveUsername(HttpServletRequest request) {
        String token = resolveToken(request);

        return getUsername(token);
    }
}