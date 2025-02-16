package org.myf.wechatofficialaccountproject.interfaces.facade;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.EventMessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.ImageMessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.TextMessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.VoiceMessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.*;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AccompanyDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ConfigurationDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.AccompanyEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.KeyTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.AccompanyQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.WechatKeyWordQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.AccompanyRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ConfigurationRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.WechatKeyWordsRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.HandlerToChainMapping;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 16:51
 * @Description: CommonUtilFacade
 */
@RestController
@RequestMapping("commonUtil")
public class CommonUtilFacade {

    @Autowired
    WechatKeyWordsRepository wechatKeyWordsRepository;
    @Autowired
    AccompanyRepository accompanyRepository;
    @Autowired
    ConfigurationRepository configurationRepository;
    @Autowired
    InitData initData;

    @GetMapping("initKeyWordsByStatic")
    public String initKeyWordsByStatic(String key, String value, KeyTypeEnum keyType, SystemBelongEnum belonger) {
        if (StringUtils.isAnyBlank(key, value)) {
            return "key/value存在为空的数据";
        }
        if (Objects.isNull(keyType) || Objects.isNull(belonger)) {
            return "keyType/belonger为空";
        }
        WechatKeyWordsDO wechatKeyWordsDO = new WechatKeyWordsDO();
        WechatKeyWordQueryParam wechatKeyWordQueryParam = new WechatKeyWordQueryParam();
        wechatKeyWordsDO.setKeyName(key);
        wechatKeyWordsDO.setKeyType(keyType);
        wechatKeyWordsDO.setBelonger(belonger);
        wechatKeyWordsDO.setValueContent(value);
        wechatKeyWordQueryParam.setKeyName(key);
        wechatKeyWordQueryParam.setKeyType(keyType);
        wechatKeyWordQueryParam.setBelonger(belonger);
        wechatKeyWordQueryParam.setValueContent(value);
        List<WechatKeyWordsDO> wechatKeyWordsDOList = wechatKeyWordsRepository.getListByParam(wechatKeyWordQueryParam);
        if (CollectionUtils.isEmpty(wechatKeyWordsDOList)) {
            wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            return "数据新增成功";
        } else if (wechatKeyWordsDOList.size() > 1) {
            return "数据更新失败,存在多条重复数据,请处理:" + JSON.toJSONString(wechatKeyWordsDOList);
        }
        wechatKeyWordsDO.setId(wechatKeyWordsDOList.get(0).getId());
        wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
        return "数据:" + JSON.toJSONString(wechatKeyWordsDOList.get(0)) + "已成功更新为:" + JSON.toJSONString(wechatKeyWordsDO);
    }

    @GetMapping("initPreciseValue")
    public String initPreciseValue() {
        for (String str : WeChatUtil.WeChatKeyWordMap.keySet()) {
            WechatKeyWordQueryParam wechatKeyWordQueryParam = new WechatKeyWordQueryParam();
            WechatKeyWordsDO wechatKeyWordsDO = new WechatKeyWordsDO();
            wechatKeyWordsDO.setKeyName(str.substring(6));
            wechatKeyWordsDO.setKeyType(KeyTypeEnum.PRECISE);
            wechatKeyWordsDO.setBelonger(SystemBelongEnum.YNSS);
            wechatKeyWordsDO.setValueContent(WeChatUtil.WeChatKeyWordMap.get(str));
            wechatKeyWordsDO.setIsValid(BooleanEnum.TRUE);
            wechatKeyWordQueryParam.setKeyName(str.substring(6));
            wechatKeyWordQueryParam.setKeyType(KeyTypeEnum.PRECISE);
            wechatKeyWordQueryParam.setBelonger(SystemBelongEnum.YNSS);
            wechatKeyWordQueryParam.setValueContent(WeChatUtil.WeChatKeyWordMap.get(str));
            List<WechatKeyWordsDO> wechatKeyWordsDOList =
                wechatKeyWordsRepository.getListByParam(wechatKeyWordQueryParam);
            if (CollectionUtils.isEmpty(wechatKeyWordsDOList)) {
                wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            } else if (wechatKeyWordsDOList.size() > 1) {
                return "数据更新失败,存在多条重复数据,请处理:" + JSON.toJSONString(wechatKeyWordsDOList);
            } else {
                wechatKeyWordsDO.setId(wechatKeyWordsDOList.get(0).getId());
                wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            }
        }
        return "true";
    }

    @GetMapping("initFuzzyValue")
    public String initFuzzyValue() {
        for (WeChatUtil.FuzzyMatchingkeyWord fuzzyMatchingkeyWord : WeChatUtil.FuzzyMatchingList) {
            WechatKeyWordQueryParam wechatKeyWordQueryParam = new WechatKeyWordQueryParam();
            WechatKeyWordsDO wechatKeyWordsDO = new WechatKeyWordsDO();
            wechatKeyWordsDO.setKeyName(fuzzyMatchingkeyWord.getKeyWord());
            wechatKeyWordsDO.setKeyType(KeyTypeEnum.FUZZY);
            wechatKeyWordsDO.setBelonger(SystemBelongEnum.YNSS);
            wechatKeyWordsDO.setValueContent(fuzzyMatchingkeyWord.getFuzzyMatchingResult());
            wechatKeyWordsDO.setIsValid(BooleanEnum.TRUE);
            wechatKeyWordQueryParam.setKeyName(fuzzyMatchingkeyWord.getKeyWord());
            wechatKeyWordQueryParam.setKeyType(KeyTypeEnum.FUZZY);
            wechatKeyWordQueryParam.setBelonger(SystemBelongEnum.YNSS);
            wechatKeyWordQueryParam.setValueContent(fuzzyMatchingkeyWord.getFuzzyMatchingResult());
            List<WechatKeyWordsDO> wechatKeyWordsDOList =
                wechatKeyWordsRepository.getListByParam(wechatKeyWordQueryParam);
            if (CollectionUtils.isEmpty(wechatKeyWordsDOList)) {
                wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            } else if (wechatKeyWordsDOList.size() > 1) {
                return "数据更新失败,存在多条重复数据,请处理:" + JSON.toJSONString(wechatKeyWordsDOList);
            } else {
                wechatKeyWordsDO.setId(wechatKeyWordsDOList.get(0).getId());
                wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            }
        }
        return "true";
    }

    @GetMapping("initAccompany")
    public String initAccompany() {
        for (AccompanyEnum accompanyEnum : AccompanyEnum.values()) {
            AccompanyDO accompanyDO = new AccompanyDO();
            AccompanyQueryParam accompanyQueryParam = new AccompanyQueryParam();
            accompanyDO.setCharacterName(accompanyEnum.getCharacterName());
            accompanyDO.setBelonger(SystemBelongEnum.YNSS);
            accompanyDO.setCharacterristic(accompanyEnum.getCharacterristic());
            accompanyDO.setIsValid(Boolean.TRUE);
            accompanyDO.setDepartment(accompanyEnum.getDepartment());
            accompanyDO.setEvaluate(accompanyEnum.getEvaluate());
            accompanyDO.setCharacterType(accompanyEnum.getCharacterType());
            accompanyDO.setIsElite(accompanyEnum.getElite());
            accompanyDO.setJadePendant(accompanyEnum.getJadePendant());
            accompanyDO.setScore(accompanyEnum.getScore());
            accompanyDO.setUrl(accompanyEnum.getUrl());
            accompanyQueryParam.setCharacterName(accompanyEnum.getCharacterName());
            accompanyQueryParam.setBelonger(accompanyDO.getBelonger());
            List<AccompanyDO> accompanyDOList = accompanyRepository.getListByParam(accompanyQueryParam);
            if (CollectionUtils.isEmpty(accompanyDOList)) {
                accompanyRepository.saveOrUpdateById(accompanyDO);
            } else if (accompanyDOList.size() > 1) {
                return "该随从已存在:" + JSON.toJSONString(accompanyDOList);
            } else {
                accompanyDO.setId(accompanyDOList.get(0).getId());
                accompanyRepository.saveOrUpdateById(accompanyDO);
            }
        }
        return "true";
    }

    @GetMapping("initChainToHandler")
    public String initChainToHandler() {


        /*ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setName(SystemBelongEnum.LEADER.name());
        configurationDO.setValue(JSON.toJSONString(map));
        configurationRepository.saveOrUpdateById(configurationDO);*/
        return JSON.toJSONString(initChain());
    }

    @GetMapping("updateConfigToMap")
    public Boolean updateConfigToMap() {
        return initData.updateConfigToMap();
    }


    public static void main(String[] args) {
        initChain();

    }

    private static Map<String, Map<String, List<HandlerToChainMapping>>> initChain(){
        Map<String, Map<String, List<HandlerToChainMapping>>> map = new HashMap();
        Map<String, List<HandlerToChainMapping>> listMap = new HashMap<>();
        HandlerToChainMapping returnWordSuffixMapping = new HandlerToChainMapping();
        returnWordSuffixMapping.setHandlerName(ReturnWordSuffixHandler.class.getName());
        returnWordSuffixMapping.setPriority(9);

        List<HandlerToChainMapping> eventList = new ArrayList<>();
        HandlerToChainMapping handlerToChainMapping = new HandlerToChainMapping();
        handlerToChainMapping.setHandlerName(EventHandler.class.getName());
        handlerToChainMapping.setPriority(1);
        //eventList.add(returnWordSuffixMapping);
        eventList.add(handlerToChainMapping);
        listMap.put(EventMessageContentHandlerChain.class.getName(), eventList);

        //图片处理链


        //文字处理链
        List<HandlerToChainMapping> textList = new ArrayList<>();
        HandlerToChainMapping httpRequestHandlerMapping = new HandlerToChainMapping();
        httpRequestHandlerMapping.setHandlerName(HttpRequestHandler.class.getName());
        httpRequestHandlerMapping.setPriority(1);

        HandlerToChainMapping registerAreaHandlerMapping = new HandlerToChainMapping();
        registerAreaHandlerMapping.setHandlerName(RegisterAreaHandler.class.getName());
        registerAreaHandlerMapping.setPriority(1);
        HandlerToChainMapping QueryFoodOrMaterialHandlerMapping = new HandlerToChainMapping();
        QueryFoodOrMaterialHandlerMapping.setHandlerName(QueryFoodOrMaterialHandler.class.getName());
        QueryFoodOrMaterialHandlerMapping.setPriority(2);
        HandlerToChainMapping SimpleKeyWordHandlerMapping = new HandlerToChainMapping();
        SimpleKeyWordHandlerMapping.setHandlerName(SimpleKeyWordHandler.class.getName());
        SimpleKeyWordHandlerMapping.setPriority(3);
        HandlerToChainMapping ComplexKeyWordHandlerMapping = new HandlerToChainMapping();
        ComplexKeyWordHandlerMapping.setHandlerName(ComplexKeyWordHandler.class.getName());
        ComplexKeyWordHandlerMapping.setPriority(4);
        HandlerToChainMapping CharacterRecognitionHandlerMapping = new HandlerToChainMapping();
        CharacterRecognitionHandlerMapping.setHandlerName(CharacterRecognitionHandler.class.getName());
        CharacterRecognitionHandlerMapping.setPriority(5);
        HandlerToChainMapping OpenAiHandlerMapping = new HandlerToChainMapping();
        OpenAiHandlerMapping.setHandlerName(OpenAiHandler.class.getName());
        OpenAiHandlerMapping.setPriority(6);
        HandlerToChainMapping SendMobileMessageHandlerMapping = new HandlerToChainMapping();
        SendMobileMessageHandlerMapping.setHandlerName(SendMobileMessageHandler.class.getName());
        SendMobileMessageHandlerMapping.setPriority(7);
        HandlerToChainMapping TuLingHandlerMapping = new HandlerToChainMapping();
        TuLingHandlerMapping.setHandlerName(TuLingHandler.class.getName());
        TuLingHandlerMapping.setPriority(8);

        //textList.add(registerAreaHandlerMapping);
        //textList.add(QueryFoodOrMaterialHandlerMapping);
        //textList.add(SimpleKeyWordHandlerMapping);
        //textList.add(ComplexKeyWordHandlerMapping);
        textList.add(httpRequestHandlerMapping);
        //textList.add(CharacterRecognitionHandlerMapping);
        //textList.add(OpenAiHandlerMapping);
        //textList.add(SendMobileMessageHandlerMapping);
        //textList.add(TuLingHandlerMapping);
        //textList.add(returnWordSuffixMapping);
        listMap.put(TextMessageContentHandlerChain.class.getName(), textList);

        //声音处理链
        List<HandlerToChainMapping> voiceList = new ArrayList<>();
        //voiceList.add(QueryFoodOrMaterialHandlerMapping);
        voiceList.add(SimpleKeyWordHandlerMapping);
        voiceList.add(ComplexKeyWordHandlerMapping);
        //voiceList.add(SendMobileMessageHandlerMapping);
        //voiceList.add(TuLingHandlerMapping);
        //voiceList.add(returnWordSuffixMapping);
        //listMap.put(VoiceMessageContentHandlerChain.class.getName(), voiceList);
        map.put(SystemBelongEnum.GAME.name(), listMap);
        System.out.println(JSON.toJSONString(map));
        return map;
    }

}
