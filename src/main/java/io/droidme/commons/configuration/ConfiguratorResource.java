package io.droidme.commons.configuration;

import java.net.URI;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author ga2merf
 */
@Path("configurator")
@Produces(MediaType.APPLICATION_JSON)
public class ConfiguratorResource {

    @Inject
    Configurator configurator;

    @Context
    UriInfo uriInfo;

    @GET
    public String getConfiguration() {
        return configurator
                .getConfiguration().toString();
    }

    @GET
    @Path("{key}")
    public String getEntry(@PathParam("key") String key) {
        return configurator
                .getConfiguration().getProperty(key);
    }

    @PUT
    @Path("{key}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addEntry(@PathParam("key") String key, String value) {
        if (configurator.getConfiguration().containsKey(key)) {
            return Response
                    .noContent()
                    .build();
        } else {
            configurator
                    .getConfiguration()
                    .put(key, value);
            URI uri = uriInfo.getAbsolutePathBuilder().build(key);
            return Response
                    .created(uri)
                    .build();
        }
    }

    @DELETE
    @Path("{key}")
    public Response deleteEntry(@PathParam("key") String key) {
        configurator
                .getConfiguration()
                .remove(key);
        return Response
                .noContent()
                .build();
    }

}
