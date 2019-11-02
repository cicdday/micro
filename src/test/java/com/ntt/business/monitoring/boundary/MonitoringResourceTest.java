package com.ntt.business.monitoring.boundary;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

public class MonitoringResourceTest {

    @Test
    public void ping() {
        assertThat(new MonitoringResource().ping(), Matchers.notNullValue());
    }
}
