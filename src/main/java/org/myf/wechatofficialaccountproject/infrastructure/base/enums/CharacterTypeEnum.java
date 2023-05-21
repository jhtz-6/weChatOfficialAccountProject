package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

/**
 *
 * @author myf
 */
public enum CharacterTypeEnum {

    SOLDIER("SOLDIER", "战士"), GUARD("GUARD", "护卫"), ARTIST("ARTIST", "艺师"), ADVISER("ADVISER", "谋士");

    private String enName;

    private String cnName;

    CharacterTypeEnum(String enName, String cnName) {
        this.enName = enName;
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
}
