package net.n2oapp.framework.api.metadata.control.plain;

/**
 * Абстрактная реализация текстовых компонентов
 */
public abstract class N2oText extends N2oPlainField {
    private Integer rows;
    private String height;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
