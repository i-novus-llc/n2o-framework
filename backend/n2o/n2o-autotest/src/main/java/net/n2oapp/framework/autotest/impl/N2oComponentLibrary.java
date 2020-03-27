package net.n2oapp.framework.autotest.impl;

import net.n2oapp.framework.autotest.api.ComponentLibrary;
import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.impl.collection.*;
import net.n2oapp.framework.autotest.impl.component.button.N2oStandardButton;
import net.n2oapp.framework.autotest.impl.component.cell.*;
import net.n2oapp.framework.autotest.impl.component.control.*;
import net.n2oapp.framework.autotest.impl.component.field.N2oStandardField;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oSimpleFieldSet;
import net.n2oapp.framework.autotest.impl.component.header.N2oAnchorMenuItem;
import net.n2oapp.framework.autotest.impl.component.header.N2oDropdownMenuItem;
import net.n2oapp.framework.autotest.impl.component.header.N2oSimpleHeader;
import net.n2oapp.framework.autotest.impl.component.modal.N2oModal;
import net.n2oapp.framework.autotest.impl.component.page.N2oLeftRightPage;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.impl.component.region.N2oPanelRegion;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oText;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oListWidget;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oStandardTableHeader;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableWidget;

import java.util.Arrays;
import java.util.List;

/**
 * Библиотека стандартных компонентов N2O для автотестирования
 */
public class N2oComponentLibrary implements ComponentLibrary {
    @Override
    public List<Class<? extends Component>> components() {
        return Arrays.asList(
                N2oSimplePage.class, N2oLeftRightPage.class, N2oSimpleHeader.class, N2oSimpleRegion.class, N2oPanelRegion.class,
                N2oTableWidget.class, N2oListWidget.class, N2oFormWidget.class, N2oStandardTableHeader.class,
                N2oModal.class, N2oStandardField.class, N2oStandardButton.class, N2oDropdownMenuItem.class, N2oAnchorMenuItem.class,
                N2oSimpleFieldSet.class, N2oMultiFieldSet.class,
                // controls
                N2oInputText.class, N2oInputSelect.class, N2oCheckbox.class, N2oSelect.class, N2oDateInterval.class,
                N2oDateInput.class, N2oCheckboxGroup.class, N2oRadioGroup.class, N2oMaskedInputControl.class,
                N2oOutputText.class, N2oTextEditor.class, N2oCodeEditor.class, N2oHtml.class, N2oRating.class,
                N2oSlider.class, N2oPills.class, N2oText.class, N2oAutoComplete.class,
                // cells
                N2oTextCell.class, N2oLinkCell.class, N2oEditCell.class, N2oCheckboxCell.class, N2oToolbarCell.class
        );
    }

    @Override
    public List<Class<? extends ComponentsCollection>> collections() {
        return Arrays.asList(N2oCells.class, N2oControls.class, N2oFields.class, N2oRegions.class, N2oTableHeaders.class,
                N2oToolbar.class, N2oWidgets.class, Menu.class, N2oFieldSets.class);
    }
}
