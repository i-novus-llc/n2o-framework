package net.n2oapp.framework.api.metadata.dataprovider;


import lombok.Getter;
import lombok.Setter;

/**
 * Модель поиска Spring бина
 */
@Getter
@Setter
public class SpringProvider implements DIProvider {
    private String springBean;

}
