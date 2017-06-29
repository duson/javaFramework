package json;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * 要使用本提供都需移除resteasy-jackson2-provider的引用
 * 
 */
@Component
@Provider
@Consumes({"application/*+json", "text/json"})
@Produces({"application/*+json", "text/json"})
public class JacksonProvider extends JacksonJsonProvider {
    public JacksonProvider() {
        setMapper(new CustomJsonMapper());
    }
}