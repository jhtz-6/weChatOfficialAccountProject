package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

/**
 * @author myf
 */
public class CustomWaterMarkHandler implements SheetWriteHandler {

    private List<String> sheetNameList;

    private List<WaterMarkModelUtil> waterMarkList = new ArrayList<>();

    public CustomWaterMarkHandler(List<WaterMarkModelUtil> waterMarkList) {
        if (CollectionUtils.isEmpty(waterMarkList)) {
            return;
        }
        this.waterMarkList = waterMarkList.stream().filter(x -> StringUtils.isNotBlank(x.getSheetName())
            && x.getWaterMarkBytes() != null && x.getWaterMarkBytes().length > 0).collect(Collectors.toList());
        sheetNameList = this.waterMarkList.stream().map(x -> x.getSheetName()).distinct().collect(Collectors.toList());
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        XSSFSheet sheet = (XSSFSheet)writeSheetHolder.getSheet();
        // 不需要添加水印，或者当前sheet页不需要添加水印
        if (CollectionUtils.isEmpty(waterMarkList) || sheetNameList.contains(sheet.getSheetName()) == false) {
            return;
        }
        // 获取当前sheet的水印
        List<WaterMarkModelUtil> sheetWaterMarkList = waterMarkList.stream()
            .filter(x -> StringUtils.equals(x.getSheetName(), sheet.getSheetName())).collect(Collectors.toList());
        // 当前sheet页不需要水印
        if (CollectionUtils.isEmpty(sheetWaterMarkList)) {
            return;
        }
        for (WaterMarkModelUtil waterMarkModel : sheetWaterMarkList) {
            // 水印数据
            byte[] waterMarkBytes = waterMarkModel.getWaterMarkBytes();
            // 插入水印
            PoiExcelUtil.insertWaterRemark(sheet, waterMarkBytes);
        }
        // 删除冻结行列信息
        waterMarkList.removeAll(sheetWaterMarkList);
        sheetNameList = waterMarkList.stream().map(x -> x.getSheetName()).distinct().collect(Collectors.toList());
    }
}
