package org.myf.wechatofficialaccountproject.infrastructure.util.helper.conver;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.KeyTypeEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-20 23:33
 * @Description: KeyTypeConverter
 */
public class KeyTypeConverter implements Converter<KeyTypeEnum> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return KeyTypeEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public KeyTypeEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration)  {
        String value = cellData.getStringValue();
        return KeyTypeEnum.getKeyTypeByValue(value);
    }

    @Override
    public WriteCellData<?> convertToExcelData(KeyTypeEnum value, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration)  {
        if (value == null) {
            return new WriteCellData("");
        } else {
            return new WriteCellData(value.getValue());
        }

    }

}
