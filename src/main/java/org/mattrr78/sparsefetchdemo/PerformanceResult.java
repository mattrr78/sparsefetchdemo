package org.mattrr78.sparsefetchdemo;

public class PerformanceResult {
    private long testTime;

    private long memoryBefore;

    private long memoryAfter;

    public long getTestTime() {
        return testTime;
    }

    public void setTestTime(long testTime) {
        this.testTime = testTime;
    }

    public long getMemoryBefore() {
        return memoryBefore;
    }

    public void setMemoryBefore(long memoryBefore) {
        this.memoryBefore = memoryBefore;
    }

    public long getMemoryAfter() {
        return memoryAfter;
    }

    public void setMemoryAfter(long memoryAfter) {
        this.memoryAfter = memoryAfter;
    }

    public long getMemoryUsed()  {
        return memoryAfter - memoryBefore;
    }
}
