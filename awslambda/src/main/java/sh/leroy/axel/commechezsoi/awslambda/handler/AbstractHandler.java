package sh.leroy.axel.commechezsoi.awslambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.Logger;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;

import java.io.IOException;
import java.util.Map;


abstract class AbstractHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    ApiGatewayResponse handleRequest(Map<String, Object> request, Context context, Logger logger) {
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
            return getAnnonce(body);
        }
    }

    protected abstract ApiGatewayResponse getAnnonces(Criteres criteres);
    protected abstract ApiGatewayResponse getAnnonce(String id);

    ApiGatewayResponse error(int code, String message, Exception e, Logger logger) {
        logger.error(message, e);

        return ApiGatewayResponse.builder()
                .setStatusCode(code)
                .setContentType(ContentType.TEXT_PLAIN.toString())
                .setRawBody(message)
                .build();
    }
}
