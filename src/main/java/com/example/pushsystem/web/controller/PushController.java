package com.example.pushsystem.web.controller;

import com.example.pushsystem.db.repository.entity.PushMessage;
import com.example.pushsystem.service.PushService;
import com.example.pushsystem.web.api.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push")
public class PushController {
    @Autowired
    private PushService pushService;

    @PostMapping
    public Response<String> push(PushMessage pushMessage) {
        pushService.push(pushMessage.getPushId(), pushMessage.getMessage());

        return Response.buildSuccess();
    }
}
