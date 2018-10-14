package sh.leroy.axel.commechezsoi.awslambda.handler.seloger;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractCriteresHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeLogerSearchHandler extends AbstractCriteresHandler {
    private static final Logger logger = LogManager.getLogger(SeLogerSearchHandler.class);
    private String token;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        logger.info(request);
        token = ((Map<String, String>) request.get("queryStringParameters")).get("token");
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse getAnnonces(Criteres criteres) {
        ObjectMapper mapper = new ObjectMapper();

        URI uri;
        try {
            uri = new URI(Constants.SELOGER_SEARCH);
        } catch (URISyntaxException e) {
            return error(500, "Error building URL", e, logger);
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject()
                    .put("pageIndex", 1)
                    .put("pageSize", 999)
                    .put("query", new JSONObject()
                            .put("minimumPrice", criteres.minPrice)
                            .put("maximumPrice", criteres.maxPrice)
                            .put("minimumLivingArea", criteres.minSurface)
                            .put("maximumLivingArea", criteres.maxSurface)
                            .put("rooms", rangeArray(criteres.minRooms, criteres.maxRooms))
                            .put("bedrooms", rangeArray(criteres.minBedrooms, criteres.maxBedrooms))
                            .put("inseeCodes", new JSONArray(Arrays.asList(criteres.getInsees())))
                            .put("transactionType", (criteres.type == AnnonceType.Location) ? 1 : 2)
                            .put("realtyTypes", 3)
                            .put("sortBy", 0));
        } catch (JSONException e) {
            return error(500, "Error creating JSON", e, logger);
        }

        StringEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            return error(500, "Error building entity", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        post.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        post.setHeader("AppToken", token);

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        logger.info(response);

        JsonNode results;
        try {
            results = mapper.readTree(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8));
        } catch (IOException e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        List<String> ads = new ArrayList<>();

        for (JsonNode ad : results.get("items")) {
            ads.add(ad.get("id").asText());
        }

        return ApiGatewayResponse.builder()
                .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                .setObjectBody(ads.toArray())
                .build();
    }

    private JSONArray rangeArray(int min, int max) {
        List<Integer> range = IntStream.range((min == 0) ? 1 : min, max).boxed().collect(Collectors.toList());
        return new JSONArray(range);
    }

}
