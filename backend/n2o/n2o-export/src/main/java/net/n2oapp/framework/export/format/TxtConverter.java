package net.n2oapp.framework.export.format;

import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import net.n2oapp.properties.StaticProperties;

import java.util.List;

public class TxtConverter extends CsvConverter {


    public TxtConverter(List<N2oDataStreamingUtil.Field> fields) {
        super(fields);
        this.columnSeparator = StaticProperties.get("n2o.ui.export.txt.columnSeparator");
        this.headerIsEnabled = StaticProperties.isEnabled("n2o.ui.export.txt.header.enabled");
    }


}
