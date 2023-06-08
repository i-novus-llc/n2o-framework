package net.n2oapp.framework.api.metadata.global.dao.query.field;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

import java.util.Map;

@Getter
@Setter
public class N2oNormalizeSwitch implements Source {

	private Map<String, String> cases;
	private String defaultCase;
}
