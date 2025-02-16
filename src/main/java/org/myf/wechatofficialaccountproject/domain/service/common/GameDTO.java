package org.myf.wechatofficialaccountproject.domain.service.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class GameDTO {

    /**
     * 角色名称
     */
    private String characName;

    /**
     * 等级
     */
    private String level;

    /**
     * 哈夫币
     */
    private String hafcoinnum;

    /**
     * 道具价值
     */
    private String propcapital;

    /**
     * 在线状态 0 离线  1 在线
     */
    private String islogined;

    /**
     * 今日登录 logintoday 1是 0 否
     */
    private String logintoday;

    /**
     * 最后登录 lastlogintime
     */
    private String lastlogintime;

    /**
     * 最后登出
     */
    private String lastlogouttime;

    /**
     * 封号状态
     */
    private String isbanuser;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("角色名称:").append(this.characName).append("\n");
        stringBuilder.append("等级:").append(this.level).append("\n");
        stringBuilder.append("哈夫币:").append(this.hafcoinnum).append("\n");
        stringBuilder.append("道具价值:").append(this.propcapital).append("\n");
        stringBuilder.append("在线状态:").append(Objects.equals(this.islogined,"0") ? "离线" : "在线").append("\n");
        stringBuilder.append("今日登录:").append(Objects.equals(this.logintoday,"0") ? "否" : "是").append("\n");
        if(StrUtil.isNotBlank(lastlogintime)){
            long time = Long.parseLong(this.lastlogintime) * 1000L;
            stringBuilder.append("最后登录:").append(DateUtil.date(new Date(time))).append("\n");
        }
        if(StrUtil.isNotBlank(lastlogouttime)){
            long time = Long.parseLong(this.lastlogouttime) * 1000L;
            stringBuilder.append("最后登出:").append(DateUtil.date(new Date(time))).append("\n");
        }
        double value = (double) (Integer.parseInt(this.hafcoinnum) + Integer.valueOf(this.propcapital)) / 1000000;
        String wareHouseValue = String.format("%.1f", value) + "M";
        stringBuilder.append("仓库价值:").append(wareHouseValue).append("\n");
        stringBuilder.append("封号状态:").append(Objects.equals(this.isbanuser,"0") ? "否" : "是").append("\n");

        return stringBuilder.toString();
    }
}
