package com.hmjk.calcbot.util;

import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.fb.FbAttachment;
import com.hmjk.calcbot.model.fb.FbMessageResponse;
import com.hmjk.calcbot.model.fb.response.FbButton;
import com.hmjk.calcbot.model.fb.response.QuickReply;
import com.hmjk.calcbot.model.fb.response.SenderAction;
import com.hmjk.calcbot.model.fb.response.template.GenericTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class FbMessengerBot {
    public static final String TAG = "FbMessengerBot->";
    private static final Logger logger = LoggerFactory.getLogger(CalcbotApplication.class);

    private String PAGE_TOKEN;
    private String FB_MSG_URL = "https://graph.facebook.com/v10.0/me/messages?access_token="; //append PAGE_TOKEN


    private final RestTemplate template = new RestTemplate();

    public FbMessengerBot(String PAGE_TOKEN){
        this.PAGE_TOKEN = PAGE_TOKEN;
    }


    public void sendReply(String recipient, String text) {
        FbMessageResponse response = new FbMessageResponse();
        response.setMessaging_type(FbMessageResponse.MESSAGE_TYPE_RESPONSE);
        response.getRecipient().put("id", recipient);
        response.getMessage().put("text", text);
        logger.info("");
        logger.info("Target url: " + FB_MSG_URL + PAGE_TOKEN);
        logger.info("Sending response: " + response);
        HttpEntity<FbMessageResponse> entity = new HttpEntity<>(response);
        String result = template.postForEntity(FB_MSG_URL + PAGE_TOKEN, entity, String.class).getBody();
        logger.info("Message result: {}", result);
    }

    public void sendSenderAction(String recipientId,String action){
        logger.info("");
        logger.info(TAG+"sendSenderAction(): sending action : "+action);
        SenderAction senderAction = new SenderAction(recipientId,action);
        sendHttpPost(senderAction.toString());
    }

    public void sendQuickReply(String recipientID){
        logger.info(TAG+"sendQuickReply()");
        String image_url = "https://imgd.aeplcdn.com/476x268/n/cw/ec/38904/mt-15-front-view.jpeg?q=80";

        List<QuickReply> quickReplies = new ArrayList<>();

        QuickReply quickReply = new QuickReply();
        quickReply.setContent_type(QuickReply.TEXT);
        quickReply.setPayload("life_village");
        quickReply.setTitle("Village");
        quickReply.setImage_url(image_url);


        quickReplies.add(quickReply);

        for (int i = 0; i < 6; i++) {
            quickReply = new QuickReply();
            quickReply.setContent_type(QuickReply.TEXT);
            quickReply.setPayload("life_city"+i);
            quickReply.setTitle("City"+i);
            quickReply.setImage_url(image_url);
            quickReplies.add(quickReply);
        }

        logger.info("");
        logger.info(TAG+"quickReply(): "+quickReply.toString());

        FbMessageResponse response = new FbMessageResponse();
        response.setMessaging_type(FbMessageResponse.MESSAGE_TYPE_RESPONSE);
        response.getRecipient().put("id", recipientID);
        response.getMessage().put("text", "What do you like?");
        response.getMessage().put("quick_replies", quickReplies);
        logger.info("Target url: " + FB_MSG_URL + PAGE_TOKEN);
        logger.info("Sending response: " + response.toString());
        sendHttpPost(response.toString());
    }

    public void sendWelcomeTemplate(String recipientId){
        logger.info(TAG+"sendTemplate(): sending template to user "+recipientId);
        FbMessageResponse response = new FbMessageResponse();
        //response.setMessaging_type(FbMessageResponse.MESSAGE_TYPE_RESPONSE);
        response.getRecipient().put("id", recipientId);

        GenericTemplate.Element element = new GenericTemplate.Element();
        element.setTitle("Welcome to calcbot");
        element.setSubtitle("Confused how to use me?. I know following operations. Click one to know more.");
        element.addButton(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Arithmetic Operation",
                "ARITHMETIC"
        ));
        element.addButton(new FbButton(
                FbButton.TYPE_POSTBACK,
                "GCD & LCM",
                "GCD_LCM"
        ));
        element.addButton(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Logarithm",
                "LOGARITHM"
        ));
        element.addButton(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Trigonometry",
                "TRIGONOMETRY"
        ));
        element.addButton(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Prime number check",
                "IS_PRIME"
        ));

        GenericTemplate templatePayload = new GenericTemplate();
        templatePayload.setTemplate_type("generic");
        templatePayload.addElement(element);

        FbAttachment attachment = new FbAttachment();
        //attachment.setType(FbAttachment.IMAGE);
        attachment.setType(FbAttachment.TEMPLATE);
        attachment.setPayload(templatePayload);

        response.getMessage().put("attachment",attachment);

        sendHttpPost(response.toString());
    }

    public void sendGenericTemplate(String recipientId, List<GenericTemplate.Element> elements){
        logger.info(TAG+"sendTemplate(): sending template to user "+recipientId);
        FbMessageResponse response = new FbMessageResponse();
        //response.setMessaging_type(FbMessageResponse.MESSAGE_TYPE_RESPONSE);
        response.getRecipient().put("id", recipientId);

        GenericTemplate templatePayload = new GenericTemplate();
        templatePayload.setTemplate_type("generic");
        templatePayload.setElements(elements);

        FbAttachment attachment = new FbAttachment();
        attachment.setType(FbAttachment.TEMPLATE);
        attachment.setPayload(templatePayload);

        response.getMessage().put("attachment",attachment);

        sendHttpPost(response.toString());
    }

    private void sendHttpPost(String s) {
        logger.info(TAG+"sendHttpPost(): posting: "+s);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(s,headers);
       //logger.info(TAG+"sendHttpPost(): url:"+FB_MSG_URL+PAGE_TOKEN);
        String result = template.postForEntity(FB_MSG_URL + PAGE_TOKEN, entity, String.class).getBody();
        logger.info("Message result: {}", result);
    }
}
