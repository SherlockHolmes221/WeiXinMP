package com.example.weixin.api;

import com.example.weixin.dao.UserDaoImp;
import com.example.weixin.model.WeChatUser;
import com.example.weixin.utils.MessageUtil;
import com.example.weixin.utils.SignUtil;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet(name = "wechat",urlPatterns = "/wechat/*")
public class CoreServlet extends HttpServlet {

    @Autowired
    private UserDaoImp userDaoImp;

    //private static final long serialVersionUID = 4323197796926899691L;

    /**
     * 确认请求来自微信服务器
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter writer=response.getWriter();
//        writer.write("123");
//        writer.close();

        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.print("null");
        out.close();
        out = null;
    }

    /**
     * 处理微信服务器发来的消息
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO 消息的接收、处理、响应

//        String accessToken = WeiXinUtil.getAccessToken();
//        System.out.print("123"+accessToken + "122132");


//        <xml>
//        <ToUserName>< ![CDATA[toUser] ]></ToUserName>
//        <FromUserName>< ![CDATA[fromUser] ]></FromUserName>
//        <CreateTime>1348831860</CreateTime>
//        <MsgType>< ![CDATA[text] ]></MsgType>
//        <Content>< ![CDATA[this is a test] ]></Content>
//        <MsgId>1234567890123456</MsgId>
//        </xml>

//        <xml>
//        <ToUserName>< ![CDATA[toUser] ]></ToUserName>
//        <FromUserName>< ![CDATA[fromUser] ]></FromUserName>
//        <CreateTime>12345678</CreateTime>
//        <MsgType>< ![CDATA[text] ]></MsgType>
//        <Content>< ![CDATA[你好] ]></Content>
//        </xml>

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        Map<String,String> map = MessageUtil.xmlToMap(request);

        //获取用户的openID
        String FromUserName = map.get("FromUserName");
//        System.out.print(FromUserName);

        //存入用户的openID到数据库


        String ToUserName = map.get("ToUserName");
        String MsgType = map.get("MsgType");
        String Content = map.get("Content");

        String xml = null;

        //文本消息
        if(MessageUtil.MESSAGE_TEXT.equals(MsgType)){

            xml = MessageUtil.initText(FromUserName,ToUserName,Content);
//            TextMessage textMessage = new TextMessage();
//
//            textMessage.setFromUserName(ToUserName);
//            textMessage.setToUserName(FromUserName);
//
//            //回复内容
//            textMessage.setContent( "您发送的信息： " + Content);
//            textMessage.setCreateTime(new Date().getTime());
//            textMessage.setMsgType(MsgType);
//
//            xml = MessageUtil.textMessageToXml(textMessage);


        }

        //订阅消息
        else if(MessageUtil.MESSAGE_EVENT.equals(MsgType)){
            String eventType = map.get("Event");

            if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){

                xml = MessageUtil.initText(FromUserName,ToUserName,MessageUtil.getMenuText());

                System.out.print(FromUserName+"关注   ");

                WeChatUser weChatUser = new WeChatUser();
                weChatUser.setOpenId(FromUserName);
                userDaoImp.save(weChatUser);

            }

            //取消订阅
            else if(MessageUtil.MESSAGE_UNSUBSCRIBE.equals(eventType)){
                System.out.print(FromUserName+"取消   ");

                WeChatUser user = new WeChatUser();
                List<WeChatUser> weChatUserList= userDaoImp.findAll();
                for(WeChatUser u : weChatUserList){
                    if(u.getOpenId().equals(FromUserName)){
                        user = u;
                        break;
                    }
                }
                System.out.print(user.getId());
                userDaoImp.delete(user);
            }

            else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){

            }
            else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){

            }

        }

        out.print(xml);
        out.close();

    }
}
