package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.page;

/**
 * Страница создания/изменения клиента
 */
public class ProtoClient implements ProtoClientSelectors {

    public ProtoClient assertPatronymic(String expected) {
        getInputByLabel("Отчество").shouldBe(Condition.value(expected));
        return page(ProtoClient.class);
    }

}
