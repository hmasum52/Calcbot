package com.hmjk.calcbot.controller;

import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.FacebookHookRequest;
import com.hmjk.calcbot.model.FacebookMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("webhook")
public class WebhookController {
    public static final String TAG = "WebhookController->";
    private static final Logger logger = LoggerFactory.getLogger(CalcbotApplication.class);
    private final String PAGE_TOKEN ="EAADdg6Xs9yUBAPV5s5AFWwW5PlAtOKOPhibqnksz9gWaKGf2OJ3guHLY3CzL7vW7aKPJnEEjI0cV2t2wT8v5B0FNnnMjZA6N2ZAEEa3FGeqwAMKxc6XqlWt7sET7uLG4hlhP9pzKVEbLBJJmDnPamLtdkkiT2Qve7a4Rz7S8M0tzpR3SOL";
    private final String VERIFY_TOKEN="calcbot_webhook_verity_token";

    private final String FB_MSG_URL="https://graph.facebook.com/v2.6/me/messages?access_token="
            + PAGE_TOKEN;
    private final RestTemplate template = new RestTemplate();

    //This is necessary for register a webhook in facebook
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String get(@RequestParam("hub.mode") String mode,
            @RequestParam(name = "hub.verify_token")String token,
                      @RequestParam(name = "hub.challenge")String challenge){
        if(mode.equals("subscribe")&&token!=null && !token.isEmpty() && token.equals(VERIFY_TOKEN)){
            logger.info(TAG+"get():WEBHOOK_VERIFIED");
            return challenge;
        }else{
            return "Wrong Token";
        }
    }

    //This method  reply all messages with: 'This is a test message'
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody FacebookHookRequest request){
        logger.info("Message from chat: {}",request);
        request.getEntry().forEach(e->{
            e.getMessaging().forEach(m->{
                String id = m.getSender().get("id");
                sendReply(id,"This is a test message");
            });
        });
    }

    private void sendReply(String id,String text){
        FacebookMessageResponse response = new FacebookMessageResponse();
        response.setMessage_type("text");
        response.getRecipient().put("id",id);
        response.getMessage().put("text",text);
        HttpEntity<FacebookMessageResponse> entity = new HttpEntity<>(response);
        String result = template.postForEntity(FB_MSG_URL,entity,String.class).getBody();
        logger.info("Message result: {}",result);
    }

}




