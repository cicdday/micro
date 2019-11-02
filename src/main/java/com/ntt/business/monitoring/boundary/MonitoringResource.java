package com.ntt.business.monitoring.boundary;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Krasimir Dzhigovechki
 */
@Path("monitoring")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class MonitoringResource {

    @GET
    @Path("ping")
    public String ping(){
        return "Alive" + System.currentTimeMillis();
    }
}
