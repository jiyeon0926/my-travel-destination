package jiyeon.travel.domain.auth.service;

import io.jsonwebtoken.Claims;
import jiyeon.travel.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final RedissonClient redissonClient;
    private final JwtProvider jwtProvider;

    public void saveAccessToken(String accessToken) {
        long now = new Date().getTime();
        Claims claims = jwtProvider.getClaims(accessToken);
        Date expiration = claims.getExpiration();
        long remainExpiration = expiration.getTime() - now;

        if (remainExpiration > 0) {
            RBucket<String> bucket = redissonClient.getBucket("blacklist:" + accessToken);
            bucket.set("blacklisted", remainExpiration, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isValidBlacklist(String accessToken) {
        return redissonClient.getBucket("blacklist:" + accessToken).isExists();
    }
}
