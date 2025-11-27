package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ActionBarAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ToolbarsAware;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.List;

/**
 * Исходная модель страницы
 */
@Getter
@Setter
public abstract class N2oBasePage extends N2oPage implements ActionBarAware, ToolbarsAware, DatasourceIdAware {
    private String datasourceId;
    private ActionBar[] actions;
    private N2oToolbar[] toolbars;
    private N2oAbstractDatasource[] datasources;
    private N2oAbstractEvent[] events;
    private SourceComponent[] items;

    public static String parseWidth(String width) {
        if (width == null)
            return null;

        String widthValue = width.trim();
        if (widthValue.isEmpty())
            return null;

        widthValue = widthValue.matches("\\d+") ? widthValue + "px" : widthValue;
        return String.format("width: %s;", widthValue);
    }

    /**
     * Добавляет контент из <left> или <right> в контент региона
     * Если ширина в  <left> или <right> задана, то оборачивает контент в <region> с этой шириной.
     *
     * @param regionContent контент региона
     * @param source        контент <left> или <right> для добавления в контент региона
     * @param width         ширина контента
     */
    void addContent(List<SourceComponent> regionContent, SourceComponent[] source, String width) {
        if (source == null) return;
        N2oCustomRegion region = new N2oCustomRegion();
        region.setContent(source);
        region.setStyle(parseWidth(width));
        regionContent.add(region);
    }

    /**
     * Автоматически вычисляет ширину для left и right регионов, если они не заданы.
     * Если оба региона присутствуют, но ширина не задана - оба получают по 50%.
     * Если один регион имеет ширину в процентах, а второй нет - второй получает оставшуюся ширину.
     *
     * @param leftWidth  ширина левого региона
     * @param rightWidth ширина правого региона
     * @return массив из двух элементов [leftWidth, rightWidth] с вычисленными ширинами
     */
    protected String[] calculateWidths(String leftWidth, String rightWidth) {
        boolean leftWidthSet = leftWidth != null && !leftWidth.trim().isEmpty();
        boolean rightWidthSet = rightWidth != null && !rightWidth.trim().isEmpty();

        if (!leftWidthSet && !rightWidthSet)
            return new String[]{"50%", "50%"};

        if (leftWidthSet && !rightWidthSet && leftWidth.trim().endsWith("%"))
            return new String[]{leftWidth, calculateRemainingPercentage(leftWidth)};

        if (!leftWidthSet && rightWidth.trim().endsWith("%"))
            return new String[]{calculateRemainingPercentage(rightWidth), rightWidth};

        return new String[]{leftWidth, rightWidth};
    }

    /**
     * Вычисляет оставшуюся ширину в процентах
     */
    private String calculateRemainingPercentage(String percentageWidth) {
        String cleanWidth = percentageWidth.trim().replace("%", "").trim();
        double value = Double.parseDouble(cleanWidth);
        double remaining = 100 - value;
        return remaining + "%";
    }
}
