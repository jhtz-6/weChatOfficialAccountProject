package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author myf
 */
public enum AccompanyEnum {

    ZHAO_YUN("赵云", true, CharacterTypeEnum.SOLDIER, "免控|沉默|高伤", DepartmentEnum.WATER, 0, "迅捷/迟缓、控制、物连、力量/吸血",
        "结合被动【无双】,"
            + "队友阵亡得越多,战斗力越强,故偶尔会有惊喜;一技能可以沉默对方,建议提高速度,可以沉默对方速度最高的随从两个回合,但赵云速度不高影响不大,照样可以沉默对方一个回合,导致对方无法释放二技能,因为随从的二技能比一技能伤害要高。",
        ""),
    DA_QIAO("大乔", true, CharacterTypeEnum.ARTIST, "驱散|复活|增伤", DepartmentEnum.WATER, 0, "医术、迅捷/迟缓、清醒、保命/庇护",
        "【半奶型艺师】,一技能解控," + "二技能复活增血,大乔对速度要求很苛刻,一技能为攻击力最高的两个随从解控,所以大桥的速度最好比攻击力最高的随从的速度高一点点,这样才能最大的利用大乔的一技能。", ""),
    LUO_SHEN("洛神", true, CharacterTypeEnum.ADVISER, "群攻|冰冻|护盾", DepartmentEnum.WATER, 0, "法连、迅捷、智力、击晕/减速",
        "洛神有伤害有控制有肉(二技能护盾)," + "可以当个半肉来用,伤害和控制都比较一般。", ""),
    LI_BAI("李白", true, CharacterTypeEnum.SOLDIER, "高伤|群攻|降暴", DepartmentEnum.WATER, 0, "迅捷、物连、暴击/命中、力量", ""
        + "李白应该是水木火里面的最强战士,输出爆炸,越战越猛,没有硬控,二技能还能回血;以李白为核心的建议让李白的等级比其他随从高出20级左右,这样可以选择不佩戴迅捷(通过玉佩基础属性为速度和等级优势来提高速度)而佩戴输出玉佩,"
        + "最大限度的增加李白的伤害。", "https://weixin.qq.com/sph/A3CNI5"),
    YUE_FEI("岳飞", true, CharacterTypeEnum.GUARD, "保护|反击|眩晕", DepartmentEnum.WATER, 0, "迅捷、控制、庇护/自愈、物连",
        "【控制型护卫】,岳飞一技能对战士100%控制," + "但现在好像不是100%了,虽然数据标的是100%,岳飞对速度要求也高,但岳飞被动会增加最多25%的速度,所以可以携带基础属性为速度的玉佩,此时可以不用携带迅捷。",
        ""),
    XI_SHI("西施", true, CharacterTypeEnum.ARTIST, "沉睡|治疗", DepartmentEnum.WATER, 0, "医术、迅捷、保命/自愈、清醒/抵抗、",
        "【纯奶型艺师】,西施一二技能均能给队友回血,奶量还是挺足的;"
            + "一技能简单介绍:极高的概率为敌方速度最高的随从附加沉睡,持续两个回合,但是被攻击后有50%的概率接触控制。西施一技能不太好用,主要好不容易控制了对面,结果还有概率解控。因为一个回合下来基本所有的随从都会挨一遍打,所以"
            + "这里我建议西施的速度低一些(主要是为了控制住对面的二技能),这样一技能的回血可以用上,如果控制了敌人," + "不至于因为己方一技能打到对面而解除控制",
        ""),
    WANG_ZHAO_JUN("王昭君", false, CharacterTypeEnum.ARTIST, "治疗|护盾", DepartmentEnum.WATER, 0, "医术、迅捷或医术、保命", "【纯奶型艺师】",
        ""),
    GAO_JIAN_LI("高渐离", false, CharacterTypeEnum.ADVISER, "减速|冰冻", DepartmentEnum.WATER, 0, "法连、迅捷", "暂无", ""),
    XIA_HOU_DUN("夏侯惇", false, CharacterTypeEnum.SOLDIER, "减速|加攻", DepartmentEnum.WATER, 0, "物连、迅捷", "暂无", ""),
    CHENG_YAO_JIN("程咬金", false, CharacterTypeEnum.GUARD, "嘲讽|免伤", DepartmentEnum.WATER, 0, "控制、迅捷、自愈(任意两个)", "暂无", ""),

    YU_JI("虞姬", true, CharacterTypeEnum.ARTIST, "格挡|回血|净化", DepartmentEnum.FIRE, 0, "迅捷、医术、保命、庇护/自愈",
        "【纯奶型艺师】,虞姬一二技能都能回血,一技能还能加护盾" + ",可以免疫一次攻击,所以建议虞姬速度也低一些,这样一技能回血可以用上,一技能护盾可以免疫敌方二技能。", ""),
    YANG_GUI_FEI("杨贵妃", true, CharacterTypeEnum.ARTIST, "重伤|反伤|回血", DepartmentEnum.FIRE, 0, "迅捷、保命、自愈、智力/法连",
        "【半奶型艺师】," + "杨贵妃半奶型艺师都很牵强,贵妃只有被动可以给队友回血,所以用贵妃的话,队友的暴击率要高,建议给队友配上暴击玉佩。", ""),
    GUO_NV_WANG("郭女王", true, CharacterTypeEnum.ADVISER, "灼烧|致盲|增伤", DepartmentEnum.FIRE, 0, "迅捷、吸血、法连、智力",
        "郭女王输出尚可,有软控概率致盲;" + "依赖灼烧,建议搭配吕布一起使用。", ""),
    XIANG_YU("项羽", true, CharacterTypeEnum.SOLDIER, "高伤|灼烧|免控", DepartmentEnum.FIRE, 0, "迅捷、物连、力量、命中/暴击",
        "项羽,我愿称之为三系最弱战士;" + "项羽没有回血技能,导致项羽很脆,经常破釜沉舟还没用上呢就嗝屁了,这就很尴尬。破釜沉舟的一技能也没有比李白一技能高出很多伤害,所以项羽真的很弱。", ""),
    LV_BU("吕布", true, CharacterTypeEnum.GUARD, "烈焰灼烧|复活", DepartmentEnum.FIRE, 0, "迟缓、反伤/物连、庇护、自愈",
        "吕布应该是五系里最肉且输出最高的护卫了" + ",纯纯的大肉盾,还记得步步被吕布支配的恐惧吗?吕布作为护卫但是没有任何保护己方随从的技能,作为大前排很合适,抗伤还能灼烧敌方。", ""),
    MA_CHAO("马超", true, CharacterTypeEnum.SOLDIER, "灼烧|高伤", DepartmentEnum.FIRE, 0, "迅捷、物连、暴伤、力量",
        "该英雄我只在模拟里使用了一下,感觉比李" + "白韩信要弱,比项羽要强," + "建议搭配吕布一起使用,二技能命中灼烧或者烈焰灼烧的100%暴击,这个要好好利用。", ""),
    MI_YUE("芈月", false, CharacterTypeEnum.ARTIST, "沉睡|治疗", DepartmentEnum.FIRE, 0, "迅捷、控制、医术(任意两个)", "暂无", ""),
    ZHOU_YU("周瑜", false, CharacterTypeEnum.ADVISER, "灼烧|暴击", DepartmentEnum.FIRE, 0, "迅捷、法连、暴击(任意两个)", "暂无", ""),
    LI_MU("李牧", false, CharacterTypeEnum.SOLDIER, "灼烧|爆发", DepartmentEnum.FIRE, 0, "迅捷、物连(任意两个)", "暂无", ""),
    FAN_LI_HUA("樊梨花", false, CharacterTypeEnum.GUARD, "沉默|反伤", DepartmentEnum.FIRE, 0, "反伤、控制", "暂无", ""),

    HAN_XIN("韩信", true, CharacterTypeEnum.SOLDIER, "追击|沉默|吸血", DepartmentEnum.WOOD, 0, "物连、迅捷、控制、暴击/力量",
        "【偏控制爆发型战士】,如果你想用韩信体验秒人和控人的快感,那你可以把韩信速度加到己方最高;如果你想用韩信打更多的输出,那你可以不把韩信速度置为己方最高,而是更多的去增加韩信输出。注意:韩信对谋士的伤害会"
            + "增加20%",
        ""),
    BIAN_QUE("扁鹊", true, CharacterTypeEnum.ARTIST, "治疗|加速|增伤", DepartmentEnum.WOOD, 0, "医术、迅捷、庇护、保命",
        "【纯奶型艺师】" + "扁鹊奶量还是挺足的,可以给队友加速和增伤,建议扁鹊速度高一些", ""),
    WEI_ZI_FU("卫子夫", true, CharacterTypeEnum.ARTIST, "治疗|复活|降疗", DepartmentEnum.WOOD, 0, "医术、迅捷、庇护、保命",
        "" + "奶量没有扁鹊多,卫子夫建议搭配木系随从一起食用,且木系随从需要为我方攻击最高的两个单位之一,这样可以触发青鸾庇护(复活技能)", ""),
    FU_SU("扶苏", true, CharacterTypeEnum.ADVISER, "群攻|荆棘|降控", DepartmentEnum.WOOD, 0, "法连、迅捷、智力、减速/魂灭",
        "偏功能性谋士,伤害不高,但是可以降低控制。" + "建议和对面控制多的随从放到一列。", ""),
    CAI_WEN_JI("蔡文姬", true, CharacterTypeEnum.ADVISER, "禁疗|群攻|高伤", DepartmentEnum.WOOD, 0, "法连、迅捷、智力/命中、击晕",
        "蔡文姬禁疗还是很恶心的," + "菜菜对速度要求高,二技能对速度比自身低的会多造成30%伤害,建议提高速度。", ""),
    DI_QING("狄青", true, CharacterTypeEnum.GUARD, "保护|反击|回血", DepartmentEnum.WOOD, 0, "迟缓、反伤、庇护、自愈",
        "狄青,肉型护卫,没有控制,可以保护" + "己方随从,出场率较低。", ""),
    HUA_TUO("华佗", false, CharacterTypeEnum.ARTIST, "治疗|复活", DepartmentEnum.WOOD, 0, "医术、迅捷或医术、保命", "暂无", ""),
    SONG_YU("宋玉", false, CharacterTypeEnum.ADVISER, "束缚|增伤", DepartmentEnum.WOOD, 0, "迅捷、法连、控制(任意两个)", "暂无", ""),
    SUN_SHANG_XIANG("孙尚香", false, CharacterTypeEnum.SOLDIER, "魂灭|破防", DepartmentEnum.WOOD, 0, "物连、迅捷", "暂无", ""),
    HUO_QU_BING("霍去病", false, CharacterTypeEnum.GUARD, "沉默|爆发", DepartmentEnum.WOOD, 0, "迅捷、控制、物连(任意两个)", "暂无", ""),

    XUAN_ZANG("玄奘", true, CharacterTypeEnum.ARTIST, "群疗|复活|续航", DepartmentEnum.SUN, 0, "医术、迅捷/迟缓、保命/自愈、净化",
        "【奶量最足艺师】,玄奘的奶量" + "应该是游戏里最多的,特别好用。", ""),
    ZHUANG_ZHOU("庄周", true, CharacterTypeEnum.ARTIST, "驱散|群疗|增益", DepartmentEnum.SUN, 0, "医术、迅捷、保命/自愈、智力",
        "【半奶型艺师】,庄周输出不太够," + "奶量也不太够,有点尴尬,不太好用。", ""),
    ZHU_GE_LIANG("诸葛亮", true, CharacterTypeEnum.ADVISER, "群伤|驱散|空城", DepartmentEnum.SUN, 0, "法连、迅捷、智力、命中/减速/击晕、",
        "诸葛亮输出还是很高的," + "没有硬控,被动空城很恶心。", ""),
    WU_ZE_TIAN("武则天", true, CharacterTypeEnum.ADVISER, "降攻|群攻|眩晕", DepartmentEnum.SUN, 0, "法连、迅捷、控制、减速/破疗/魂灭/抵抗控制",
        "输出控制性型谋士," + "输出不低,控制更是很恶心,运气好把对面全部控住,这还能输？建议武则天速度高一些,可以先手控制对面。", ""),
    HUA_MU_LAN("花木兰", true, CharacterTypeEnum.SOLDIER, "单体|反击|吸血", DepartmentEnum.SUN, 0, "击晕、力量、吸血、命中、物连",
        "" + "木兰强在被动:柔身反击+心流,所以根据被动我们可以看出来木兰对速度几乎无要求,因为木兰只要被打就可以反击;为什么建议携带命中呢？因为命中对携带迅捷(一般输出都会携带迅捷)的随从伤害会增加15%,相当于"
            + "又是一个高级力量(高级力量:物理伤害增加15%);而且物连对木兰的柔身反击无法触发,所以物连的优先级低于命中。",
        "https://weixin.qq.com/sph/AxsuAP"),
    MU_GUI_YING("穆桂英", true, CharacterTypeEnum.GUARD, "群攻|破防|感光", DepartmentEnum.SUN, 0, "迅捷/迟缓、物连、自愈、庇护",
        "功能性护卫,没有硬控;" + "一般用穆桂英是为了给对面加感光。出场率较低。", ""),
    ZHONG_WU_YAN("钟无艳", true, CharacterTypeEnum.GUARD, "群攻|破防|感光", DepartmentEnum.SUN, 0, "迅捷、物连、自愈、庇护",
        "保护性护卫,没有硬控;" + "迅捷可以先手打感光、黑豹庇护保护队友,黑豹庇护吸收的伤害偏低(施法者生命值上限的10%)持续两个回合,该随从强度感觉偏低。", ""),
    NA_LAN_NONG_RUO("纳兰容若", false, CharacterTypeEnum.ARTIST, "降攻|驱散", DepartmentEnum.SUN, 0, "医术、迅捷或医术、保命", "暂无", ""),
    zhan_zhao("展昭", false, CharacterTypeEnum.SOLDIER, "连击|降疗", DepartmentEnum.SUN, 0, "迅捷、物连、清醒(任意两个)", "暂无", ""),

    DIAO_CHAN("貂蝉", true, CharacterTypeEnum.ARTIST, "护盾|增益|减益", DepartmentEnum.YIN, 0, "医术、迅捷、法连、保命",
        "貂蝉很好用,被动怜香惜玉也很恶心;" + "可以加盾、回血、软控对面,出场率较高。", ""),
    PAN_AN("潘安", true, CharacterTypeEnum.ARTIST, "回血|减益转移", DepartmentEnum.YIN, 0, "医术、迟缓、法连、保命/清醒",
        "被动可以移除并转移非控制类负面状态," + "所以建议后手;输出和治疗都比较一般;阴系越多一技能越强势;总体而言随从强度中等偏上。", ""),
    SI_MA_YI("司马懿", true, CharacterTypeEnum.ADVISER, "噬魂|引爆|群攻", DepartmentEnum.YIN, 0, "法连、迅捷、智力、减速/魂灭/破疗/命中",
        "司马懿伤害很高,但是很脆,对艺师有额外20%伤害", ""),
    ZHAO_FEI_YAN("赵飞燕", true, CharacterTypeEnum.ADVISER, "噬魂|攻守兼顾", DepartmentEnum.YIN, 0, "法连、迅捷、智力、减速/魂灭/破疗/命中",
        "赵飞燕只在是用力用过,伤害也挺高的,没有硬控,、" + "在谋士里算比较肉的。", ""),
    CAO_CAO("曹操", true, CharacterTypeEnum.SOLDIER, "单体|爆发|追击", DepartmentEnum.YIN, 0, "迅捷、物连、清醒、力量",
        "曹操伤害还是很高的,用来打前排的" + "和收割残血的。", ""),
    LAN_LING_WANG("兰陵王", true, CharacterTypeEnum.SOLDIER, "眩晕|增伤|魂力", DepartmentEnum.YIN, 0, "迅捷、物连、控制、力量/命中",
        "兰陵王出场率还是很高的," + "用来打后排和收割残血,伤害很高,还有硬控。被动和赵云有些像,有人祭天,法力无边。", ""),
    DIAN_WEI("典韦", true, CharacterTypeEnum.GUARD, "噬魂|眩晕|免伤", DepartmentEnum.YIN, 0, "迟缓、反伤、庇护/自愈、控制/减速",
        "肉型护卫,有小概率硬控,、" + "大概率叠加噬魂。", ""),
    ZHANG_LIAO("张辽", true, CharacterTypeEnum.GUARD, "噬魂|沉默|血伤", DepartmentEnum.YIN, 0, "物连、迅捷、控制、嘲讽/减速/免伤",
        "定位是护卫但是主动技能确实战士型的," + "被动技能是护卫型的,导致既没有战士的高输出也没有护卫的保人和免伤;所以随从比较一般。", ""),
    GUI_GU_ZI("鬼谷子", false, CharacterTypeEnum.ADVISER, "噬魂|沉默", DepartmentEnum.YIN, 0, "法连、迅捷、控制(任意两个)", "暂无", ""),
    BAI_QI("白起", false, CharacterTypeEnum.GUARD, "嘲讽|破防", DepartmentEnum.YIN, 0, "迟缓、反伤、庇护、自愈、控制(任意两个)", "暂无", ""),

    ;

    public static AccompanyEnum getAccompanyByName(String characterName) {
        if (StringUtils.isEmpty(characterName)) {
            return null;
        }
        return Arrays.stream(AccompanyEnum.values()).filter(x -> x.getCharacterName().equals(characterName)).findAny()
            .orElse(null);
    }

    public static List<AccompanyEnum> getAccompanyByCharacterType(CharacterTypeEnum characterType) {
        if (Objects.isNull(characterType)) {
            return null;
        }
        return Arrays.stream(AccompanyEnum.values()).filter(x -> x.getCharacterType().equals(characterType))
            .collect(Collectors.toList());
    }

    private String characterName;
    /**
     * 是否精英
     */
    private Boolean isElite;
    /**
     * 人物类型
     */
    private CharacterTypeEnum characterType;

    /**
     * 特点
     */
    private String characterristic;
    /**
     * 系别
     */
    private DepartmentEnum department;

    private Integer score;
    /**
     * 玉佩
     */
    private String jadePendant;

    /**
     * 评价
     */
    private String evaluate;

    /**
     * url
     */
    private String url;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Boolean getElite() {
        return isElite;
    }

    public void setElite(Boolean elite) {
        isElite = elite;
    }

    public CharacterTypeEnum getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterTypeEnum characterType) {
        this.characterType = characterType;
    }

    public String getCharacterristic() {
        return characterristic;
    }

    public void setCharacterristic(String characterristic) {
        this.characterristic = characterristic;
    }

    public DepartmentEnum getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEnum department) {
        this.department = department;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getJadePendant() {
        return jadePendant;
    }

    public void setJadePendant(String jadePendant) {
        this.jadePendant = jadePendant;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    AccompanyEnum(String characterName, Boolean isElite, CharacterTypeEnum characterType, String characterristic,
        DepartmentEnum department, Integer score, String jadePendant, String evaluate, String url) {
        this.characterName = characterName;
        this.isElite = isElite;
        this.characterType = characterType;
        this.characterristic = characterristic;
        this.department = department;
        this.score = score;
        this.jadePendant = jadePendant;
        this.evaluate = evaluate;
        this.url = url;
    }

    @Override
    public String toString() {
        return "随从名:" + characterName + ";\n" + "系别:" + department.getCnName() + ";\n" + "定位:"
            + characterType.getCnName() + ";\n" + "特点:" + characterristic + ";\n" + "玉佩建议:" + jadePendant + ";\n"
            + "是否精英随从:" + (isElite ? "是" : "否") + ";\n" + "评价:" + evaluate + ";\n" + "随从精彩视频:"
            + (StringUtils.isEmpty(url) ? "暂无,待补充" : url);
    }
}
