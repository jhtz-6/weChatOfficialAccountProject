package org.myf.wechatofficialaccountproject.infrastructure.util.netty.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.OpenAiClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-27 10:36
 * @Description: ServerHandler
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    private WebSocketServerHandshaker handshaker;
    private static final OpenAiClient OPENAI_CLIENT =
        (OpenAiClient)ApplicationContextUtil.getBeanByName("openAiClient");

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("exceptionCaught.caught: ", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("ServerHandlerchannelActive:");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame)msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 检查是否建立 WebSocket 连接
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
            "ws://" + WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.IP) + ":8824/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 处理 WebSocket 消息
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
        } else if (frame instanceof TextWebSocketFrame) {
            InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
            String ip = socketAddress.getAddress().toString();
            // 处理文本消息
            TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
            String message = textFrame.text();
            LOGGER.info("Received message: " + message);
            WeChatMessageDTO weChatMessageDTO = new WeChatMessageDTO();
            buildWeChatMessageDTO(weChatMessageDTO, message, ip);
            // 响应消息给客户端
            String openAiResult = OPENAI_CLIENT.getResultByOpenAi(weChatMessageDTO, 0, null);
            ctx.channel()
                .writeAndFlush(new TextWebSocketFrame(Objects.isNull(openAiResult) ? "系统异常,请稍后再试" : openAiResult));
        } else if (frame instanceof PingWebSocketFrame) {
            // 处理 Ping 消息
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
        } else {
            // 不支持的消息类型
            throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
        }
    }

    private void buildWeChatMessageDTO(WeChatMessageDTO weChatMessageDTO, String receiveMessage, String ip) {
        weChatMessageDTO.setFromUserName("netty:" + ip.replaceAll("/", ""));
        weChatMessageDTO.setContent("chatgpt" + receiveMessage);
    }

}
