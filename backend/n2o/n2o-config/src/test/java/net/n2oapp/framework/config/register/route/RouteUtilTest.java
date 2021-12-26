package net.n2oapp.framework.config.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование методов класса {@link RouteUtil}
 */
public class RouteUtilTest {

    @Test
    public void normalize() {
        assertThat(RouteUtil.normalize("test"), is("/test"));
        assertThat(RouteUtil.normalize("/test/"), is("/test"));
        assertThat(RouteUtil.normalize("//test"), is("/test"));
        assertThat(RouteUtil.normalize("/test//test2"), is("/test/test2"));
        assertThat(RouteUtil.normalize("/test///test2"), is("/test/test2"));
        assertThat(RouteUtil.normalize("test///test2//"), is("/test/test2"));
    }

    @Test
    public void normalizeParam() {
        assertThat(RouteUtil.normalizeParam("test"), is("test"));
        assertThat(RouteUtil.normalizeParam("test.id"), is("test_id"));
        assertThat(RouteUtil.normalizeParam("test*.id"), is("test_id"));
        assertThat(RouteUtil.normalizeParam("test[0].id"), is("test_0_id"));
    }

    @Test
    public void getParams() {
        assertThat(RouteUtil.getParams("/a/b/c/d"), is(Collections.emptyList()));
        assertThat(RouteUtil.getParams("/a/b/c/d?id=123"), is(Collections.emptyList()));
        assertThat(RouteUtil.getParams("/a/b/c/d?id=:a"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getParams("/:a"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getParams("/:a?id=123"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getParams("/:a?id=123&name=:b"), is(Arrays.asList("a", "b")));
        assertThat(RouteUtil.getParams("/:a/:b/c/d/:e"), is(Arrays.asList("a", "b", "e")));
        assertThat(RouteUtil.getParams("/a/:a/b/:b/:e"), is(Arrays.asList("a", "b", "e")));
    }

    @Test
    public void getPathParams() {
        assertThat(RouteUtil.getPathParams("/a/b/c/d"), is(Collections.emptyList()));
        assertThat(RouteUtil.getPathParams("/a/b/c/d?id=:a"), is(Collections.emptyList()));
        assertThat(RouteUtil.getPathParams("/:a"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getPathParams(":a"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getPathParams("/:a?id=123"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getPathParams("/:a?id=123&name=:b"), is(Collections.singletonList("a")));
        assertThat(RouteUtil.getPathParams("/:a/:b/c/d/:e"), is(Arrays.asList("a", "b", "e")));
        assertThat(RouteUtil.getPathParams("/a/:a/b/:/:e/"), is(Arrays.asList("a", "e")));
    }

    @Test
    public void parseQueryParams() {
        assertThat(RouteUtil.parseQueryParams("text"), nullValue());
        Map<String, String> params = RouteUtil.parseQueryParams("id=123");
        assertThat(params.get("id"), is("123"));
        params = RouteUtil.parseQueryParams("id=:a");
        assertThat(params.get("id"), is(":a"));
        params = RouteUtil.parseQueryParams("id=123&name=:b&surname=Ivanov");
        assertThat(params.get("id"), is("123"));
        assertThat(params.get("name"), is(":b"));
        assertThat(params.get("surname"), is("Ivanov"));
    }

    @Test
    public void convertPathToId() {
        assertThat(RouteUtil.convertPathToId(null), nullValue());
        assertThat(RouteUtil.convertPathToId(""), is("_"));
        assertThat(RouteUtil.convertPathToId("/"), is("_"));
        assertThat(RouteUtil.convertPathToId("page"), is("page"));
        assertThat(RouteUtil.convertPathToId("/page/"), is("page"));
        assertThat(RouteUtil.convertPathToId("/page/widget"), is("page_widget"));
        assertThat(RouteUtil.convertPathToId("//page//widget//"), is("page_widget"));
        assertThat(RouteUtil.convertPathToId("/page/:id"), is("page"));
        assertThat(RouteUtil.convertPathToId("/page/:id/"), is("page"));
        assertThat(RouteUtil.convertPathToId("/page/:id/widget"), is("page_widget"));
        assertThat(RouteUtil.convertPathToId("/page/master/:widget_id/detail/:detail_id"), is("page_master_detail"));
        assertThat(RouteUtil.convertPathToId("/:param/page/master/:widget_id/detail/:detail_id"), is("param_page_master_detail"));
    }

    @Test
    public void resolveParams() {
        DataSet data = new DataSet();
        data.put("a", 1);
        data.put("b", "test");
        assertThat(RouteUtil.resolveUrlParams("/a/:a/b/:b", data), is("/a/1/b/test"));
        assertThat(RouteUtil.resolveUrlParams("/a/:a/b/:b", data, Collections.singleton("a"), null), is("/a/1/b/:b"));
        assertThat(RouteUtil.resolveUrlParams("/a/:a/b/:b", data, null, Collections.singleton("a")), is("/a/:a/b/test"));
    }

    @Test
    public void isApplicationUrl() {
        assertThat(RouteUtil.isApplicationUrl("https://google.com"), is(false));
        assertThat(RouteUtil.isApplicationUrl("google.com"), is(true));
        assertThat(RouteUtil.isApplicationUrl("//test"), is(false));
        assertThat(RouteUtil.isApplicationUrl("/test"), is(true));
        assertThat(RouteUtil.isApplicationUrl("test"), is(true));
        assertThat(RouteUtil.isApplicationUrl("test/test2"), is(true));
        assertThat(RouteUtil.isApplicationUrl("../test"), is(true));
        assertThat(RouteUtil.isApplicationUrl("../../test"), is(true));
    }

    @Test
    public void join() {
        assertThat(RouteUtil.join("/", "/test"), is("/test"));
        assertThat(RouteUtil.join("/parent", "/child"), is("/parent/child"));
        assertThat(RouteUtil.join("/parent", "../child"), is("/child"));
        assertThat(RouteUtil.join("/parent1/parent2", "../child"), is("/parent1/child"));
        assertThat(RouteUtil.join("/parent1/parent2", "../../child"), is("/child"));
        try {
            RouteUtil.join("/", "../child");
            Assert.fail();
        } catch (IncorrectRouteException e) {
        }
        try {
            RouteUtil.join("/parent", "../../child");
            Assert.fail();
        } catch (IncorrectRouteException e) {
        }
        assertThat(RouteUtil.join(null, "/test"), is("/test"));
        assertThat(RouteUtil.join("/parent/child", "../"), is("/parent"));
        assertThat(RouteUtil.join("/parent", "../"), is("/"));
    }

    @Test
    public void absolute() {
        //domain relative
        assertThat(RouteUtil.absolute("/test", null), is("/test"));
        assertThat(RouteUtil.absolute("/test", ""), is("/test"));
        assertThat(RouteUtil.absolute("/test", "/"), is("/test"));
        assertThat(RouteUtil.absolute("/child", "/parent"), is("/child"));
        //path relative
        assertThat(RouteUtil.absolute("test", null), is("/test"));
        assertThat(RouteUtil.absolute("test", ""), is("/test"));
        assertThat(RouteUtil.absolute("test", "/"), is("/test"));
        assertThat(RouteUtil.absolute("test", "/parent"), is("/parent/test"));
        assertThat(RouteUtil.absolute("test", "/parent/child"), is("/parent/child/test"));
        assertThat(RouteUtil.absolute("test/test2", "/parent"), is("/parent/test/test2"));
        //parent path relative
        assertThat(RouteUtil.absolute("../child", "/parent"), is("/child"));
        assertThat(RouteUtil.absolute("../child", "/parent1/parent2"), is("/parent1/child"));
        assertThat(RouteUtil.absolute("../../child", "/parent1/parent2"), is("/child"));
        try {
            RouteUtil.absolute("../child", "/");
            Assert.fail();
        } catch (IncorrectRouteException e) {
        }
        try {
            RouteUtil.absolute("../../child", "/parent");
            Assert.fail();
        } catch (IncorrectRouteException e) {
        }
        assertThat(RouteUtil.absolute("../", "/parent/child"), is("/parent"));
        assertThat(RouteUtil.absolute("../../", "/parent/child"), is("/"));
        assertThat(RouteUtil.absolute("../", "/parent"), is("/"));
    }

    @Test
    public void parent() {
        assertThat(RouteUtil.parent("/"), is("../"));
        assertThat(RouteUtil.parent("/test"), is("../test"));
        assertThat(RouteUtil.parent("/1/2"), is("../1/2"));

        assertThat(RouteUtil.parent(RouteUtil.parent("/")), is("../../"));
        assertThat(RouteUtil.parent(RouteUtil.parent("/test")), is("../../test"));
        assertThat(RouteUtil.parent(RouteUtil.parent("/1/2")), is("../../1/2"));
    }

    @Test
    public void addQueryParams() {
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        ModelLink nameLink = new ModelLink(ReduxModel.resolve, "main", "name");
        nameLink.setParam("nameParam");
        queryMapping.put("name", nameLink);
        ModelLink surnameLink = new ModelLink(ReduxModel.resolve, "main", "surname");
        queryMapping.put("surname", surnameLink);
        ModelLink vipLink = new ModelLink(true);
        vipLink.setParam("vipParam");
        queryMapping.put("vip", vipLink);
        ModelLink genderLink = new ModelLink(1);
        queryMapping.put("gender", genderLink);
        assertThat(RouteUtil.addQueryParams("/base", queryMapping),
                is("/base?nameParam=:name&surname=:surname&vipParam=true&gender=1"));
    }

    @Test
    public void parsePath() {
        assertThat(RouteUtil.parsePath("/example?test=1&par=val"), is("/example"));
        assertThat(RouteUtil.parsePath("/example?"), is("/example"));
        assertThat(RouteUtil.parsePath("/example"), is("/example"));
    }

    @Test
    public void parseQuery() {
        assertThat(RouteUtil.parseQuery("/example?test=1&par=val"), is("test=1&par=val"));
        assertThat(RouteUtil.parseQuery("?test=1&par=val"), is("test=1&par=val"));
        assertThat(RouteUtil.parseQuery("/example?"), is(""));
        assertThat(RouteUtil.parseQuery("/example"), nullValue());
    }

}
