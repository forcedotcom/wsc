package com.sforce.ws.codegen.metadata;

import com.sforce.ws.bind.NameMapper;
import com.sforce.ws.wsdl.Definitions;

/**
 * @author hhildebrand
 * @since 184
 */
public class ConnectorMetadata extends ClassMetadata {

    private final String endpoint;

    public ConnectorMetadata(Definitions definitions, String packagePrefix) {
        this(NameMapper.getPackageName(definitions.getTargetNamespace(), packagePrefix),
                (definitions.getApiType() != null ? definitions.getApiType().name() : "Soap") + "Connection",
                definitions.getService().getPort().getSoapAddress().getLocation());
    }

    public ConnectorMetadata(String packageName, String className, String endpoint) {
        super(packageName, className);
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
