package org.myf.wechatofficialaccountproject.application.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.KeyTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 16:57
 * @Description: WechatKeyWordsDTO
 */
@Data
public class WechatKeyWordsDTO {

    @ExcelIgnore
    private Integer id;

    @ExcelProperty("关键词")
    private String keyName;

    @ExcelProperty("关键词类型")
    private KeyTypeEnum keyType;

    @ExcelIgnore
    private SystemBelongEnum belonger;

    @ExcelProperty("返回结果")
    @ColumnWidth(30)
    private String valueContent;

    @ExcelIgnore
    private BooleanEnum isValid;

    @ExcelIgnore
    private Date updateTime;

}
