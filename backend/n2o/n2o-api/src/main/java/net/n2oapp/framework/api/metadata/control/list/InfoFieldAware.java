package net.n2oapp.framework.api.metadata.control.list;

/**
 * Маркер контрола содержащего дополнительную инфу
 */
public interface InfoFieldAware {

    String LABEL = "label";

    /**
     * @return поле, содержащее дополнительную информацию
     */
    String getInfoFieldId();
    void setInfoFieldId(String infoFieldId);

    /**
     * @return поле, содержащее стиль отображения дополнительной информации
     */
    String getInfoStyle();
    void setInfoStyle(String infoStyle);
}
