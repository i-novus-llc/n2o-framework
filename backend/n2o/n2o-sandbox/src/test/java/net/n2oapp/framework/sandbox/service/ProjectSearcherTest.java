package net.n2oapp.framework.sandbox.service;

import net.n2oapp.framework.sandbox.templates.ProjectSearcher;
import net.n2oapp.framework.sandbox.templates.SearchProjectModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(
        classes = {ProjectSearcher.class},
        properties = {"n2o.sandbox.projectSearchFolders:/test_examples"})
public class ProjectSearcherTest {

    @Autowired
    private ProjectSearcher projectSearcher;

    @Test
    public void searchTest() throws URISyntaxException, IOException {
        // matches only in one project
        List<SearchProjectModel> result = projectSearcher.search("table");

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getProjectId(), is("test_examples_case1"));
        List<SearchProjectModel.Item> items = result.get(0).getItems();
        assertThat(items.size(), is(2));
        assertThat(items.get(0).getFilename(), is("index.page.xml"));
        assertThat(items.get(0).getLine(), is("<table query-id=\"test\">"));
        assertThat(items.get(0).getLineNumber(), is(4));
        assertThat(items.get(1).getFilename(), is("index.page.xml"));
        assertThat(items.get(1).getLine(), is("</table>"));
        assertThat(items.get(1).getLineNumber(), is(9));


        // matches in several projects
        result = projectSearcher.search("test.json");

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getProjectId(), is("test_examples_case1"));
        items = result.get(0).getItems();
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getFilename(), is("test.query.xml"));
        assertThat(items.get(0).getLine(), is("<test file=\"test.json\"/>"));
        assertThat(items.get(0).getLineNumber(), is(4));

        assertThat(result.get(1).getProjectId(), is("test_examples_case2"));
        items = result.get(1).getItems();
        assertThat(items.size(), is(1));
        assertThat(items.get(0).getFilename(), is("test.query.xml"));
        assertThat(items.get(0).getLine(), is("<test file=\"test.json\"/>"));
        assertThat(items.get(0).getLineNumber(), is(4));

        // matches in none projects
        result = projectSearcher.search("abc");

        assertThat(result.size(), is(0));
    }

    @Test
    public void matchesInFilesWithDifferentExtensionsTest() throws URISyntaxException, IOException {
        List<SearchProjectModel> result = projectSearcher.search("form");

        assertThat(result.size(), is(2));
        assertThat(result.get(0).getProjectId(), is("test_examples_case1"));
        List<SearchProjectModel.Item> items = result.get(0).getItems();
        assertThat(items.size(), is(1));
        // properties file
        assertThat(items.get(0).getFilename(), is("application.properties"));
        assertThat(items.get(0).getLine(), is("n2o.api.widget.form.src=FormWidget"));
        assertThat(items.get(0).getLineNumber(), is(1));

        // xml files
        assertThat(result.get(1).getProjectId(), is("test_examples_case2"));
        assertThat(result.get(1).getItems().size(), is(4));
    }

    @Test
    public void excludeJsonTest() throws URISyntaxException, IOException {
        List<SearchProjectModel> result = projectSearcher.search("test4");
        assertThat(result.size(), is(0));
    }

    @Test
    public void projectMatchesLimitTest() throws URISyntaxException, IOException {
        List<SearchProjectModel> result = projectSearcher.search("id");

        // limit by 10
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getProjectId(), is("test_examples_case1"));
        assertThat(result.get(0).getItems().size(), is(7));

        assertThat(result.get(1).getProjectId(), is("test_examples_case2"));
        assertThat(result.get(1).getItems().size(), is(10));
    }

}

