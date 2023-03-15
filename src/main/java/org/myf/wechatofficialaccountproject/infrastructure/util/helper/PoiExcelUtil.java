package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 * @author myf
 */
public class PoiExcelUtil {

    public static void insertWaterRemark(XSSFSheet sheet, byte[] bytes) {
        // 水印图片数据关联sheet对象
        XSSFWorkbook workbook = sheet.getWorkbook();
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        String relationId = sheet.addRelation(null, XSSFRelation.IMAGES, workbook.getAllPictures().get(pictureIdx))
            .getRelationship().getId();
        // 将水印图片设置为sheet的背景颜色
        sheet.getCTWorksheet().addNewPicture().setId(relationId);
    }
}
