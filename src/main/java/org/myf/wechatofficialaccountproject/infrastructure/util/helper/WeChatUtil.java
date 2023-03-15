package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.myf.wechatofficialaccountproject.application.dto.FoodDTO;
import org.myf.wechatofficialaccountproject.application.dto.MaterialDTO;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.AccompanyEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.GameFishDTO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 20:07
 * @Description: WeChatUtil
 */
public final class WeChatUtil {

    public static String XWXSB = "公众号功能如下:。\n" + "大朝会系列视频可在微信视频号搜索小屋写随笔进行查看\n"
        + "16、新增关键词:【关键词】、【价格表】、【大朝会】、【琉璃拟饵】、【紧急】、【进群】;钓鱼类相关关键词示例:【银河溪】、【蓝鳖虾】、【橙色鱼】、【紫色鱼】、【血红龙】等等。\n"
        + "15、其它部分关键词:【吃瓜】、【兑换码】、【合服】、【并蒂双莲】、【性价比】、【王爷性价比】、【皇帝性价比】、【贵妃性价比】等。\n"
        + "14、查询随从(近期已优化):请发送随从名称【李白】或者【武则天】等。\n" + "13、查询不含特定材料的菜谱:请参考【皇帝的特不要烧酒】或者【特不要烧酒】或者【皇帝不要烧酒】。\n"
        + "12、查询含特定材料的菜谱:请参考【皇帝+烧酒】。\n" + "1、可以发送游戏里的菜谱截图,公众号会自动返回菜谱结果。\n" + "2、发送关键字【菜谱】可以得到游戏里的所有菜名。\n"
        + "3、发送关键字【食材】可以得到游戏里的所有食材。\n" + "4、支持根据菜谱搜索食材和根据食材搜索菜谱。\n" + "5、发送关键字【小屋写随笔】可以实时获取该公众号的功能。\n"
        + "6、奖励功能:成功添加菜谱的可以发送关键字【奖励】,来获取相应奖励~~~\n" + "7、添加功能,发送关键字【添加】可以获取具体如何添加菜谱\n"
        + "8、可以通过输入【王爷】或【太医】或【贵妃】或【皇帝】来查询菜谱~~\n" + "9、可以通过输入[人物加数字],示例:【王爷 19943】来查询对应人物的推荐菜谱~~"
        + "10、建议功能:可以通过输入【建议】后跟内容,来发送你的对于公众号的建议\n" + "11、输入原材料,系统可以推荐最高分菜谱或性价比菜谱\n";
    private static final String SM_URL =
        "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247484492&idx=1&sn=a66a774f44e5c6532a3ec3d9d74aa17b&chksm=c2920fbff5e586a922babe8b1210675b3fded14457bf4d5817028b1fc99a1ecd29cc0d1dcbb1#rd\n\n"
            + "https://mp.weixin.qq.com/s/y-nvIZmomxjwSZ7TBhHvpQ";
    private static final String MENU_RECOMEND_URL =
        "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483910&idx=1&sn=362b06228bd51c419b9e968c985b3ed0&chksm=c29209f5f5e580e35bb9c77d6c5606146dda77481955f1fe48498c7f8e0e3e48072701121221#rd";
    private static final String TG_UTL =
        "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483804&idx=1&sn=a24979d138336720c6d18ee026160b1d&chksm=c2920a6ff5e58379f06718fef414cd3d0aed9ab7af805f188240b0b77be1c568f1985c6731dc#rd";
    private static final String JY_URL =
        "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483809&idx=1&sn=91274a76d43679a9be4e610ff7b3f3bd&chksm=c2920a52f5e58344669e3bf2841081cd347a30a66e890d4e76b593eef2795d567557d7e4b6df#rd";
    private static final String LBM = "20221025更新:\n" + "枫叶2a9fe3a9bbcmAyM\n" + "培养53864c649PiSt8U\n"
        + "普通夺宝dfb7e0fccHcV4Xw\n" + "VIP8888\nVIP648\nVIP328\nSSFH8432\n新兑换码：mxw4f3e7bgyajLt 古籍\n"
        + "qus719a0b7FZbiz 鲜花\n" + "drse4a9acwZmuFP 名师贴\n" + "20220918兑换码更新:\n" + "qgr1c8057bKEgJ6\n"
        + "w2ec8c572vWfThV\n" + "eu6d79832cYw8yz\n" + "k23970fe1pXajBQ\n" + "w2ec8cbf0jw7GFA \n" + "k239719113xhUcv\n"
        + "hea95883bs67uEQ";
    private static final String ACCOMPANY_RECCOMEND =
        "游戏里的随从一共分为四类:战士、谋士、艺师、护卫;其中:战士是用来打物理输出的,谋士是用来打法术伤害的,艺师是用来给随从回血或者增加收益的,护卫是用来抗伤和保护随从得。\n"
            + "可以上阵的随从一共有五个,这里面一般既要有输出又要有回血或者抗伤的,所依我们便可以把阵容大致分为以下几类:\n"
            + "阵容一:三输出+两艺师;这个阵容可以说是游戏里最常见的了;三输出可以是两个战士+一个谋士,也可以是一个战士+两个谋士;两艺师一般是一个纯奶艺师+一个半奶艺师;纯奶艺师就是两个技能都能回血的艺师,半奶艺师是"
            + "只有一个技能就可以回血。(我其中一个号就是这个阵容:玄奘、木兰、武则天、貂蝉、兰陵王)\n"
            + "阵容二:三输出+一艺师+一护卫;这个阵容在游戏里也比较多,三输出和阵容一一样;一艺师可以是纯奶型艺师(建议)也可以是半奶型艺师(阵容较脆);一护卫没有过多要求。\n"
            + "阵容三:三输出+两护卫;这个阵容很少见,也不太建议,因为没有艺师续航。\n"
            + "阵容四:两输出+一护卫+两艺师;这个阵容在游戏里用的也比较多,这个阵容偏肉,两输出建议一战士一谋士;护卫不多说均可;两艺师建议一个纯奶型艺师,一个半奶型艺师。(我其中一个号就是这个阵容:"
            + "李白、郭女王、吕布、杨贵妃、玄奘)\n" + "阵容五:两输出+两护卫+一艺师;这个阵容在游戏里也很少见,也不太建议。\n"
            + "最后:可以发送随从名字得到该随从的相关信息;也可以发送【战士】、【护卫】、【艺师】、【谋士】等关键字(不需要发送【】)来获取相应随从信息。";
    private static final String UPGRADE = "关于随从升星和升等级的一些建议。\n"
        + "随从升星和升等级是游戏里每一个人都会遇到的事情；经常看到会有人问，我该给哪一个随从升等级或升星；所以特开一贴。\n"
        + "1、关于升星：升星主要加的是血量和攻击力；那血量和攻击力给到什么样的随从可以利益最大化呢？给到阵容里用来打输出的随从；像木兰、诸葛、兰陵王、李白等等。因为这些随从想要打出更高的输出就需要更高的攻击力。所以一般情况下优先给阵容里打输出的随从升星。\n"
        + "2、关于升等级；升等级虽然也会加血加攻击（很少）但更主要的是加速度。那主要加的速度给到什么样的随从可以利益最大化呢？答案是给到那些需要先手硬/软控制的随从；像武则天、貂蝉、岳飞等这些对速度比较依赖的随从。因为我们不依靠这些随从打出很高的伤害；但需要这些随从先手；所以一般情况下优先给阵容里需要先手控制的随从升等级。\n"
        + "\n" + "最后总结一下：\n" + "一般情况下，优先给打输出的随从升星；优先给先手控制的随从升等级";
    private static String TO_GROUP = "https://www.520myf.com:8089/wx.jpg\n\n  如果显示群人数已满,可以通过菜单右下角的【联系我】添加wx拉你进群。";
    private static String BZQH = "百转千回主要看反应速度,几点小技巧:\n"
        + "优先选择方向,比如出现【左或红色】,我们看到左或就可以了,不需要关心或后面是什么颜色,然后去选择正确的方向即可,因为选择方向大脑只需计算一次,眼睛看到方向,传给大脑,大脑发出指令给手,手即可做出动作。\n"
        + "而选择颜色需要大脑计算两次,眼睛看到颜色,传给大脑,大脑再发出指令告诉眼睛,眼睛再寻找正确的颜色所在位置,找到后告诉大脑,大脑再发出指令给到手。所以优先选择方向。\n\n"
        + "非非等于是,双重否定等于肯定,非非左就是左。\n\n"
        + "另外一个就是最难的是带有且字的:【非左且非绿色】,这样的很难,也没有很好的技巧(探索中),,我在这个地方必挂,但到这样的选择时已经40+分了,已经很高了(小的目前最高44分,暂排区第一)。";
    public static final String OCR_GEGIN_CONTENT =
        "文字识别开始,请发送游戏里原材料图片(寝宫->灶台->＋号);图片发送完毕后,请再发送关键词【文字识别结束】" + "使用视频教程:https://www.520myf.com:8089/wzsb.mp4";
    public static final String CURRENT_PERSON_KEY = "current_person_";
    public static final String NUM_CURRENT_PERSON = "num_current_person";
    public static final String OCR_MENU_ACTION = "ocr_menu_action:";
    public static final String OCR_MENU_CONTENT = "ocr_menu_content:";
    public static List<MenuDTO> MENU_LIST = Lists.newArrayList();
    public static List<MaterialDTO> MATERIAL_LIST = Lists.newArrayList();
    public static List<FoodDTO> FOOD_LIST = Lists.newArrayList();
    public static List<String> MATERIAL_NAME_LIST = new ArrayList<>();
    public static Map<String, String> FISH_KEY_MAPS = Maps.newHashMap();
    public static List<String> BELONG_USER_LIST = new ArrayList<>();
    public static List<GameFishDTO> FISH_LIST = Lists.newArrayList();
    public static List<String> FISH_BAIT_LIST = Lists.newArrayList();
    public static List<String> FISH_ADDRESS_LIST = Lists.newArrayList();
    public static Map<String, List<GameFishDTO>> FISH_COLOR_MAP = new HashMap<>();
    private static List<GameFishDTO> ORANGE_FISH_LIST = Lists.newArrayList();
    private static List<GameFishDTO> PURPLE_FISH_LIST = Lists.newArrayList();
    public static Map<String, String> WeChatKeyWordMap = Maps.newHashMap();
    public static List<String> RECOMMEND_MENU_LIST = new ArrayList<>();
    public static Map<String, GameFishDTO> FISH_MAP;
    public static List<String> FISH_KEY_WORDS = Lists.newArrayList();
    public static Map<String, AccompanyEnum> ACCOMPANY_MAP = new HashMap<>();
    public static Map<String, Integer> CATEGORY_MAP = new HashMap<>();
    public static Set<MenuDTO> FRAGRANCE_MENU_SET = Sets.newHashSet();
    public static List<String> BELONG_USER_COSTPERFORMANCE_LIST = new ArrayList<>();
    public static List<String> AREA_LIST = new ArrayList<>();
    public static String TENCENT_APPKEY = "TENCENT_APPKEY";
    public static String TENCENT_APPID = "TENCENT_APPID";
    public static String TENCENT_SIGN = "TENCENT_SIGN";
    public static String TENCENT_PHONE = "TENCENT_PHONE";
    public static String BAIDU_APPKEY = "BAIDU_APPKEY";
    public static String BAIDU_APPID = "BAIDU_APPID";
    public static String BAIDU_SECRET_KEY = "BAIDU_SECRET_KEY";
    public static Map<String, String> CONFIGURATION_MAP = new HashMap<>();
    public static long CURRENT_PERSON_TIMEOUT = 600000L;
    public static String RESPONSE_FORMAT = "<xml>\n" + "<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
        + "<FromUserName><![CDATA[%2$s]]></FromUserName>\n" + "<CreateTime>%3$s</CreateTime>\n"
        + "<MsgType><![CDATA[%4$s]]></MsgType>\n" + "<Content><![CDATA[%5$s]]></Content>\n" + "</xml>";

    static {
        WeChatKeyWordMap.put("赛马", SM_URL);
        WeChatKeyWordMap.put("跑马", SM_URL);
        WeChatKeyWordMap.put("excel", "http://110.40.208.47:8088/xwxsb/wx/menuExcel\n\n第一个sheet页是菜谱;第二个sheet页是价格");
        WeChatKeyWordMap.put("EXCEL", "http://110.40.208.47:8088/xwxsb/wx/menuExcel\n\n第一个sheet页是菜谱;第二个sheet页是价格");
        WeChatKeyWordMap.put("价格表", "http://110.40.208.47:8088/xwxsb/wx/menuExcel\n\n第一个sheet页是菜谱;第二个sheet页是价格");
        WeChatKeyWordMap.put("价格", "http://110.40.208.47:8088/xwxsb/wx/menuExcel\n\n第一个sheet页是菜谱;第二个sheet页是价格");
        WeChatKeyWordMap.put("盛世芳华管理员", "ssfh666");
        WeChatKeyWordMap.put("菜谱推荐", MENU_RECOMEND_URL);
        WeChatKeyWordMap.put("推荐菜谱", MENU_RECOMEND_URL);
        WeChatKeyWordMap.put("紫笋茶", "紫笋茶是趣物,不是菜。");
        WeChatKeyWordMap.put("投稿", TG_UTL);
        WeChatKeyWordMap.put("建议", JY_URL);
        WeChatKeyWordMap.put("吃瓜", "http://122.51.212.21:8080/xwxsb/ssfh/44dg.do");
        WeChatKeyWordMap.put("阵容", ACCOMPANY_RECCOMEND);
        WeChatKeyWordMap.put("阵容推荐", ACCOMPANY_RECCOMEND);
        WeChatKeyWordMap.put("推荐阵容", ACCOMPANY_RECCOMEND);
        WeChatKeyWordMap.put("兑换码", LBM);
        WeChatKeyWordMap.put("礼包码", LBM);
        WeChatKeyWordMap.put("小屋写随笔", XWXSB);
        WeChatKeyWordMap.put("excel密码", "xwxsb");
        WeChatKeyWordMap.put("关键词", "https://www.520myf.com:8089/xwxsb_gjc.xlsx");
        WeChatKeyWordMap.put("菜谱识图",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247484029&idx=1&sn=9a48771aa"
                + "39df1ad419c1876f64a5fd6&chksm=c292098ef5e58098292db1aa774c8ac3bc1b590c63b644d57cb5678526e741129d4f18561ad1#rd");
        WeChatKeyWordMap.put("语音",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483830&idx=1&sn=9ee009931abfc"
                + "7fe3c85a0f3c8a39139&chksm=c2920a45f5e58353c770b058ab5f84255d367bb2e8f0c36bb337e22e3ddec7af33b8b375790b#rd");
        WeChatKeyWordMap.put("添加", "你好,众人拾柴火焰高,2021/11/06菜谱添加功能上线,示例使用如下:发送关键字【蛋炒饭:米饭+鸡蛋】即可添加。格式一定要对哦~~");
        WeChatKeyWordMap.put("紧急", "如果想发信息给我,可以直接在关键字【紧急】后面跟内容即可,就会把这条信息以短信的形式发到我的手机上;" + "示例(不需要【】):【紧急我要找到你不管南北东西】");

        RECOMMEND_MENU_LIST.add("推荐菜谱");
        RECOMMEND_MENU_LIST.add("推荐性价比菜谱");
        RECOMMEND_MENU_LIST.add("推荐最高分菜谱");
        RECOMMEND_MENU_LIST.add("推荐王爷性价比菜谱");
        RECOMMEND_MENU_LIST.add("推荐皇帝性价比菜谱");
        RECOMMEND_MENU_LIST.add("推荐贵妃性价比菜谱");
        RECOMMEND_MENU_LIST.add("推荐太医性价比菜谱");
        RECOMMEND_MENU_LIST.add("推荐王爷最高分菜谱");
        RECOMMEND_MENU_LIST.add("推荐皇帝最高分菜谱");
        RECOMMEND_MENU_LIST.add("推荐贵妃最高分菜谱");
        RECOMMEND_MENU_LIST.add("推荐太医最高分菜谱");
        RECOMMEND_MENU_LIST.add("推荐王爷菜谱");
        RECOMMEND_MENU_LIST.add("推荐皇帝菜谱");
        RECOMMEND_MENU_LIST.add("推荐贵妃菜谱");
        RECOMMEND_MENU_LIST.add("推荐太医菜谱");

        FISH_BAIT_LIST.add("蓝鳖虾");
        FISH_BAIT_LIST.add("琉璃拟饵");
        FISH_BAIT_LIST.add("乌贼丝");
        FISH_BAIT_LIST.add("发酵玉米耳");
        FISH_BAIT_LIST.add("银质鱼饵");
        FISH_BAIT_LIST.add("肉干");

        FISH_ADDRESS_LIST.add("忘川湖");
        FISH_ADDRESS_LIST.add("银河溪");
        FISH_ADDRESS_LIST.add("仙礁海");

        FISH_COLOR_MAP.put("橙色鱼", ORANGE_FISH_LIST);
        FISH_COLOR_MAP.put("紫色鱼", PURPLE_FISH_LIST);

        ORANGE_FISH_LIST.add(new GameFishDTO("血红龙", "18:0-24:0", "忘川湖", "", "蓝鳖虾、琉璃拟饵", 20, "特供鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("鳌花鱼", "全天", "忘川湖", "", "蓝鳖虾、琉璃拟饵", 16, "专供鱼肉"));
        ORANGE_FISH_LIST.add(new GameFishDTO("金龙鱼", "全天", "忘川湖", "", "琉璃拟饵", 20, "特供鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("公牛鲨", "全天", "忘川湖", "", "蓝鳖虾、琉璃拟饵", 16, "专供鱼肉"));

        ORANGE_FISH_LIST.add(new GameFishDTO("樱花钩吻鲑", "全天", "银河溪", "", "乌贼丝", 20, "特供鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("七彩神仙", "全天", "银河溪", "", "发酵玉米耳、乌贼丝", 14, "精致鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("银曼龙", "6:0-12:0", "银河溪", "", "发酵玉米耳", 16, "专供鱼肉"));

        ORANGE_FISH_LIST.add(new GameFishDTO("七彩麒麟", "全天", "仙礁海", "", "银质鱼饵、肉干", 20, "特供鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("泰坦炮弹", "全天", "仙礁海", "", "银质鱼饵、肉干", 10, "优良鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("红鹦鹉", "全天", "仙礁海", "", "银质鱼饵、肉干", 10, "优良鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("玫瑰尾鱼", "全天", "仙礁海", "", "银质鱼饵、肉干", 10, "优良鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("偕老同穴", "全天", "仙礁海", "", "银质鱼饵、肉干", 16, "专供鱼肉"));
        PURPLE_FISH_LIST.add(new GameFishDTO("蛇王孔雀鱼", "全天", "仙礁海", "", "肉干", 14, "精致鱼肉"));

        FISH_LIST.addAll(ORANGE_FISH_LIST);
        FISH_LIST.addAll(PURPLE_FISH_LIST);
        FISH_MAP = FISH_LIST.stream().collect(Collectors.toMap(GameFishDTO::getFishName, Function.identity()));

        FISH_KEY_WORDS.add("特供鱼肉获得方式");
        FISH_KEY_WORDS.add("专供鱼肉获得方式");
        FISH_KEY_WORDS.add("精致鱼肉获得方式");
        FISH_KEY_WORDS.add("优良鱼肉获得方式");

        FISH_KEY_MAPS.put("特供鱼肉", "如何获得特供鱼肉的关键词:【特供鱼肉获得方式】");
        FISH_KEY_MAPS.put("专供鱼肉", "如何获得专供鱼肉的关键词:【专供鱼肉获得方式】");
        FISH_KEY_MAPS.put("精致鱼肉", "如何获得精致鱼肉的关键词:【精致鱼肉获得方式】");
        FISH_KEY_MAPS.put("优良鱼肉", "如何获得优良鱼肉的关键词:【优良鱼肉获得方式】");

        BELONG_USER_LIST.add("皇帝");
        BELONG_USER_LIST.add("王爷");
        BELONG_USER_LIST.add("太医");
        BELONG_USER_LIST.add("贵妃");

        CATEGORY_MAP.put("尚", 0);
        CATEGORY_MAP.put("御", 0);
        CATEGORY_MAP.put("普", 0);
        CATEGORY_MAP.put("普1", 1);
        CATEGORY_MAP.put("特", 0);
        CATEGORY_MAP.put("特1", 1);
        CATEGORY_MAP.put("精", 0);
        CATEGORY_MAP.put("精1", 1);
        CATEGORY_MAP.put("精2", 2);
        CATEGORY_MAP.put("精3", 3);
        CATEGORY_MAP.put("精4", 4);

        BELONG_USER_COSTPERFORMANCE_LIST.add("性价比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("皇帝性价比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("王爷性价比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("太医性价比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("贵妃性价比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("皇帝坑比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("王爷坑比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("太医坑比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("贵妃坑比");
        BELONG_USER_COSTPERFORMANCE_LIST.add("坑比");

        AREA_LIST.add("芳华年月");
        AREA_LIST.add("有凤来仪");
        AREA_LIST.add("倾国倾城");
        AREA_LIST.add("风姿绰约");
        AREA_LIST.add("百鸟朝凤");
        // ---------------小程序
        AREA_LIST.add("芳华如梦");
        AREA_LIST.add("如花似玉");
        AREA_LIST.add("仙姿玉貌");
        AREA_LIST.add("金枝玉叶");
        AREA_LIST.add("般般入画");

        AREA_LIST.add("顾盼生辉");
        AREA_LIST.add("闭月羞花");
        AREA_LIST.add("国色天香");
        AREA_LIST.add("锦绣年华");
        AREA_LIST.add("沉鱼落雁");

        for (AccompanyEnum accompany : AccompanyEnum.values()) {
            ACCOMPANY_MAP.put(accompany.getCharacterName(), accompany);
        }
    }

    @Data
    @AllArgsConstructor
    public static class FuzzyMatchingkeyWord {
        String keyWord;
        String fuzzyMatchingResult;
    }

    public final static List<FuzzyMatchingkeyWord> FuzzyMatchingList = Lists.newArrayList(new FuzzyMatchingkeyWord("玉佩",
        "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483846&idx=1&sn=e154af48e326827305d74096e421dd27&chksm=c2920a35f5e58323ba4b917cc252266736818376dde740a13b2f91894fbb17ef07ef0ec5c1e4#rd"),
        new FuzzyMatchingkeyWord("随从",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483835&idx=1&sn=fcdc2159da74c1b5606e27bdcce6d1bb&chksm=c2920a48f5e5835e8984d0afd851da67a5732d623ff710af05cd2bbfe03079af9182ccbe9095#rd"),
        new FuzzyMatchingkeyWord("饕餮",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483841&idx=1&sn=284df6e9dc54348b165e2e5383612cf1&chksm=c2920a32f5e58324bbfb0f71d571a2dbf8ae5518b2eec7db4f497aba6229efd059fe7813f136#rd"),
        new FuzzyMatchingkeyWord("钓鱼",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247483861&idx=1&sn=d849b1dd97038f8d8c3de631bc4fc310&chksm=\" +\n"
                + "                    \"c2920a26f5e583308a8b223df6b5a38c58b50bc6eb7788cbf6cb03f04551814ba1d04944be5a#rd\\n\\n 该钓鱼攻略很久没更新了,建议通过关键词来查询:【橙色鱼】、【紫色鱼】"),
        new FuzzyMatchingkeyWord("合服",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247484313&idx=1&sn=c39ac3868aa55438b58181992eadeb72&chksm=c292086af5e5817c2ab3db6646a069754c021c447dae13c331aaffbab38973926ab9ce28d2e0#rd"),
        new FuzzyMatchingkeyWord("升级", UPGRADE), new FuzzyMatchingkeyWord("升星", UPGRADE),
        new FuzzyMatchingkeyWord("并蒂双莲",
            "https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247484394&idx=1&sn=5c16f8ddb89262832e2d97c2f052d1a9&chksm=c2920819f5e5810f6c304a4f0258902df9dc00d3be38599b2d78b57b66b4f8432378207b1090#rd"),
        new FuzzyMatchingkeyWord("加群", TO_GROUP), new FuzzyMatchingkeyWord("进群", TO_GROUP),
        new FuzzyMatchingkeyWord("大朝会",
            "大朝会攻略: https://mp.weixin.qq.com/s?__biz=MzkzNzE4OTAyMA==&mid=2247484790&idx=1&sn=a643dba1e4fe084a5f6509bc6b14a7cb&chksm=c2920e85f5e58793cd86adfb412263cef0563b9ecd02c1ffa32f422308ac80505983acbc3cd2#rd"
                + "\n\n大朝会系列视频可以在微信视频号搜索小屋写随笔进行查看"),
        new FuzzyMatchingkeyWord("百转千回", BZQH),
        new FuzzyMatchingkeyWord("雅宴谜集", "https://kdocs.cn/l/cs06sz40MAIw\n\n做了一个文档所有人可以编辑,有新题目大家直接补充就行了,注意颜色就行。"),
        new FuzzyMatchingkeyWord("答题", "https://kdocs.cn/l/cs06sz40MAIw\n\n做了一个文档所有人可以编辑,有新题目大家直接补充就行了,注意颜色就行。"),
        new FuzzyMatchingkeyWord("周年庆", "周年庆已有活动攻略关键词:【百转千回】、【雅宴谜集】\n\n其它活动持续更新中,也欢迎大家投稿活动攻略(有奖励哦)~~~"));

}
