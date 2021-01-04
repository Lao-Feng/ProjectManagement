package com.yonglilian.common.util;

import java.util.Random;

public class  SerialNum {

    private static SerialNum serialNum;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private final long workerId;
    private final static long sequenceBits = 10L;
    private final static long twepoch = 1288834974657L;
    private final static long workerIdBits = 4L;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    private final static long workerIdShift = sequenceBits;
    private final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceMask = -1L ^ -1L << sequenceBits;

    private SerialNum(final long workerId) {
        super();
        if (workerId > SerialNum.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", SerialNum.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public static synchronized SerialNum getInstance() {
        if (serialNum == null) {
            Random r = new Random();
            serialNum = new SerialNum(r.nextInt(8) + 1);
        }
        return serialNum;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & SerialNum.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            String msg = String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp);
            throw new RuntimeException(msg);
        }

        this.lastTimestamp = timestamp;
        return ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << SerialNum.workerIdShift) | (this.sequence);
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}
