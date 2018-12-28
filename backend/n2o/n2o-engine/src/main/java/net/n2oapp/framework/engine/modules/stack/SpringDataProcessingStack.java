package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.engine.factory.integration.spring.SpringEngineFactory;
import net.n2oapp.framework.api.bean.LocatedBeanPack;
import net.n2oapp.framework.api.processing.N2oModule;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: operhod
 * Date: 23.11.13
 * Time: 16:13
 */
public class SpringDataProcessingStack extends DataProcessingStack implements ApplicationContextAware {
    private ApplicationContext context;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    @Override
    protected List<N2oModule> findModules() {
        List<N2oModule> res = new ArrayList<>();
        for (Map.Entry<String, N2oModule> entry : OverrideBean.removeOverriddenBeans(context.getBeansOfType(N2oModule.class)).entrySet()) {
            N2oModule module = entry.getValue();
            module.setId(entry.getKey());
            res.add(module);
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<LocatedBeanPack<N2oModule>> findModulePacks() {
        List<LocatedBeanPack<N2oModule>> res = new ArrayList<>();
        for (Map.Entry<String, LocatedBeanPack> entry : context.getBeansOfType(LocatedBeanPack.class).entrySet()) {
            LocatedBeanPack pack = entry.getValue();
            if (N2oModule.class.isAssignableFrom(pack.getBeanClass()))
                res.add(pack);
        }
        return res;
    }


}
