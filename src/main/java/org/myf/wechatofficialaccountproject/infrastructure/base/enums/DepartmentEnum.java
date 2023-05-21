package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

/**
 *
 * @author myf
 */
public enum DepartmentEnum {

    WATER("WATER", "水系"), FIRE("FIRE", "火系"), WOOD("WOOD", "木系"), SUN("SUN", "阳系"), YIN("YIN", "阴系");

    private String enName;

    private String cnName;

    DepartmentEnum(String enName, String cnName) {
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
