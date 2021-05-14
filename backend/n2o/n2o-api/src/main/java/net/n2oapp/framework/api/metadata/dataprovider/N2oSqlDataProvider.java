package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

/**
 *  SQL провайдер данных
 */
@Setter
@Getter
public class N2oSqlDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String query;
    private String filePath;
    private String rowMapper;
    private String connectionUrl;
    private String username;
    private String password;
    private String jdbcDriver;
}
