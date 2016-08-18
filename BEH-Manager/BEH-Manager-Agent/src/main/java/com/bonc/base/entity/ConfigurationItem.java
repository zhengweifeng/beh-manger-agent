package com.bonc.base.entity;

/**
 * 配置文件配置项，兼容xml，=，黑白名单配置项格式
 *
 *
 * @version        16/08/17
 * @author         tianbaochao    
 */
public class ConfigurationItem {

    // key
    private String name;

    // value
    private String value;

    // desc
    private String desc;

    // final
    private String strFinal;

    public ConfigurationItem(){
        desc = "";
        strFinal = "false";
    }
    /**
     * Method description
     *
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Method description
     *
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Method description
     *
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Method description
     *
     *
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getStrFinal() {
        return strFinal;
    }

    /**
     * Method description
     *
     *
     * @param strFinal
     */
    public void setStrFinal(String strFinal) {
        this.strFinal = strFinal;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
