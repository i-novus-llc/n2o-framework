package net.n2oapp.framework.config.io.query;

import net.n2oapp.framework.api.metadata.global.dao.query.field.N2oNormalizeSwitch;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;

public class NormalizeSwitchIO implements TypedElementIO<N2oNormalizeSwitch> {

	@Override
	public Class<N2oNormalizeSwitch> getElementClass() {
		return N2oNormalizeSwitch.class;
	}

	@Override
	public String getElementName() {
		return "switch";
	}

	@Override
	public void io(Element e, N2oNormalizeSwitch s, IOProcessor p) {
		p.childrenToStringMap(e, null, "case", "value", null, s::getCases, s::setCases);
		p.childrenText(e, "default", s::getDefaultCase, s::setDefaultCase);
	}
}
