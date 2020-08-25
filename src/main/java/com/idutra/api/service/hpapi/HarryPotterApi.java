package com.idutra.api.service.hpapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/")
public interface HarryPotterApi {

    @GET
    @Path("/houses")
    Response listHouses(@QueryParam("key") String key);

    @GET
    @Path("/houses/{houseId}")
    Response getHouseById(@QueryParam("key") String key, @PathParam("houseId") String houseId);

    @GET
    @Path("/characters")
    Response getCharactersByName(@QueryParam("key") String key, @QueryParam("name") String name);
}

