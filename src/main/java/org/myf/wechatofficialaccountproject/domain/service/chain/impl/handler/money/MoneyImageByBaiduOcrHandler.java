package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.money;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.ImageByBaiduOcrHandler;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrWordsResult;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:13
 * @Description: 微信支付成功百度OCR处理图片相关逻辑
 */
@Service
public class MoneyImageByBaiduOcrHandler extends ImageByBaiduOcrHandler {

    /**
     * PAY_RESULT_KEY_WORDS
     */
    private static List<String> PAY_RESULT_KEY_WORDS = new ArrayList<>();

    static {
        PAY_RESULT_KEY_WORDS.add("支付成功");
        PAY_RESULT_KEY_WORDS.add("￥");
        PAY_RESULT_KEY_WORDS.add("收款方");
    }

    /**
     * 支付明细截图 空格 全部去掉
     */
    private static List<String> PAY_DETAIL_KEY_WORDS = new ArrayList<>();

    static {
        PAY_DETAIL_KEY_WORDS.add("转给AAMoore");
        PAY_DETAIL_KEY_WORDS.add("-");
    }



    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        String handleImageResult = "无法识别的图片,请联系管理员处理~";
        /*String ocrMenuAction =
            redisClient.getValueByKey(WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName());*/
        //if (BooleanEnum.TRUE.value.equals(ocrMenuAction)) {
        BaiduOcrResponse generalImageResult =
                baiduOcrClient.getWebImageByPhotoUrl(weChatMessageDTO.getPicUrl());
        if (Objects.isNull(generalImageResult)) {
            return "公众号当前访问图片识别人数过多,请稍后再试~~";
        } else {
            List<BaiduOcrWordsResult> resultList = generalImageResult.getWords_result();
            weChatMessageDTO.setContent(JSONUtil.toJsonStr(resultList));
            //需要有支付成功、￥68.00、收款方、收款方名字(收款方下一个是收款方名字)
            String picStr = resultList.stream().map(BaiduOcrWordsResult::getWords).collect(Collectors.joining()).replaceAll(" ","");
            String result = checkPicStr(picStr);
            if(StrUtil.isNotBlank(result)){
                return result;
            }
            for (BaiduOcrWordsResult baiduOcrResultWords : resultList) {
                //这里需要把￥68.00 提取出来68.00 然后放到文本内容中
                String words = baiduOcrResultWords.getWords();
                if (words.startsWith("￥") || words.startsWith("-")) {
                    String handKeyWordResult = WeChatUtil.WeChatKeyWordMap.get(
                            ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + words.substring(1));
                    if (StringUtils.isNotBlank(handKeyWordResult)) {
                        return handKeyWordResult;
                    }
                }
            }
        }
        //}
        return handleImageResult;
    }

    private String checkPicStr(String picStr) {
        boolean isMoneyPic = true;
        //两个模式鉴定,一个是支付成功截图
        /**
         * [{"words":"12:32"},{"words":"费"},{"words":"4G"},{"words":"5G"},{"words":"支付成功"},
         * {"words":"￥38.80"},{"words":"收款方"},{"words":"AA Moore"},{"words":"完成"}]
         */
        //一个是账单明细
        /**
         * [{"words":"12:32"},{"words":"5G"},{"words":"×"},{"words":"全部账单"},{"words":"赞赏码-转给AA Moore"},
         * {"words":"-52.00"},{"words":"当前状态"},{"words":"朋友已收钱"},{"words":"收款方备注"},{"words":"赞赏码"},
         * {"words":"支付时间"},{"words":"2025年6月8日12：32：40"},{"words":"收款方"},{"words":"AA Moore"},
         * {"words":"支付方式"},{"words":"零钱"},{"words":"转账单号"},{"words":"1000108101250608000632118073750"},
         * {"words":"79612"},{"words":"账单服务"},{"words":"对订单有疑惑"},{"words":"本服务由财付通提供"}]
         */
        for (String word : PAY_RESULT_KEY_WORDS) {
            if (!picStr.contains(word)) {
                isMoneyPic = false;
            }
        }
        if(!isMoneyPic){
            isMoneyPic = true;
            //再判断 明细截图
            for (String word : PAY_DETAIL_KEY_WORDS) {
                if (!picStr.contains(word)) {
                    isMoneyPic = false;
                }
            }
        }
        if (!isMoneyPic) {
            return "无法识别的图片,请联系管理员处理";
        }
        isMoneyPic = false;
        //再判断 收款方是否正确
        String[] payeeArray = WeChatUtil.WeChatKeyWordMap.get(
                ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + "payee").split(",");
        for (int i = 0; i < payeeArray.length; i++) {
            if (!isMoneyPic && picStr.contains(payeeArray[i].replaceAll(" ",""))) {
                isMoneyPic = true;
            }
        }
        if (!isMoneyPic) {
            return "无法识别的图片,请联系管理员处理";
        }
        return "";
    }

}
