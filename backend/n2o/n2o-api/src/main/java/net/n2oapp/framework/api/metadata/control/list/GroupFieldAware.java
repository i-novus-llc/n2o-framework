package net.n2oapp.framework.api.metadata.control.list;

/**
 * @author iryabov
 * @since 24.08.2015
 */
public interface GroupFieldAware {
    //поле, содержащее значение группы
    String getGroupFieldId();
    void setGroupFieldId(String groupFieldId);
}
