package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import lombok.Getter;

/**
 * @author myf
 */
@Getter
public abstract class SheetModelBuilder {
    protected final String sheetName;

    protected SheetModelBuilder(String sheetName) {
        this.sheetName = sheetName;
    }

    abstract protected SheetModel build();
}
