package net.n2oapp.framework.config.script;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.util.async.MultiThreadRunner;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScriptProcessorTest {

    private final ScriptProcessor scriptProcessor = new ScriptProcessor();

    private ScriptEngine getScriptEngine() {
        return ScriptProcessor.getScriptEngine();
    }

    @Test
    void resolveLinks() {
        assertThat(ScriptProcessor.resolveLinks("test"), is("test"));
        assertThat(ScriptProcessor.resolveLinks("{test}"), is("`test`"));
        assertThat(ScriptProcessor.resolveLinks("Hello, {test}"), is("`'Hello, '+test`"));
        assertThat(ScriptProcessor.resolveLinks("${test}"), is("${test}"));
        assertThat(ScriptProcessor.resolveLinks("#{test}"), is("#{test}"));
        assertThat(ScriptProcessor.resolveLinks("`test`"), is("`test`"));
        assertThat(ScriptProcessor.resolveLinks("true"), is("true"));
        assertThat(ScriptProcessor.resolveLinks("false"), is("false"));
        assertThat(ScriptProcessor.resolveLinks("Hello, {test == 1 ? 2 : test == 2 ? 3 : 4} . {test == 1 ? 2 : 3}"),
                is("`'Hello, '+(test == 1 ? 2 : test == 2 ? 3 : 4)+' . '+(test == 1 ? 2 : 3)`"));
    }

    @Test
    void testResolveExpression() {
        assertThat(ScriptProcessor.resolveExpression("{id}"), is("`id`"));
        assertThat(ScriptProcessor.resolveExpression("1"), is(1));
        assertThat(ScriptProcessor.resolveExpression("true"), is(true));
        assertThat(ScriptProcessor.resolveExpression("1,2"), is("1,2"));
        assertThat(ScriptProcessor.resolveExpression("test"), is("test"));
        assertThat(ScriptProcessor.resolveExpression("`1+1`"), is("`1+1`"));
        assertThat(ScriptProcessor.resolveExpression("Test{id}"), is("`'Test'+id`"));
        assertThat(ScriptProcessor.resolveExpression("Test {surname} {name}"), is("`'Test '+surname+' '+name`"));
        assertThat(ScriptProcessor.resolveExpression("Test {id} #{surname} {name} ${patr}"),
                is("`'Test '+id+' #{surname} '+name+' ${patr}'`"));
        assertThat(ScriptProcessor.resolveExpression("#{id}"), is("#{id}"));
        assertThat(ScriptProcessor.resolveExpression("${id}"), is("${id}"));
        assertThat(ScriptProcessor.resolveExpression("{gender*.id}"), is("`gender.map(function(t){return t.id})`"));
        assertThat(ScriptProcessor.resolveExpression("{name} test {gender*.id}"),
                is("`name+' test '+gender.map(function(t){return t.id})`"));
        assertThat(ScriptProcessor.resolveExpression("{test == 1}"), is("`test == 1`"));
        assertThat(ScriptProcessor.resolveExpression("Hello, {test + 1}"), is("`'Hello, '+test + 1`"));

        assertThat(ScriptProcessor.resolveArrayExpression("1"), is(Collections.singletonList(1)));
        assertThat(ScriptProcessor.resolveArrayExpression("1", "2"), is(Arrays.asList(1, 2)));
        assertThat(ScriptProcessor.resolveArrayExpression("Test1", "Test2"), is(Arrays.asList("Test1", "Test2")));
        assertThat(ScriptProcessor.resolveArrayExpression("true", "false"), is(Arrays.asList(true, false)));
    }

    @Test
    void invertExpression() {
        assertThat(ScriptProcessor.invertExpression("{check}"), is("`!(check)`"));
        assertThat(ScriptProcessor.invertExpression("true"), is(false));
        assertThat(ScriptProcessor.invertExpression("false"), is(true));
    }

    @Test
    void testResolveFunction() {
        assertThat(ScriptProcessor.resolveFunction("if (gender.id = 1) return 'М'; else return 'Ж';"),
                is("(function(){if (gender.id = 1) return 'М'; else return 'Ж';}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("gender.id == 1"),
                is("gender.id == 1"));
        assertThat(ScriptProcessor.resolveFunction("function(){if (gender.id = 1) return 'М'; else return 'Ж';}"),
                is("(function(){if (gender.id = 1) return 'М'; else return 'Ж';}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("function () {function helper() \n { return Math.random(); } const a = helper() return a;}"),
                is("(function () {function helper() \n { return Math.random(); } const a = helper() return a;}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("(function(){ '123'; })()"),
                is("(function(){ '123'; })()"));
        assertThat(ScriptProcessor.resolveFunction(ScriptProcessor.resolveFunction("gender.id == 1")),
                is("gender.id == 1"));
        assertThat(ScriptProcessor.resolveFunction("function helper() {return Math.random();} const a = helper() return a;"),
                is("(function(){function helper() {return Math.random();} const a = helper() return a;}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("['USER','ADMIN'].some(function(p) { return roles.includes(p) })"),
                is("['USER','ADMIN'].some(function(p) { return roles.includes(p) })"));
        assertThat(ScriptProcessor.resolveFunction("test.some(function(p) { return roles.includes(p) })"),
                is("test.some(function(p) { return roles.includes(p) })"));
        assertThat(ScriptProcessor.resolveFunction(" return multi.slice(0, index).concat(multi.slice(index + 1))"),
                is("(function(){return multi.slice(0, index).concat(multi.slice(index + 1))}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction(" const result = [...input]; return result.sort(function(first, second) { return first - second });"),
                is("(function(){const result = [...input]; return result.sort(function(first, second) { return first - second });}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("i1 ? i1.filter( x => { return x % 2 }) : 'empty list'"),
                is("i1 ? i1.filter( x => { return x % 2 }) : 'empty list'"));
        assertThat(ScriptProcessor.resolveFunction("i1 ? i1.filter( function(x) { return x % 2 }) : 'empty list'"),
                is("i1 ? i1.filter( function(x) { return x % 2 }) : 'empty list'"));
        assertThat(ScriptProcessor.resolveFunction("list.filter(function(x) { return x >= 5 })"),
                is("list.filter(function(x) { return x >= 5 })"));
        assertThat(ScriptProcessor.resolveFunction("if(type && type.id === 2) { return null; } else { return anotherName; }"),
                is("(function(){if(type && type.id === 2) { return null; } else { return anotherName; }}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("typeof isModifyMode != 'undefined' &amp;&amp; isModifyMode != true &amp;&amp; notIdentified != true"),
                is("typeof isModifyMode != 'undefined' &amp;&amp; isModifyMode != true &amp;&amp; notIdentified != true"));
        assertThat(ScriptProcessor.resolveFunction("isModifyMode != true &amp;&amp; notIdentified != true"),
                is("isModifyMode != true &amp;&amp; notIdentified != true"));
        assertThat(ScriptProcessor.resolveFunction("!isModifyMode &amp;&amp; !notIdentified"),
                is("!isModifyMode &amp;&amp; !notIdentified"));
        assertThat(ScriptProcessor.resolveFunction("let num = 5; alert(num)"),
                is("(function(){let num = 5; alert(num)}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("for(let i = 0; i<10; i++) {} alert(i)"),
                is("(function(){for(let i = 0; i<10; i++) {} alert(i)}).call(this)"));
        assertThat(ScriptProcessor.resolveFunction("while(i < 10) {} alert(i)"),
                is("(function(){while(i < 10) {} alert(i)}).call(this)"));
    }

    @Test
    void testBuildExpressionForSwitch() {
        N2oSwitch n2oSwitch = new N2oSwitch();
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch), nullValue());
        n2oSwitch.setValueFieldId("status");
        Map<Object, String> cases = new HashMap<>();
        cases.put(1, "blue");
        cases.put(2, "red");
        n2oSwitch.setResolvedCases(cases);
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch),
                is("`status == 1 ? 'blue' : status == 2 ? 'red' : null`"));

        n2oSwitch.setDefaultCase("gray");
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch),
                is("`status == 1 ? 'blue' : status == 2 ? 'red' : 'gray'`"));

        cases.put(3, "{name == 'Нина' ? 'black' : 'white'}");
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch),
                is("`status == 1 ? 'blue' : status == 2 ? 'red' : status == 3 ? (name == 'Нина' ? 'black' : 'white') : 'gray'`"));

        cases = new HashMap<>();
        cases.put("ok", "blue");
        cases.put("failed", "red");
        n2oSwitch.setResolvedCases(cases);
        n2oSwitch.setDefaultCase("gray");

        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch),
                is("`status == 'ok' ? 'blue' : status == 'failed' ? 'red' : 'gray'`"));
    }


    @Test
    void testCreateFunctionCall() {
        assertThat(ScriptProcessor.createFunctionCall("func"), is("func()"));
        assertThat(ScriptProcessor.createFunctionCall("func", "arg1"), is("func('arg1')"));
        assertThat(ScriptProcessor.createFunctionCall("func", "arg1", "arg2"), is("func('arg1','arg2')"));
        assertThat(ScriptProcessor.createFunctionCall("func", "arg1", "arg2"), is("func('arg1','arg2')"));
        assertThat(ScriptProcessor.createFunctionCall("func", "arg1", 1, true), is("func('arg1',1,true)"));
    }

    @Test
    void createSelfInvokingFunction() {
        assertThat(ScriptProcessor.createSelfInvokingFunction("1==1"), is("function(){1==1}()"));
    }

    @Test
    void testIfNotUndefined() {
        //строки
        String exp1 = ScriptProcessor.ifNotUndefined("id == 1", "id");
        assertThat(exp1, is("(typeof id === 'undefined') || (id == 1)"));
        String exp2 = ScriptProcessor.ifNotUndefined("indiv.id == dep.org.id", "indiv.id", "dep.org.id");
        assertThat(exp2, is(
                "(typeof indiv === 'undefined') || " +
                        "(typeof indiv.id === 'undefined') || " +
                        "(typeof dep === 'undefined') || " +
                        "(typeof dep.org === 'undefined') || " +
                        "(typeof dep.org.id === 'undefined') || " +
                        "(indiv.id == dep.org.id)"));

        //вычислялки
        assertTrue(ScriptProcessor.evalForBoolean(exp1, new DataSet()));
        assertTrue(ScriptProcessor.evalForBoolean(exp1, new DataSet("id", 1)));
        assertFalse(ScriptProcessor.evalForBoolean(exp1, new DataSet("id", 2)));

        assertTrue(ScriptProcessor.evalForBoolean(exp2, new DataSet()));
        assertTrue(ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv", 1)));
        assertTrue(ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1)));
        assertTrue(ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1).add("dep.org.id", 1)));
        assertFalse(ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1).add("dep.org.id", 2)));
    }

    @Test
    void testEvalForBoolean() {
        assertOnException(() -> {
            try {
                ScriptProcessor.eval("bas script", new DataSet());
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }, RuntimeException.class);
        assertFalse(ScriptProcessor.evalForBoolean("bas script", new DataSet()));
        assertTrue(ScriptProcessor.evalForBoolean("test", new DataSet("test", true)));
        assertTrue(ScriptProcessor.evalForBoolean("test", new DataSet("test", "some value")));
        assertFalse(ScriptProcessor.evalForBoolean("test", new DataSet("test", false)));
    }

    @Test
    void buildIsNullExpressionTest() {
        String exp = scriptProcessor.buildIsNullExpression("indiv.gender.id");
        String str = "(typeof indiv === 'undefined') || (typeof indiv.gender === 'undefined') || (typeof indiv.gender.id === 'undefined') || (indiv.gender.id === null)";
        assertEquals(str, exp);
        exp = scriptProcessor.buildIsNullExpression("name");
        assertEquals(("(typeof name === 'undefined') || (name === null)"), exp);
    }

    @Test
    void buildIsNotNullExpressionTest() {
        String exp = scriptProcessor.buildIsNotNullExpression("name");
        assertEquals("name != null", exp);
    }

    @Test
    void buildMoreExpressionTest() throws ScriptException {
        //числа
        String exp = scriptProcessor.buildMoreExpression("num", 5);
        ScriptEngine engine = getScriptEngine();

        engine.put("num", 6);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 9);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 4);
        assertFalse((Boolean) engine.eval(exp));
        engine.put("num", 5);
        assertFalse((Boolean) engine.eval(exp));
    }

    @Test
    void buildLessExpressionTest() throws ScriptException {
        //числа
        String exp = scriptProcessor.buildLessExpression("num", 5);
        ScriptEngine engine = getScriptEngine();
        engine.put("num", 6);
        assertFalse((Boolean) engine.eval(exp));
        engine.put("num", 9);
        assertFalse((Boolean) engine.eval(exp));
        engine.put("num", 4);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 5);
        assertFalse((Boolean) engine.eval(exp));
    }


    @Test
    void buildInIntervalExpressionTest() throws ScriptException {
        //числа
        String exp = scriptProcessor.buildInIntervalExpression("num", new Interval<>(1, 10));
        ScriptEngine engine = getScriptEngine();
        engine.put("num", 2);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 9);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 11);
        assertFalse((Boolean) engine.eval(exp));
        engine.put("num", 10);
        assertFalse((Boolean) engine.eval(exp));
    }


    @Test
    void buildInListExpressionTest() throws ScriptException {
        String exp = scriptProcessor.buildInListExpression("name", Arrays.asList("John", "Marry"));
        ScriptEngine engine = getScriptEngine();
        engine.put("name", "John");
        assertTrue((Boolean) engine.eval(exp));
        engine.put("name", "Marry");
        assertTrue((Boolean) engine.eval(exp));
        engine.put("name", "Bobby");
        assertFalse((Boolean) engine.eval(exp));

        exp = scriptProcessor.buildInListExpression("someBoolean", Arrays.asList(true, false));
        engine = getScriptEngine();
        engine.put("someBoolean", true);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("someBoolean", false);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("someBoolean", "s");
        assertFalse((Boolean) engine.eval(exp));
    }

    @Test
    void buildLikeAndLikeStartExpressionTest() throws ScriptException {
        String exp = scriptProcessor
                .buildLikeExpression("name", "est str");
        ScriptEngine engine = getScriptEngine();
        engine.put("name", "Test string");
        assertTrue((Boolean) engine.eval(exp));
        engine.put("name", "Not");
        assertFalse((Boolean) engine.eval(exp));

        exp = scriptProcessor
                .buildLikeStartExpression("name", "Test");
        engine = getScriptEngine();
        engine.put("name", "Test string");
        assertTrue((Boolean) engine.eval(exp));
        engine.put("name", "est string");
        assertFalse((Boolean) engine.eval(exp));
    }

    @Test
    void buildNotInListExpressionTest() throws ScriptException {
        String exp = scriptProcessor.buildNotInListExpression("name", Arrays.asList("John", "Marry"));
        ScriptEngine engine = getScriptEngine();
        engine.put("name", "John");
        assertFalse((Boolean) engine.eval(exp));
        engine.put("name", "Marry");
        assertFalse((Boolean) engine.eval(exp));
        engine.put("name", "Bobby");
        assertTrue((Boolean) engine.eval(exp));
    }

    @Test
    void buildEqualExpressionTest() throws ScriptException {
        String exp = scriptProcessor.buildEqualExpression("name", "John");
        assertEquals("name == 'John'", exp);
        ScriptEngine engine = getScriptEngine();
        engine.put("name", "John");
        assertTrue((Boolean) engine.eval(exp));
        engine.put("name", "Bobby");
        assertFalse((Boolean) engine.eval(exp));

        exp = scriptProcessor.buildEqualExpression("num", 1);
        engine.put("num", 1);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("num", 2);
        assertFalse((Boolean) engine.eval(exp));

        Date value = new Date();
        exp = scriptProcessor.buildEqualExpression("date", value);
        String actualValue = new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT).format(value);
        engine.put("date", actualValue);
        assertTrue((Boolean) engine.eval(exp));
        engine.put("date", "01.01.1970 03:01");
        assertFalse((Boolean) engine.eval(exp));
    }

    @Test
    void buildNotEqualExpressionTest() throws ScriptException {
        String exp = scriptProcessor.buildNotEqExpression("name", "John");
        assertEquals("name != 'John'", exp);
        ScriptEngine engine = getScriptEngine();
        engine.put("name", "John");
        assertFalse((Boolean) engine.eval(exp));
        engine.put("name", "Bobby");
        assertTrue((Boolean) engine.eval(exp));

        exp = scriptProcessor.buildNotEqExpression("num", 1);
        engine.put("num", 1);
        assertFalse((Boolean) engine.eval(exp));
        engine.put("num", 2);
        assertTrue((Boolean) engine.eval(exp));
    }

    @Test
    void testVarExtractor() {
        String script = "a == 1 && a.b.c == 1 && b[0].c == 1";
        Set<String> names = ScriptProcessor.extractVars(script);
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("a.b.c"));
        assertTrue(names.contains("b[0].c"));
    }

    @Test
    void testExtractPropertiesOf() {
        String script = "a == 1 && a.b.c == 1 && b.c == 1 && x.y == 1";
        Map<String, Set<String>> names = ScriptProcessor
                .extractPropertiesOf(script, Arrays.asList("a", "b"));
        assertEquals(2, names.size());
        assertTrue(names.get("a").contains("b"));
        assertTrue(names.get("b").contains("c"));
    }

    @Test
    void testAddContextFor() {
        String script = "a == 1 && a.b.c == 1 && b.c == 1 && x.y == 1";
        String modScript = ScriptProcessor.addContextFor(script, "z", Arrays.asList("a", "b"));
        System.out.println(modScript);
        assertTrue(modScript.contains("z.a == 1 && z.a.b.c == 1 && z.b.c == 1 && x.y == 1"));
    }

    @Test
    void testAddContextForAll() {
        String script = "a == 1 && b == 1 && x.y == 1";
        String modScript = ScriptProcessor.addContextForAll(script, "z");
        assertTrue(modScript.contains("z.a == 1 && z.b == 1 && z.x.y == 1"));
    }

    @Test
    void testSimplifyArrayLinks() {
        assertEquals("id, name, address.id, gender", ScriptProcessor
                .simplifyArrayLinks("id, name, address.id, gender[0].id, gender[0].name"));
    }

    /*
     * test moment.js functions in scriptProcessor
     * */
    @Test
    void testAddMomentJs() throws ExecutionException, InterruptedException {
        String js = "moment(day, 'DD.MM.YYYY').format('DD-MM-YY');";
        MultiThreadRunner runner = new MultiThreadRunner();
        int errorCount = runner.run(() -> {
            Random random = new Random();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yy");
            Calendar day = new GregorianCalendar();
            day.add(Calendar.DAY_OF_YEAR, random.nextInt(731));
            DataSet dataSet = new DataSet();
            dataSet.put("day", format.format(day.getTime()));
            String result = ScriptProcessor.eval(js, dataSet);
            return result.equals(format1.format(day.getTime()));
        });
        assertThat(errorCount, is(0));
    }

    /*
     * test globalDateFuncs.js functions in scriptProcessor
     * */
    @Test
    void testGlobalDateFuncsJs() throws ExecutionException, InterruptedException {
        String nowJs = "Date.now()";
        String todayJs = "(new Date()).getTime()";
        String yesterdayJs = "(new Date(new Date() - 1)).getTime()";
        MultiThreadRunner runner = new MultiThreadRunner();
        int errorCount = runner.run(() -> {
            Calendar today = new GregorianCalendar();
            Calendar yesterday = new GregorianCalendar();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            DataSet dataSet = new DataSet();
            long nowRes = ((Double) ScriptProcessor.eval(nowJs, dataSet)).longValue();
            long todayRes = ((Double) ScriptProcessor.eval(todayJs, dataSet)).longValue();
            long yesterdayRes = ((Double) ScriptProcessor.eval(yesterdayJs, dataSet)).longValue();
            return nowRes == today.getTimeInMillis() && todayRes == today.getTimeInMillis()
                    && yesterdayRes == yesterday.getTimeInMillis();
        });
        assertThat(errorCount, is(50));
    }

    @Test
    void testAddNumeralJs() throws ExecutionException, InterruptedException {
        String js = "var number = numeral(); number.set(num); var val = 100; var difference = number.difference(val); difference;";
        MultiThreadRunner runner = new MultiThreadRunner();
        int errorCount = runner.run(() -> {
            Random random = new Random();
            double temp = random.nextInt(100000);
            DataSet dataSet = new DataSet();
            dataSet.put("num", temp);
            Double result = ScriptProcessor.eval(js, dataSet);
            Double diff = Math.abs(temp - 100.0);
            if (!result.equals(diff))
                System.out.println("temp=" + temp + ", result = " + result + ", diff=" + diff);
            return result.equals(diff);
        });
        assertThat(errorCount, is(0));
    }

    /*
     * test that underscore.js functions are thread safe
     * */
    @Test
    void testAddUnderscoreJs() throws ExecutionException, InterruptedException {
        String js = "var sum = _.reduce(arr, function(memo, num){ return memo + num; }, 0); sum;";
        MultiThreadRunner runner = new MultiThreadRunner();
        int errorCount = runner.run(() -> {
            Random random = new Random();
            int[] temp = new int[10];
            int summ = 0;
            for (int i = 0; i < 10; i++) {
                temp[i] = random.nextInt(100);
                summ = summ + temp[i];
            }
            DataSet dataSet = new DataSet();
            dataSet.put("arr", temp);
            Double result = ScriptProcessor.eval(js, dataSet);
            Double sum = (double) summ;
            if (!result.equals(sum))
                System.out.println("temp=" + List.of(temp) + ", summ = " + sum + ", result=" + result);
            return result.equals(sum);
        });
        assertThat(errorCount, is(0));
    }

    @Test
    void testCustomFunctions() throws ScriptException {
        //moment
        assertThat(ScriptProcessor.eval("moment('06.02.2019').format('DD.MM.YYYY')", new DataSet()), is("02.06.2019"));
        //numeral
        assertThat(ScriptProcessor.eval("numeral(1.5).format('0.00')", new DataSet()), is("1.50"));
        //lodash
        assertThat(ScriptProcessor.eval("_.join(['a', 'b', 'c'], '~')", new DataSet()), is("a~b~c"));
    }
}