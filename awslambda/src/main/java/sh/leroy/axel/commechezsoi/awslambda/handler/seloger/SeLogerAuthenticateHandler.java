package sh.leroy.axel.commechezsoi.awslambda.handler.seloger;

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractHandler;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractStringHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class SeLogerAuthenticateHandler implements AbstractHandler {
    public static final Logger logger = LogManager.getLogger(SeLogerAuthenticateHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        URI uri;
        try {
            uri = new URI(Constants.SELOGER_AUTHENTICATE);
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(uri);
        get.setHeader("AppToken", Constants.SELOGER_TOKEN);
        get.setHeader("AppGuid", Constants.SELOGER_GUID);

        HttpResponse response;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        try {
            String token = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);
            token = StringUtils.remove(token, "\"");

            return ApiGatewayResponse.builder()
                .setContentType(ContentType.TEXT_PLAIN.getMimeType())
                .setObjectBody(token)
                .build();

        } catch (IOException e) {
            return error(500, "Error while parsing the response", e, logger);
        }
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context, Logger logger) {
        return null;
    }
}
