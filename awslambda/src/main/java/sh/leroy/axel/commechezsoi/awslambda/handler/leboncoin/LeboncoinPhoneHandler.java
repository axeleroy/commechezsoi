package sh.leroy.axel.commechezsoi.awslambda.handler.leboncoin;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.Utils;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractStringHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeboncoinPhoneHandler extends AbstractStringHandler {
    private static final Logger logger = LogManager.getLogger(LeboncoinPhoneHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse handleString(String id) {
        ObjectMapper mapper = new ObjectMapper();

        URI adUri;
        try {
            adUri = new URI(Constants.LEBONCOIN_PHONE);
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app_id", Constants.LEBONCOIN_APPID));
        params.add(new BasicNameValuePair("key", Constants.LEBONCOIN_TOKEN));
        params.add(new BasicNameValuePair("text", "1"));
        params.add(new BasicNameValuePair("list_id", Utils.getNumericId(id)));

        HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(adUri);
        post.setHeader("content-type", ContentType.APPLICATION_FORM_URLENCODED.toString());
        post.setHeader("Accept", ContentType.APPLICATION_JSON.toString());
        post.setHeader("Origin", Constants.LEBONCOIN_APPID);
        post.setEntity(new UrlEncodedFormEntity(params, ContentType.APPLICATION_FORM_URLENCODED.getCharset()));

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        JsonNode result;
        try {
            result = mapper.readTree(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8));
        } catch (Exception e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        logger.debug(result);
        String phone = (result.get("utils").get("status").asText().equals("OK")) ? result.get("utils").get("phonenumber").asText() : null;

        return ApiGatewayResponse.builder()
                .setContentType(ContentType.TEXT_PLAIN.getMimeType())
                .setRawBody(phone)
                .build();
    }
}
