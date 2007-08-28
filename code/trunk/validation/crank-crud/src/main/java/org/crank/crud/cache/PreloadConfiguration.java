package org.crank.crud.cache;

import java.util.List;

/**
 * Configuration info holder.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public class PreloadConfiguration {
    private String cacheName;
    private String preloadingHQL = "";
    private int preloadingRecordCount = 0;
    private List<String> childrenToInitialize;

    public List<String> getChildrenToInitialize() {
        return childrenToInitialize;
    }

    public void setChildrenToInitialize( List<String> childrenToInitialize ) {
        this.childrenToInitialize = childrenToInitialize;
    }

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

}
