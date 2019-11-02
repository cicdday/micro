package com.ntt.business.monitoring.boundary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class MonitoringResourceIT {

    @Test
    public void ping() {
        final WebTarget path =
                ClientBuilder.newClient().target(System.getProperty("test-target"))
                        .path("micro-sample")
                        .path("rs")
                        .path("monitoring")
                        .path("ping");
        final Response response = path.request().get();
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }
}
