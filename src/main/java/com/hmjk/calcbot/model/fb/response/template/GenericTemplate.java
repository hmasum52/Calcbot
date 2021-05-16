package com.hmjk.calcbot.model.fb.response.template;

import com.google.gson.Gson;
import com.hmjk.calcbot.model.fb.response.FbButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
"attachment":{
      "type":"template",
      "payload":{
        "template_type":"generic",
        "elements":[
           {
            "title":"Welcome!",
            "image_url":"https://petersfancybrownhats.com/company_image.png",
            "subtitle":"We have the right hat for everyone.",
            "default_action": {
              "type": "web_url",
              "url": "https://petersfancybrownhats.com/view?item=103",
              "webview_height_ratio": "tall",
            },
            "buttons":[
              {
                "type":"web_url",
                "url":"https://petersfancybrownhats.com",
                "title":"View Website"
              },{
                "type":"postback",
                "title":"Start Chatting",
                "payload":"DEVELOPER_DEFINED_PAYLOAD"
              }
            ]
          }
        ]
      }
    }
 */
public class GenericTemplate implements TemplatePayload {
    private String template_type;
    private List<Element> elements;

    @Override
    public String getTemplate_type() {
        return template_type;
    }

    @Override
    public void setTemplate_type(String type) {
        this.template_type = type;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element){
        if(elements == null)
            elements = new ArrayList<>();
        elements.add(element);
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String  getPlayLoadAsString() {
        return new Gson().toJson(this);
    }

    /*
           {
            "title":"Welcome!",
            "image_url":"https://petersfancybrownhats.com/company_image.png",
            "subtitle":"We have the right hat for everyone.",
            "default_action": {
              "type": "web_url",
              "url": "https://petersfancybrownhats.com/view?item=103",
              "webview_height_ratio": "tall",
            },
            "buttons":[
              {
                "type":"web_url",
                "url":"https://petersfancybrownhats.com",
                "title":"View Website"
              },{
                "type":"postback",
                "title":"Start Chatting",
                "payload":"DEVELOPER_DEFINED_PAYLOAD"
              }
            ]
          }
     */
    public static class Element{
        private String title;
        private String image_url;
        private String subtitle;
        private Map<String,String>  default_action;
        private List<FbButton> buttons;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public Map<String, String> getDefault_action() {
            return default_action;
        }

        public void setDefault_action(Map<String, String> default_action) {
            this.default_action = default_action;
        }

        public List<FbButton> getButtons() {
            return buttons;
        }

        public void setButtons(List<FbButton> buttons) {
            this.buttons = buttons;
        }

        public void addButton(FbButton button){
            if(buttons == null)
                buttons = new ArrayList<>();
            buttons.add(button);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return Objects.equals(title, element.title) &&
                    Objects.equals(image_url, element.image_url) &&
                    Objects.equals(subtitle, element.subtitle) &&
                    Objects.equals(default_action, element.default_action) &&
                    Objects.equals(buttons, element.buttons);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, image_url, subtitle, default_action, buttons);
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
