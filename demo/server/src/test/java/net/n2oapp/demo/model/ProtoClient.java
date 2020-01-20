package net.n2oapp.demo.model;

import static com.codeborne.selenide.Selenide.page;

/**
 * Страница создания/изменения клиента
 */
public class ProtoClient implements ProtoClientSelectors {

    public ProtoClient assertPatronymic(String expected) {
        assert expected.equals(getInputByLabel("Отчество").getValue());
        return page(ProtoClient.class);
    }

}
