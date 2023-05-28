package org.myf.wechatofficialaccountproject.domain.service.chain;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface MessageContentHandler {

    /**
     * 处理消息内容,返回处理结果
     * 
     * @param weChatMessageDTO
     * @return
     */
    String handlerMessageContent(WeChatMessageDTO weChatMessageDTO);

    /**
     * 匹配条件,返回true才会去执行具体处理方法
     * 
     * @param weChatMessageDTO
     * @return
     */
    boolean isMatched(WeChatMessageDTO weChatMessageDTO);

    /**
     * 是否要继续执行后面的处理器,默认不执行,如果需要执行,处理器则只会返回最后一次的处理结果。
     * 
     * @param weChatMessageDTO
     * @return
     */
    default boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    };

    interface RegisterArea {
        String REGISTER_TIP = "(请大人登记下所在区服(多个区服之间用;隔开),示例:【区服:倾国倾城;有凤来仪】)";
    }

    interface SendMobile {
        int MAX_LENGTH = 150;
        String EXCEED_LENGTH = "最多50个字,字数超限。";
    }

    interface TuLing {
        String ERROR_RES = "机器人没电了~~~正在充电中";
    }

    interface CharacterRecognition {
        String OCR = "文字识别";
        String START_OCR = "文字识别开始";
        String END_OCR = "文字识别结束";
        String DEFAULT_RESULT = "大人,您尚未发送文字识别图片。请发送关键词【文字识别开始】";

    }

    interface Event {
        String UN_SUBSCEIBE_WORD = "谢谢你取关之后又来关注我,";
    }

    interface OpenAi {
        String DEFAULT_OPENAI_RESULT = "您尚未发送chatgpt相关请求;请参考示例(必须以chatgpt开头): chatgpt帮我写一份情书、chatgpt以我爱打游戏写一首打油诗\n"
            + "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247485132&idx=1&sn=d4ac8d1f048ead3b2f56607a32d241a8&chksm=c2920d3ff5e58429e96dc55e2b5d2c4b91434a467db9baffb2c62e711d3c9b18efec5b8f01a3#rd";
        String IN_PROCESS = "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
        String NEED_TO_GET_RESULT = "您有chatgpt结果尚未接收,请发送chatgpt来接收。";
        String NEED_MORE_REQUEST = "以下数据来自chatgpt(请发送chatgpt1来获取剩下的内容)" + ":\n";
        String RESULT = "以下数据来自chatgpt" + ":\n";
    }

}
