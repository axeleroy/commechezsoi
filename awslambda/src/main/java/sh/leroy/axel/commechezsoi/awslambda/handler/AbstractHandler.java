package sh.leroy.axel.commechezsoi.awslambda.handler;

import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;


public interface AbstractHandler extends RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    ApiGatewayResponse handleRequest(Map<String, Object> request, Context context, Logger logger);

    default ApiGatewayResponse error(int code, String message, Exception e, Logger logger) {
        logger.error(message, e);

        return ApiGatewayResponse.builder()
                .setStatusCode(code)
                .setContentType(ContentType.TEXT_PLAIN.toString())
                .setRawBody(message)
                .build();
    }
}
