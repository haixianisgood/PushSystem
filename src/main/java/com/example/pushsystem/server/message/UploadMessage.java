package com.example.pushsystem.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadMessage {
    private String token;
    private String pushId;
    private String confirmId;
}
