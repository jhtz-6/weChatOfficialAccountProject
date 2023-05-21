package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.myf.wechatofficialaccountproject.application.dto.AccompanyDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.GameFishDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 12:53
 * @Description: 通用工具类
 */
public final class CommonUtil {

    private static CommonUtil commonUtil = null;
    static {
        if (Objects.isNull(commonUtil)) {
            commonUtil = new CommonUtil();
        }
    }

    private CommonUtil() {}

    public static Map<String, String> convertServerletInputStreamToMap(ServletInputStream servletInputStream)
        throws Exception {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = Maps.newHashMap();
        Document document = saxReader.read(servletInputStream);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    public static void copyPropertiesWithNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static void copyPropertiesExceptNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static Date getDateTimeByTimeStamp(String timeStamp) {
        return new Date(new Long(timeStamp) * 1000);
    }

    public static Map<String, String> CONVERTNUMBERCNTOEN_MAP = new HashMap();
    static {
        CONVERTNUMBERCNTOEN_MAP.put("一", "1");
        CONVERTNUMBERCNTOEN_MAP.put("二", "2");
        CONVERTNUMBERCNTOEN_MAP.put("三", "3");
        CONVERTNUMBERCNTOEN_MAP.put("四", "4");
        CONVERTNUMBERCNTOEN_MAP.put("五", "5");
        CONVERTNUMBERCNTOEN_MAP.put("六", "6");
        CONVERTNUMBERCNTOEN_MAP.put("七", "7");
        CONVERTNUMBERCNTOEN_MAP.put("八", "8");
        CONVERTNUMBERCNTOEN_MAP.put("九", "9");
        CONVERTNUMBERCNTOEN_MAP.put("十", "10");
    }

    public static String getSimpleCostPerformanceWords(Double cost_performance) {
        if (Objects.isNull(cost_performance)) {
            return "";
        } else {
            String result = cost_performance + "(";
            if (cost_performance >= 10.0D) {
                result = result + "★★★★★";
            } else if (cost_performance >= 7.6D) {
                result = result + "★★★★";
            } else if (cost_performance >= 5.6D) {
                result = result + "★★★";
            } else if (cost_performance >= 3.6D) {
                result = result + "★★";
            } else {
                result = result + "★";
            }

            result = result + ")";
            return result;
        }
    }

    public static String getCostPerformanceWords(Double cost_performance) {
        if (Objects.isNull(cost_performance)) {
            return "";
        }
        String result = ";" + cost_performance + "(";
        if (cost_performance >= 5.567) {
            result = result + "性价比★★★★★";
        } else if (cost_performance >= 4.983) {
            result = result + "性价比★★★★";
        } else if (cost_performance >= 3.563) {
            result = result + "性价比★★★";
        } else if (cost_performance >= 3.23) {
            result = result + "性价比★★";
        } else {
            result = result + "性价比★";
        }
        result = result + ")";
        return result;

    }

    public static String conactMenuOneUserWithCost(MenuDTO menuDTO) {
        String menuResult = menuDTO.getFood() + ": " + menuDTO.getRawMaterial();
        if (Objects.nonNull(menuDTO.getMinNum()) && Objects.nonNull(menuDTO.getMaxNum())) {
            menuResult = menuResult + "; " + menuDTO.getMaxNum();
        }
        if (StringUtils.isNotBlank(menuDTO.getCategory())) {
            menuResult = menuResult + "; " + menuDTO.getCategory();
        }
        if (StringUtils.isNotBlank(menuDTO.getBelongUser())) {
            menuResult = menuResult + "; " + menuDTO.getBelongUser();
        }
        if (Objects.nonNull(menuDTO.getCostPerformance()) && Objects.nonNull(menuDTO.getPrice())) {
            menuResult =
                menuResult + ";" + menuDTO.getPrice() + "元宝" + getCostPerformanceWords(menuDTO.getCostPerformance());
        }
        return menuResult;
    }

    public static String conactOnlyOneMenu(MenuDTO menuDTO) {
        String menuResult = menuDTO.getFood() + ": " + menuDTO.getRawMaterial();
        if (Objects.nonNull(menuDTO.getMinNum()) && Objects.nonNull(menuDTO.getMaxNum())) {
            menuResult = menuResult + "; " + menuDTO.getMinNum() + "~" + menuDTO.getMaxNum();
        }
        if (StringUtils.isNotBlank(menuDTO.getCategory())) {
            menuResult = menuResult + "; " + menuDTO.getCategory();
        }
        if (StringUtils.isNotBlank(menuDTO.getBelongUser())) {
            menuResult = menuResult + "; " + menuDTO.getBelongUser();
        }
        return menuResult;
    }

    public static String conactMoreThanOneMenu(MenuDTO menu) {
        String menuResult = menu.getFood() + ": " + menu.getRawMaterial();
        if (Objects.nonNull(menu.getMinNum()) && Objects.nonNull(menu.getMaxNum())) {
            menuResult += "; " + menu.getMinNum() + "~" + menu.getMaxNum();
        }
        if (StringUtils.isNotBlank(menu.getCategory())) {
            menuResult += "; " + menu.getCategory();
        }
        if (StringUtils.isNotBlank(menu.getBelongUser())) {
            menuResult += "; " + menu.getBelongUser();
        }
        menuResult += "。\n";
        return menuResult;
    }

    public static int convertDoubleToInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    public static String conactFish(GameFishDTO gameFishDTO) {
        String gameFishResult = "";
        if (Objects.isNull(gameFishDTO)) {
            return gameFishResult;
        }
        gameFishResult += gameFishDTO.toString();
        return gameFishResult;
    }

    public static String conactAccompany(AccompanyDTO accompanyDTO) {
        if (Objects.isNull(accompanyDTO)) {
            return null;
        }
        return accompanyDTO.toString();
    }

    public static String conactMenuOneUserWithCostPerformance(MenuDTO menuDTO) {
        String menuResult = menuDTO.getFood() + ": " + menuDTO.getRawMaterial();
        if (Objects.nonNull(menuDTO.getMinNum()) && Objects.nonNull(menuDTO.getMaxNum())) {
            menuResult += "; " + menuDTO.getMaxNum();
        }
        if (StringUtils.isNotBlank(menuDTO.getCategory())) {
            menuResult += "; " + menuDTO.getCategory();
        }
        if (StringUtils.isNotBlank(menuDTO.getBelongUser())) {
            menuResult += "; " + menuDTO.getBelongUser();
        }
        if (Objects.nonNull(menuDTO.getCostPerformance()) && Objects.nonNull(menuDTO.getPrice())) {
            menuResult += ";" + menuDTO.getPrice() + "元宝" + ";性价比:" + menuDTO.getCostPerformance();
        }
        return menuResult;
    }

    public static String conactMenu(MenuDTO menuDTO) {
        String menuResult = menuDTO.getFood() + ": " + menuDTO.getRawMaterial();
        if (Objects.nonNull(menuDTO.getMinNum()) && Objects.nonNull(menuDTO.getMaxNum())) {
            menuResult += "; " + menuDTO.getMinNum() + "~" + menuDTO.getMaxNum();
        }
        if (StringUtils.isNotBlank(menuDTO.getCategory())) {
            menuResult += "; " + menuDTO.getCategory();
        }
        if (StringUtils.isNotBlank(menuDTO.getBelongUser())) {
            menuResult += "; " + menuDTO.getBelongUser();
        }
        menuResult += "。\n";
        return menuResult;
    }

    public static String replaceCharacterByMap(Map<String, String> replaceMap, String info) {
        if (StringUtils.isBlank(info)) {
            return null;
        }
        for (String key : replaceMap.keySet()) {
            info = info.replaceAll(key, replaceMap.get(key));
        }
        return info;
    }

    public static String recommendMenuByContent(String content) {
        String result = "";
        try {
            content = content.replaceAll(" ", ";");
            String copycontent = content;
            Integer subStr;
            if (content.startsWith(WeChatUtil.RECOMMENDED_MENU)) {
                if (':' == content.charAt(4) || '：' == content.charAt(4)) {
                    subStr = 5;
                } else {
                    subStr = 4;
                }
            } else if (content.startsWith("推荐性价比菜谱") || content.startsWith("推荐最高分菜谱")) {
                if (':' == content.charAt(7) || '：' == content.charAt(7)) {
                    subStr = 8;
                } else {
                    subStr = 7;
                }
            } else {
                if (':' == content.charAt(9) || '：' == content.charAt(9)) {
                    subStr = 10;
                } else {
                    subStr = 9;
                }
            }
            copycontent = copycontent.substring(subStr);
            Map<String, Integer> inputMap = new HashMap<>();
            String[] split = copycontent.split(";");
            for (String str : split) {
                if (StringUtils.isBlank(str)) {
                    continue;
                }
                String[] strings = str.split("个");
                inputMap.put(strings[1].trim(),
                    Integer.valueOf(StringUtils.isBlank(CommonUtil.CONVERTNUMBERCNTOEN_MAP.get(strings[0])) ? strings[0]
                        : CommonUtil.CONVERTNUMBERCNTOEN_MAP.get(strings[0])));
            }

            String bteweenResult = "";
            Integer maxNum = 0;
            List<MenuDTO> menuDTOList =
                Lists.newArrayList(WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()));
            if (content.startsWith("推荐王爷性价比菜谱") || content.startsWith("推荐皇帝性价比菜谱") || content.startsWith("推荐贵妃性价比菜谱")
                || content.startsWith("推荐太医性价比菜谱")) {
                String belongUser = content.substring(2, 4);
                menuDTOList = menuDTOList.stream()
                    .filter(x -> belongUser.equals(x.getBelongUser()) && Objects.nonNull(x.getCostPerformance()))
                    .sorted(Comparator.comparing(MenuDTO::getCostPerformance).reversed()).collect(Collectors.toList());
                result = result + content.substring(0, 9) + "如下";
            } else if (content.startsWith("推荐王爷最高分菜谱") || content.startsWith("推荐皇帝最高分菜谱")
                || content.startsWith("推荐贵妃最高分菜谱") || content.startsWith("推荐太医最高分菜谱")) {
                String belongUser = content.substring(2, 4);
                menuDTOList = menuDTOList.stream()
                    .filter(x -> belongUser.equals(x.getBelongUser()) && Objects.nonNull(x.getMaxNum()))
                    .sorted(Comparator.comparing(MenuDTO::getMaxNum).reversed()).collect(Collectors.toList());
                result = result + content.substring(0, 9) + "如下";
            } else if (content.contains("性价比")) {
                menuDTOList = menuDTOList.stream().filter(x -> Objects.nonNull(x.getCostPerformance()))
                    .sorted(Comparator.comparing(MenuDTO::getCostPerformance).reversed()).collect(Collectors.toList());
                result = result + "推荐性价比菜谱如下";
            } else {
                menuDTOList = menuDTOList.stream().filter(x -> Objects.nonNull(x.getMaxNum()))
                    .sorted(Comparator.comparing(MenuDTO::getMaxNum).reversed()).collect(Collectors.toList());
                result = result + "推荐最高分菜谱如下";
            }
            Map<String, Integer> costMap = new HashMap<>();
            Integer num = 0;
            for (MenuDTO menuDTO : menuDTOList) {
                String[] materialArray = menuDTO.getRawMaterial().replaceAll(" ", "").split("\\+");
                Integer minNum;
                Integer firstRawNum = inputMap.get(materialArray[0].trim());
                Integer secondRawNum = inputMap.get(materialArray[1].trim());
                if (Objects.isNull(secondRawNum) || Objects.isNull(firstRawNum) || secondRawNum == 0
                    || firstRawNum == 0) {
                    continue;
                }
                if (firstRawNum < secondRawNum) {
                    minNum = firstRawNum;
                } else {
                    minNum = secondRawNum;
                }
                if (materialArray.length > 2) {
                    Integer thirdRwaNum = inputMap.get(materialArray[2]);
                    if (Objects.isNull(thirdRwaNum)) {
                        continue;
                    }
                    if (minNum > thirdRwaNum) {
                        minNum = thirdRwaNum;
                    }
                    if (minNum == 0) {
                        continue;
                    }
                    inputMap.put(materialArray[2], thirdRwaNum - minNum);
                    if (Objects.isNull(costMap.get(materialArray[2]))) {
                        costMap.put(materialArray[2], minNum);
                    } else {
                        costMap.put(materialArray[2], minNum + costMap.get(materialArray[2]));
                    }
                }
                if (minNum == 0) {
                    continue;
                }
                num = num + minNum;
                if (num >= 99) {
                    minNum = minNum - (num - 99);
                    num = 99;
                }
                maxNum = maxNum + menuDTO.getMaxNum() * minNum;
                inputMap.put(materialArray[0], firstRawNum - minNum);
                inputMap.put(materialArray[1], secondRawNum - minNum);
                if (Objects.isNull(costMap.get(materialArray[0]))) {
                    costMap.put(materialArray[0], minNum);
                } else {
                    costMap.put(materialArray[0], minNum + costMap.get(materialArray[0]));
                }
                if (Objects.isNull(costMap.get(materialArray[1]))) {
                    costMap.put(materialArray[1], minNum);
                } else {
                    costMap.put(materialArray[1], minNum + costMap.get(materialArray[1]));
                }
                bteweenResult = bteweenResult + minNum + "个【" + menuDTO.getFood().trim() + ":"
                    + menuDTO.getRawMaterial().trim() + "】;";
                if (Objects.nonNull(menuDTO.getCostPerformance())) {
                    bteweenResult = bteweenResult + "性价比:"
                        + CommonUtil.getSimpleCostPerformanceWords(menuDTO.getCostPerformance()) + ";";
                }
                bteweenResult = bteweenResult + menuDTO.getBelongUser() + ";\n";
                if (num == 99) {
                    break;
                }
            }
            String costResult = "需消耗:";
            for (String key : costMap.keySet()) {
                if (Objects.nonNull(costMap.get(key)) && costMap.get(key) > 0) {
                    costResult = costResult + costMap.get(key) + "个" + key.trim() + ";\n";
                }
            }
            result = result + "(可实现" + maxNum + "分;" + costResult + ")" + "\n可制作:" + bteweenResult;
        } catch (Exception e) {
            result = "格式错误接收到的内容是:\n" + content + "\n使用说明:\n"
                + "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483910&idx=1&sn=362b06228bd51c419b9e968c985b3ed0&chksm=c29209f5f5e580e35bb9c77d6c5606146dda77481955f1fe48498c7f8e0e3e48072701121221#rd";
        }
        return result;
    }

    public static String getResultByPostJson(String url, String json) throws IOException {
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            BasicResponseHandler handler = new BasicResponseHandler();
            // 解决中文乱码问题
            StringEntity entity = new StringEntity(json, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost, handler);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String unicodeToUtf8(String theString) {
        if (StringUtils.isBlank(theString)) {
            return "";
        }
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char)value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    }
                    if (aChar == 'r') {
                        aChar = '\r';
                    }
                    if (aChar == 'n') {
                        aChar = '\n';
                    }
                    if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public static Boolean addValueToChatgptNumMap(String formUserName) {
        if (Objects.isNull(WeChatUtil.CHATGPT_NUM_MAP.get(formUserName))) {
            WeChatUtil.CHATGPT_NUM_MAP.put(formUserName, 1);
            return true;
        } else if (WeChatUtil.CHATGPT_NUM_MAP.get(formUserName) < WeChatUtil.CHATGPT_NUM) {
            WeChatUtil.CHATGPT_NUM_MAP.put(formUserName, WeChatUtil.CHATGPT_NUM_MAP.get(formUserName) + 1);
            return true;
        }
        return false;
    }

    private final static String PHOTO_IP = "https://110.40.208.47:8089/photo/";

    public static String getPhotoUrl() {
        Integer randomNumber = getRandomNumber(1, 10);
        return PHOTO_IP + randomNumber + ".jpg";
    }

    private static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static void main(String[] args) {
        System.out.println(getRandomNumber(1, 10));
    }

}
