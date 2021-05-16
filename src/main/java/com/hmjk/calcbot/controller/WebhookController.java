package com.hmjk.calcbot.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.*;
import com.hmjk.calcbot.model.fb.response.SenderAction;
import com.hmjk.calcbot.model.wit.WitMessageResponse;
import com.hmjk.calcbot.util.FbMessengerBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


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

    private String FB_MSG_URL = "https://graph.facebook.com/v10.0/me/messages?access_token="; //append PAGE_TOKEN
    private String WIT_MESSAGE_URL = "https://api.wit.ai/message?v=20210510";
    private String WIT_SPEECH_URL = "https://api.wit.ai/speech?v=20210510";

    private final RestTemplate template = new RestTemplate();
    private FbMessengerBot messengerBot;

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

        if(messengerBot==null){
            messengerBot = new FbMessengerBot(PAGE_TOKEN);
        }
        //System.out.println(PAGE_TOKEN);
        logger.info(TAG+"post(): Message from chat webhook: {}", request);

        request.getEntry().forEach(entry -> {
            entry.getMessaging().forEach(messaging -> {
                if(messaging!=null){
                    String senderID= messaging.getSender().get("id");
                    messengerBot.sendSenderAction(senderID, SenderAction.TYPING_ON);

                    FbMessage message = messaging.getMessage();
                    if(message!=null){
                        responseToMessage(senderID,message);
                    }
                    FbPostBack postBack = messaging.getPostback();
                    if(postBack!=null){
                        responseToPostBack(senderID,postBack);
                    }

                    messengerBot.sendSenderAction(senderID, SenderAction.TYPING_OFF);
                }else{
                    logger.error(TAG+"post(): messaging is null");
                }
            });
        });
    }

    private void responseToMessage(String senderID, FbMessage message) {
        if (message.getText() != null) { //user sent a text message
            logger.info(senderID + " sent: " + message.getText());
            if(message.getText().equalsIgnoreCase("hello")){
               // messengerBot.sendReply(senderID,"Hello. Welcome. Ask me any arithmetic operations.");
                messengerBot.sendTemplate(senderID);
            }else{
                makeWitRequest(senderID,message.getText());
            }
        }
        if (message.getAttachments() != null) {
            message.getAttachments().forEach(fbAttachment -> {
                if (fbAttachment.getType().equals("audio")) {
                    Gson gson = new Gson();
                    JsonObject object = new Gson().fromJson(gson.toJson(fbAttachment.getPayload()),JsonObject.class);
                    String url = object.get("url").getAsString();
                    if (url != null) {
                        logger.info("audio url: " + url);
                        postAudioToWit(senderID, url);
                    }
                }
            });
        }
    }

    private void responseToPostBack(String senderID, FbPostBack postBack) {
    }


    private void makeWitRequest(String userID, String text) {
        try{
            logger.info("makeWitRequest: making wit request");
            String correctEncodedURL = WIT_MESSAGE_URL + "&q=" + URLEncoder.encode(text,"UTF-8");
            URL url = new URL(correctEncodedURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + WIT_TOKEN);
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                WitMessageResponse witMessageResponse = new Gson().fromJson(response.toString(),WitMessageResponse.class);
                calculateFromWitResponse(witMessageResponse, userID);
                logger.info("Response from wit: " + witMessageResponse);
            } else {
                System.out.println("GET request not worked");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void postAudioToWit(String userID,String url) {
        logger.info("postAudioToWit: making wit request");
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            // MAKE MP4========================================
            File file = new File("input.mp4");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
               // outputStream.write(dataBuffer, 0, bytesRead);
                fileOutputStream.write(dataBuffer,0,bytesRead);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            // CONVERT TO MP3 ========================================================
            ProcessBuilder builder = new ProcessBuilder();
            String cmd = "ffmpeg -i "+file.getAbsolutePath()+" -vn -acodec libmp3lame out.mp3";

            File out = new File("out"+System.currentTimeMillis()+".mp3");
            builder.command("ffmpeg","-y", "-i",file.getAbsolutePath(),"-vn","-acodec", "libmp3lame",out.getAbsolutePath());
            System.out.println(cmd);
            //builder.command(cmd);
            Process process = builder.start();
            process.waitFor();
            logger.info("mp4 to mp3 conversion.");


            // POST MP3 TO WIT =====================================================
            URL witUrl = new URL(WIT_SPEECH_URL);
            HttpURLConnection connection = (HttpURLConnection) witUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + WIT_TOKEN);
            connection.setRequestProperty("content-type", "audio/mpeg3");
           // connection.setRequestProperty("Transfer-encoding", "chunked");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            FileInputStream mp3In = new FileInputStream(out);
            dataBuffer = new byte[1024];
            while ((bytesRead = mp3In.read(dataBuffer, 0, 1024)) != -1) {
                dataOutputStream.write(dataBuffer, 0, bytesRead);
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            mp3In.close();
            out.delete();
            file.delete();


            int responseCode = connection.getResponseCode();
            System.out.println("status: "+responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in1 = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in1.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                WitMessageResponse witMessageResponse = new Gson().fromJson(response.toString(),WitMessageResponse.class);
                logger.info("Response from wit: " + witMessageResponse);
                calculateFromWitResponse(witMessageResponse,userID);
            } else {
                System.out.println("POST request not worked");
            }
        } catch (IOException | InterruptedException e) {
            // handle exception
            e.printStackTrace();
        }
    }

    public static final String OPERATOR = "arithmetic__operator:arithmetic__operator";
    public static final String NUMBER = "wit$number:number";

    private void calculateFromWitResponse(WitMessageResponse response,String senderID){
        StringBuilder builder = new StringBuilder();
        builder.append("You said:").append("\n");
        builder.append(response.getText()).append("\n");
        builder.append("Answer:").append("\n");
        if(response.getEntities()!=null){
            List<String> operators = new ArrayList<>();
            if(response.getEntities().has(OPERATOR)){
                JsonArray array = response.getEntities().getAsJsonArray(OPERATOR);
                for (int i = 0; i < array.size(); i++) {
                    JsonObject o = array.get(i).getAsJsonObject();
                    if(o.has("value")){
                        operators.add(o.get("value").getAsString());
                    }
                }
            }
            operators.forEach(str->{
                System.out.println("operator: "+str);
            });

            List<Double> numbers = new ArrayList<>();
            if(response.getEntities().has(NUMBER)){
                JsonArray array = response.getEntities().getAsJsonArray(NUMBER);
                for (int i = 0; i < array.size(); i++) {
                    JsonObject o = array.get(i).getAsJsonObject();
                    if(o.has("value")){
                        numbers.add(o.get("value").getAsDouble());
                    }
                }
            }
            numbers.forEach(str->{
                System.out.println("number: "+str);
            });
            if(numbers.size()<=0 || (numbers.size()-1)!=operators.size()){
                builder.append("Invalid Expression.").append("\n");
            }else{
                double ans = numbers.get(0); //55
                for (int i = 1; i <numbers.size(); i++) {
                    System.out.println(operators.get(i-1));
                    switch (operators.get(i-1)){
                        case "*":
                            ans *= numbers.get(i);
                            break;
                        case "+":
                            ans += numbers.get(i);
                            break;
                        case "-":
                            ans -= numbers.get(i);
                            break;
                        case "/":
                            ans /= numbers.get(i);
                            break;
                        case "%":
                            ans %= numbers.get(i);
                            break;
                    }
                    System.out.println("ans: "+ans);
                }
                builder.append(ans);
            }
        }
        messengerBot.sendReply(senderID, builder.toString());
    }

   /* @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody String request) {
        //System.out.println(PAGE_TOKEN);
        logger.info("Message from chat: {}", request);
        Map<String, String> maps = new HashMap<>();
    }*/



}




