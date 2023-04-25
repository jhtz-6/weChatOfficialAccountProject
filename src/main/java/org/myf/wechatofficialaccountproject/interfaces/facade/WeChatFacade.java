package org.myf.wechatofficialaccountproject.interfaces.facade;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        Map<String, String> map = Maps.newHashMap();
        try {
            map = CommonUtil.convertServerletInputStreamToMap(request.getInputStream());
            weChatMessageResponse = weChatApplicationService.handleMsgbyMap(map);
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            response.getWriter().write(buildResponseWrite(weChatMessageResponse, weChatMessageResponse.getContent()));
        } catch (IOException ioException) {
            LOGGER.error("WeChatFacade.ioException: {},weChatMessageResponse:{},map:{}", ioException,
                JSON.toJSONString(weChatMessageResponse), JSON.toJSONString(map));
        } catch (Exception e) {
            try {
                response.getWriter().write(buildResponseWrite(weChatMessageResponse, e.getMessage()));
            } catch (IOException ex) {
            }
            LOGGER.error("WeChatFacade.e: {},weChatMessageResponse:{},map:{}", e,
                JSON.toJSONString(weChatMessageResponse), JSON.toJSONString(map));
        }
    }

    @GetMapping("menuExcel")
    public void menuExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("盛世芳华菜谱及价格", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<MenuDTO> menuList = Lists.newArrayList(WeChatUtil.MENU_LIST);
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
            weChatMessageResponse.getFromUserName(), weChatMessageResponse.getCreateTime(),
            weChatMessageResponse.getMsgType(), content);
    }

}
