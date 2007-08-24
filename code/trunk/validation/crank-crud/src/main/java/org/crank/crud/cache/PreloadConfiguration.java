package org.crank.crud.cache;

/**
 * Configuration info holder.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public class PreloadConfiguration {
    private String cacheName;
    private boolean eternal = false;
    private boolean overflowToDisk = true;
    private boolean diskPersistent = false;
    private int maxElementsInMemory = 10000;
    private int timeToIdleSeconds = 120;
    private int timeToLiveSeconds = 120;
    private int diskExpiryThreadIntervalSeconds = 120;
    //TODO: Make this an enum?
    private String memoryStoreEvictionPolicy = "LRU";

    private String preloadingHQL = "";
    private int preloadingRecordCount = 0;

    public String getPreloadingHQL() {
        return preloadingHQL;
    }

    public void setPreloadingHQL(String preloadingHQL) {
        this.preloadingHQL = preloadingHQL;
    }

    public int getPreloadingRecordCount() {
        return preloadingRecordCount;
    }

    public void setPreloadingRecordCount(int preloadingRecordCount) {
        this.preloadingRecordCount = preloadingRecordCount;
    }

    public String getCacheName() {
        return cacheName;
    }

    void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getDiskExpiryThreadIntervalSeconds() {
        return diskExpiryThreadIntervalSeconds;
    }

    public void setDiskExpiryThreadIntervalSeconds(int diskExpiryThreadIntervalSeconds) {
        this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
    }

    public boolean isDiskPersistent() {
        return diskPersistent;
    }

    public void setDiskPersistent(boolean diskPersistent) {
        this.diskPersistent = diskPersistent;
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public int getMaxElementsInMemory() {
        return maxElementsInMemory;
    }

    public void setMaxElementsInMemory(int maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
    }

    public String getMemoryStoreEvictionPolicy() {
        return memoryStoreEvictionPolicy;
    }

    public void setMemoryStoreEvictionPolicy(String memoryStoreEvictionPolicy) {
        this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
    }

    public boolean isOverflowToDisk() {
        return overflowToDisk;
    }

    public void setOverflowToDisk(boolean overflowToDisk) {
        this.overflowToDisk = overflowToDisk;
    }

    public int getTimeToIdleSeconds() {
        return timeToIdleSeconds;
    }

    public void setTimeToIdleSeconds(int timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

}
