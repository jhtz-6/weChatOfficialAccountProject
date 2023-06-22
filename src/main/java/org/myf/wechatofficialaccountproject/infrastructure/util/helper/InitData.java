package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.github.qcloudsms.SmsSingleSender;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.*;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AccompanyDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ConfigurationDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.UserDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SfyxEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.HandlerToChainMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil.CONFIGURATION_MAP;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 23:25
 * @Description: InitData 初始化数据
 */
@Component
public class InitData {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitData.class);

    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ConfigurationRepository configurationRepositoryl;
    @Autowired
    WechatKeyWordsRepository wechatKeyWordsRepository;
    @Autowired
    AccompanyRepository accompanyRepository;
    @Autowired
    UserRepository userRepository;

    public static SmsSingleSender SSENDER;
    public static AipOcr CLIENT;
    public static OpenAiStreamClient OPENAI_STREAM_CLIENT;
    public static OpenAiClient UNFBX_OPENAI_CLIENT;
    public static Map<String, Map<String, List<HandlerToChainMapping>>> CHAIN_TO_HANDLER_MAP = new HashMap(8);
    private static ReentrantLock UPDATE_DATA_LOCK = new ReentrantLock();
    private static ReentrantLock UPDATE_CONFIG_LOCK = new ReentrantLock();
    public static COSClient COS_CLIENT;
    public static String TENCENT_PHOTO_DOMAIN_NAME;

    @PostConstruct
    public void initStaticData() {
        updateDataToMap(null);
        updateConfigToMap();
        MaterialQueryParam materialQueryParam = new MaterialQueryParam();
        materialRepository.selectListByParam(materialQueryParam).stream().forEach(x -> {
            MaterialDTO materialDTO = new MaterialDTO();
            CommonUtil.copyPropertiesWithNull(x, materialDTO);
            WeChatUtil.MATERIAL_LIST.add(materialDTO);
        });
        WeChatUtil.MATERIAL_NAME_LIST =
            WeChatUtil.MATERIAL_LIST.stream().map(x -> x.getMaterialName()).collect(Collectors.toList());
        FoodQueryParam foodQueryParam = new FoodQueryParam();
        foodRepository.selectListByParam(foodQueryParam).stream().forEach(x -> {
            FoodDTO foodDTO = new FoodDTO();
            CommonUtil.copyPropertiesWithNull(x, foodDTO);
            foodDTO.setFoodName(foodDTO.getFoodName().trim());
            WeChatUtil.FOOD_LIST.add(foodDTO);
        });

        UserQueryParam userQueryParam = new UserQueryParam();
        List<UserDO> userDOList = userRepository.getListByParam(userQueryParam);
        userDOList.stream().forEach(x -> WeChatUtil.USER_TO_BELONGER_MAP.put(x.getLoginName(), x.getBelonger()));

    }

    public Boolean updateConfigToMap() {
        UPDATE_CONFIG_LOCK.lock();
        try {
            List<ConfigurationDO> configurationDOList =
                configurationRepositoryl.selectListByParam(new ConfigurationQueryParam());
            for (ConfigurationDO configurationDO : configurationDOList) {
                CONFIGURATION_MAP.put(configurationDO.getName(), configurationDO.getValue());
            }
            SSENDER = new SmsSingleSender(Integer.parseInt(CONFIGURATION_MAP.get(WeChatUtil.TENCENT_APPID)),
                CONFIGURATION_MAP.get(WeChatUtil.TENCENT_APPKEY));

            CLIENT = new AipOcr(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.BAIDU_APPID),
                WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.BAIDU_APPKEY),
                WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.BAIDU_SECRET_KEY));

            OPENAI_STREAM_CLIENT = OpenAiStreamClient.builder().connectTimeout(50).readTimeout(50).writeTimeout(50)
                .apiKey(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.OPENAI_APIKEY))
                .apiHost(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.OPENAI_APIHOST)).build();

            UNFBX_OPENAI_CLIENT =
                OpenAiClient.builder().apiHost(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.OPENAI_APIHOST))
                    .apiKey(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.OPENAI_APIKEY)).connectTimeout(50)
                    .readTimeout(50).writeTimeout(50).build();

            COSCredentials cred =
                new BasicCOSCredentials(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.TENCENT_COS_SECRET_ID),
                    WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.TENCENT_COS_SECRET_KEY));
            ClientConfig clientConfig =
                new ClientConfig(new Region(WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.TENCENT_COS_REGION)));
            clientConfig.setHttpProtocol(HttpProtocol.https);
            COS_CLIENT = new COSClient(cred, clientConfig);
            TENCENT_PHOTO_DOMAIN_NAME = WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.TENCENT_PHOTO_DOMAIN_NAME);

            for (SystemBelongEnum systemBelongEnum : SystemBelongEnum.values()) {
                if (CONFIGURATION_MAP.containsKey(systemBelongEnum.name())) {
                    CHAIN_TO_HANDLER_MAP = JSON.parseObject(CONFIGURATION_MAP.get(systemBelongEnum.name()), Map.class);
                }
            }
            MessageContentHandlerChain.CLASS_TO_HANDLER_MAP = new HashMap<>();
        } catch (Exception e) {
            LOGGER.error("updateConfigToMap:", e);
            return false;
        } finally {
            UPDATE_CONFIG_LOCK.unlock();
        }
        return true;
    }

    /**
     * 根据belonger来同步数据,belonger则会同步所有belonger的数据
     * 
     * @param belonger
     * @return
     */
    public Boolean updateDataToMap(SystemBelongEnum belonger) {
        UPDATE_DATA_LOCK.lock();
        try {
            // 同步menu
            MenuQueryParam menuParam = new MenuQueryParam();
            menuParam.setSfyx(SfyxEnum.YOU_XIAO.getName());
            menuParam.setBelonger(belonger);
            WeChatUtil.MENU_LIST_MAP.remove(belonger);
            WeChatUtil.FRAGRANCE_MENU_SET_MAP.remove(belonger);
            menuRepository.selectListByParam(menuParam).stream().forEach(x -> {
                MenuDTO menuDTO = new MenuDTO();
                CommonUtil.copyPropertiesWithNull(x, menuDTO);
                menuDTO.setRawMaterial(menuDTO.getRawMaterial().replaceAll("＋", "+").replaceAll(" ", ""));
                menuDTO.setFood(menuDTO.getFood().trim());
                if (WeChatUtil.MENU_LIST_MAP.containsKey(menuDTO.getBelonger())) {
                    WeChatUtil.MENU_LIST_MAP.get(menuDTO.getBelonger()).add(menuDTO);
                } else {
                    WeChatUtil.MENU_LIST_MAP.put(menuDTO.getBelonger(), Lists.newArrayList(menuDTO));
                }
                if (menuDTO.getFood().endsWith("香") || menuDTO.getFood().equals("梅花龙脑")) {
                    if (WeChatUtil.FRAGRANCE_MENU_SET_MAP.containsKey(menuDTO.getBelonger())) {
                        WeChatUtil.FRAGRANCE_MENU_SET_MAP.get(menuDTO.getBelonger()).add(menuDTO);
                    } else {
                        WeChatUtil.FRAGRANCE_MENU_SET_MAP.put(menuDTO.getBelonger(), Sets.newHashSet(menuDTO));
                    }
                }
            });
            // 同步keyWord
            WechatKeyWordQueryParam wechatKeyWordQueryParam = new WechatKeyWordQueryParam();
            wechatKeyWordQueryParam.setIsValid(BooleanEnum.TRUE);
            wechatKeyWordQueryParam.setBelonger(belonger);
            // 存入map时的key为belonger+keyName
            List<WechatKeyWordsDO> wechatKeyWordsDOList =
                wechatKeyWordsRepository.getListByParam(wechatKeyWordQueryParam);
            for (WechatKeyWordsDO wechatKeyWordsDO : wechatKeyWordsDOList) {
                if (StringUtils.isBlank(wechatKeyWordsDO.getKeyName())) {
                    continue;
                }
                WechatKeyWordsDTO wechatKeyWordsDTO = new WechatKeyWordsDTO();
                CommonUtil.copyPropertiesWithNull(wechatKeyWordsDO, wechatKeyWordsDTO);
                switch (wechatKeyWordsDTO.getKeyType()) {
                    case PRECISE:
                        WeChatUtil.WeChatKeyWordMap.put(
                            wechatKeyWordsDTO.getBelonger() + wechatKeyWordsDTO.getKeyName(),
                            wechatKeyWordsDTO.getValueContent());
                        continue;
                    case FUZZY:
                        WeChatUtil.FuzzyMatchingkeyWord fuzzyMatchingkeyWord = WeChatUtil.FuzzyMatchingList.stream()
                            .filter(x -> wechatKeyWordsDTO.getKeyName().equals(x.getKeyWord())
                                && wechatKeyWordsDTO.getKeyType().equals(x.getKeyType()))
                            .findAny().orElse(null);
                        if (Objects.nonNull(fuzzyMatchingkeyWord)) {
                            WeChatUtil.FuzzyMatchingList.remove(WeChatUtil.FuzzyMatchingList);
                        }
                        WeChatUtil.FuzzyMatchingList
                            .add(new WeChatUtil.FuzzyMatchingkeyWord(wechatKeyWordsDTO.getKeyName(),
                                wechatKeyWordsDTO.getValueContent(), wechatKeyWordsDTO.getKeyType()));
                        continue;
                    default:
                }
            }
            // 同步accompany
            AccompanyQueryParam accompanyQueryParam = new AccompanyQueryParam();
            accompanyQueryParam.setIsValid(Boolean.TRUE);
            accompanyQueryParam.setBelonger(belonger);
            List<AccompanyDO> accompanyDOList = accompanyRepository.getListByParam(accompanyQueryParam);
            for (AccompanyDO accompanyDO : accompanyDOList) {
                AccompanyDTO accompanyDTO = new AccompanyDTO();
                CommonUtil.copyPropertiesWithNull(accompanyDO, accompanyDTO);
                WeChatUtil.ACCOMPANY_MAP.put(accompanyDTO.getBelonger() + accompanyDTO.getCharacterName(),
                    accompanyDTO);
            }
        } catch (Exception e) {
            LOGGER.error("updateDataToMap.belonger:{}", belonger, e);
            if (Objects.isNull(belonger)) {
                throw new IllegalArgumentException("初始化数据失败");
            }
            return false;
        } finally {
            UPDATE_DATA_LOCK.unlock();
        }
        return true;
    }

}
