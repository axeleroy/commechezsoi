package sh.leroy.axel.commechezsoi.awslambda.handler;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;

import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;

public abstract class AbstractStringHandler implements AbstractHandler{
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context, Logger logger) {
        String body = (String) request.get("body");
        return handleString(body);
    }

    public abstract ApiGatewayResponse handleString(String body);
}
