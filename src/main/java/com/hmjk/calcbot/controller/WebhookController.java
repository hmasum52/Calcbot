package com.hmjk.calcbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class WebhookController {
    @RequestMapping("/webhook")
    @ResponseBody
    public String webhook(){
        return "hello webhook";
    }
}
