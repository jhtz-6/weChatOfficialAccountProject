package org.myf.wechatofficialaccountproject.interfaces.facade;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.*;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.*;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.AccompanyQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.UserQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.WechatKeyWordQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.conver.KeyTypeConverter;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.listener.KeyTypeImportListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 10:07
 * @Description: PageConfigFacade
 */
@RestController
@RequestMapping("ssfh")
public class SsfhFacade {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    WechatKeyWordsRepository wechatKeyWordsRepository;
    @Autowired
    AccompanyRepository accompanyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InitData initData;
    @Autowired
    FoodRepository foodRepository;

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pages/ssfh/index");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pages/ssfh/login");
        return modelAndView;
    }

    @GetMapping("/queryAllData")
    public List<MenuDTO> queryAllData(HttpServletRequest request) {
        MenuQueryParam menuQueryParam = new MenuQueryParam();
        SystemBelongEnum belonger = getBelongerByRequest(request);
        List<MenuDO> menuList = menuRepository.selectListByParam(menuQueryParam);
        return menuList.stream().filter(x -> Objects.isNull(belonger) || belonger.equals(x.getBelonger())).map(x -> {
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(x, menuDTO);
            return menuDTO;
        }).collect(Collectors.toList());
    }

    @GetMapping("/queryKeyWordData")
    public List<WechatKeyWordsDTO> queryKeyWordData(HttpServletRequest request) {
        WechatKeyWordQueryParam wechatKeyWordQueryParam = new WechatKeyWordQueryParam();
        SystemBelongEnum belonger = getBelongerByRequest(request);
        List<WechatKeyWordsDO> wechatKeyWordsDOList = wechatKeyWordsRepository.getListByParam(wechatKeyWordQueryParam);
        List<WechatKeyWordsDTO> wechatKeyWordsDTOList = wechatKeyWordsDOList.stream()
            .filter(x -> Objects.isNull(belonger) || belonger.equals(x.getBelonger())).map(x -> {
                WechatKeyWordsDTO wechatKeyWordsDTO = new WechatKeyWordsDTO();
                BeanUtils.copyProperties(x, wechatKeyWordsDTO);
                return wechatKeyWordsDTO;
            }).collect(Collectors.toList());
        return wechatKeyWordsDTOList;
    }

    @GetMapping("/queryAccompanyData")
    public List<AccompanyDTO> queryAccompanyData(HttpServletRequest request) {
        AccompanyQueryParam accompanyQueryParam = new AccompanyQueryParam();
        SystemBelongEnum belonger = getBelongerByRequest(request);
        List<AccompanyDO> accompanyDOList = accompanyRepository.getListByParam(accompanyQueryParam);
        List<AccompanyDTO> accompanyDTOList = accompanyDOList.stream()
            .filter(x -> Objects.isNull(belonger) || belonger.equals(x.getBelonger())).map(x -> {
                AccompanyDTO accompanyDTO = new AccompanyDTO();
                BeanUtils.copyProperties(x, accompanyDTO);
                return accompanyDTO;
            }).collect(Collectors.toList());
        return accompanyDTOList;
    }

    @ResponseBody
    @RequestMapping({"/checkLogin"})
    public String checkLogin(HttpServletRequest request) {
        String sessionUserName = (String)request.getSession().getAttribute(WeChatUtil.USER_NAME);
        String sessionUserPhoto = (String)request.getSession().getAttribute(WeChatUtil.USER_PHOTO);
        Map<String, String> checkmap = new HashMap(8);
        /*if (StringUtils.isBlank(sessionUserName)) {
            sessionUserName = WeChatUtil.DEFAULT_USER_NAME;
        }*/
        if (StringUtils.isNotBlank(sessionUserName)) {
            checkmap.put(WeChatUtil.USER_NAME, sessionUserName);
            checkmap.put(WeChatUtil.USER_PHOTO, sessionUserPhoto);
        }
        return JSON.toJSONString(checkmap);
    }

    @ResponseBody
    @PostMapping({"/login"})
    public String login(@RequestBody UserDTO user, HttpServletRequest request) throws Exception {
        Map checkmap = new HashMap();
        if (StringUtils.isAnyBlank(user.getLoginName(), user.getLoginPassword())) {
            checkmap.put("result", false);
            checkmap.put("errorMessage", "账号或密码为空");
            return JSON.toJSONString(checkmap);
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setLoginName(user.getLoginName());
        userQueryParam.setLoginPassword(user.getLoginPassword());
        UserDO userDO = userRepository.selectOneByParam(userQueryParam);
        if (Objects.isNull(userDO)) {
            checkmap.put("result", false);
            checkmap.put("errorMessage", "账号或密码错误");
            return JSON.toJSONString(checkmap);
        } else {
            request.getSession().setAttribute(WeChatUtil.USER_NAME, user.getLoginName());
            request.getSession().setAttribute(WeChatUtil.LOGIN_PASSWORD, user.getLoginPassword());
            request.getSession().setAttribute(WeChatUtil.USER_PHOTO,
                // https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png
                CommonUtil.getPhotoUrl());
            /*String sessionUserName = (String)request.getSession().getAttribute("userName");
            String sessionUserPhoto = (String)request.getSession().getAttribute("userPhoto");
            if (StringUtils.isBlank(sessionUserName)) {
                sessionUserName = "游客";
            }*/
            checkmap.put(WeChatUtil.USER_NAME, user.getLoginName());
            checkmap.put("result", true);
            checkmap.put(WeChatUtil.USER_PHOTO, request.getSession().getAttribute(WeChatUtil.USER_PHOTO));
        }
        /*if (Objects.isNull(user) || !"盛世芳华菜谱管理员".equals(user.getLoginName())
            || !"ssfh666".equals(user.getLoginPassword())) {
            checkmap.put("result", false);
            checkmap.put("errorMessage", "账号或密码错误");
        } else {
            request.getSession().setAttribute(WeChatUtil.USER_NAME, user.getLoginName());
            request.getSession().setAttribute(WeChatUtil.USER_NAME,
                "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            String sessionUserName = (String)request.getSession().getAttribute("userName");
            String sessionUserPhoto = (String)request.getSession().getAttribute("userPhoto");
            if (StringUtils.isBlank(sessionUserName)) {
                sessionUserName = "游客";
            }
            checkmap.put("userName", sessionUserName);
            checkmap.put("result", true);
            checkmap.put("userPhoto", sessionUserPhoto);
        }*/
        return JSON.toJSONString(checkmap);
    }

    @PostMapping("/updateFoodData")
    @Transactional
    public String updateFoodData(@RequestBody MenuDTO menuDTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap(8);
        String sessionUserName = (String)request.getSession().getAttribute(WeChatUtil.USER_NAME);
        if (StringUtils.isBlank(sessionUserName)) {
            map.put("result", false);
            map.put("errorMessage", "您尚未登录,暂无权限更新菜谱~~");
        } else {
            SystemBelongEnum belonger = getBelongerByRequest(request);
            if (Objects.isNull(menuDTO.getId())) {
                MenuQueryParam menuQueryParam = new MenuQueryParam();
                menuQueryParam.setFood(menuDTO.getFood());
                menuQueryParam.setSfyx(SfyxEnum.YOU_XIAO.getName());
                menuQueryParam.setBelonger(belonger);
                List<MenuDO> menuDOList = menuRepository.selectListByParam(menuQueryParam);
                if (CollectionUtils.isNotEmpty(menuDOList)) {
                    map.put("result", false);
                    map.put("errorMessage", "已经存在" + menuDTO.getFood() + ",不可再次添加");
                    return JSON.toJSONString(map);
                }
                FoodDO foodDO = new FoodDO();
                foodDO.setBelonger(belonger);
                foodDO.setFoodName(menuDTO.getFood());
                foodRepository.saveOrUpdateById(foodDO);
            }
            MenuDO menuDO = new MenuDO();
            BeanUtils.copyProperties(menuDTO, menuDO);
            menuDO.setBelonger(belonger);
            menuRepository.saveOrUpdateById(menuDO);
            map.put("result", true);
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/updateKeyWordData")
    public String updateKeyWordData(@RequestBody WechatKeyWordsDTO wechatKeyWordsDTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap(8);
        String sessionUserName = (String)request.getSession().getAttribute(WeChatUtil.USER_NAME);
        if (StringUtils.isBlank(sessionUserName)) {
            map.put("result", false);
            map.put("errorMessage", "您尚未登录,暂无权限更新关键字~~");
        } else {
            SystemBelongEnum belonger = getBelongerByRequest(request);
            if (Objects.isNull(wechatKeyWordsDTO.getId())) {
                WechatKeyWordQueryParam queryParam = new WechatKeyWordQueryParam();
                queryParam.setBelonger(belonger);
                queryParam.setKeyName(wechatKeyWordsDTO.getKeyName());
                queryParam.setIsValid(BooleanEnum.TRUE);
                List<WechatKeyWordsDO> wechatKeyWordsDOS = wechatKeyWordsRepository.getListByParam(queryParam);
                if (CollectionUtils.isNotEmpty(wechatKeyWordsDOS)) {
                    map.put("result", false);
                    map.put("errorMessage", "已经存在" + wechatKeyWordsDTO.getKeyName() + ",不可再次添加");
                    return JSON.toJSONString(map);
                }
            }
            WechatKeyWordsDO wechatKeyWordsDO = new WechatKeyWordsDO();
            BeanUtils.copyProperties(wechatKeyWordsDTO, wechatKeyWordsDO);
            wechatKeyWordsDO.setBelonger(belonger);
            wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
            map.put("result", true);
        }
        return JSON.toJSONString(map);
    }

    @PostMapping("/updateAccompanyData")
    public String updateAccompanyData(@RequestBody AccompanyDTO accompanyDTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap(8);
        String sessionUserName = (String)request.getSession().getAttribute(WeChatUtil.USER_NAME);
        if (StringUtils.isBlank(sessionUserName)) {
            map.put("result", false);
            map.put("errorMessage", "您尚未登录,暂无权限更新随从~~");
        } else {
            SystemBelongEnum belonger = getBelongerByRequest(request);
            if (Objects.isNull(accompanyDTO.getId())) {
                AccompanyQueryParam queryParam = new AccompanyQueryParam();
                queryParam.setBelonger(belonger);
                queryParam.setIsValid(Boolean.TRUE);
                queryParam.setCharacterName(accompanyDTO.getCharacterName());
                List<AccompanyDO> accompanyDOList = accompanyRepository.getListByParam(queryParam);
                if (CollectionUtils.isNotEmpty(accompanyDOList)) {
                    map.put("result", false);
                    map.put("errorMessage", "已经存在" + accompanyDTO.getCharacterName() + ",不可再次添加");
                    return JSON.toJSONString(map);
                }
            }
            AccompanyDO accompanyDO = new AccompanyDO();
            BeanUtils.copyProperties(accompanyDTO, accompanyDO);
            accompanyDO.setBelonger(belonger);
            accompanyRepository.saveOrUpdateById(accompanyDO);
            map.put("result", true);
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/queryKeyTypeEnum")
    public List<EnumDTO> queryKeyTypeEnum() {
        List<EnumDTO> keyTypeEnumList = Lists.newArrayList();
        for (KeyTypeEnum keyTypeEnum : KeyTypeEnum.values()) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setName(keyTypeEnum.getName());
            enumDTO.setValue(keyTypeEnum.getValue());
            keyTypeEnumList.add(enumDTO);
        }
        return keyTypeEnumList;
    }

    @GetMapping("/queryCharacterTypeEnum")
    public List<EnumDTO> queryCharacterTypeEnum() {
        List<EnumDTO> characterTypeEnumList = Lists.newArrayList();
        for (CharacterTypeEnum characterTypeEnum : CharacterTypeEnum.values()) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setName(characterTypeEnum.getEnName());
            enumDTO.setValue(characterTypeEnum.getCnName());
            characterTypeEnumList.add(enumDTO);
        }
        return characterTypeEnumList;
    }

    @GetMapping("/queryDepartmentEnum")
    public List<EnumDTO> queryDepartmentEnum() {
        List<EnumDTO> departmentEnumList = Lists.newArrayList();
        for (DepartmentEnum departmentEnum : DepartmentEnum.values()) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setName(departmentEnum.getEnName());
            enumDTO.setValue(departmentEnum.getCnName());
            departmentEnumList.add(enumDTO);
        }
        return departmentEnumList;
    }

    @GetMapping("/queryBelongUserList")
    public List<EnumDTO> queryBelongUserList() {
        List<EnumDTO> belongUserList = new ArrayList<>();
        for (String str : WeChatUtil.BELONG_USER_LIST) {
            EnumDTO enumDTO = new EnumDTO();
            enumDTO.setName(str);
            enumDTO.setValue(str);
            belongUserList.add(enumDTO);
        }
        return belongUserList;
    }

    @GetMapping("/queryCatrgoryList")
    public List<EnumDTO> queryCatrgoryList() {
        List<EnumDTO> catrgoryList = Lists.newArrayList();
        for (String key : WeChatUtil.CATEGORY_MAP.keySet()) {
            if (WeChatUtil.CATEGORY_MAP.get(key) == 0) {
                EnumDTO enumDTO = new EnumDTO();
                enumDTO.setName(key);
                enumDTO.setValue(key);
                catrgoryList.add(enumDTO);
            }
        }
        return catrgoryList;
    }

    @GetMapping("/queryBooleanEnum")
    public List<EnumDTO> queryBooleanEnum() {
        List<EnumDTO> BooleanEnumList = Lists.newArrayList();
        EnumDTO enumDTO = new EnumDTO();
        enumDTO.setName(Boolean.TRUE.booleanValue());
        enumDTO.setValue("是");
        BooleanEnumList.add(enumDTO);
        enumDTO = new EnumDTO();
        enumDTO.setName(Boolean.FALSE.booleanValue());
        enumDTO.setValue("否");
        BooleanEnumList.add(enumDTO);
        return BooleanEnumList;
    }

    @GetMapping("/synchronousData")
    public String synchronousData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap(8);
        if (initData.updateDataToMap(getBelongerByRequest(request))) {
            map.put("result", true);
            return JSON.toJSONString(map);
        }
        map.put("result", false);
        map.put("errorMessage", "同步数据失败,请联系管理员进行处理~~~");
        return JSON.toJSONString(map);
    }

    @GetMapping("/keyWordExport")
    public void keyWordExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("关键词", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerConverter(new KeyTypeConverter())
            .inMemory(Boolean.TRUE).build();
        List<WechatKeyWordsDTO> wechatKeyWordsDTOList = queryKeyWordData(request);
        try {
            WriteSheet writeMenuSheet = EasyExcel.writerSheet(0, "关键词").head(WechatKeyWordsDTO.class).build();
            excelWriter.write(wechatKeyWordsDTOList, writeMenuSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    @PostMapping("keyWordImport")
    @ResponseBody
    public String keyWordExport(MultipartFile file, HttpServletRequest request) {
        Map<String, Object> map = new HashMap(8);
        if (!file.getOriginalFilename().endsWith("xlsx") && !file.getOriginalFilename().endsWith("xls")) {
            map.put("result", false);
            map.put("errorMessage", "只能上传excel");
            return JSON.toJSONString(map);
        }
        try {
            EasyExcel
                .read(file.getInputStream(), WechatKeyWordsDTO.class,
                    new KeyTypeImportListener(wechatKeyWordsRepository, getBelongerByRequest(request)))
                .registerConverter(new KeyTypeConverter()).sheet().doRead();
        } catch (Exception e) {
            map.put("result", false);
            map.put("errorMessage", e);
            return JSON.toJSONString(map);
        }
        map.put("result", true);
        return JSON.toJSONString(map);
    }

    private SystemBelongEnum getBelongerByRequest(HttpServletRequest request) {
        if (Objects.isNull(request.getSession())
            || Objects.isNull(request.getSession().getAttribute(WeChatUtil.USER_NAME))) {
            return null;
        }
        String userName = (String)request.getSession().getAttribute(WeChatUtil.USER_NAME);
        return WeChatUtil.USER_TO_BELONGER_MAP.get(userName);

    }
}
