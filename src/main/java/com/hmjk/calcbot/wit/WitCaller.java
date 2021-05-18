package com.hmjk.calcbot.wit;

import com.google.gson.Gson;
import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.wit.WitMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WitCaller {
    public static final String TAG = "WitCaller->";
    private static final Logger logger = LoggerFactory.getLogger(CalcbotApplication.class);

    private String WIT_TOKEN;

    private final String WIT_MESSAGE_URL = "https://api.wit.ai/message?v=20210510";
    private final String WIT_SPEECH_URL = "https://api.wit.ai/speech?v=20210510";
    public static final String OPERATOR = "arithmetic__operator:arithmetic__operator";
    public static final String NUMBER = "wit$number:number";

    public WitCaller(String token){
        this.WIT_TOKEN = token;
    }

    public WitMessageResponse sendTextMessageToWit(String userID, String text) {
        WitMessageResponse witMessageResponse = null;
        try{
            logger.info(TAG+"makeWitRequest: making wit request");
            String correctEncodedURL = WIT_MESSAGE_URL + "&q=" + URLEncoder.encode(text,"UTF-8");
            URL url = new URL(correctEncodedURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + WIT_TOKEN);
            int responseCode = con.getResponseCode();
            System.out.println(TAG+"GET Response Code :: " + responseCode);
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
                witMessageResponse = new Gson().fromJson(response.toString(),WitMessageResponse.class);
                logger.info("Response from wit: " + witMessageResponse);
            } else {
                System.out.println("GET request not worked");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return witMessageResponse;
    }

    public WitMessageResponse sendVoiceMessageToWit(String url) {
        logger.info("postAudioToWit: making wit request");
        WitMessageResponse witMessageResponse = null;
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
            logger.info("mp4 to mp3 conversion finished.");


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
                witMessageResponse = new Gson().fromJson(response.toString(),WitMessageResponse.class);
                logger.info("Response from wit: " + witMessageResponse);
            } else {
                System.out.println("POST request not worked");
            }
        } catch (IOException | InterruptedException e) {
            // handle exception
            e.printStackTrace();
        }
        return witMessageResponse;
    }
}
