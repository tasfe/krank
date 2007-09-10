package org.crank.crud.cache;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.MBeanServer;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class EHCacheService implements CacheService {
//
//    private CacheManager cacheManager;
//    private boolean setupMBeans;
//    private boolean mbeansWereSetup;
//    private static Logger logger = Logger.getLogger( EHCacheService.class );
//    
//    public void setSetupMBeans( boolean setupMBeans ) {
//        this.setupMBeans = setupMBeans;
//    }
//    
//    public boolean isSetupMBeans() {
//        return setupMBeans;
//    }
//
//    public void createCache( PreloadConfiguration crankCacheConfiguration ) {
//        validateCacheInstance();
//        if (!hasCache(crankCacheConfiguration.getCacheName())) {
//            //otherwise add new cache.
//            cacheManager.addCache( createEHCacheWithConfiguration( crankCacheConfiguration ) );
//        }
//    }
//    
//    private void validateCacheInstance() {
//        if (cacheManager == null) {
//            if (cacheManager.ALL_CACHE_MANAGERS.size() > 1) {
//                logger.warn("Too many cache managers present");
//                //throw new RuntimeException("Multiple cache managers not currently supported!!");  
//            }
//            cacheManager = (CacheManager) CacheManager.ALL_CACHE_MANAGERS.get( 0 );
//            if (cacheManager == null) {
//                throw new RuntimeException("Cannot create cache - CacheManager instance is null. Must defined second level cache in persistence.xml, and application context (injecting into EHCacheService).");    
//            }
//        }
//        if (setupMBeans && !mbeansWereSetup) {
//            try {
//                Class.forName( "net.sf.ehcache.management.ManagementService" );
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException("EHCache 1.3.0 or greater is required for MBean functionality.", e);
//            }
//            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
//            ManagementService.registerMBeans(cacheManager, mBeanServer, false, false, false, true);
//            mbeansWereSetup = true;
//        }
//    }
//    
//    private MemoryStoreEvictionPolicy getMemoryStoreEvictionPolicy(PreloadConfiguration crankCacheConfiguration) {
//        //TODO: make sure this actually works.
//        return MemoryStoreEvictionPolicy.fromString( crankCacheConfiguration.getMemoryStoreEvictionPolicy() );
//    }
//
//    public void expireCache( String cacheName ) {
//        validateCacheInstance();
//        if (!hasCache( cacheName )) {
//            throw new RuntimeException("Cannot expire cache - cache doesn't exist:" + cacheName);
//        }   
//        cacheManager.removeCache( cacheName );
//    }
//
//    public boolean hasCache( String cacheName ) {
//        validateCacheInstance();
//        return cacheManager.cacheExists( cacheName );
//    }
//
//    public List<String> listCaches() {
//        validateCacheInstance();
//        String[] cacheNames = cacheManager.getCacheNames();
//        return Arrays.asList( cacheNames );
//    }
//
//    public void stop() {
//        if (cacheManager != null) {
////            cacheManager.clearAll();
////            cacheManager.shutdown();
////            cacheManager = null;
//        }
//    }
//
//    public List<String> listCacheManagers() {
//        List<String> managers = new ArrayList<String>();
//        logger.debug("List of cache managers:");
//        for (Object object : CacheManager.ALL_CACHE_MANAGERS) {
//            CacheManager cacheManager = (CacheManager) object;
//            logger.debug( cacheManager.getName() );
//            managers.add( cacheManager.getName() );
//            logger.debug("List of caches in cache manager:");
//            for (String cacheName : cacheManager.getCacheNames()) {
//                logger.debug( cacheName );
//            }
//        }
//        return managers;
//    }

}
