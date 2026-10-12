package net.n2oapp.framework.config.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

class StylesResolverTest {

    @Test
    void test() {
        Map<String, String> oneStyle = StylesResolver.resolveStyles("margin-top: 16px; ");
        assertThat(oneStyle.size(), is(1));
        assertThat(oneStyle.get("marginTop"), is("16px"));

        assertThat(StylesResolver.resolveStylesToString("margin-top: 16px;"), is("marginTop:16px"));

        Map<String, String> twoStyles = StylesResolver.resolveStyles("box-shadow: 0 0 1px #fff,0 0 5px rgba(0,0,0,0.3);    padding: 3px 5px");
        assertThat(twoStyles.size(), is(2));
        assertThat(twoStyles.get("boxShadow"), is("0 0 1px #fff,0 0 5px rgba(0,0,0,0.3)"));
        assertThat(twoStyles.get("padding"), is("3px 5px"));

        assertThat(StylesResolver.resolveStyles(" "), nullValue());
        assertThat(StylesResolver.resolveStyles(null), nullValue());

        Map<String, String> v = StylesResolver.resolveStyles("   background-color : red   ;   padding  :   30px   30px   30px   30px   ");
        assertThat(v.size(), is(2));
        assertThat(v.get("backgroundColor"), is("red"));
        assertThat(v.get("padding"), is("30px 30px 30px 30px"));
    }

    @Test
    void invalidInputTest() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> StylesResolver.resolveStyles("box-shadow: illegal : argument;")
        );
    }

    @Test
    void validInputTest() {
        // проверяем не только отсутствие исключения, но и корректность полученного значения
        Map<String, String> styles = StylesResolver.resolveStyles(
                "background: #fff url('https://thumbs.dreamstime.com/b/abstract-geometric-hexagon-pattern-background-blue-grey-honeycomb-design-427607203.jpg?w=992') repeat-x;");
        assertThat(styles.size(), is(1));
        assertThat(styles.get("background"),
                is("#fff url('https://thumbs.dreamstime.com/b/abstract-geometric-hexagon-pattern-background-blue-grey-honeycomb-design-427607203.jpg?w=992') repeat-x"));
    }

    @Test
    void edgeCasesTest() {
        // двоеточие внутри url(): без кавычек, в одинарных и в двойных
        Map<String, String> unquotedUrl = StylesResolver.resolveStyles("background: #fff url(http://example.org/a.png?w=99) repeat-x;");
        assertThat(unquotedUrl.size(), is(1));
        assertThat(unquotedUrl.get("background"), is("#fff url(http://example.org/a.png?w=99) repeat-x"));

        Map<String, String> singleQuotedUrl = StylesResolver.resolveStyles("background: #fff url('http://example.org/a.png?w=99') repeat-x;");
        assertThat(singleQuotedUrl.size(), is(1));
        assertThat(singleQuotedUrl.get("background"), is("#fff url('http://example.org/a.png?w=99') repeat-x"));

        Map<String, String> doubleQuotedUrl = StylesResolver.resolveStyles("background: #fff url(\"http://example.org/a.png\") repeat-x;");
        assertThat(doubleQuotedUrl.size(), is(1));
        assertThat(doubleQuotedUrl.get("background"), is("#fff url(\"http://example.org/a.png\") repeat-x"));

        // точка с запятой внутри скобок и внутри кавычек не разделяет объявления
        Map<String, String> dataUrl = StylesResolver.resolveStyles("background-image: url(data:image/svg+xml;base64,PHN2Zz4=); padding: 3px");
        assertThat(dataUrl.size(), is(2));
        assertThat(dataUrl.get("backgroundImage"), is("url(data:image/svg+xml;base64,PHN2Zz4=)"));
        assertThat(dataUrl.get("padding"), is("3px"));

        Map<String, String> quotedSemicolon = StylesResolver.resolveStyles("content: 'a; b'; padding: 3px");
        assertThat(quotedSemicolon.size(), is(2));
        assertThat(quotedSemicolon.get("content"), is("'a; b'"));
        assertThat(quotedSemicolon.get("padding"), is("3px"));

        // пустые объявления допустимы по стандарту css и игнорируются
        Map<String, String> emptyDeclarations = StylesResolver.resolveStyles(";; margin-top: 16px;;  ;padding: 3px;;");
        assertThat(emptyDeclarations.size(), is(2));
        assertThat(emptyDeclarations.get("marginTop"), is("16px"));
        assertThat(emptyDeclarations.get("padding"), is("3px"));

        assertThat(StylesResolver.resolveStyles(" ; ;; "), nullValue());

        // схлопываются любые пробельные символы, а не только пробел
        Map<String, String> whitespace = StylesResolver.resolveStyles("margin\t-top:\n\t16px\t 0;");
        assertThat(whitespace.size(), is(1));
        assertThat(whitespace.get("marginTop"), is("16px 0"));

        // объявление без двоеточия и лишнее двоеточие вне кавычек - ошибка
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> StylesResolver.resolveStyles("margin-top 16px;")
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> StylesResolver.resolveStyles("box-shadow: illegal : argument\";")
        );
    }
}