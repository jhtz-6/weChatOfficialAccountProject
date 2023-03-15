package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import lombok.Getter;

/**
 * sheet页信息
 *
 * @author xudongmaster
 */
@Getter
public class SheetModel {
    /**
     * sheet名称
     */
    protected String sheetName;

    public SheetModel(SheetModelBuilder sheetModelBuilder) {
        this.sheetName = sheetModelBuilder.getSheetName();
    }

    protected SheetModel() {

    }
}
