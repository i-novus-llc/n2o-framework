package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;

public class IncorrectMergeTypeException extends N2oException {

    private static final String MESSAGE = "Метаданная, заданная через ref-id=\"%s\" имеет несовместимый тип. Ожидаемый тип: <%s>.";

    public IncorrectMergeTypeException(String refId, String elementName) {
        super(String.format(MESSAGE, refId, elementName));
    }
}
