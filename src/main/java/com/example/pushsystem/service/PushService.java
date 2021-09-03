package com.example.pushsystem.service;

public interface PushService {
    void push(String pushId, byte[] message);
    void pushGroup(String groupId, byte[] message);
}
