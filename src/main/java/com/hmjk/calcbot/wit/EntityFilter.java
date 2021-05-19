package com.hmjk.calcbot.wit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hmjk.calcbot.CalcbotApplication;
import com.hmjk.calcbot.model.wit.GenericEntity;
import com.hmjk.calcbot.model.wit.WitEntity;
import com.hmjk.calcbot.model.wit.WitMessageResponse;
import com.hmjk.calcbot.model.wit.WitNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityFilter {

    public static final String TAG = "EntityFilter->";
    private static final Logger logger = LoggerFactory.getLogger(CalcbotApplication.class);

    //is prime
    public static final String IS_PRIME = "prime";

    //number
    public static final String WIT_NUMBER = "wit$number:number";
    //arithmetic operator
    public static final String ARITHMETIC_OPERATOR = "arithmetic_operator";
    //gcd lcm
    public static final String MATH_GCD = "math_gcd";
    public static final String MATH_LCM = "math_lcm";
    //sqrt
    public static final String MATH_SQRT = "math_sqrt";

    //logarithm
    public static final String MATH_LOG = "math_log";

    //trigonometry
    public static final String MATH_SIN = "math_sin";
    public static final String MATH_COSEC = "math_cosec";
    public static final String MATH_COS = "math_cos";
    public static final String MATH_SEC = "math_sec";
    public static final String MATH_TAN = "math_tan";
    public static final String MATH_COT = "math_cot";

    List<GenericEntity> witEntities;
    List<WitNumber> witNumbers;

    private WitMessageResponse witMessageResponse;
    private final IntentFilter intentFilter = new IntentFilter();


    public EntityFilter filter(WitMessageResponse witMessageResponse) {
        this.witMessageResponse = witMessageResponse;
        witNumbers = new ArrayList<>();
        witEntities = new ArrayList<>();

        logger.info(TAG + "filter()");

        Type witNumberListType = new TypeToken<List<WitNumber>>() {
        }.getType();
        Type witEntityListType = new TypeToken<List<GenericEntity>>() {
        }.getType();
        Gson gson = new Gson();
        JsonObject entities = witMessageResponse.getEntities();
        for (Map.Entry<String, JsonElement> e : entities.entrySet()) {
            if (e.getKey().contains(WIT_NUMBER)) {
                this.witNumbers = gson.fromJson(e.getValue(), witNumberListType);
            } else {
                List<GenericEntity> temp = gson.fromJson(e.getValue(), witEntityListType);
                witEntities.addAll(temp);
            }
        }

        witNumbers.sort((t1, t2) -> { //sort in ascending order
            if (t1.getStart() == t2.getStart())
                return 0;
            return t1.getStart() > t2.getStart() ? 1 : -1;
        });

        witEntities.sort((t1, t2) -> { //sort in descending order
            if (t1.getConfidence() == t2.getConfidence())
                return 0;
            return t1.getConfidence() < t2.getConfidence() ? 1 : -1;
        });

        return this;
    }


    public String calculateAndBuildResponse() {
        logger.info(TAG + "calculateAndBuildResponse()");

        if (witNumbers.size() == 0) {
            return "Invalid! No number parsed";
        }

        if (witEntities.size() == 0) {
            String intentResult = checkIfThereIsAnyIntent(witMessageResponse);
            return intentResult == null ? "Invalid! I didn't get what you asked for." : intentResult;
        }

        String response = "";
        GenericEntity witEntity = witEntities.get(0);

        logger.info(TAG+"calculateAndBuildResponse(): operation: "+witEntity.getName());

        double num = witNumbers.get(0).getValue();

        switch (witEntity.getName()) {
            case IS_PRIME:
                if(((int)num-num) == 0.0){
                    response = isPrime((int)num) ? (int)num + " is a prime number." : (int)(num) + " is not a prime number.";
                }else{
                    response = "Invalid! can't prime check."+num+  " is a floating point number.";
                }
                break;
            case ARITHMETIC_OPERATOR:
                response = doArithmeticOperation(witEntity.getValue());
                break;
            case MATH_GCD:
                response = calculateGCD();
                break;
            case MATH_LCM:
                response = calculateLCM();
                break;
            case MATH_SQRT:
                response = calculateSqrt();
                break;
            case MATH_LOG:
                response = calculateLog();
                break;
            case MATH_SIN:
                if(((int)num-num) == 0.0){
                    response = "sin("+(int)num ;
                }else{
                    response = "sin("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                response += ") := "+String.format("%.2f",Math.sin(num));
                break;
            case MATH_COSEC:
                if(((int)num-num) == 0.0){
                    response = "cosec("+(int)num;
                }else{
                    response = "cosec("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                double sin = Math.sin(num);
                response += ") := " + (sin != 0.0 ? String.format("%.2f .", 1/sin) : "Infinity.");
                break;
            case MATH_COS:
                if(((int)num-num) == 0.0){
                    response = "cos("+(int)num ;
                }else{
                    response = "cos("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                response += ") := "+String.format("%.2f .",Math.cos(num));
                break;
            case MATH_SEC:
                if(((int)num-num) == 0.0){
                    response = "sec("+(int)num;
                }else{
                    response = "sec("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                double cos = Math.cos(num);
                response += ") := " + (cos != 0.0 ? String.format("%.2f .", 1/cos): "Infinity.");
                break;
            case MATH_TAN:
                if(((int)num-num) == 0.0){
                    response = "tan("+(int)num ;
                }else{
                    response = "tan("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                response += ") := "+String.format("%.2f .",Math.tan(num));
                break;
            case MATH_COT:
                if(((int)num-num) == 0.0){
                    response = "cot("+(int)num;
                }else{
                    response = "cot("+String.format("%2f",num);
                }
                num = (num/180)*Math.PI;
                double cot = Math.tan(num);
                response += ") := " + (cot != 0.0 ? String.format("%.2f .", 1/cot): "Infinity.");
                break;
            default:
                response = "Invalid! I don't know the operation.";
        }

        return response;
    }

    private String checkIfThereIsAnyIntent(WitMessageResponse witMessageResponse) {
        String intent = intentFilter.filter(witMessageResponse);
        if(intent== null){
            return null;
        }
        switch (intent){
            case IntentFilter.MATH_GCD:
                return calculateGCD();
            case IntentFilter.MATH_LCM:
                return calculateLCM();
            case IntentFilter.MATH_SQRT:
                return calculateSqrt();
            default:
                return null;
        }
    }

    private String doArithmeticOperation(String operator) {
        logger.info(TAG + "doArithmeticOperation()");

        if (witNumbers.size() != 2) {
            return "Invalid! Sorry I didn't understand you.";
        }

        double ans = witNumbers.get(0).getValue();
        String response = ans+" "+operator + " " +witNumbers.get(1).getValue() + " := " ;
        switch (operator) {
            case "*":
                ans *= witNumbers.get(1).getValue();
                break;
            case "+":
                ans += witNumbers.get(1).getValue();
                break;
            case "-":
                ans -= witNumbers.get(1).getValue();
                break;
            case "/":
                ans /= witNumbers.get(1).getValue();
                break;
            case "%":
                ans %= witNumbers.get(1).getValue();
                break;
            default:
                response = "Invalid operation.";
        }
        response += ans +" .";
        return response;
    }

    private String calculateGCD() {
        logger.info(TAG + "calculateGCD()");


        StringBuilder ans = new StringBuilder("GCD(" + (int) witNumbers.get(0).getValue());

        double num = witNumbers.get(0).getValue();
        int result = (int) witNumbers.get(0).getValue();


        if (((int) num - num) != 0.0) {
            return "Invalid! Can't determine the gcd of float number " + num +" .";
        }

        for (int i = 1; i < witNumbers.size(); i++) {
            num = witNumbers.get(i).getValue();

            if (((int) num - num) != 0.0) {
                return "Invalid! Can't determine the gcd of float number " + num;
            }

            ans.append(", ").append((int)num);
            result = gcd((int) num, result);
            if (result == 1) {
                break;
            }
        }
        ans.append(") := ").append(result);
        return ans.toString();
    }


    int gcd(int a, int b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }

    private String calculateLCM() {
        logger.info(TAG + "calculateLCM()");

        StringBuilder ans = new StringBuilder("LCM(" + (int) witNumbers.get(0).getValue());

        double num = witNumbers.get(0).getValue();
        int result = (int) witNumbers.get(0).getValue();

        if (((int) num - num) != 0.0) {
            return "Invalid! Can't determine the GCD of float number " + num;
        }

        for (int i = 1; i < witNumbers.size(); i++) {
            num = witNumbers.get(i).getValue();

            if (((int) num - num) != 0.0) {
                return "Invalid! Can't determine the LCM of float number " + num;
            }

            System.out.println((int)num+ " "+result);
            ans.append(", ").append((int)num);
            result = lcm((int) num, result);
            if (result == 1) {
                break;
            }
        }
        System.out.println();
        ans.append(") := ").append(result);
        return ans.toString();
    }

    int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }


    private String calculateSqrt() {
        logger.info(TAG + "calculateSqrt()");
        StringBuilder ans = new StringBuilder();
        double num = witNumbers.get(0).getValue();
        ans.append("sqrt(").append(num).append(") := ").append(Math.sqrt(num));
        return ans.toString();
    }

    private String calculateLog(){
        int base = -1;
        double num = witNumbers.get(0).getValue();
        if(witNumbers.size()>1){
            base = (int) num;
            num = witNumbers.get(1).getValue();
        }
        double ans;
        String response;
        if(base == -1){
            ans = Math.log(num);
            response = "log(";
        }else if(base == 10){
            ans = Math.log10(num);
            response = "log10(";
        }else{
            ans = Math.log10(num)/Math.log10(base);
            response = "log"+base+"(";
        }
        return response  +String.format("%.2f",num)+") := "+String.format("%.2f",ans);
    }

    private boolean isPrime(int n) {

        if (n == 1) {
            return false;
        }

        for (int i = 2; i*i < n; i++) {
            if (n % i == 0) return false;
        }

        return true;
    }


}
