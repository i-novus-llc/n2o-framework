package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.processing.DataProcessing;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpringDataProcessingStack extends DataProcessingStack implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    protected List<DataProcessing> findModules() {
        List<DataProcessing> res = new ArrayList<>();
        for (Map.Entry<String, DataProcessing> entry : OverrideBean.removeOverriddenBeans(context.getBeansOfType(DataProcessing.class)).entrySet()) {
            DataProcessing module = entry.getValue();
            res.add(module);
        }
        return res;
    }
}
