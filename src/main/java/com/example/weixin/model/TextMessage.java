package com.example.weixin.model;

public class TextMessage extends BaseMassage{
//        <xml>
//        <ToUserName>< ![CDATA[toUser] ]></ToUserName>
//        <FromUserName>< ![CDATA[fromUser] ]></FromUserName>
//        <CreateTime>12345678</CreateTime>
//        <MsgType>< ![CDATA[text] ]></MsgType>
//        <Content>< ![CDATA[你好] ]></Content>
//        <MsgId>1234567890123456</MsgId>
//        </xml>

    private String Content;
    private String MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
