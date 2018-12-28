package net.n2oapp.framework.api.metadata.global.dao.tools;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 06.09.13
 * Time: 12:47
 */
public class N2oRmi implements Serializable {
    private String host;
    private Integer port;
    private String service;

    public N2oRmi() {
    }

    public N2oRmi(String host, Integer port, String service) {
        this.host = host;
        this.port = port;
        this.service = service;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }


    public static String getUrl(N2oRmi rmi) {
        String host = rmi.getHost();
        int port = rmi.getPort();
        if (host == null) host = "localhost";
        if (port == 0) port = 1199;
        return "rmi://" + host + ":" + port + "/" + rmi.getService();
    }

}
