package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.FoodDTO;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:13
 * @Description: 查询食物或食材相关逻辑
 */
@Service
public class QueryFoodOrMaterialHandler implements MessageContentHandler {

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        StringBuilder queryFoodOrMaterialResult = new StringBuilder();
        String content = weChatMessageDTO.getContent();
        String[] contentArray = content.split(" ");
        int index = 1;
        for (String contentStr : contentArray) {
            if (StringUtils.isNotBlank(contentStr)) {
                List<FoodDTO> foodDOList = WeChatUtil.FOOD_LIST.stream()
                    .filter(x -> ThreadLocalHolder.BELONGER_THREAD_LOCAL.get().equals(x.getBelonger())
                        && contentStr.equals(x.getFoodName()))
                    .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(foodDOList)) {
                    // 说明填的是food 想要查询material
                    List<MenuDTO> menuList = WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get())
                        .stream().filter(x -> contentStr.equals(x.getFood())).collect(Collectors.toList());
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
                        List<MenuDTO> menuList =
                            WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()).stream()
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

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return !(!StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name)
            || weChatMessageDTO.getContent().startsWith("独特") || weChatMessageDTO.getContent().startsWith("建议"));
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
