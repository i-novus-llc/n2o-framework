package net.n2oapp.demo;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты ProtoPage
 */
public class ProtoPage implements ProtoPageSelectors {

    /**
     * Проверка правильности селекторов
     */
    public ProtoPage checkAllElementsExists() {
        getMainTableHead().shouldBe(Condition.exist);
        getMainTableRows().shouldBe(CollectionCondition.sizeGreaterThan(0));
        getMainTableFilter().shouldBe(Condition.exist);

        getTableHeaderSurname().shouldBe(Condition.exist);
        getFilterGenderMale().shouldBe(Condition.exist);
        getFilterGenderFemale().shouldBe(Condition.exist);
        getFilterGenderUnknown().shouldBe(Condition.exist);
        getFilterSearchButton().shouldBe(Condition.exist);
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
     * Проверка работы поиска по фамилии и имени
     */
    public ProtoPage assertNameAndSurname() {
        getFilterSurname().val("Лапа");
        getFilterName().val("ера");
        getFilterSearchButton().click();

        getMainTableRows().shouldHaveSize(1);
        getMainTableRows().get(0).$$("td").get(0).shouldHave(text("Лапаева"));
        getMainTableRows().get(0).$$("td").get(1).shouldHave(text("Вера"));

        return page(ProtoPage.class);
    }
}
