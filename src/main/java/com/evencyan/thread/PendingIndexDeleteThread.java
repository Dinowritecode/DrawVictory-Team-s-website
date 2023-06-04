package com.evencyan.thread;

import org.springframework.data.redis.core.RedisTemplate;

public class PendingIndexDeleteThread extends Thread {
    private final RedisTemplate<String, Object> redisTemplate;
    private final int id, pendingUserExpire;

    public PendingIndexDeleteThread(int id, int pendingUserExpire, RedisTemplate<String, Object> redisTemplate) {
        this.id = id;
        this.redisTemplate = redisTemplate;
        this.pendingUserExpire = pendingUserExpire;
        this.setPriority(Thread.MIN_PRIORITY);
        super.setName("PendingIndexDeleteThread-" + id);
    }

    @Override
    public void run() {
        try {
            sleep(pendingUserExpire * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.opsForSet().remove("pending", id);
    }
}
