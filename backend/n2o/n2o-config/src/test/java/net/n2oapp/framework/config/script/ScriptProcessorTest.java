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
import static org.springframework.test.util.AssertionErrors.assertTrue;

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
        assertThat(ScriptProcessor.resolveLinks("Hello, {test == 1 ? 2 : test == 2 ? 3 : 4} . {test == 1 ? 2 : 3}"), is("`'Hello, '+(test == 1 ? 2 : test == 2 ? 3 : 4)+' . '+(test == 1 ? 2 : 3)`"));
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
        assertThat(ScriptProcessor.resolveExpression("Test {id} #{surname} {name} ${patr}"), is("`'Test '+id+' #{surname} '+name+' ${patr}'`"));
        assertThat(ScriptProcessor.resolveExpression("#{id}"), is("#{id}"));
        assertThat(ScriptProcessor.resolveExpression("${id}"), is("${id}"));
        assertThat(ScriptProcessor.resolveExpression("{gender*.id}"), is("`gender.map(function(t){return t.id})`"));
        assertThat(ScriptProcessor.resolveExpression("{name} test {gender*.id}"), is("`name+' test '+gender.map(function(t){return t.id})`"));
        assertThat(ScriptProcessor.resolveExpression("{test == 1}"), is("`test == 1`"));
        assertThat(ScriptProcessor.resolveExpression("Hello, {test + 1}"), is("`'Hello, '+test + 1`"));

        assertThat(ScriptProcessor.resolveArrayExpression("1"), is(Collections.singletonList(1)));
        assertThat(ScriptProcessor.resolveArrayExpression("1", "2"), is(Arrays.asList(1, 2)));
        assertThat(ScriptProcessor.resolveArrayExpression("Test1", "Test2"), is(Arrays.asList("Test1", "Test2")));
        assertThat(ScriptProcessor.resolveArrayExpression("true", "false"), is(Arrays.asList(true, false)));
        //todo реализовать для сложных вариантов
        //assertThat( scriptProcessor.resolveArrayExpression("{id}"), is("`[id]`"));
        //assertThat( scriptProcessor.resolveArrayExpression("{id1}", "{id2}"), is("`[id1,id2]`"));
        //assertThat( scriptProcessor.resolveArrayExpression("{id1}", "test"), is("`[id1,'test']`"));
        //assertThat( scriptProcessor.resolveArrayExpression("Test{id1}", "Test{id2}"), is("`['Test'+id1,'Test'+id2]`"));
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
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch), is("`status == 1 ? 'blue' : status == 2 ? 'red' : null`"));

        n2oSwitch.setDefaultCase("gray");
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch), is("`status == 1 ? 'blue' : status == 2 ? 'red' : 'gray'`"));

        cases.put(3, "{name == 'Нина' ? 'black' : 'white'}");
        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch),
                is("`status == 1 ? 'blue' : status == 2 ? 'red' : status == 3 ? (name == 'Нина' ? 'black' : 'white') : 'gray'`"));

        cases = new HashMap<>();
        cases.put("ok", "blue");
        cases.put("failed", "red");
        n2oSwitch.setResolvedCases(cases);
        n2oSwitch.setDefaultCase("gray");

        assertThat(ScriptProcessor.buildSwitchExpression(n2oSwitch), is("`status == 'ok' ? 'blue' : status == 'failed' ? 'red' : 'gray'`"));
    }


    @Test
    void testCreateFunctionCall() {
        assert ScriptProcessor.createFunctionCall("func").equals("func()");
        assert ScriptProcessor.createFunctionCall("func", "arg1").equals("func('arg1')");
        assert ScriptProcessor.createFunctionCall("func", "arg1", "arg2").equals("func('arg1','arg2')");
        assert ScriptProcessor.createFunctionCall("func", "arg1", "arg2").equals("func('arg1','arg2')");
        assert ScriptProcessor.createFunctionCall("func", "arg1", 1, true).equals("func('arg1',1,true)");

    }

    @Test
    void createSelfInvokingFunction() {
        assert ScriptProcessor.createSelfInvokingFunction("1==1").equals("function(){1==1}()");
    }

    @Test
    void testIfNotUndefined() {
        //строки
        String exp1 = ScriptProcessor.ifNotUndefined("id == 1", "id");
        assert exp1.equals("(typeof id === 'undefined') || (id == 1)");
        String exp2 = ScriptProcessor.ifNotUndefined("indiv.id == dep.org.id", "indiv.id", "dep.org.id");
        assert exp2.equals(
                "(typeof indiv === 'undefined') || " +
                        "(typeof indiv.id === 'undefined') || " +
                        "(typeof dep === 'undefined') || " +
                        "(typeof dep.org === 'undefined') || " +
                        "(typeof dep.org.id === 'undefined') || " +
                        "(indiv.id == dep.org.id)");

        //вычислялки
        assert ScriptProcessor.evalForBoolean(exp1, new DataSet());
        assert ScriptProcessor.evalForBoolean(exp1, new DataSet("id", 1));
        assert !ScriptProcessor.evalForBoolean(exp1, new DataSet("id", 2));

        assert ScriptProcessor.evalForBoolean(exp2, new DataSet());
        assert ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv", 1));
        assert ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1));
        assert ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1).add("dep.org.id", 1));
        assert !ScriptProcessor.evalForBoolean(exp2, new DataSet("indiv.id", 1).add("dep.org.id", 2));

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
        assert !(ScriptProcessor.evalForBoolean("bas script", new DataSet()));
        assert (ScriptProcessor.evalForBoolean("test", new DataSet("test", true)));
        assert !(ScriptProcessor.evalForBoolean("test", new DataSet("test", false)));
    }

    @Test
    void buildIsNullExpressionTest() {
        String exp = scriptProcessor.buildIsNullExpression("indiv.gender.id");
        assert exp.equals("(typeof indiv === 'undefined') || (typeof indiv.gender === 'undefined') || (typeof indiv.gender.id === 'undefined') || (indiv.gender.id === null)");
        exp = scriptProcessor.buildIsNullExpression("name");
        assert exp.equals("(typeof name === 'undefined') || (name === null)");
    }

    @Test
    void buildIsNotNullExpressionTest() {
        String exp = scriptProcessor.buildIsNotNullExpression("name");
        assert exp.equals("name != null");
    }

    @Test
    void buildMoreExpressionTest() {
        //числа
        String exp = scriptProcessor.buildMoreExpression("num", 5);
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("num", 6);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 9);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 4);
            assert !(Boolean) engine.eval(exp);
            engine.put("num", 5);
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }

    @Test
    void buildLessExpressionTest() {
        //числа
        String exp = scriptProcessor.buildLessExpression("num", 5);
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("num", 6);
            assert !(Boolean) engine.eval(exp);
            engine.put("num", 9);
            assert !(Boolean) engine.eval(exp);
            engine.put("num", 4);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 5);
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }


    @Test
    void buildInIntervalExpressionTest() {
        //числа
        String exp = scriptProcessor.buildInIntervalExpression("num", new Interval(1, 10));
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("num", 2);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 9);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 11);
            assert !(Boolean) engine.eval(exp);
            engine.put("num", 10);
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }


    @Test
    void buildInListExpressionTest() {
        String exp = scriptProcessor.buildInListExpression("name", Arrays.asList("John", "Marry"));
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("name", "John");
            assert (Boolean) engine.eval(exp);
            engine.put("name", "Marry");
            assert (Boolean) engine.eval(exp);
            engine.put("name", "Bobby");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
        exp = scriptProcessor.buildInListExpression("someBoolean", Arrays.asList(true, false));
        engine = getScriptEngine();
        try {
            engine.put("someBoolean", true);
            assert (Boolean) engine.eval(exp);
            engine.put("someBoolean", false);
            assert (Boolean) engine.eval(exp);
            engine.put("someBoolean", "s");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }

    @Test
    void buildLikeAndLikeStartExpressionTest() {
        String exp = scriptProcessor
                .buildLikeExpression("name", "est str");
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("name", "Test string");
            assert (Boolean) engine.eval(exp);
            engine.put("name", "Not");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }

        exp = scriptProcessor
                .buildLikeStartExpression("name", "Test");
        engine = getScriptEngine();
        try {
            engine.put("name", "Test string");
            assert (Boolean) engine.eval(exp);
            engine.put("name", "est string");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }

    @Test
    void buildNotInListExpressionTest() {
        String exp = scriptProcessor.buildNotInListExpression("name", Arrays.asList("John", "Marry"));
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("name", "John");
            assert !(Boolean) engine.eval(exp);
            engine.put("name", "Marry");
            assert !(Boolean) engine.eval(exp);
            engine.put("name", "Bobby");
            assert (Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }

    @Test
    void buildEqualExpressionTest() {
        String exp = scriptProcessor.buildEqualExpression("name", "John");
        assert exp.equals("name == 'John'");
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("name", "John");
            assert (Boolean) engine.eval(exp);
            engine.put("name", "Bobby");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
        exp = scriptProcessor.buildEqualExpression("num", 1);
        try {
            engine.put("num", 1);
            assert (Boolean) engine.eval(exp);
            engine.put("num", 2);
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
        Date value = new Date();
        exp = scriptProcessor.buildEqualExpression("date", value);
        String actualValue = new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT).format(value);
        try {
            engine.put("date", actualValue);
            assertTrue("", (Boolean) engine.eval(exp));
            engine.put("date", "01.01.1970 03:01");
            assert !(Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
    }


    @Test
    void buildNotEqualExpressionTest() {
        String exp = scriptProcessor.buildNotEqExpression("name", "John");
        assert exp.equals("name != 'John'");
        ScriptEngine engine = getScriptEngine();
        try {
            engine.put("name", "John");
            assert !(Boolean) engine.eval(exp);
            engine.put("name", "Bobby");
            assert (Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }
        exp = scriptProcessor.buildNotEqExpression("num", 1);
        try {
            engine.put("num", 1);
            assert !(Boolean) engine.eval(exp);
            engine.put("num", 2);
            assert (Boolean) engine.eval(exp);
        } catch (ScriptException e) {
            assert false;
        }

    }


    @Test
    void testVarExtractor() {
        String script = "a == 1 && a.b.c == 1 && b[0].c == 1";
        Set<String> names = ScriptProcessor.extractVars(script);
        assert 3 == names.size();
        assert names.contains("a");
        assert names.contains("a.b.c");
        assert names.contains("b[0].c");
    }

    @Test
    void testExtractPropertiesOf() {
        String script = "a == 1 && a.b.c == 1 && b.c == 1 && x.y == 1";
        Map<String, Set<String>> names = ScriptProcessor
                .extractPropertiesOf(script, Arrays.asList("a", "b"));
        assert 2 == names.size();
        assert names.get("a").contains("b");
        assert names.get("b").contains("c");
    }

    @Test
    void testAddContextFor() {
        String script = "a == 1 && a.b.c == 1 && b.c == 1 && x.y == 1";
        String modScript = ScriptProcessor.addContextFor(script, "z", Arrays.asList("a", "b"));
        System.out.println(modScript);
        assert modScript.contains("z.a == 1 && z.a.b.c == 1 && z.b.c == 1 && x.y == 1");
    }

    @Test
    void testAddContextForAll() {
        String script = "a == 1 && b == 1 && x.y == 1";
        String modScript = ScriptProcessor.addContextForAll(script, "z");
        assert modScript.contains("z.a == 1 && z.b == 1 && z.x.y == 1");
    }

    @Test
    void testSimplifyArrayLinks() {
        assert ScriptProcessor
                .simplifyArrayLinks("id, name, address.id, gender[0].id, gender[0].name")
                .equals("id, name, address.id, gender");
    }

    /*
     * test moment.js functions in scriptProcessor
     * */
    @Test
    void testAddMomentJs() throws ExecutionException, InterruptedException {
        String js = "moment(day, 'DD.MM.YYYY').format('DD-MM-YY');";
        MultiThreadRunner runner = new MultiThreadRunner();
        runner.run(() -> {
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
        runner.run(() -> {
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
    }

    @Test
    void testAddNumeralJs() throws ExecutionException, InterruptedException {
        String js = "var number = numeral(); number.set(num); var val = 100; var difference = number.difference(val); difference;";
        MultiThreadRunner runner = new MultiThreadRunner();
        runner.run(() -> {
            Random random = new Random();
            Double temp = Double.valueOf(random.nextInt(100000));
            DataSet dataSet = new DataSet();
            dataSet.put("num", temp);
            Double result = ScriptProcessor.eval(js, dataSet);
            Double diff = Math.abs(temp - Double.valueOf(100));
            if (!result.equals(diff))
                System.out.println("temp=" + temp + ", result = " + result + ", diff=" + diff);
            return result.equals(diff);
        });
    }

    /*
     * test that underscore.js functions are thread safe
     * */
    @Test
    void testAddUnderscoreJs() throws ExecutionException, InterruptedException {
        String js = "var sum = _.reduce(arr, function(memo, num){ return memo + num; }, 0); sum;";
        MultiThreadRunner runner = new MultiThreadRunner();
        runner.run(() -> {
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
            Double sum = Double.valueOf(summ);
            if (!result.equals(sum))
                System.out.println("temp=" + Arrays.asList(temp) + ", summ = " + sum + ", result=" + result);
            return result.equals(sum);
        });
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

    @Test
    void testReduce() {
        assertThat(ScriptProcessor.and(Arrays.asList("test1 || test2", "test3 || test4")), is("(test1 || test2) && (test3 || test4)"));
        assertThat(ScriptProcessor.and(Collections.singletonList("test1")), is("test1"));
        assertThat(ScriptProcessor.and(null), nullValue());

        assertThat(ScriptProcessor.or(Arrays.asList("test1 || test2", "test3 || test4")), is("(test1 || test2) || (test3 || test4)"));
        assertThat(ScriptProcessor.or(Arrays.asList("test1")), is("test1"));
        assertThat(ScriptProcessor.or(null), nullValue());
    }
}
