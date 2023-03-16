package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.baidu.aip.ocr.AipOcr;
import com.github.qcloudsms.SmsSingleSender;
import com.unfbx.chatgpt.OpenAiStreamClient;
import org.myf.wechatofficialaccountproject.application.dto.FoodDTO;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ConfigurationDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.ConfigurationQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.FoodQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MaterialQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ConfigurationRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.FoodRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.MaterialRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil.CONFIGURATION_MAP;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 23:25
 * @Description: InitData 初始化数据
 */
@Component
public class InitData {

    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    ConfigurationRepository configurationRepositoryl;

    public static SmsSingleSender SSENDER;
    public static AipOcr CLIENT;
    public static OpenAiStreamClient OPENAI_STREAM_CLIENT;

    @PostConstruct
    public void initStaticData() {
        MenuQueryParam menuParam = new MenuQueryParam();
        menuRepository.selectListByParam(menuParam).stream().forEach(x -> {
            MenuDTO menuDTO = new MenuDTO();
            CommonUtil.copyProperties(x, menuDTO);
            menuDTO.setRawMaterial(menuDTO.getRawMaterial().replaceAll("＋", "+").replaceAll(" ", ""));
            menuDTO.setFood(menuDTO.getFood().trim());
            WeChatUtil.MENU_LIST.add(menuDTO);
            if (menuDTO.getFood().endsWith("香") || menuDTO.getFood().equals("梅花龙脑")) {
                WeChatUtil.FRAGRANCE_MENU_SET.add(menuDTO);
            }
            WeChatUtil.MENU_LIST = WeChatUtil.MENU_LIST.stream().sorted(Comparator.comparing(MenuDTO::getCreateTime))
                .collect(Collectors.toList());
        });
        MaterialQueryParam materialQueryParam = new MaterialQueryParam();
        materialRepository.selectListByParam(materialQueryParam).stream().forEach(x -> {
            MaterialDTO materialDTO = new MaterialDTO();
            CommonUtil.copyProperties(x, materialDTO);
            WeChatUtil.MATERIAL_LIST.add(materialDTO);
        });
        WeChatUtil.MATERIAL_NAME_LIST =
            WeChatUtil.MATERIAL_LIST.stream().map(x -> x.getMaterialName()).collect(Collectors.toList());
        FoodQueryParam foodQueryParam = new FoodQueryParam();
        foodRepository.selectListByParam(foodQueryParam).stream().forEach(x -> {
            FoodDTO foodDTO = new FoodDTO();
            CommonUtil.copyProperties(x, foodDTO);
            foodDTO.setFoodName(foodDTO.getFoodName().trim());
            WeChatUtil.FOOD_LIST.add(foodDTO);
        });
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
    }
}
