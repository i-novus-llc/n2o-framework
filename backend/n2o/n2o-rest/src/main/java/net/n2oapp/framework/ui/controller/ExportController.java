package net.n2oapp.framework.ui.controller;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportResponse;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;


@Controller
public class ExportController {

    public ExportResponse export(Map<String, String[]> parameters, String type) {
        ExportResponse response = new ExportResponse();
        DataSet params = getParams(parameters);

        switch (type) {
            case "csv":
                response.setFileName(createScv(params));
        }

        if (response.getFileName() != null)
            response.setStatus(400);
        return response;
    }

    private String createScv(DataSet params) {
        Workbook wkb = new Workbook();

        // Доступ к первому рабочему листу рабочей книги.
        Worksheet worksheet = wkb.getWorksheets().get(0);

        // Добавьте соответствующий контент в ячейку
        params.forEach((k, v) -> worksheet.getCells().get(k).putValue(v));

        try {
            wkb.save("Excel.csv");
            return "Excel.csv";
        } catch (Exception e) {
            return null;
        }
    }

    private DataSet getParams(Map<String, String[]> queryParams) {
        DataSet data = new DataSet();
        if (queryParams != null)
            queryParams.forEach((k, v) -> data.put(k, v.length == 1 ? v[0] : Arrays.asList(v)));
        return data;
    }
}
