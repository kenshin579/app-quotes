package kr.pe.advenoh.cache.controller;

import kr.pe.advenoh.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/number/square/{number}")
    public Object getNumber(@PathVariable(name = "number") Long number) {
        return String.format("{\"square\": %s}", cacheService.square(number));
    }

    @DeleteMapping("/all")
    public void clearCache() {
        cacheService.clearCache();
    }

    @GetMapping("/all")
    public Object getAllCacheKeys() {
        Map<Object, Object> result = new HashMap<>();

        String[] cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            log.info("cacheName : {}", cacheName);
            Cache cache = cacheManager.getCache(cacheName);
            for (Object key : cache.getKeys()) {
                Element element = cache.get(key);
                if (element != null) {
                    log.info("key : {} element : {} value : {}", key, element, element.getObjectValue());
                    result.put(key, element);
                }
            }
        }

        return result;
    }
}
