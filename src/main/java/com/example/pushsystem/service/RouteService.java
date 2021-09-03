package com.example.pushsystem.service;

import java.util.List;
import java.util.Map;

public interface RouteService {
    /**
     * 根据推送目标的id，获取其连接的服务器名
     * @param pushId 推送目标的id
     * @return 服务器名，若为null，则目标不在线
     */
    String pushServerTag(String pushId);

    /**
     * 获取组群的在线成员
     * @param groupId 组群的id
     * @return map<成员的推送id， 服务器名>
     */
    Map<String, String> groupOnlineMembers(String groupId);
    List<String> onlineDevices();
    void deviceOnline(String pushId);
    void deviceOffline(String pushId);
}
