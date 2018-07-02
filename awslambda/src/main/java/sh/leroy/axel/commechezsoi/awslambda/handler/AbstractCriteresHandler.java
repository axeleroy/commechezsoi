package sh.leroy.axel.commechezsoi.awslambda.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;

public abstract class AbstractCriteresHandler implements AbstractHandler {

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context, Logger logger) {
            ObjectMapper mapper = new ObjectMapper();
            String body = (String) request.get("body");

            if (body == null || body.trim().isEmpty()) {
                return error(404, "Empty body", null, logger);
            }

            Criteres criteres;
            try {
                criteres = mapper.readValue(body, Criteres.class);
                return getAnnonces(criteres);
            } catch (IOException e) {
                return error(500, "Error while parsing criteria", e, logger);
            }
    }

    public abstract ApiGatewayResponse getAnnonces(Criteres criteres);
}
