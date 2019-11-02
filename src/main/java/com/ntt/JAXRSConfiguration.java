package com.ntt;

import com.ntt.business.monitoring.boundary.MonitoringResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 *
 */
@javax.ws.rs.ApplicationPath("rs")
public class JAXRSConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(MonitoringResource.class);
        return classes;
    }
}
