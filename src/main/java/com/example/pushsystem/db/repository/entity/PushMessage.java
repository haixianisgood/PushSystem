package com.example.pushsystem.db.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushMessage {
    private String pushId;
    private byte[] message;
}
