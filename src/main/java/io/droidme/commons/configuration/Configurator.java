package io.droidme.commons.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;

/**
 *
 * @author droidme
 */
@Singleton
@Path("configurator")
@javax.ws.rs.Produces(MediaType.TEXT_PLAIN)
public class Configurator {

    private Properties configuration;

    @Inject
    Logger log;

    @Inject
    Stage stage;

    @Inject
    private Instance<ConfigurationProvider> configurationProvider;

    @PostConstruct
    private void init() {
        configuration = defaults();
        configuration.putAll(custom());
    }

    public Properties defaults() {
        Properties props = new Properties();
        List<String> configFiles = Arrays.asList(
                "system.config",
                "stage/" + stage.name().toLowerCase() + ".config");
        configFiles.forEach(file -> {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(file);
            if (inputStream != null) {
                try {
                    log.debug("loading file: {}", file);
                    props.load(inputStream);
                } catch (IOException ex) {
                    log.error("Error loading {}", file, ex);
                }
            }

        });
        return props;
    }

    public Properties custom() {
        Properties props = new Properties();
        configurationProvider
                .forEach(provider -> {
                    props.putAll(provider.getConfiuration());
                });
        return props;
    }

    @Produces
    public String getString(InjectionPoint ip) {
        String key = obtainConfigurable(ip);
        String value = configuration.getProperty(key);
        if (value == null) {
            log.warn("Key {} not properly configured.", key);
        }
        return value;
    }

    @Produces
    public int getInteger(InjectionPoint ip) {
        String value = getString(ip);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    @Produces
    public boolean getBoolean(InjectionPoint ip) {
        String value = getString(ip);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    String obtainConfigurable(InjectionPoint ip) {
        AnnotatedField field = (AnnotatedField) ip.getAnnotated();
        Configurable configurable = field
                .getAnnotation(Configurable.class);
        if (configurable != null) {
            return configurable.value();
        } else {
            String clazzName = ip
                    .getMember().getDeclaringClass().getName();
            String memberName = ip
                    .getMember().getName();
            return clazzName + "." + memberName;
        }
    }
    
    @GET
    public String getConfiguration() {
        return this.configuration.toString();
    }
    
    @GET
    @Path("{key}")
    public String getEntry(@PathParam("key") String key) {
        return configuration.getProperty(key);
    }
    
    @PUT
    @Path("{key}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addEntry(@PathParam("key") String key, String value, 
            @Context UriInfo uriInfo) {
        if (configuration.containsKey(key)) {
            return Response.noContent().build();
        } else {
            configuration.put(key, value);
            URI uri = uriInfo.getAbsolutePathBuilder().build(key);
            return Response.created(uri).build();
        }
    }
    
    @DELETE
    @Path("{key}")
    public Response deleteEntry(@PathParam("key") String key) {
        configuration.remove(key);
        return Response.noContent().build();
    }

}
