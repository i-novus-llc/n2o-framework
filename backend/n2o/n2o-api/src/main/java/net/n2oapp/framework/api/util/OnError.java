package net.n2oapp.framework.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: operehod
 * Date: 03.03.2015
 * Time: 11:34
 */
public interface OnError {

    Logger logger = LoggerFactory.getLogger(OnError.class);

    void onError(Exception e);

    OnError NOTHING = e -> logger.error(e.getMessage(), e);

    OnError THROW = e -> {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        throw new RuntimeException(e);
    };

}
