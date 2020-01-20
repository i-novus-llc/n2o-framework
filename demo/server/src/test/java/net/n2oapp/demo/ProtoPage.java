package net.n2oapp.demo;


import com.codeborne.selenide.CollectionCondition;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.page;

/**
 * Тесты ProtoPage
 */
public class ProtoPage implements ProtoPageSelectors {

    /**
     * Проверка правильности селекторов
     */
    public ProtoPage checkAllElementsExists() {
        getMainTableHead().shouldBe(exist);
        getMainTableRows().shouldBe(CollectionCondition.sizeGreaterThan(0));
        getMainTableFilter().shouldBe(exist);

        getTableHeaderSurname().shouldBe(exist);
        getFilterGenderMale().shouldBe(exist);
        getFilterGenderFemale().shouldBe(exist);
        getFilterGenderUnknown().shouldBe(exist);
        getFilterSearchButton().shouldBe(exist);
        return page(ProtoPage.class);
    }

    /**
     * Проверка работы фильтра по полу
     */
    public ProtoPage assertGender() {
        getFilterGenderFemale().click();
        getFilterSearchButton().click();
        assert isAllMatch(getCol(getMainTableRows(), 4), "Женский");

        getFilterGenderFemale().click();
        getFilterGenderMale().click();
        getFilterSearchButton().click();
        assert isAllMatch(getCol(getMainTableRows(), 4), "Мужской");

        getFilterGenderMale().click();
        getFilterGenderUnknown().click();
        getFilterSearchButton().click();
        assert getCol(getMainTableRows(), 4).isEmpty();

        return page(ProtoPage.class);
    }

    /**
     * Проверка работы сортировки по фамилии
     */
    public ProtoPage assertSorting() {
        getTableHeaderSurname().click();

        assert isSorted(getCol(getMainTableRows(), 0), true);

        getTableHeaderSurname().click();
        assert isSorted(getCol(getMainTableRows(), 0), false);

        getTableHeaderSurname().click();
        List<String> list = getCol(getMainTableRows(), 0);
        assert !isSorted(list, true);
        assert !isSorted(list, false);

        return page(ProtoPage.class);
    }

    /**
     * Проверка работы очистки фильтра
     */
    public void assertClearFilter() {
        getFilterName().val("Мария");
        getFilterGenderFemale().click();
        getFilterVip().click();

        getFilterClearButton().click();

        getFilterName().shouldHave(value(""));
//        getFilterGenderFemale().getAttribute("checked");
//        getFilterVip().shouldNotHave(attribute("checked"));
    }
}
