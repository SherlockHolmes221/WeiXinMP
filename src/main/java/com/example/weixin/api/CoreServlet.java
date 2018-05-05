package com.example.weixin.api;

import com.example.weixin.dao.UserDaoImp;
import com.example.weixin.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    }
}
