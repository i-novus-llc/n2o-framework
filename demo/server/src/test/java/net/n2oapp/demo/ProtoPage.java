package net.n2oapp.demo;


import java.util.List;

import static com.codeborne.selenide.Selenide.*;

/**
 * Тесты ProtoPage
 */
public class ProtoPage implements N2oProtoPage {

    /**
     * Проверка работы фильтра по полу
     */
    public ProtoPage assertGender() {
        clickFilterFemale();
        clickSearchFilter();
        assert isAllMatch(getCol(getMainTableRows(), 4), "Женский");

        clickFilterFemale();
        clickFilterMale();
        clickSearchFilter();
        assert isAllMatch(getCol(getMainTableRows(), 4), "Мужской");

        clickFilterMale();
        clickFilterUnknownGender();
        clickSearchFilter();
        assert getCol(getMainTableRows(), 4).isEmpty();

        return page(ProtoPage.class);
    }

    /**
     *  Проверка работы сортировки по фамилии
     */
    public ProtoPage assertSorting() {
        clickSortBySurname();

        assert isSorted(getCol(getMainTableRows(), 0), true);

        clickSortBySurname();
        assert isSorted(getCol(getMainTableRows(), 0), false);

        clickSortBySurname();
        List<String> list = getCol(getMainTableRows(), 0);
        assert !isSorted(list, true);
        assert !isSorted(list, false);

        return page(ProtoPage.class);
    }

}
