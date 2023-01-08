package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "FIRpX30pMqDbiAkmIfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9m10icegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

    public String create(UserEntity userEntity) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));
    /*
    { // header
        "alg": "HS512"
    }
    { // payload
        "sub" : "~~~~"
        "iss" : "demo app"
        "iat" : "1595733657"
        "exp" : "1596597657"
    }
     */
        // JWT Token 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_Key
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) //sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String create(final Authentication authentication) {
        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal();
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .setSubject(userPrincipal.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws 메서드가 Base 64로 디코딩 및 파싱
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명 후, Token의 서명과 비교
        // 위조되지 않았다면 페이로드 리턴, 위조라면 예외를 넘김
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
