package net.n2oapp.framework.config;

import net.n2oapp.engine.factory.EngineNotFoundException;

public abstract class IncorrectXmlException extends EngineNotFoundException {

    protected IncorrectXmlException(Object type, String message) {
        super(type, message);
    }
}
