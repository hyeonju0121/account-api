package com.example.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedissonClient redissonClient;

    public String getLock() {
        RLock lock = redissonClient.getLock("sampleLock"); // redissonClient 에 "sampleLock" 라는 이름의 스핀락을 가져온다.

        try {
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS); // 가져온 락으로 스핀락 시도
            if(!isLock) {  // 1초 동안 기다리면서 락을 시도해보고, 그 시간동안 락 획득에 실패하는 경우
                log.error("============= Lock acquisition failed =========");
                return "Lock failed";
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
        }

        return "Lock success"; // 락 획득에 성공하는 경우, 최대 3초 시간 후에 자동으로
    }
}
