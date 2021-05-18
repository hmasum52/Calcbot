package com.hmjk.calcbot.wit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hmjk.calcbot.model.wit.WitIntent;
import com.hmjk.calcbot.model.wit.WitMessageResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class IntentFilter {
    public static final String ARITHMETIC_OPERATION = "arithmetic_operation";
    public static final String MATH_LOGARITHM = "math_logarithm";
    public static final String MATH_TRIGONOMETRY = "math_trigonometry";
    public static final String MATH_SQRT = "math_sqrt";
    public static final String MATH_GCD = "math_gcd";
    public static final String MATH_LCM = "math_lcm";
    public static final String NUMBER_PROPERTY = "number_property";

    public String filter(WitMessageResponse witMessageResponse){
        List<WitIntent> witIntents = witMessageResponse.getIntents();
        if(witIntents!=null){
            witIntents.sort((t1, t2) -> { //sort in descending order
                if(t1.getConfidence() == t2.getConfidence())
                    return 0;
                return t1.getConfidence() < t2.getConfidence()? 1 : -1;
            });
        }

        if(witIntents.size() == 0)
            return  null;

        return witIntents.get(0).getName();
    }
}
