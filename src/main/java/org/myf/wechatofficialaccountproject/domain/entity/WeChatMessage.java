package org.myf.wechatofficialaccountproject.domain.entity;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.FoodDTO;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.Category;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.GameFishDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil.FuzzyMatchingList;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 23:54
 * @Description: WeChatMessage领域实体
 */
@Getter
public class WeChatMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatMessage.class);

    private String fromUserName;

    private MsgTypeEnum msgType;

    private String picUrl;

    private String content;
    private String handleWeChatMessageResult = "";

    private List<MenuDTO> menuList;

    private WeChatMessage(Builder builder) {
        this.fromUserName = builder.getFromUserName();
        this.msgType = builder.getMsgType();
        this.picUrl = builder.getPicUrl();
        this.content = builder.getContent();
        this.menuList = builder.getMENU_LIST();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Getter
    public static final class Builder {
        private String fromUserName;

        private MsgTypeEnum msgType;

        private String picUrl;

        private String content;

        private List<MenuDTO> MENU_LIST;

        public Builder fromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
            return this;
        }

        public Builder msgType(MsgTypeEnum msgType) {
            this.msgType = msgType;
            return this;
        }

        public Builder picUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder menuList(List<MenuDTO> MENU_LIST) {
            this.MENU_LIST = MENU_LIST;
            return this;
        }

        public WeChatMessage build() {
            return new WeChatMessage(this);
        }

    }

    public String handKeyWord() {
        if (StringUtils.equalsAny(msgType.name(), MsgTypeEnum.TEXT.name(), MsgTypeEnum.VOICE.name())) {
            if (StringUtils.isNotBlank(getHandleResultByKeyMap())
                || StringUtils.isNotBlank(getHandleResultByKeyList())) {
                return handleWeChatMessageResult;
            }
        }
        return null;
    }

    public String moneyHandKeyWord() {
        if (StringUtils.equalsAny(MsgTypeEnum.IMAGE.name())) {
            if (StringUtils.isNotBlank(getHandleResultByKeyMap())
                    || StringUtils.isNotBlank(getHandleResultByKeyList())) {
                return handleWeChatMessageResult;
            }
        }
        return null;
    }

    private String getHandleResultByKeyMap() {
        if (StringUtils
            .isNotBlank(WeChatUtil.WeChatKeyWordMap.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + content))) {
            handleWeChatMessageResult =
                WeChatUtil.WeChatKeyWordMap.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + content);
        }
        return handleWeChatMessageResult;
    }

    private String getHandleResultByKeyList() {
        WeChatUtil.FuzzyMatchingkeyWord fuzzyMatchingkeyWord = FuzzyMatchingList.stream()
            .filter(x -> x.getKeyType().equals(x.getKeyType())
                && x.getBelonger().equals(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get())
                && content.contains(x.getKeyWord()))
            .findAny().orElse(null);
        if (Objects.nonNull(fuzzyMatchingkeyWord)) {
            handleWeChatMessageResult = fuzzyMatchingkeyWord.getFuzzyMatchingResult();
        }
        return handleWeChatMessageResult;
    }

    public String errorCorrection() {
        if (content.length() > 2) {
            List<MenuDTO> menuDTOList = menuList.stream().filter(x -> x.getFood().contains(content.substring(0, 2)))
                .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(menuDTOList) && content.length() >= 3) {
                menuDTOList =
                    menuList.stream().filter(x -> x.getFood().contains(content.substring(content.length() - 2)))
                        .collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(menuDTOList)) {
                for (MenuDTO menuDTO : menuDTOList) {
                    handleWeChatMessageResult += menuDTO.getFood() + ";\n";
                }
            }
        }
        return StringUtils.isNotBlank(handleWeChatMessageResult) ? "查询菜谱错误,请确认是否有该菜谱,提示:" + handleWeChatMessageResult
            : handleWeChatMessageResult;
    }

    public String handleCategory() {
        StringBuilder handleCategoryResult = new StringBuilder();
        List<MenuDTO> menuDTOList;
        if (WeChatUtil.CATEGORY_MAP.containsKey(content)) {
            Integer beginNum = WeChatUtil.CATEGORY_MAP.get(content);
            menuDTOList = menuList.stream().filter(x -> content.substring(0, 1).equals(x.getCategory()))
                .collect(Collectors.toList());
            menuDTOList = menuDTOList.subList(beginNum * 30, Math.min((beginNum + 1) * 30, menuDTOList.size()));
            if (Category.COMMON.getValue().equals(content)) {
                handleCategoryResult.append("(请输入【普1】获取剩余的菜谱)\n");
            } else if (Category.PREMIUM.getValue().equals(content)) {
                handleCategoryResult.append("(请输入【精1】【精2】【精3】【精4】获取剩余的菜谱)\n");
            } else if (Category.SPECIAL.getValue().equals(content)) {
                handleCategoryResult.append("(请输入【特1】获取剩余的菜谱)\n");
            }
            for (int i = 0; i < menuDTOList.size(); i++) {
                MenuDTO menuDTO = menuDTOList.get(i);
                handleCategoryResult.append("特".equals(content) ? menuDTO.getFood() : (i + 1) + ":"
                    + menuDTO.getRawMaterial() + " " + menuDTO.getBelongUser() + " 好感度:" + menuDTO.getMaxNum() + "\n");
            }
        } else if (content.length() > 1 && WeChatUtil.BELONG_USER_LIST.contains(content.substring(0, 2))
            && !content.contains("+") && !content.contains("不要")) {
            if (!content.contains(" ")) {
                // 想查询菜谱归属者
                menuDTOList = menuList.stream().filter(x -> content.substring(0, 2).equals(x.getBelongUser()))
                    .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(menuDTOList)) {
                    Integer ceil = 0;
                    if (content.length() == 2) {
                        ceil = menuDTOList.size() % 15;
                        if (ceil == 0) {
                            ceil = menuDTOList.size() / 15;
                        } else {
                            ceil = menuDTOList.size() / 15 + 1;
                        }
                        String enterWords = "请输入:";
                        for (int i = 0; i < ceil - 1; i++) {
                            enterWords = enterWords + "【" + content + (i + 1) + "】 ";
                        }
                        enterWords = enterWords + "来获取剩余的菜谱。\n";
                        handleCategoryResult.append(content).append("一共有").append(menuDTOList.size()).append("种菜,需分")
                            .append(ceil).append("次发送,").append(enterWords);
                    }
                    if (content.length() == 2) {
                        content = content + 0;
                    }
                    try {
                        Integer num = Integer.valueOf(content.substring(2));
                        if (num * 15 >= menuDTOList.size()) {
                            handleCategoryResult.append(content.substring(0, 2)).append("没有更多菜谱了~~~");
                        } else {
                            List<MenuDTO> subList =
                                menuDTOList.subList(num * 15, (Math.min((num + 1) * 15, menuDTOList.size())));
                            int index = 1;
                            for (MenuDTO menuDTO : subList) {
                                handleCategoryResult.append(index).append("、").append(CommonUtil.conactMenu(menuDTO));
                                index++;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("handleCategory.content:{}", content, e);
                        handleCategoryResult = new StringBuilder("查询菜谱错误,请检查输入的是否正确。");
                    }
                }
            } else {
                String[] contentArray = content.split(" ");
                menuDTOList = menuList.stream()
                    .filter(x -> contentArray[0].equals(x.getBelongUser()) && contentArray[1].equals(x.getCategory()))
                    .collect(Collectors.toList());
                for (int i = 0; i < menuDTOList.size(); i++) {
                    MenuDTO menuDTO = menuDTOList.get(i);
                    handleCategoryResult.append(i + 1).append("、").append(menuDTO.getFood()).append(":")
                        .append(menuDTO.getRawMaterial()).append(" 好感度:").append(menuDTO.getMaxNum()).append("\n");
                }
            }
        }
        return handleCategoryResult.toString();
    }

    public String handleFishText() {
        StringBuilder handleFishTextResult = new StringBuilder();
        List<GameFishDTO> gameFishDTOList = Lists.newArrayList();
        if (WeChatUtil.FISH_COLOR_MAP.containsKey(content) || WeChatUtil.FISH_MAP.containsKey(content)) {
            GameFishDTO gameFishDTO = WeChatUtil.FISH_MAP.get(content);
            List<GameFishDTO> gameFishVOList = WeChatUtil.FISH_COLOR_MAP.get(content);
            if (CollectionUtils.isNotEmpty(gameFishVOList)) {
                gameFishDTOList.addAll(gameFishVOList);
            } else {
                gameFishDTOList.add(gameFishDTO);
            }
        } else if (WeChatUtil.FISH_BAIT_LIST.contains(content)) {
            for (GameFishDTO gameFishDTO : WeChatUtil.FISH_LIST) {
                if (gameFishDTO.getBait().contains(content)) {
                    gameFishDTOList.add(gameFishDTO);
                }
            }
        } else if (WeChatUtil.FISH_ADDRESS_LIST.contains(content)) {
            for (GameFishDTO gameFishVO : WeChatUtil.FISH_LIST) {
                if (gameFishVO.getFishAddress().equals(content)) {
                    gameFishDTOList.add(gameFishVO);
                }
            }
        }
        for (int i = 0; i < gameFishDTOList.size(); i++) {
            handleFishTextResult.append(i + 1).append("、").append(CommonUtil.conactFish(gameFishDTOList.get(i)));
        }
        return handleFishTextResult.toString();
    }

    public String handleCostPerformance() {
        if (!WeChatUtil.BELONG_USER_COSTPERFORMANCE_LIST.contains(content)) {
            return null;
        }
        String handleCostPerformanceResult = "";
        if (!StringUtils.equalsAny(content, "性价比", "坑比")) {
            menuList = menuList.stream()
                .filter(
                    x -> StringUtils.isNotBlank(x.getBelongUser()) && x.getBelongUser().equals(content.substring(0, 2)))
                .collect(Collectors.toList());
        }
        if (content.contains("坑比")) {
            List<MenuDTO> list = new ArrayList<>();
            for (MenuDTO x : menuList) {
                if (Objects.nonNull(x.getCostPerformance())) {
                    list.add(x);
                }
            }
            list.sort(Comparator.comparing(MenuDTO::getCostPerformance));
            menuList = list;
        } else {
            menuList = menuList.stream().filter(x -> Objects.nonNull(x.getCostPerformance()))
                .sorted(Comparator.comparing(MenuDTO::getCostPerformance).reversed()).collect(Collectors.toList());
        }
        for (int i = 0; i < 10; i++) {
            handleCostPerformanceResult +=
                (i + 1) + "、" + CommonUtil.conactMenuOneUserWithCostPerformance(menuList.get(i)) + "\n";
        }
        return handleCostPerformanceResult;
    }

    public String handleSpecialWord() {
        StringBuilder handleSpecialWordResult = new StringBuilder();
        List<FoodDTO> foodDTOList = Lists.newArrayList(WeChatUtil.FOOD_LIST.stream()
            .filter(x -> ThreadLocalHolder.BELONGER_THREAD_LOCAL.get().equals(x.getBelonger()))
            .collect(Collectors.toList()));
        if ("菜谱".equals(content)) {
            handleSpecialWordResult = new StringBuilder(
                "目前盛世芳华共有[" + foodDTOList.size() + "]种菜名(因微信消息长度限制,菜谱需要分两批发送,请发送" + "[菜谱1]、[菜谱2]获取剩下的菜谱),前[100]种如下:");
            for (int i = 0; i < 100; i++) {
                handleSpecialWordResult.append(foodDTOList.get(i).getFoodName()).append("、");
            }
        } else if ("菜谱1".equals(content)) {
            handleSpecialWordResult = new StringBuilder("目前盛世芳华共有[" + foodDTOList.size() + "]种菜名,剩下的如下:");
            for (int i = 100; i < 200; i++) {
                handleSpecialWordResult.append(foodDTOList.get(i).getFoodName()).append("、");
            }
        } else if ("菜谱2".equals(content)) {
            handleSpecialWordResult = new StringBuilder("如下:");
            for (int i = 200; i < foodDTOList.size(); i++) {
                handleSpecialWordResult.append(foodDTOList.get(i).getFoodName()).append("、");
            }
        }
        if (StringUtils.isNotBlank(handleSpecialWordResult.toString())) {
            handleSpecialWordResult =
                new StringBuilder(handleSpecialWordResult.substring(0, handleSpecialWordResult.length() - 1));
            return handleSpecialWordResult.toString();
        }
        String handleSpecialMaterialWordResult = handleSpecialMaterialWord();
        if (StringUtils.isNotBlank(handleSpecialMaterialWordResult)) {
            return handleSpecialMaterialWordResult;
        }
        String handleFishKeyWordResult = handleFishKeyWord();
        if (StringUtils.isNotBlank(handleFishKeyWordResult)) {
            return handleFishKeyWordResult;
        }
        if (content.contains("香谱")) {
            Object[] fraganceMenuArray =
                WeChatUtil.FRAGRANCE_MENU_SET_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()).toArray();
            for (int i = 0; i < fraganceMenuArray.length; i++) {
                handleFishKeyWordResult += (i + 1) + "、" + ((MenuDTO)fraganceMenuArray[i]).getFood() + ":"
                    + ((MenuDTO)fraganceMenuArray[i]).getRawMaterial() + ";\n";
            }
            return handleFishKeyWordResult;
        } else if (content.startsWith("独特")) {
            content = content.replaceAll(",", " ");
            String[] contentArray = content.split(" ");
            List<MaterialDTO> materialDTOList = Lists.newArrayList(WeChatUtil.MATERIAL_LIST);
            handleSpecialWordResult.append("可制作:");
            int num = 0;
            for (int i = 0; i < materialDTOList.size(); i++) {
                MaterialDTO materialDTO = materialDTOList.get(i);
                List<MaterialDTO> firstList = materialDTOList;
                List<MaterialDTO> secondList = materialDTOList;
                List<MenuDTO> menuDTOList;
                if ((contentArray.length >= 2)) {
                    firstList = materialDTOList.stream().filter(x -> contentArray[1].equals(x.getMaterialName()))
                        .collect(Collectors.toList());
                    menuDTOList = menuList.stream().filter(x -> contentArray[1].equals(x.getRawMaterial()))
                        .collect(Collectors.toList());
                } else {
                    menuDTOList =
                        menuList.stream().filter(x -> materialDTO.getMaterialName().equals(x.getRawMaterial()))
                            .collect(Collectors.toList());
                }
                if (contentArray.length == 3) {
                    menuDTOList = menuDTOList.stream().filter(x -> x.getRawMaterial().contains(contentArray[2]))
                        .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(menuDTOList)) {
                        handleSpecialWordResult.append(contentArray[1]).append("+").append(contentArray[2])
                            .append("\n");
                        for (int d = 0; d < 10; d++) {
                            if (!contentArray[1].equals(materialDTOList.get(d).getMaterialName())
                                && !contentArray[2].equals(materialDTOList.get(d).getMaterialName())) {
                                handleSpecialWordResult.append(contentArray[1]).append("+").append(contentArray[2])
                                    .append("+").append(materialDTOList.get(d).getMaterialName()).append("\n");
                            }
                        }
                        return handleSpecialWordResult.toString();
                    } else {
                        int nums = 0;
                        for (int m = 0; m < materialDTOList.size(); m++) {
                            String name = materialDTOList.get(m).getMaterialName();
                            if (CollectionUtils.isEmpty(materialDTOList.stream()
                                .filter(x -> x.getMaterialName().contains(name)).collect(Collectors.toList()))) {
                                nums++;
                                handleSpecialWordResult.append(contentArray[1]).append("+").append(contentArray[2])
                                    .append("+").append(materialDTOList.get(m).getMaterialName()).append("\n");
                                if (nums > 9) {
                                    break;
                                }
                            }
                        }
                        return handleSpecialWordResult.toString();
                    }
                }
                if (contentArray.length == 4) {
                    menuDTOList = menuDTOList.stream()
                        .filter(x -> x.getRawMaterial().contains(contentArray[1])
                            && x.getRawMaterial().contains(contentArray[2])
                            && x.getRawMaterial().contains(contentArray[3]))
                        .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(menuDTOList)) {
                        handleSpecialWordResult = new StringBuilder(
                            "可制作:" + contentArray[1] + "+" + contentArray[2] + "+" + contentArray[3] + "\n");
                    } else {
                        handleSpecialWordResult =
                            new StringBuilder(menuDTOList.get(0).getFood() + ":" + menuDTOList.get(0).getRawMaterial()
                                + " " + menuDTOList.get(0).getBelongUser() + " " + menuDTOList.get(0).getMaxNum());
                    }
                    return handleSpecialWordResult.toString();
                } else if (contentArray.length > 4) {
                    return "独特的菜肴输入格式有问题:请参考【独特的菜肴 豆腐 鸡蛋】";
                }
                if (CollectionUtils.isEmpty(menuDTOList)) {
                    handleSpecialWordResult.append(contentArray[1]).append("+").append(contentArray[2]).append("\n");
                    for (int d = 0; d < 10; d++) {
                        if (!contentArray[1].equals(materialDTOList.get(d).getMaterialName())
                            && !contentArray[2].equals(materialDTOList.get(d).getMaterialName())) {
                            handleSpecialWordResult.append(contentArray[1]).append("+").append(contentArray[2])
                                .append("+").append(materialDTOList.get(d).getMaterialName()).append("\n");
                        }
                    }
                    return handleSpecialWordResult.toString();
                }
                for (MaterialDTO dto : firstList) {
                    if (materialDTOList.get(i).getMaterialName().equals(dto.getMaterialName())) {
                        continue;
                    }
                    boolean findsecond = false;
                    for (MenuDTO menuDTO : menuDTOList) {
                        if (menuDTO.getRawMaterial().contains(dto.getMaterialName())) {
                            findsecond = true;
                            // 说明第二道食材是可以和第一道食材组成菜谱的。开始判断第三道食材
                            for (MaterialDTO value : secondList) {
                                if (materialDTOList.get(i).getMaterialName().equals(value.getMaterialName())
                                    || dto.getMaterialName().equals(value.getMaterialName())) {
                                    continue;
                                }
                                boolean findthird = false;
                                for (MenuDTO menuThird : menuDTOList) {
                                    if (menuThird.getRawMaterial().contains(value.getMaterialName())) {
                                        findthird = true;
                                    }
                                }
                                if (!findthird) {
                                    num++;
                                    handleSpecialWordResult.append(materialDTOList.get(i).getMaterialName()).append("+")
                                        .append(dto.getMaterialName()).append("+").append(value.getMaterialName())
                                        .append(";\n");
                                    if (num > 9) {
                                        return handleSpecialWordResult.toString();
                                    }
                                }
                            }
                        }
                    }
                    if (!findsecond) {
                        num++;
                        handleSpecialWordResult.append(materialDTOList.get(i).getMaterialName()).append("+")
                            .append(dto.getMaterialName()).append(";\n");
                        if (num > 9) {
                            return handleSpecialWordResult.toString();
                        }
                    }
                }
            }
        } else if (content.contains("不要")) {
            try {
                String[] contentArray = content.split("不要");
                List<MenuDTO> menuDTOList = menuList.stream().filter(x -> StringUtils.isNotBlank(x.getBelongUser()))
                    .collect(Collectors.toList());
                if (contentArray.length == 2) {
                    if (StringUtils.isNotBlank(contentArray[0])) {
                        if (contentArray[0].contains("的")) {
                            String[] splitArray = contentArray[0].split("的");
                            if (StringUtils.isNotBlank(splitArray[0])) {
                                menuDTOList = menuDTOList.stream().filter(x -> splitArray[0].equals(x.getBelongUser()))
                                    .collect(Collectors.toList());
                            }
                            if (StringUtils.isNotBlank(splitArray[1])) {
                                menuDTOList = menuDTOList.stream().filter(x -> splitArray[1].equals(x.getCategory()))
                                    .collect(Collectors.toList());
                            }
                        } else {
                            if (WeChatUtil.BELONG_USER_LIST.contains(contentArray[0])) {
                                menuDTOList =
                                    menuDTOList.stream().filter(x -> contentArray[0].equals(x.getBelongUser()))
                                        .collect(Collectors.toList());
                            } else {
                                menuDTOList = menuDTOList.stream().filter(x -> contentArray[0].equals(x.getCategory()))
                                    .collect(Collectors.toList());
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(contentArray[1])) {
                        menuDTOList = menuDTOList.stream().filter(x -> !x.getRawMaterial().contains(contentArray[1]))
                            .collect(Collectors.toList());
                    }
                    if (menuDTOList.size() > 20) {
                        menuDTOList = menuDTOList.subList(0, 20);
                    }
                    for (int i = 0; i < menuDTOList.size(); i++) {
                        MenuDTO menuDTO = menuDTOList.get(i);
                        handleSpecialWordResult.append(i + 1).append("、")
                            .append(CommonUtil.conactMenuOneUserWithCost(menuDTO)).append("\n");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("handleSpecialWord.content:{}", JSON.toJSONString(content), e);
                handleSpecialWordResult = new StringBuilder("输入格式错误,请参考【皇帝的特不要烧酒】或者【特不要烧酒】或者【皇帝不要烧酒】");
            }
            return handleSpecialWordResult.toString();
        }
        return null;
    }

    public String handleFishKeyWord() {
        StringBuilder handleFishKeyWordResult = new StringBuilder();
        if (WeChatUtil.FISH_KEY_WORDS.contains(content)) {
            String fishKeyWord = content.substring(0, 4);
            List<GameFishDTO> gameFishDTOList =
                WeChatUtil.FISH_LIST.stream().filter(x -> x.getFish().equals(fishKeyWord)).collect(Collectors.toList());
            for (int i = 0; i < gameFishDTOList.size(); i++) {
                handleFishKeyWordResult.append(i + 1).append("、").append(CommonUtil.conactFish(gameFishDTOList.get(i)));
            }
        }
        return handleFishKeyWordResult.toString();
    }

    public String handleSpecialMaterialWord() {
        StringBuilder handleSpecialMaterialWordResult = new StringBuilder();
        if ("食材".equals(content)) {
            List<MaterialDTO> materialDTOList = Lists.newArrayList(WeChatUtil.MATERIAL_LIST);
            handleSpecialMaterialWordResult = new StringBuilder("目前盛世芳华共有[" + materialDTOList.size() + "]种食材,如下:");
            for (MaterialDTO materialDTO : materialDTOList) {
                handleSpecialMaterialWordResult.append(materialDTO.getMaterialName()).append("、");
            }
            handleSpecialMaterialWordResult = new StringBuilder(
                handleSpecialMaterialWordResult.substring(0, handleSpecialMaterialWordResult.length() - 1));
        }
        return handleSpecialMaterialWordResult.toString();
    }

    public String handleBelongOrNumText() {
        String handleBelongOrNumTextResult = "";
        String belongUser;
        if (content.length() >= 2 && WeChatUtil.BELONG_USER_LIST.contains(content.substring(0, 2))) {
            if (content.contains("+")) {
                String[] contentArray = content.split("\\+");
                // 想查询指定人物指定材料的菜谱
                List<MenuDTO> menuDTOList = menuList.stream()
                    .filter(
                        x -> contentArray[0].equals(x.getBelongUser()) && x.getRawMaterial().contains(contentArray[1]))
                    .collect(Collectors.toList());
                if (menuDTOList.size() == 0) {
                    return contentArray[0] + "表示:我并不吃:" + contentArray[1];
                }
                for (int i = 0; i < menuDTOList.size(); i++) {
                    MenuDTO menuDTO = menuDTOList.get(i);
                    handleBelongOrNumTextResult += (i + 1) + "、" + CommonUtil.conactMenuOneUserWithCost(menuDTO) + "\n";
                }
            } else if (content.length() > 3 && !content.contains("不要")) {
                try {
                    belongUser = content.substring(0, 2);
                    content = content.replace(" ", "").substring(2);
                    // 强转为数字
                    Double value = (double)Integer.valueOf(content) / 99;
                    Integer num = CommonUtil.convertDoubleToInt(Math.ceil(value));
                    if (num <= 92) {
                        handleBelongOrNumTextResult = "这个分数,大人你怕是当不了日榜第一吧,给王爷做99个蛋炒饭就行了。。";
                    } else if (num > 593) {
                        handleBelongOrNumTextResult = "这么高的分数,大人给王爷做99份麻辣鱼也实现不了啊~~~~大人在难为小的。。";
                    } else if (num > 380) {
                        String username;
                        String foodname;
                        if (num > 462) {
                            username = "王爷";
                            foodname = "麻辣鱼";
                        } else if (num > 386) {
                            username = "皇帝";
                            foodname = "佛跳墙";
                        } else {
                            username = "皇帝";
                            foodname = "四色鱼肉饺";
                        }
                        handleBelongOrNumTextResult = "这么高的分数,大人只有给【" + username + "】做【99】份【" + foodname
                            + "】才能实现了啊~~~~99份" + foodname + ",小的只能说大人太壕了";
                    } else {
                        MenuDTO menu = null;
                        for (MenuDTO menuDTO : menuList) {
                            if (Objects.nonNull(menuDTO.getMaxNum()) && menuDTO.getMaxNum() >= num
                                && belongUser.equals(menuDTO.getBelongUser()) && Objects.nonNull(menuDTO.getPrice())) {
                                if (Objects.isNull(menu) || menu.getPrice() > menuDTO.getPrice()) {
                                    menu = menuDTO;
                                }
                            }
                        }
                        if (Objects.isNull(menu)) {
                            handleBelongOrNumTextResult = "您输入的内容,小的解析失败,请联系管理员进行处理~~";
                        } else {
                            int maxnum = menu.getMaxNum() * 99;
                            handleBelongOrNumTextResult = "对于【" + content + "】这个分数,小的推荐大人给【" + menu.getBelongUser()
                                + "】做99份【" + menu.getFood() + ":" + menu.getRawMaterial() + ";" + menu.getMaxNum()
                                + CommonUtil.getCostPerformanceWords(menu.getCostPerformance()) + "】,可以实现【" + maxnum
                                + "】分呢~~~";
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("handleBelongOrNumText.e:", e);
                }

            }
        }
        return handleBelongOrNumTextResult;
    }

}
