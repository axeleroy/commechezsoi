package sh.leroy.axel.commechezsoi.awslambda.handler.bienici;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class BienIciAuthenticateHandler implements AbstractHandler {
    public static final Logger logger = LogManager.getLogger(BienIciAuthenticateHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        ObjectMapper mapper = new ObjectMapper();

        URI uri;
        try {
            uri = new URI(Constants.BIENICI_AUTHENTICATE);
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        try {
            JsonNode jsonNode = mapper.readTree(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8));
            String token = jsonNode.get("account").get("token").asText();

            return ApiGatewayResponse.builder()
                .setContentType(ContentType.TEXT_PLAIN.getMimeType())
                .setRawBody(token)
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
