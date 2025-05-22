package ticket_online.ticket_online.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ticket_online.ticket_online.model.AppUser;
import ticket_online.ticket_online.model.User;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import com.auth0.jwt.algorithms.Algorithm;

@Component
@Slf4j
public class JwtUtil {
    private String JWTSECRET = "ZQuRaEtd1m3xC9NuT20g4ihjcMf82ihC3Ut3mDgQeSbv5orIbYqNMMqZnEemqm37CJwmxKNKdmBD4eCeB3M9zUjP9J94cttPkRE0";

    private String appName = "online ticket";

    private long expiryTimeInSeconds = 36000L;

    public String generateToken(AppUser userLogin){
        try{
            return JWT.create()
                    .withSubject(userLogin.getEmail())
                    .withIssuedAt(new Date())
                    .withClaim("role", "ROLE_" + userLogin.getRole().name())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiryTimeInSeconds * 1000))
                    .sign(Algorithm.HMAC256(JWTSECRET));

        } catch (JWTCreationException e){
//            log.error("generate token : {}",e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public DecodedJWT decodedJWT(String token){
        Algorithm algorithm = Algorithm.HMAC256(JWTSECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
//        System.out.println(verifier);
        return  verifier.verify(token);
    }

    public String verifyToken(String token){
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWTSECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject(); // isinya email
        } catch (JWTVerificationException e){
//            log.error("Error Verifying token : {}",e.getMessage());
            throw new RuntimeException(e.getMessage(), e);

        }
    }


    public Map<String, String> getUserInfoByToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(JWTSECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String,String> userInfo = new HashMap<>();
//            userInfo.put("UserId", decodedJWT.getSubject());
//            userInfo.put("role", decodedJWT.getClaim("role").asString());

            return userInfo;
        }catch (JWTVerificationException e) {
//            log.error("Error getUserInfoByToken : {}",e.getMessage());
            throw new RuntimeException(e.getMessage(), e);

        } catch (Exception e) {
//            log.error("Error getUserInfoByToken : {}",e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
