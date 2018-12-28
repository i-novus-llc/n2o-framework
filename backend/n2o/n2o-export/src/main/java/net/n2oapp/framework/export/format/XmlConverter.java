package net.n2oapp.framework.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import net.n2oapp.data.streaming.converter.impl.xml.XmlConverterBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XmlConverter extends XmlConverterBase<DataSet> {

    private List<N2oDataStreamingUtil.Field> fields;

    public XmlConverter(List<N2oDataStreamingUtil.Field> fields) {
        this.fields = fields;
    }

    @Override
    protected Collection<Attribute> retrieveRows(DataSet dataSet) {
        List<Attribute> res = new ArrayList<>();
        for (N2oDataStreamingUtil.Field field : fields) {
            Object value = dataSet.get(field.getId());
            if (value != null)
                res.add(new Attribute(field.getId(), N2oDataStreamingUtil.toString(value)));
        }
        return res;
    }
}
