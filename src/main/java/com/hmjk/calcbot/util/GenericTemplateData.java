package com.hmjk.calcbot.util;

import com.hmjk.calcbot.model.fb.response.FbButton;

import java.util.ArrayList;
import java.util.List;

public class GenericTemplateData {
    public static List<FbButton> buttonList = new ArrayList<>();
    static {
        buttonList.add(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Arithmetic Operation",
                "ARITHMETIC"
        ));
        buttonList.add(new FbButton(
                FbButton.TYPE_POSTBACK,
                "GCD & LCM",
                "GCD_LCM"
        ));
        buttonList.add(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Logarithm",
                "LOGARITHM"
        ));
        buttonList.add(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Trigonometry",
                "TRIGONOMETRY"
        ));
        buttonList.add(new FbButton(
                FbButton.TYPE_POSTBACK,
                "Prime number check",
                "IS_PRIME"
        ));
    }

    private String image_url;
    private String title;
    private String subTitle;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
