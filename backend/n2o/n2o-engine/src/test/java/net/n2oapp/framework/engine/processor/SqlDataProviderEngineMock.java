package net.n2oapp.framework.engine.processor;

import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Заглушка SqlQueryEngine специально подготовленная для QueryProcessorTest
 */
public class SqlDataProviderEngineMock implements MapInvocationEngine<N2oSqlDataProvider> {

    @Override
    public Object invoke(N2oSqlDataProvider invocation, Map<String, Object> data) {
        //добавлены limit и offset
        Integer limit = (Integer) data.get("limit");
        Integer offset = (Integer) data.get("offset");
        Integer count = (Integer) data.get("count");

        //используется плейсхолдер :filters, а не :where
        assertThat(invocation.getQuery(), containsString(":filters"));
        List<String> join = (List<String>) data.get("join");
        List<String> filters = (List<String>) data.get("filters");
        List<String> select = (List<String>) data.get("select");
        List<String> sortings = (List<String>) data.get("sorting");

        //select есть независимо от того count запрос или data
        assertThat(select.size(), is(2));
        assertThat(select, hasItem("a.second_name"));
        assertThat(select, hasItem("a.patr_name"));

        assertThat(join.size(), is(2));//join 2 из-за фильтра и сортировки
        assertThat(join, hasItem("inner join persons p on a.person_id = p.id"));//добавлен из-за sorting
        assertThat(join, hasItem("left join genders g on a.gender_id = g.id"));//добавлен из-за включенного фильтра по gender.name

        assertThat(filters.size(), is(3));
        assertThat(filters, hasItem("a.second_name = :surname"));//сгенерировано по дефолту
        assertThat(data.get("surname"), is("Фамилия"));

        assertThat(filters, hasItem("a.patr_name = :patrName"));
        assertThat(data.get("patrName"), is("default"));//дефолтный фильтр default-value="default"

        assertThat(filters, hasItem("g.genders like :genderName"));
        assertThat(data.get("genderName"), is("женский"));//с маленькой буквы из-за normalize="#this.toLowerCase()"

        assertThat(sortings.size(), is(1));
        assertThat(sortings, hasItem("sortingName :nameDirection"));//есть направление сортировки
        assertThat(data.get("nameDirection"), is("desc"));//дефолтный маппинг направления сортировки [fieldId]Direction

        if (invocation.getQuery().contains("count(a)")) {
            //COUNT
            assertThat(limit, nullValue());
            assertThat(offset, nullValue());
            assertThat(count, nullValue());

            return Collections.singletonList(new Object[]{11});
        } else {
            //DATA

            assertThat(invocation.getQuery(), containsString("limit :limit"));
            assertThat(invocation.getQuery(), containsString("offset :offset"));

            assertThat(limit, is(5));
            assertThat(offset, is(5));
            assertThat(count, is(10));

            //используется плейсхолдер :sorting, а не :order
            assertThat(invocation.getQuery(), containsString(":sorting"));

            Object[] record = new Object[]{"Фамилия", null};//id поставит query processor
            List<Object[]> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(record);
            }
            return list;
        }
    }

    @Override
    public Class<? extends N2oSqlDataProvider> getType() {
        return N2oSqlDataProvider.class;
    }
}
