package org.myf.wechatofficialaccountproject.application.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 17:55
 * @Description: MaterialDTO
 */
@Data
public class MaterialDTO {

    @ExcelIgnore
    private Integer id;

    @ExcelProperty("食材名")
    private String materialName;

    @ExcelIgnore
    private String sfyx = "1";

    @ExcelProperty("价格")
    private Integer price;

    @ExcelProperty("数量")
    private Integer num;

    @ExcelProperty("种植耗时")
    private String spentTime;
}
