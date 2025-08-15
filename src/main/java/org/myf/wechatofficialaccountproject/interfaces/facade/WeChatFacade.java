package org.myf.wechatofficialaccountproject.interfaces.facade;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageResponse;
import org.myf.wechatofficialaccountproject.application.service.WeChatApplicationService;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-03-04 18:03
 * @Description: 处理微信公众号消息
 */
@RestController
@RequestMapping("weChat")
public class WeChatFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatFacade.class);

    @Autowired
    WeChatApplicationService weChatApplicationService;

    @PostMapping("/msg")
    public void msg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.LEADER);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/msg")
    public String msg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    @PostMapping("/yHuJiuMsg")
    public void YHuJiuMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.YHJ);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/yHuJiuMsg")
    public String yHuJiuMsg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    /**
     * pdx和yhj一样
     *
     * @param request
     * @param response
     */
    @PostMapping("/pdxMsg")
    public void pdxMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.YHJ);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/pdxMsg")
    public String pdxMsg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    @PostMapping("/anSuiMsg")
    public void AnSuiMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.ANSUI);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/zznhMsg")
    public String zznhMsg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    @PostMapping("/zznhMsg")
    public void zznhMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.ZZNH);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/gameMsg")
    public String gameMsg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    @PostMapping("/gameMsg")
    public void gameMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.GAME);
        handleOriginalMsg(request, response);
    }

    @GetMapping("/weMoneyMsg")
    public String weMoneyMsg(String echostr, String signature, String timestamp, String nonce) {
        System.out.println("echostr:" + echostr);
        return echostr;
    }

    @PostMapping("/weMoneyMsg")
    public void weMoneyMsg(HttpServletRequest request, HttpServletResponse response) {
        ThreadLocalHolder.BELONGER_THREAD_LOCAL.set(SystemBelongEnum.WEMONEY);
        handleOriginalMsg(request, response);
    }

    private void handleOriginalMsg(HttpServletRequest request, HttpServletResponse response) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        Map<String, String> map = Maps.newHashMap();
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        try {
            map = CommonUtil.convertServerletInputStreamToMap(request.getInputStream());
            buildResponse(weChatMessageResponse, map);
            weChatMessageResponse.setContent(weChatApplicationService.handleMsgByMap(map));
            //如果是wemoney且是1 则发送图片
            LOGGER.info("handleOriginalMsg.weChatMessageResponse:{}", JSONUtil.toJsonStr(weChatMessageResponse));
            /*if(Objects.equals(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get(),SystemBelongEnum.WEMONEY) &&
                    Objects.equals(map.get("MsgType"),"text") &&
            Objects.equals(map.get("Content"),"1")){
                weChatMessageResponse.setMsgType(MsgTypeEnum.IMAGE.getName());
                String picResponseWrite = buildMoneyPicResponseWrite(weChatMessageResponse, weChatMessageResponse.getContent());
                LOGGER.info("handleOriginalMsg.picResponseWrite:{}",picResponseWrite);
                response.getWriter().write(picResponseWrite);
                return;
            }*/

            response.getWriter().write(buildResponseWrite(weChatMessageResponse, weChatMessageResponse.getContent()));
        } catch (IOException ioException) {
            LOGGER.error("weChatMessageResponse:{},map:{}", JSON.toJSONString(weChatMessageResponse),
                    JSON.toJSONString(map), ioException);
        } catch (Exception e) {
            try {
                response.getWriter().write(buildResponseWrite(weChatMessageResponse, e.getMessage()));
            } catch (IOException ex) {
                LOGGER.error("An error occurred while writing the response", ex);
            }
            LOGGER.error("weChatMessageResponse:{},map:{}", JSON.toJSONString(weChatMessageResponse),
                    JSON.toJSONString(map), e);
        }
    }

    @GetMapping("menuExcel")
    public void menuExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("盛世芳华菜谱及价格", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        //默认导出leader数据
        List<MenuDTO> menuList = Lists.newArrayList(WeChatUtil.MENU_LIST_MAP.get(SystemBelongEnum.LEADER));
        List<MaterialDTO> materials = Lists.newArrayList(WeChatUtil.MATERIAL_LIST);
        materials = materials.stream().filter(x -> Objects.nonNull(x.getPrice())).collect(Collectors.toList());
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        List<WaterMarkModelUtil> waterMarkList = new ArrayList<>();

        // 水印数据
        byte[] waterMarkBytes = POICommonUtil.createWaterMark("公众号:小屋写随笔", 600, 450, new Color(220, 181, 21, 100),
                new Font("微软雅黑", Font.BOLD, 40));
        waterMarkList.add(WaterMarkModelUtil.createWaterMarkModel("菜谱", waterMarkBytes));
        waterMarkList.add(WaterMarkModelUtil.createWaterMarkModel("价格", waterMarkBytes));

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).inMemory(Boolean.TRUE)
                .registerWriteHandler(new CustomWaterMarkHandler(waterMarkList))
                .registerWriteHandler(horizontalCellStyleStrategy).build();
        try {
            WriteSheet writeMenuSheet = EasyExcel.writerSheet(0, "菜谱").head(MenuDTO.class).build();
            excelWriter.write(menuList, writeMenuSheet);

            WriteSheet writeMaterialSheet = EasyExcel.writerSheet(1, "价格").head(MaterialDTO.class).build();
            excelWriter.write(materials, writeMaterialSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private String buildResponseWrite(WeChatMessageResponse weChatMessageResponse, String content) {
        return String.format(WeChatUtil.RESPONSE_FORMAT, weChatMessageResponse.getToUserName(),
                weChatMessageResponse.getFromUserName(), DateUtils.dateToString(new Date(), null),
                weChatMessageResponse.getMsgType(), content);
    }

    private String buildMoneyPicResponseWrite(WeChatMessageResponse weChatMessageResponse, String content) {
        return String.format(WeChatUtil.RESPONSE_PIC_FORMAT, weChatMessageResponse.getToUserName(),
                weChatMessageResponse.getFromUserName(), DateUtils.dateToString(new Date(), null),
                weChatMessageResponse.getMsgType(), content);
    }

    private void buildResponse(WeChatMessageResponse weChatMessageResponse, Map<String, String> map) {
        weChatMessageResponse.setFromUserName(map.get("ToUserName"));
        weChatMessageResponse.setToUserName(map.get("FromUserName"));
        weChatMessageResponse.setMsgType(MsgTypeEnum.TEXT.name);
    }

}
