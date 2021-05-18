package com.hmjk.calcbot.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.fb.FbHookRequest;
import com.hmjk.calcbot.model.fb.FbMessage;
import com.hmjk.calcbot.model.fb.FbPostBack;
import com.hmjk.calcbot.model.fb.response.SenderAction;
import com.hmjk.calcbot.model.wit.WitMessageResponse;
import com.hmjk.calcbot.util.FbMessengerBot;
import com.hmjk.calcbot.wit.EntityFilter;
import com.hmjk.calcbot.wit.WitCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

            if (witMessageResponse.getTraits().has("wit$greetings")) {
                messengerBot.sendDocTemplate(senderID,"Welcome to calcbot");
            } else {
                //calculateFromWitResponse(witMessageResponse, senderID);
                String response = entityFilter.filter(witMessageResponse).calculateAndBuildResponse();
                messengerBot.sendReply(senderID, response);
                if(response.contains("Invalid")){
                    messengerBot.sendDocTemplate(senderID,"Opps! I didn't get you.");
                }

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
                        if (witMessageResponse.getTraits().has("wit$greetings")) {
                            messengerBot.sendDocTemplate(senderID,"Welcome to calcbot");
                        } else {
                            String response = entityFilter.filter(witMessageResponse).calculateAndBuildResponse();
                            response = "You sent: " + witMessageResponse.getText() + "\n\n" + response;
                            messengerBot.sendReply(senderID, response);
                            if(response.contains("Invalid")){
                                messengerBot.sendDocTemplate(senderID,"Opps! I didn't get you.");
                            }
                        }
                    }
                }
            });
        }
    }

    public static final String ARITHMETIC = "ARITHMETIC";

    private void responseToPostBack(String senderID, FbPostBack postBack) {
        logger.info(TAG+"responseToPostBack");
        StringBuilder message = new StringBuilder("");
        switch (postBack.getPayload()) {
            case ARITHMETIC:
                message.append("I know +, - , /, % of two number.").append("\n");
                message.append("Send a message like the following examples.").append("\n\n")
                        .append("Text: 100 + 78 or 100/5").append("\n")
                        .append("Voice: one hundred plus seventy eight").append("\n")
                        .append("or fifty two divided by four")
                ;
                break;
            case "GCD_LCM":
                message.append("Send me message with gcd or lcm within the message and" +
                        " numbers like the following examples: ")
                        .append("\n\n");
                message.append("Text: Gcd(56, 78, 16) or lcm 45 56 78").append("\n\n");
                message.append("Voice: What is the gcd of fifty six and seventy eight and sixteen");
                break;
            case "LOG_TRIGONOMETRY":
                message.append("Send me message with base(default e or ln) for logarithm and " +
                        "angle in degree with ratio name for trigonometry" +
                        " within the message like the following example:")
                        .append("\n\n");
                message.append("Text: log(56) or log10(100)").append("\n");
                message.append("or log 10 base 500").append("\n")
                        .append(" or sin 60  or sin(60)").append("\n\n");
                message.append("Voice: log ten base five hundred five").append("\n")
                .append("or sin sixty or tan thirty");
                break;
        }
        messengerBot.sendReply(senderID, message.toString());
    }

   /* @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody String request) {
        //System.out.println(PAGE_TOKEN);
        logger.info("Message from chat: {}", request);
        Map<String, String> maps = new HashMap<>();
    }*/


}




