package com.example.pushsystem.server;

public interface PushServer {
    void push(String pushId, byte[] message);
    void start(int port);
    void stop();
}
