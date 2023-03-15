package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import lombok.Getter;

/**
 * @author myf
 */
@Getter
public class WaterMarkModelUtil extends SheetModel {

    private byte[] waterMarkBytes;

    private WaterMarkModelUtil() {}

    public static WaterMarkModelUtil createWaterMarkModel(String sheetName, byte[] waterMarkBytes) {
        WaterMarkModelUtil waterMarkModel = new WaterMarkModelUtil();
        // sheet页名称
        waterMarkModel.sheetName = sheetName;
        // 水印数据
        waterMarkModel.waterMarkBytes = waterMarkBytes;
        return waterMarkModel;
    }
}
