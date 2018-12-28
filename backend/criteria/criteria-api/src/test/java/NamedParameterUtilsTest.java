import org.junit.Test;
import net.n2oapp.criteria.api.util.NamedParameterUtils;
import net.n2oapp.criteria.api.util.QueryBlank;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User: operhod
 * Date: 16.04.14
 * Time: 12:58
 */
public class NamedParameterUtilsTest {

    @Test
    public void testParse() {
        List<String> namedParameters = NamedParameterUtils
                .parseNamedParameters("where date < :date.end and date > :date.begin");
        assert namedParameters.size() == 2;
        assert
                (namedParameters.get(0).equals("date.end") && namedParameters.get(1).equals("date.begin"))
                        || (namedParameters.get(0).equals("date.begin") && namedParameters.get(1).equals("date.end"));
        namedParameters = NamedParameterUtils
                .parseNamedParameters("where funding.id in (:funding*.id)");
        assert namedParameters.size() == 1;
        assert namedParameters.get(0).equals("funding*.id");

    }

    @Test
    public void testPrepareQueryWithUnappropriateLiterals() {
        String actual = "where date < :date.end and fundingService.id in (:fundingService[].id)";
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("date.end", "");
        args.put("fundingService[].id", "");
        HashMap<String, String> replacers = new HashMap<>();
        replacers.put(".", "DOT");
        replacers.put("[]", "");
        QueryBlank queryBuild = NamedParameterUtils.prepareQuery(actual, args, replacers);
        assert queryBuild.getQuery().equals("where date < :dateDOTend and fundingService.id in (:fundingServiceDOTid)");
        assert queryBuild.getArgs().size() == 2;
        assert queryBuild.getArgs().containsKey("dateDOTend");
        assert queryBuild.getArgs().containsKey("fundingServiceDOTid");
    }


    @Test
    public void testPrepareQueryWithNull() {
        String actual = "where date < :date.end and fundingService.id in (:fundingService[].id) and id = :id";
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("date.end", null);
        args.put("fundingService[].id", null);
        HashMap<String, String> replacers = new HashMap<>();
        replacers.put(".", "DOT");
        replacers.put("[]", "");
        QueryBlank queryBuild = NamedParameterUtils.prepareQuery(actual, args, replacers);
        assert queryBuild.getQuery().equals("where date < null and fundingService.id in (null) and id = null");
        assert queryBuild.getArgs().size() == 2;
        assert queryBuild.getArgs().containsKey("dateDOTend");
        assert queryBuild.getArgs().containsKey("fundingServiceDOTid");

        actual = ":gender.id is null or genderId=:gender.id or :gender.id = null";
        args = new HashMap<>();
        args.put("gender.id", null);
        queryBuild = NamedParameterUtils.prepareQuery(actual, args, Collections.emptyMap());
        assert queryBuild.getQuery().equals("null is null or genderId=null or null = null");
        assert queryBuild.getArgs().size() == 1;
        assert queryBuild.getArgs().containsKey("gender.id");

        //плейсхолдеры начинаются одинаково, и один из них в конце
        actual = ":gender is null or :gender.id is null or 1=:gender.id";
        args = new HashMap<>();
        args.put("gender.id", null);
        args.put("gender", null);
        queryBuild = NamedParameterUtils.prepareQuery(actual, args, Collections.emptyMap());
        assert queryBuild.getQuery().equals("null is null or null is null or 1=null");
        assert queryBuild.getArgs().size() == 2;
        assert queryBuild.getArgs().containsKey("gender.id");
        assert queryBuild.getArgs().containsKey("gender");

        //плейсхолдеры начинаются одинаково, и один из них не null
        actual = ":gender is null or :gender.id";
        args = new HashMap<>();
        args.put("gender.id", 1);
        args.put("gender", null);
        queryBuild = NamedParameterUtils.prepareQuery(actual, args, Collections.emptyMap());
        assert queryBuild.getQuery().equals("null is null or :gender.id");
        assert queryBuild.getArgs().size() == 2;
        assert queryBuild.getArgs().get("gender.id").equals(1);
        assert queryBuild.getArgs().containsKey("gender");
    }


    @Test
    /**
     * бывают ситуации когда один плейсхолдер является частью другого (пр. gender, genderId)
     * В таких случаях можно пользоваться сориторовкой плейсхолдеров by length
     * Примечание. Это удобно когда эти плейсхолдеры будут заменяться в исходной строке через replaceAll
     */
    public void testPlaceholdersOrder() {

        List<String> namedParameters = NamedParameterUtils
                .parseNamedParameters("where patient like :patient and patientId like > :patientId");
        //порядок как в строке
        assert namedParameters.get(0).equals("patient");
        assert namedParameters.get(1).equals("patientId");
        List<String> sortedNamedParameters = NamedParameterUtils.sortByLength(namedParameters);
        assert sortedNamedParameters.get(0).equals("patientId");
        assert sortedNamedParameters.get(1).equals("patient");
        assert !sortedNamedParameters.equals(namedParameters);
    }
}
