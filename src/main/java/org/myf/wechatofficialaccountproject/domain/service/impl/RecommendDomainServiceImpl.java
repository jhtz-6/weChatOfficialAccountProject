package org.myf.wechatofficialaccountproject.domain.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.RecommendDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RecommendMenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisCilent;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.RecommendMenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.RecommendMenuRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 22:29
 * @Description: RecommendDomainServiceImpl
 */
@Service
@Deprecated
public class RecommendDomainServiceImpl implements RecommendDomainService {

    @Resource
    RecommendMenuRepository recommendMenuRepository;
    @Resource
    RedisCilent redisCilent;

    @Override
    public String characterRecognition(WeChatMessageDTO weChatMessageDTO) {
        if (!MsgTypeEnum.TEXT.name.equals(weChatMessageDTO.getMsgType())) {
            return null;
        }
        String handleResult = "";
        String ocrMenuContentKey = WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName();
        String ocrMenuActionKey = WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName();
        if ("文字识别开始".equals(weChatMessageDTO.getContent())) {
            redisCilent.addValueToRedis(ocrMenuActionKey, BooleanEnum.TRUE.value, 1000 * 60 * 60 * 24L);
            if (StringUtils.isNotBlank(redisCilent.getValueByKey(ocrMenuContentKey))
                && redisCilent.deleteValueByKey(ocrMenuContentKey)) {
                RecommendMenuQueryParam recommendMenuQueryParam = new RecommendMenuQueryParam();
                recommendMenuQueryParam.setFromUserName(weChatMessageDTO.getFromUserName());
                List<RecommendMenuDO> recommendMenuDOList =
                    recommendMenuRepository.selectListByParam(recommendMenuQueryParam);
                if (CollectionUtils.isNotEmpty(recommendMenuDOList)) {
                    for (RecommendMenuDO x : recommendMenuDOList) {
                        RecommendMenuDO recommendMenu = new RecommendMenuDO();
                        recommendMenu.setSfyx("0");
                        recommendMenu.setId(x.getId());
                        recommendMenuRepository.saveOrUpdateByID(recommendMenu);
                    }
                }
            }
            handleResult = WeChatUtil.OCR_GEGIN_CONTENT;
        } else if (StringUtils.equalsAny(weChatMessageDTO.getContent(), "文字识别结束")
            || weChatMessageDTO.getContent().contains("文字识别")) {
            if (StringUtils.isEmpty(redisCilent.getValueByKey(ocrMenuContentKey))) {
                handleResult = "大人,您尚未发送文字识别图片。请发送关键词【文字识别开始】";
            } else {
                String afterwords = weChatMessageDTO.getContent().substring(4);
                String ocrMenuContentCalue = redisCilent.getValueByKey(ocrMenuContentKey);
                if (WeChatUtil.RECOMMEND_MENU_LIST.contains(afterwords)) {
                    if (StringUtils.isBlank(ocrMenuContentCalue)) {
                        RecommendMenuQueryParam recommendMenuQueryParam = new RecommendMenuQueryParam();
                        recommendMenuQueryParam.setFromUserName(weChatMessageDTO.getFromUserName());
                        List<RecommendMenuDO> recommendMenuDOList =
                            recommendMenuRepository.selectListByParam(recommendMenuQueryParam);
                        if (CollectionUtils.isNotEmpty(recommendMenuDOList)) {
                            ocrMenuContentCalue = recommendMenuDOList.get(0).getMenuContent();
                            redisCilent.addValueToRedis(ocrMenuContentKey, ocrMenuContentCalue, 1000 * 60 * 60 * 24L);
                        }
                    }
                    handleResult = CommonUtil.recommendMenuByContent(afterwords + ":" + ocrMenuContentCalue);
                } else {
                    RecommendMenuDO recommendMenuDO = new RecommendMenuDO();
                    recommendMenuDO.setFromUserName(weChatMessageDTO.getFromUserName());
                    recommendMenuDO.setMenuContent(ocrMenuContentCalue);
                    recommendMenuRepository.saveOrUpdateByID(recommendMenuDO);
                    handleResult = CommonUtil.recommendMenuByContent("推荐菜谱:" + ocrMenuContentCalue);
                }
            }
        }
        return handleResult;
    }

}
