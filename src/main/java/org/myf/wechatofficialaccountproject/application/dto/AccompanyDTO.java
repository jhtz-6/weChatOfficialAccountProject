package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.CharacterTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.DepartmentEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 21:30
 * @Description: AccompanyDTO
 */
@Data
public class AccompanyDTO {
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String characterName;

    private Boolean isElite;

    private CharacterTypeEnum characterType;

    private String characterristic;

    private DepartmentEnum department;

    private Integer score;

    private String jadePendant;

    private String evaluate;

    private String url;

    private Boolean isValid;

    private SystemBelongEnum belonger;

    @Override
    public String toString() {
        return "随从名:" + characterName + ";\n" + "系别:" + department.getCnName() + ";\n" + "定位:"
                + characterType.getCnName() + ";\n" + "特点:" + characterristic + ";\n" + "玉佩建议:" + jadePendant + ";\n"
                + "是否精英随从:" + (isElite ? "是" : "否") + ";\n" + "评价:" + evaluate + ";\n" + "随从精彩视频:"
                + (StringUtils.isEmpty(url) ? "暂无,待补充" : url);
    }
}
