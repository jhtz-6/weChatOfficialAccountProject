package org.myf.wechatofficialaccountproject.application.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 16:16
 * @Description: MenuDTO
 */
@Data
public class MenuDTO {

    @ExcelIgnore
    private Integer id;

    @ExcelProperty("菜名")
    @ColumnWidth(12)
    private String food;

    @ExcelProperty("材料")
    @ColumnWidth(30)
    private String rawMaterial;

    @ExcelIgnore
    private Date createTime;

    @ExcelIgnore
    private Date updateTime;

    @ExcelIgnore
    private String sfyx;

    @ExcelProperty("分类")
    private String category;

    @ExcelProperty("好感度最小值")
    private Integer minNum;

    @ExcelProperty("好感度最大值")
    private Integer maxNum;

    @ExcelProperty("喜爱者")
    private String belongUser;

    @ExcelProperty("最低价格")
    private Double price;

    @ExcelProperty("性价比分数")
    private Double costPerformance;

    @ExcelIgnore
    private SystemBelongEnum belonger;

}
