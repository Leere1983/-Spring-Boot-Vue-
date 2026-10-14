package com.spboot.app.utils;

import cn.hutool.core.date.DateTime;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * jwt工具类,将数据进行加密和解密操作
 * @author tanwubo
 */
@Component
public class JwtTokenUtils {

    //    @Value("${jwt.expiration}")
    //    private Long expiration;

    public SecretKey key;

    public JwtTokenUtils() {
        key = (SecretKey) readObj();
        if (key == null) {
            key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            writeObj(key);
        }
    }

    // 序列化
    public static void writeObj(Object p) {
        try {
            File file = new File("JwtTokenUtils.txt");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(p);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 反序列化
    public static Object readObj() {
        Object p = null;
        try {
            File file = new File("JwtTokenUtils.txt");
            String f = file.getAbsolutePath();
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            try {
                p = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(this.key).parseClaimsJws(token).getBody();
    }

    public String generateToken(Object userId, Map<String, Object> claims) {
        return doGenerateToken(claims, String.valueOf(userId));
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = DateTime.now();
        //        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(createdDate)
            //                .setExpiration(expirationDate)
            .signWith(this.key)
            .compact();
    }
    //    private Date calculateExpirationDate(Date createdDate) {
    //        return new Date(createdDate.getTime() + expiration * 1000);
    //    }

}
