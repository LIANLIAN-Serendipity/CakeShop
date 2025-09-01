package cn.smxy.zhouxuelian9.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class JWTUtil {
    public static final String secretKey = "wqen!@3sd23s%"; // 秘钥

    // 生成令牌
    public static String createToken(Map<String, String> payload) {
        Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.SECOND, 60*60*24*7); // 令牌有效期7天
        calendar.add(Calendar.SECOND, 60*60*24*7); // 令牌有效期1分钟
        JWTCreator.Builder builder = JWT.create()
                .withExpiresAt(calendar.getTime()); // 设置过期时间

        Set<String> keys = payload.keySet();
        for (String key : keys) {
            String value = payload.get(key);
            builder.withClaim(key, value);
        }

        String token = builder.sign(Algorithm.HMAC256(secretKey));
        return token;
    }

    // 校验令牌
    public static DecodedJWT verifyToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }
}