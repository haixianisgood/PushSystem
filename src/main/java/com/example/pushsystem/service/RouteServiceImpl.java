package com.example.pushsystem.service;

import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService{
    @Autowired
    private RedissonClient redis;

    @Value("${serverName}")
    String serverTag;

    @Override
    public String pushServerTag(String pushId) {
        RLock lock = redis.getLock("lock:"+pushId);
        lock.lock();

        RMap<String, String> properties = redis.getMap("map:"+pushId);

        if(properties.isExists()) {
            String serverTag = properties.get("serverTag");
            lock.unlock();
            return serverTag;
        } else {
            lock.unlock();
            return null;
        }
    }

    @Override
    public Map<String, String> groupOnlineMembers(String groupId) {
        RMap<String, String> groupMembers = redis.getMap("group:"+groupId);

        return groupMembers.readAllMap();
    }

    @Override
    public List<String> onlineDevices() {
        Iterable<String> keys =  redis.getKeys().getKeysByPattern("map:*");
        List<String> keyList = new ArrayList<>();
        for(String key:keys) {
            keyList.add(key.substring(4));
        }

        return keyList;
    }

    @Override
    public void deviceOnline(String pushId) {
        RLock lock = redis.getLock("lock:"+pushId);
        lock.lock();

        RMap<String, String> propertyMap = redis.getMap("map:"+pushId);
        propertyMap.put("pushId", pushId);
        propertyMap.put("serverTag", serverTag);

        lock.unlock();
    }

    @Override
    public void deviceOffline(String pushId) {
        RLock lock = redis.getLock("lock:"+pushId);
        lock.lock();

        RMap<String, String> propertyMap = redis.getMap("map:"+pushId);
        propertyMap.delete();

        lock.unlock();
    }

}
