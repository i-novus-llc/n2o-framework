package net.n2oapp.register.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 15:48
 */
public class GlobalMessageSource extends ResourceBundleMessageSource implements InitializingBean {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected static final String NOTE = "_note";

    protected String namesPackage;

    @Override
    public void afterPropertiesSet() throws Exception {
        initMessageSource(namesPackage);
    }

    protected void initMessageSource(String namesPackage) {
        Set<String> fileNames = new HashSet<String>();
        try {
            PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
            String pack = (!namesPackage.endsWith("/") ? namesPackage + "/" : namesPackage);
            Resource[] resources = r.getResources("classpath*:" + pack + "*.properties");
            for (Resource resource : resources) {
                int endIdx = resource.getFilename().indexOf('_');
                if (endIdx < 0) {
                    endIdx = resource.getFilename().indexOf(".properties");
                }
                fileNames.add(pack + resource.getFilename().substring(0, endIdx));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setBasenames(fileNames.toArray(new String[fileNames.size()]));
    }

    public void setNamesPackage(String namesPackage) {
        this.namesPackage = namesPackage;
    }

    public String getNameByCode(String code) {
        try {
            return getMessage(code, null, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            log.warn("name by code '" + code + "' not found in package " + namesPackage);
            return code;
        }
    }

    public String getNoteByCode(String code) {
        try {
            return getMessage(code + NOTE, null, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            return null;
        }
    }
}
