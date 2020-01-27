package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.StandardTableHeader;
import net.n2oapp.framework.autotest.api.component.header.TableHeader;

public interface TableHeaders extends ComponentsCollection {
    StandardTableHeader header(int index);

    StandardTableHeader header(Condition findBy);

    <T extends TableHeader> T header(int index, Class<T> componentClass);

    <T extends TableHeader> T header(Condition findBy, Class<T> componentClass);
}
