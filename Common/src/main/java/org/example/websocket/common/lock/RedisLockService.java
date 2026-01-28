package org.example.websocket.common.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 락을 획득하고 작업 실행
     * 
     * @param lockKey 락 키
     * @param waitTimeSeconds 락 획득 대기 시간 (초)
     * @param leaseTimeSeconds 락 유지 시간 (초)
     * @param task 실행할 작업
     * @param <T> 반환 타입
     * @return 작업 결과
     */
    public <T> T executeWithLock(String lockKey, long waitTimeSeconds, long leaseTimeSeconds, Supplier<T> task) {
        String lockValue = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        long waitTimeMillis = waitTimeSeconds * 1000;

        try {
            // 락 획득 시도
            while (!tryLock(lockKey, lockValue, leaseTimeSeconds)) {
                // 대기 시간 초과 확인
                if (System.currentTimeMillis() - startTime > waitTimeMillis) {
                    log.warn("락 획득 타임아웃: {}", lockKey);
                    throw new RuntimeException("락 획득 실패: 다른 사용자가 처리 중입니다");
                }
                
                // 100ms 대기 후 재시도
                Thread.sleep(100);
            }

            log.debug("락 획득 성공: {}", lockKey);

            // 작업 실행
            return task.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            // 락 해제
            unlock(lockKey, lockValue);
        }
    }

    /**
     * 락 획득 시도
     */
    private boolean tryLock(String lockKey, String lockValue, long leaseTimeSeconds) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, leaseTimeSeconds, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 락 해제 (자신이 획득한 락만 해제)
     */
    private void unlock(String lockKey, String lockValue) {
        try {
            String currentValue = redisTemplate.opsForValue().get(lockKey);
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(lockKey);
                log.debug("락 해제 성공: {}", lockKey);
            }
        } catch (Exception e) {
            log.error("락 해제 중 오류 발생: {}", lockKey, e);
        }
    }
}
