package org.myf.wechatofficialaccountproject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.FoodDTO;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.WeChatDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.*;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.BaiduOcrClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisCilent;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.TencentShortMessageClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.TuLingClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrWordsResult;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.TuLingResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.DateUtils;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.TuLingUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 12:46
 * @Description: WeChatDomainService实现类
 */
@Service
public class WeChatDomainServiceImpl implements WeChatDomainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatDomainServiceImpl.class);

    @Autowired
    private WeChatRepository weChatRepository;
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    AdviseRepository adviseRepository;
    @Autowired
    BaiduOcrClient baiduOcrClient;
    @Autowired
    RedisCilent redisCilent;
    @Autowired
    TuLingClient tuLingClient;
    @Resource
    TencentShortMessageClient tencentShortMessageClient;

    @Override
    public <T> WeChatMessageDO convertDtoToDo(T t) {
        WeChatMessageDO weChatMessageDO = new WeChatMessageDO();
        BeanUtils.copyProperties(t, weChatMessageDO);
        return weChatMessageDO;
    }

    @Override
    public void saveWeChatMessageDO(WeChatMessageDO weChatMessageDO) {
        weChatRepository.saveOrUpdateById(weChatMessageDO);
    }

    @Override
    public String handleWeChatMessageDTO(WeChatMessageDTO weChatMessageDTO) {
        WeChatMessage weChatMessage = new WeChatMessage(weChatMessageDTO.getFromUserName(),
            MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType()), weChatMessageDTO.getPicUrl(),
            weChatMessageDTO.getContent());
        String handKeyWordResult = weChatMessage.handKeyWord();
        if (StringUtils.isNotBlank(handKeyWordResult)) {
            return handKeyWordResult;
        }
        String handleNumTextResult = weChatMessage.handleBelongOrNumText();
        if (StringUtils.isNotBlank(handleNumTextResult)) {
            return handleNumTextResult;
        }
        String handleSpecialWordResult = weChatMessage.handleSpecialWord();
        if (StringUtils.isNotBlank(handleSpecialWordResult)) {
            return handleSpecialWordResult;
        }
        if (WeChatUtil.ACCOMPANY_MAP.containsKey(weChatMessageDTO.getContent())) {
            return CommonUtil.conactAccompany(WeChatUtil.ACCOMPANY_MAP.get(weChatMessageDTO.getContent()));
        }
        String handleCostPerformanceResult = weChatMessage.handleCostPerformance();
        if (StringUtils.isNotBlank(handleCostPerformanceResult)) {
            return handleCostPerformanceResult;
        }
        String handleFishTextResult = weChatMessage.handleFishText();
        if (StringUtils.isNotBlank(handleFishTextResult)) {
            return handleFishTextResult;
        }
        String handleCategoryResult = weChatMessage.handleCategory();
        if (StringUtils.isNotBlank(handleCategoryResult)) {
            return handleCategoryResult;
        }
        if (weChatMessage.getContent().startsWith("建议")) {
            if (weChatMessage.getContent().length() > 2000) {
                return "大人,您的建议内容过长~~~ 系统录入失败";
            } else {
                AdviseDO adviseDO = new AdviseDO();
                adviseDO.setContent(weChatMessage.getContent());
                adviseDO.setUserId(weChatMessage.getFromUserName());
                adviseRepository.saveOrUpdateById(adviseDO);
                return "大人,您的宝贵建议【" + weChatMessage.getContent() + "】已被系统成功录入~~~";
            }
        }
        if (StringUtils.isNotBlank(WeChatUtil.RECOMMEND_MENU_LIST.stream()
            .filter(x -> weChatMessage.getContent().startsWith(x)).findAny().orElse(null))) {
            return CommonUtil.recommendMenuByContent(weChatMessage.getContent());
        }
        String errorCorrectionResult = weChatMessage.errorCorrection();
        if (StringUtils.isNotBlank(errorCorrectionResult)) {
            return errorCorrectionResult;
        }
        return null;
    }

    @Override
    public String handleText(WeChatMessageDTO weChatMessageDTO) {
        String queryFoodOrMaterialResult = queryFoodOrMaterial(weChatMessageDTO);
        if (StringUtils.isNotBlank(queryFoodOrMaterialResult)) {
            return queryFoodOrMaterialResult;
        }
        String weChatMessageDTOContent = weChatMessageDTO.getContent();
        if (weChatMessageDTOContent.startsWith("紧急")) {
            if (weChatMessageDTOContent.length() > 100) {
                return "最多50个字,字数超限。";
            }
            String[] params = new String[9];
            try {
                params[0] = weChatMessageDTOContent.substring(0, Math.min(6, weChatMessageDTOContent.length()));
                params[1] = weChatMessageDTOContent.substring(6, Math.min(12, weChatMessageDTOContent.length()));
                params[2] = weChatMessageDTOContent.substring(12, Math.min(18, weChatMessageDTOContent.length()));
                params[3] = weChatMessageDTOContent.substring(18, Math.min(24, weChatMessageDTOContent.length()));
                params[4] = weChatMessageDTOContent.substring(24, Math.min(30, weChatMessageDTOContent.length()));
                params[5] = weChatMessageDTOContent.substring(30, Math.min(36, weChatMessageDTOContent.length()));
                params[6] = weChatMessageDTOContent.substring(36, Math.min(42, weChatMessageDTOContent.length()));
                params[7] = weChatMessageDTOContent.substring(42, Math.min(48, weChatMessageDTOContent.length()));
                params[8] = weChatMessageDTOContent.substring(48, Math.min(54, weChatMessageDTOContent.length()));
            } catch (Exception e) {
                LOGGER.error("WeChatDomainServiceImpl.e:{},weChatMessageDTOContent:{}", e,
                    JSON.toJSONString(weChatMessageDTOContent));
            }
            SmsSingleSenderResult smsSingleSenderResult;
            try {
                smsSingleSenderResult = tencentShortMessageClient.sendShortMessagebyParams(params, 415610);
            } catch (Exception e) {
                LOGGER.error("sendMessagebyTencent.e:{},weChatMessageDTOContent:{}", e,
                    JSON.toJSONString(weChatMessageDTOContent));
                return "短信发送失败:" + e;
            }
            if (smsSingleSenderResult.result != 0) {
                LOGGER.warn("weChatMessageDTOContent:" + JSON.toJSONString(weChatMessageDTOContent));
                return "短信发送失败错误信息:" + smsSingleSenderResult.errMsg;
            }
            return "大人的短信已经成功发送到小的手机上了~~~";
        }
        return null;
    }

    @Override
    public String handleImageByBaiduOcr(WeChatMessageDTO weChatMessageDTO) {
        if (!MsgTypeEnum.IMAGE.name.equals(weChatMessageDTO.getMsgType())) {
            return null;
        }
        String handleImageResult = "";
        if (BooleanEnum.TRUE.value
            .equals(redisCilent.getValueByKey(WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName()))) {
            BaiduOcrResponse generalImageResult =
                baiduOcrClient.getGeneralImageByPhotoUrl(weChatMessageDTO.getPicUrl());
            if (Objects.isNull(generalImageResult)) {
                return "公众号当前访问图片识别人数过多,请稍后再试~~";
            } else {
                List<BaiduOcrWordsResult> resultList = generalImageResult.getWords_result();
                resultList = resultList.stream().map(x -> {
                    if (x.getWords().equals("萝ト")) {
                        x.setWords("萝卜");
                    }
                    return x;
                }).collect(Collectors.toList());
                for (BaiduOcrWordsResult baiduOcrResultWords : resultList) {
                    BaiduOcrWordsResult MaterialNum;
                    if (WeChatUtil.MATERIAL_NAME_LIST.contains(baiduOcrResultWords.getWords())) {
                        Integer materialLeft = baiduOcrResultWords.getLocation().getLeft();
                        Integer materialTop = baiduOcrResultWords.getLocation().getTop();
                        MaterialNum = resultList.stream()
                            .filter(x -> !x.getWords().equals(baiduOcrResultWords.getWords())
                                && StringUtils.isNumeric(x.getWords()) && (x.getLocation().getLeft() - materialLeft) > 0
                                && (x.getLocation().getLeft() - materialLeft) < 200
                                && materialTop - x.getLocation().getTop() > 0
                                && materialTop - x.getLocation().getTop() < 60)
                            .findAny().orElse(null);

                        if (Objects.isNull(MaterialNum)) {
                            handleImageResult += "1个" + baiduOcrResultWords.getWords() + ";";
                        } else {
                            handleImageResult += MaterialNum.getWords() + "个" + baiduOcrResultWords.getWords() + ";";
                        }
                    }
                }
                if (StringUtils.isNotBlank(handleImageResult)) {
                    if (StringUtils.isNotBlank(
                        redisCilent.getValueByKey(WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName()))) {
                        redisCilent.addValueToRedis(WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName(),
                            redisCilent.getValueByKey(WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName())
                                + handleImageResult,
                            1000 * 60 * 60 * 24L);
                    }
                    return "收到的内容是:" + handleImageResult;
                }
            }
        } else {
            // 说明是进行菜谱查询
            BaiduOcrResponse webImageResult = baiduOcrClient.getWebImageByPhotoUrl(weChatMessageDTO.getPicUrl());
            if (Objects.isNull(webImageResult)) {
                return "公众号当前访问图片识别人数过多,请稍后再试~~";
            } else {
                List<BaiduOcrWordsResult> resultList = webImageResult.getWords_result();
                if (CollectionUtils.isEmpty(resultList)) {
                    return "大人,您是要和我斗图吗？\n对于图片我有如下功能关键词:【文字识别开始】、【菜谱识图】";
                }
                List<String> ResultStrList = resultList.stream().map(x -> x.getWords()).collect(Collectors.toList());
                List<MenuDTO> menuList = WeChatUtil.MENU_LIST.stream()
                    .filter(x -> ResultStrList.contains(x.getFood().trim())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(menuList)) {
                    for (int i = 0; i < menuList.size(); i++) {
                        MenuDTO menuDTO = new MenuDTO();
                        CommonUtil.copyProperties(menuList.get(i), menuDTO);
                        handleImageResult =
                            handleImageResult + (i + 1) + "、" + CommonUtil.conactMenuOneUserWithCost(menuDTO) + "\n";
                    }
                    return handleImageResult;
                }
            }
            handleImageResult = "大人,您是要和我斗图吗？\n对于图片我有如下功能关键词:【文字识别开始】、【菜谱识图】";
        }
        return handleImageResult;
    }

    @Override
    public String handleByTuLing(String message) {
        TuLingResponse tuLingResponse = tuLingClient.getResultByTuling(message);
        if (Objects.isNull(tuLingResponse)) {
            return "机器人没电了~~~正在充电中";
        } else if (TuLingUtil.TULING_ERROR_MAP.containsKey(tuLingResponse.getIntent().getCode())) {
            return TuLingUtil.TULING_ERROR_MAP.get(tuLingResponse.getIntent().getCode());
        } else if (StringUtils.isNotBlank(tuLingResponse.getResults().get(0).getValues().getText())) {
            return tuLingResponse.getResults().get(0).getValues().getText() + "\n(来自机器人:"
                + tuLingResponse.getRebotName() + ")";
        }
        return "看来我还需要继续学习,小的看不懂大人输入的:[" + message + "]是什么意思啊~~\n" + "我有很多功能哦,大人可以发送【关键词】来知悉哦~~";
    }

    @Override
    public String getCurrentPersonNum(String setKey, String value, Long timeOut) {
        redisCilent.addValueToRedis(setKey, value, timeOut);
        Set<String> keySet = redisCilent.getAllKeys("current_person_*");
        if (CollectionUtils.isNotEmpty(keySet)) {
            redisCilent.addValueToRedis("num_current_person", keySet.size() + "", null);
        }
        return redisCilent.getValueByKey("num_current_person");
    }

    private String queryFoodOrMaterial(WeChatMessageDTO weChatMessageDTO) {
        if (weChatMessageDTO.getContent().startsWith("独特") || weChatMessageDTO.getContent().startsWith("建议")) {
            return null;
        }
        StringBuilder queryFoodOrMaterialResult = new StringBuilder();
        String content = weChatMessageDTO.getContent();
        String[] contentArray = content.split(" ");
        int index = 1;
        for (String contentStr : contentArray) {
            if (StringUtils.isNotBlank(contentStr)) {
                List<FoodDTO> foodDOList = WeChatUtil.FOOD_LIST.stream().filter(x -> contentStr.equals(x.getFoodName()))
                    .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(foodDOList)) {
                    // 说明填的是food 想要查询material
                    List<MenuDTO> menuList = WeChatUtil.MENU_LIST.stream().filter(x -> contentStr.equals(x.getFood()))
                        .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(menuList)) {
                        for (MenuDTO menuDTO : menuList) {
                            if (contentArray.length > 1) {
                                queryFoodOrMaterialResult.append(index).append("、")
                                    .append(CommonUtil.conactOnlyOneMenu(menuDTO));
                            } else {
                                queryFoodOrMaterialResult.append(CommonUtil.conactOnlyOneMenu(menuDTO));
                            }
                            // 添加性价比
                            if (Objects.nonNull(menuDTO.getCostPerformance()) && Objects.nonNull(menuDTO.getPrice())) {
                                queryFoodOrMaterialResult.append(";").append(menuDTO.getPrice()).append("元宝")
                                    .append(CommonUtil.getCostPerformanceWords(menuDTO.getCostPerformance()));
                            }
                            queryFoodOrMaterialResult.append("。");
                            index++;
                        }
                    }
                } else {
                    // 说明填的可能是material
                    List<MaterialDTO> materialList = WeChatUtil.MATERIAL_LIST.stream()
                        .filter(x -> contentStr.equals(x.getMaterialName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(materialList)) {
                        MaterialDTO materialDTO = materialList.get(0);
                        List<MenuDTO> menuList = WeChatUtil.MENU_LIST.stream()
                            .filter(x -> x.getRawMaterial().contains(contentStr)).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(menuList)) {
                            if (Objects.nonNull(materialDTO.getNum()) && Objects.nonNull(materialDTO.getPrice())) {
                                queryFoodOrMaterialResult = new StringBuilder(materialDTO.getMaterialName() + "最低价格是:"
                                    + materialDTO.getPrice() + "元宝,可获得数量是" + materialDTO.getNum() + "个"
                                    + (StringUtils.isNotBlank(materialDTO.getSpentTime())
                                        ? ",种植耗时:" + materialDTO.getSpentTime() + "个小时。\n" : "。\n"));
                            }
                            if (menuList.size() > 40) {
                                Integer menuSize = menuList.size();
                                menuList = menuList.subList(0, 40);
                                queryFoodOrMaterialResult.append("菜谱数量过多有:").append(menuSize).append(",只展示:前")
                                    .append(menuList.size()).append("个。\n");
                            }
                            for (MenuDTO menuDTO : menuList) {
                                if (menuList.size() > 1) {
                                    if (menuList.size() < 20) {
                                        queryFoodOrMaterialResult.append(index).append("、")
                                            .append(CommonUtil.conactMoreThanOneMenu(menuDTO));
                                    } else {
                                        queryFoodOrMaterialResult.append(menuDTO.getFood()).append(": ")
                                            .append(menuDTO.getRawMaterial()).append("。\n");
                                    }
                                } else {
                                    queryFoodOrMaterialResult.append(CommonUtil.conactMoreThanOneMenu(menuDTO));
                                }
                                index++;
                            }
                        }
                        queryFoodOrMaterialResult.insert(0,
                            Objects.toString(WeChatUtil.FISH_KEY_MAPS.get(content), "") + "\n");
                    }
                }
            }
        }
        return queryFoodOrMaterialResult.toString();
    }

}
