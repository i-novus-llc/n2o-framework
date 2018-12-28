package net.n2oapp.framework.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import net.n2oapp.properties.StaticProperties;
import net.n2oapp.data.streaming.converter.impl.csv.CsvConverterBase;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CsvConverter extends CsvConverterBase<DataSet> {

    protected List<N2oDataStreamingUtil.Field> fields;
    protected boolean headerIsEnabled = StaticProperties.isEnabled("n2o.ui.export.csv.header.enabled");

    public CsvConverter(List<N2oDataStreamingUtil.Field> fields) {
        this.fields = fields;
        this.columnSeparator = StaticProperties.get("n2o.ui.export.csv.columnSeparator");
    }


    @Override
    public byte[] getOpening() {
        if (headerIsEnabled) {
            StringBuilder result = new StringBuilder();
            boolean begin = true;
            for (N2oDataStreamingUtil.Field field : fields) {

                if (!begin)
                    result.append(columnSeparator);
                begin = false;

                result.append('"').append(field.getLabel()).append('"');
            }

            result.append("\r\n");
            return result.toString().getBytes(Charset.forName(encoding));
        }
        return super.getOpening();
    }


    @Override
    protected Collection<String> retrieveValues(DataSet dataSet) {
        return fields.stream()
                .map(field -> toString(dataSet.get(field.getId()), field))
                .collect(toList());
    }

    private static String toString(Object value, N2oDataStreamingUtil.Field field) {

        if (value == null)
            return null;

        String res = N2oDataStreamingUtil.toString(value);
        if ("string".equals(field.getDomain()))
            res = '"' + res + '"';
        return res;
    }
}
