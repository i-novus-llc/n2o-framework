package net.n2oapp.framework.autotest.impl;

import net.n2oapp.framework.autotest.api.ComponentLibrary;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.impl.collection.*;
import net.n2oapp.framework.autotest.impl.component.button.N2oDropdownButton;
import net.n2oapp.framework.autotest.impl.component.button.N2oStandardButton;
import net.n2oapp.framework.autotest.impl.component.cell.*;
import net.n2oapp.framework.autotest.impl.component.control.*;
import net.n2oapp.framework.autotest.impl.component.drawer.N2oDrawer;
import net.n2oapp.framework.autotest.impl.component.field.N2oButtonField;
import net.n2oapp.framework.autotest.impl.component.field.N2oIntervalField;
import net.n2oapp.framework.autotest.impl.component.field.N2oStandardField;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oMultiFieldSetItem;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oSimpleFieldSet;
import net.n2oapp.framework.autotest.impl.component.header.N2oAnchorMenuItem;
import net.n2oapp.framework.autotest.impl.component.header.N2oDropdownMenuItem;
import net.n2oapp.framework.autotest.impl.component.header.N2oSearchItem;
import net.n2oapp.framework.autotest.impl.component.header.N2oSimpleHeader;
import net.n2oapp.framework.autotest.impl.component.modal.N2oModal;
import net.n2oapp.framework.autotest.impl.component.page.*;
import net.n2oapp.framework.autotest.impl.component.region.*;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oAlert;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oImage;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oStatus;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oText;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oListWidget;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.N2oCalendarEvent;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.N2oCalendarToolbar;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.N2oCalendarWidget;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.view.*;
import net.n2oapp.framework.autotest.impl.component.widget.cards.N2oCard;
import net.n2oapp.framework.autotest.impl.component.widget.cards.N2oCardsWidget;
import net.n2oapp.framework.autotest.impl.component.widget.chart.N2oArea;
import net.n2oapp.framework.autotest.impl.component.widget.chart.N2oAreaChartWidget;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableFilterHeader;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableMultiHeader;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableSimpleHeader;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableWidget;
import net.n2oapp.framework.autotest.impl.component.widget.tiles.N2oTile;
import net.n2oapp.framework.autotest.impl.component.widget.tiles.N2oTilesWidget;

import java.util.Arrays;
import java.util.List;

/**
 * Библиотека стандартных компонентов N2O для автотестирования
 */
public class N2oComponentLibrary implements ComponentLibrary {
    @Override
    public List<Class<? extends Component>> components() {
        return Arrays.asList(
                // pages
                N2oSimplePage.class, N2oLeftRightPage.class, N2oStandardPage.class, N2oTopLeftRightPage.class,
                N2oModal.class, N2oDrawer.class, N2oSimpleHeader.class, N2oSearchablePage.class,
                // regions
                N2oSimpleRegion.class, N2oPanelRegion.class, N2oLineRegion.class,
                N2oTabsRegion.class, N2oScrollspyRegion.class,
                // widgets
                N2oListWidget.class, N2oFormWidget.class,
                N2oTilesWidget.class, N2oTile.class,
                N2oTableWidget.class, N2oTableSimpleHeader.class, N2oTableFilterHeader.class, N2oTableMultiHeader.class,
                N2oCardsWidget.class, N2oCard.class,
                // widgets (chart)
                N2oAreaChartWidget.class, N2oArea.class,
                // widgets (calendar)
                N2oCalendarWidget.class, N2oCalendarToolbar.class, N2oCalendarEvent.class,
                N2oCalendarMonthView.class, N2oCalendarAgendaView.class, N2oCalendarDayView.class, N2oCalendarWeekView.class,
                N2oCalendarDayViewHeader.class, N2oCalendarWeekViewHeader.class,
                // fieldsets
                N2oSimpleFieldSet.class, N2oLineFieldSet.class, N2oMultiFieldSet.class, N2oMultiFieldSetItem.class,
                // buttons
                N2oDropdownButton.class, N2oStandardButton.class, N2oDropdownMenuItem.class, N2oAnchorMenuItem.class, N2oSearchItem.class,
                // fields
                N2oStandardField.class, N2oIntervalField.class, N2oButtonField.class, N2oImage.class,
                // controls
                N2oInputText.class, N2oInputSelect.class, N2oCheckbox.class, N2oSelect.class, N2oDateInterval.class,
                N2oDateInput.class, N2oCheckboxGroup.class, N2oRadioGroup.class, N2oMaskedInput.class,
                N2oOutputText.class, N2oOutputList.class, N2oTextEditor.class, N2oCodeEditor.class, N2oHtml.class, N2oRating.class,
                N2oSlider.class, N2oPills.class, N2oText.class, N2oAutoComplete.class, N2oProgress.class, N2oStatus.class, N2oAlert.class,
                N2oInputMoney.class, N2oPasswordControl.class, N2oTextArea.class, N2oInputSelectTree.class, N2oFileUploadControl.class,
                N2oImageUploadControl.class, N2oNumberPicker.class, N2oTimePicker.class,
                // cells
                N2oTextCell.class, N2oLinkCell.class, N2oEditCell.class, N2oCheckboxCell.class, N2oToolbarCell.class,
                N2oBadgeCell.class, N2oIconCell.class, N2oImageCell.class, N2oProgressBarCell.class, N2oRatingCell.class,
                N2oListCell.class, N2oTooltipListCell.class, N2oRadioCell.class, N2oFileUploadCell.class
        );
    }

    @Override
    public List<Class<? extends ComponentsCollection>> collections() {
        return Arrays.asList(N2oCells.class, N2oControls.class, N2oFields.class, N2oRegions.class, N2oTableHeaders.class,
                N2oToolbar.class, N2oWidgets.class, N2oRegionItems.class, Menu.class, N2oFieldSets.class, N2oAlerts.class);
    }
}
