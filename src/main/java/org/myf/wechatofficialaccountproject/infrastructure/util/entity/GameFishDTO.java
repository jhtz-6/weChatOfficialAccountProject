package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-08 16:53
 * @Description: GameFishDTO
 */
@Data
@AllArgsConstructor
public class GameFishDTO {

    private String fishName;

    private String fishTime;

    private String fishAddress;

    private String fishColor;

    /**
     * 鱼饵
     */
    private String bait;

    /**
     * 疲劳值
     */
    private Integer fatigueValue;

    private String fish;

    @Override
    public String toString() {
        return "鱼名:" + fishName + ";出现位置:" + fishAddress + ";出现时间:" + fishTime + ";鱼饵:" + bait + ";疲劳值:" + fatigueValue
            + ";对应鱼肉:" + fish + "。\n";
    }
}
