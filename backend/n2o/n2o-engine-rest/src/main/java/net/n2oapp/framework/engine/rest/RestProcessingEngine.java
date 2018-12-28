package net.n2oapp.framework.engine.rest;

import net.n2oapp.engine.factory.simple.BeanListAware;
import net.n2oapp.properties.StaticProperties;

import java.util.Map;


/**
 * @author operehod
 * @since 30.05.2015
 */
public class RestProcessingEngine extends BeanListAware<RestProcessing> {


    public void process(String method, String query, Map<String, Object> args, Map<String, String> headers) {
        if (StaticProperties.isEnabled("n2o.engine.rest.processing.enabled"))
            for (RestProcessing processing : getBeans()) {
                processing.process(method, query, args, headers);
            }
    }


    @Override
    public Class<RestProcessing> getBeanClass() {
        return RestProcessing.class;
    }
}
