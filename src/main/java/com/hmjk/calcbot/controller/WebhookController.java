package com.hmjk.calcbot.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.fb.FbHookRequest;
import com.hmjk.calcbot.model.fb.FbMessage;
import com.hmjk.calcbot.model.fb.FbPostBack;
import com.hmjk.calcbot.model.fb.response.SenderAction;
import com.hmjk.calcbot.model.wit.WitMessageResponse;
import com.hmjk.calcbot.service.LastAnswerService;
import com.hmjk.calcbot.util.FbMessengerBot;
import com.hmjk.calcbot.wit.EntityFilter;
import com.hmjk.calcbot.wit.WitCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hasan Masum
 */
@RestController
@RequestMapping("webhook")
public class WebhookController {
    public static final String TAG = "WebhookController->";
    private static final Logger logger = LoggerFactory.getLogger(CalcbotApplication.class);
    @Value("${FB-PAGE-TOKEN}")
    private String PAGE_TOKEN;
    @Value("${FB-WEBHOOK-VERIFY-TOKEN}")
    private String VERIFY_TOKEN;
    @Value("${WIT-TOKEN}")
    private String WIT_TOKEN;

    private String WIT_MESSAGE_URL = "https://api.wit.ai/message?v=20210510";
    private String WIT_SPEECH_URL = "https://api.wit.ai/speech?v=20210510";


    private FbMessengerBot messengerBot;
    private WitCaller witCaller;
    private EntityFilter entityFilter;


    //This is necessary for register a webhook in facebook
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String get(@RequestParam("hub.mode") String mode,
                      @RequestParam(name = "hub.verify_token") String token,
                      @RequestParam(name = "hub.challenge") String challenge) {
        if (mode.equals("subscribe") && token != null && !token.isEmpty() && token.equals(VERIFY_TOKEN)) {
            logger.info(TAG + "get():WEBHOOK_VERIFIED");
            return challenge;
        } else {
            return "Wrong Token";
        }
    }

    //This method  reply all messages with: 'This is a test message'
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody FbHookRequest request) {
        if (messengerBot == null) {
            messengerBot = new FbMessengerBot(PAGE_TOKEN);
            witCaller = new WitCaller(WIT_TOKEN);
            entityFilter = new EntityFilter();
        }
        //System.out.println(PAGE_TOKEN);
        logger.info(TAG + "post(): Message from chat webhook: {}", request);

        request.getEntry().forEach(entry -> {
            entry.getMessaging().forEach(messaging -> {
                if (messaging != null) {
                    String senderID = messaging.getSender().get("id");
                    messengerBot.sendSenderAction(senderID, SenderAction.TYPING_ON);

                    //send response to message
                    FbMessage message = messaging.getMessage();
                    if (message != null) {
                        responseToMessage(senderID, message);
                    }

                    //send response to post back
                    FbPostBack postBack = messaging.getPostback();
                    if (postBack != null) {
                        responseToPostBack(senderID, postBack);
                    }

                    messengerBot.sendSenderAction(senderID, SenderAction.TYPING_OFF);
                } else {
                    logger.error(TAG + "post(): messaging is null");
                }
            });
        });
    }

    private void responseToMessage(String senderID, FbMessage message) {
        if (message.getText() != null) { //user sent a text message
            WitMessageResponse witMessageResponse = witCaller.sendTextMessageToWit(senderID, message.getText());

            if(witMessageResponse.getTraits().has("wit$greetings") ){
                messengerBot.sendWelcomeTemplate(senderID);
            }else{
                //calculateFromWitResponse(witMessageResponse, senderID);
                String response = entityFilter.filter(witMessageResponse).calculateAndBuildResponse();
                messengerBot.sendReply(senderID, response);
            }

        }
        //get the audio message and send response
        if (message.getAttachments() != null) {
            message.getAttachments().forEach(fbAttachment -> {
                if (fbAttachment.getType().equals("audio")) {
                    Gson gson = new Gson();
                    JsonObject object = new Gson().fromJson(gson.toJson(fbAttachment.getPayload()), JsonObject.class);
                    String url = object.get("url").getAsString();
                    if (url != null) {
                        logger.info("audio url: " + url);
                        WitMessageResponse witMessageResponse = witCaller.sendVoiceMessageToWit(url);
                        if(witMessageResponse.getTraits().has("wit$greetings") ){
                            messengerBot.sendWelcomeTemplate(senderID);
                        }else{
                            String response = entityFilter.filter(witMessageResponse).calculateAndBuildResponse();
                            response = "You sent: " + witMessageResponse.getText() + "\n\n" + response;
                            messengerBot.sendReply(senderID, response);
                        }
                    }
                }
            });
        }
    }

    private void responseToPostBack(String senderID, FbPostBack postBack) {
    }

   /* @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody String request) {
        //System.out.println(PAGE_TOKEN);
        logger.info("Message from chat: {}", request);
        Map<String, String> maps = new HashMap<>();
    }*/


}




