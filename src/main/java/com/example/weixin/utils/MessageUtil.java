package com.example.weixin.utils;

import com.example.weixin.model.TextMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT= "event";
    public static final String MESSAGE_SUBSCRIBE= "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE= "unsubscribe";
    public static final String MESSAGE_CLICK= "click";
    public static final String MESSAGE_VIEW= "view";


    /*
    xml转化为map集合
     */
    public static Map<String,String> xmlToMap(HttpServletRequest request){
        Map<String,String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = reader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element root = doc.getRootElement();

        List<Element> list = root.elements();

        for(Element e : list){
            map.put(e.getName(),e.getText());
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /*
    将消息对象转换为xml
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);

    }


    //封装一个文字信息为xml
    public static String initText(String ToUserName,String FromUserName,String Content){
        TextMessage textMessage = new TextMessage();

        textMessage.setFromUserName(FromUserName);
        textMessage.setToUserName(ToUserName);

        //回复内容
        textMessage.setContent(Content);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MESSAGE_TEXT);

        return MessageUtil.textMessageToXml(textMessage);

    }

    public static String getMenuText(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("欢迎您的关注!\n");
        stringBuffer.append("公众号的菜单功能有：\n");
        stringBuffer.append("1.function1\n");
        stringBuffer.append("2.function2\n");
        stringBuffer.append("3.function3\n");
        return stringBuffer.toString();
    }
}
