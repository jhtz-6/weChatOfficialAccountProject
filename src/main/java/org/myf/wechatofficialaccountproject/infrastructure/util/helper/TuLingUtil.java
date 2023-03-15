package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: myf
 * @CreateTime: 2023-03-09 20:29
 * @Description: TuLingUtil
 */
public final class TuLingUtil {

    public static Map<Integer, String> TULING_ERROR_MAP = new HashMap<>();
    public static final Map<String, String> TULING_REBOT_NAME_MAP = new HashMap<String, String>();

    private static final String REBOR_ONE = "b22bcb0afa394a1a921e2b755dc8fb04";
    private static final String REBOR_TWO = "608f00c378c64e20bfd98eb91d36295d";
    private static final String REBOR_THREE = "b9a6d3626e7d458bb40bf63a822cf8da";
    private static final String REBOR_FOUR = "42744d24a42f43f1a2b07a30e83af77b";
    private static final String REBOR_FIVE = "a2544e4976ce4e4e94de9bf4e3a98794";

    static {

        TULING_REBOT_NAME_MAP.put(REBOR_ONE, "满腹经纶学霸猫");
        TULING_REBOT_NAME_MAP.put(REBOR_TWO, "无敌可爱青春猫");
        TULING_REBOT_NAME_MAP.put(REBOR_THREE, "霸气侧漏总裁猫");
        TULING_REBOT_NAME_MAP.put(REBOR_FOUR, "清纯双尾异瞳猫");
        TULING_REBOT_NAME_MAP.put(REBOR_FIVE, "迷倒众瞄贵妇猫");

        TULING_ERROR_MAP.put(5000, "无解析结果");
        TULING_ERROR_MAP.put(6000, "暂不支持该功能");
        TULING_ERROR_MAP.put(4000, "请求参数格式错误");
        TULING_ERROR_MAP.put(4001, "加密方式错误");
        TULING_ERROR_MAP.put(4002, "无功能权限");
        TULING_ERROR_MAP.put(4003, "该apikey没有可用请求次数");
        TULING_ERROR_MAP.put(4005, "无功能权限");
        TULING_ERROR_MAP.put(4100, "apikey不合法");
        TULING_ERROR_MAP.put(4007, "userid获取失败");
        TULING_ERROR_MAP.put(4200, "上传格式错误");
        TULING_ERROR_MAP.put(4400, "没有上传合法userid");
        TULING_ERROR_MAP.put(4300, "批量操作超过限制");
        TULING_ERROR_MAP.put(4500, "userid申请个数超过限制");
        TULING_ERROR_MAP.put(4600, "输入内容为空");
        TULING_ERROR_MAP.put(4602, "输入文本内容超长(上限150)");
    }

}
