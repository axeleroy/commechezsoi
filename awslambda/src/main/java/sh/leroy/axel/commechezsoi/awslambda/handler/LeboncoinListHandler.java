package sh.leroy.axel.commechezsoi.awslambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeboncoinListHandler extends AbstractHandler {
    private static final Logger logger = LogManager.getLogger(LeboncoinListHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        return handleRequest(request, context, logger);
    }

    @Override
    protected ApiGatewayResponse getAnnonces(Criteres criteres) {
        ObjectMapper mapper = new ObjectMapper();

        URI uri;
        try {
             URIBuilder builder = new URIBuilder(Constants.LEBONCOIN_LIST)
                .addParameter("mrs", String.valueOf(criteres.minPrice))
                .addParameter("mre", String.valueOf(criteres.maxPrice))
                .addParameter("sqs", getSurface(criteres.minSurface))
                .addParameter("sqe", getSurface(criteres.maxSurface))
                .addParameter("ros", String.valueOf(criteres.minRooms))
                .addParameter("roe", String.valueOf(criteres.maxRooms))
                .addParameter("zipcode", String.join(",", criteres.getZipcodes()));
             uri = builder.build();
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.toString());
        post.setEntity(new StringEntity(Constants.LEBONCOIN_TOKEN, "UTF-8"));

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        JsonNode results;
        try {
            results = mapper.readTree(IOUtils.toString(response.getEntity().getContent(),"UTF-8"));
        } catch (Exception e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        List<String> ads = new ArrayList<>();

        for (JsonNode ad : results.get("ads")) {
            ads.add(ad.get("list_id").asText());
        }

        return ApiGatewayResponse.builder()
                .setContentType(ContentType.APPLICATION_JSON.toString())
                .setObjectBody(ads.toArray())
                .build();
    }

    @Override
    protected ApiGatewayResponse getAnnonce(String id) {
        return null;
    }

    private String getSurface(int surface) {
        if (surface == 0)
            return "0";
        else if (surface <= 20)
            return "1";
        else if (surface <= 25)
            return "2";
        else if (surface <= 30)
            return "3";
        else if (surface <= 35)
            return "4";
        else if (surface <= 40)
            return "5";
        else if (surface <= 50)
            return "6";
        else if (surface <= 60)
            return "7";
        else if (surface <= 70)
            return "8";
        else if (surface <= 80)
            return "9";
        else if (surface <= 90)
            return "10";
        else if (surface <= 100)
            return "11";
        else if (surface <= 110)
            return "12";
        else if (surface <= 120)
            return "13";
        else if (surface <= 150)
            return "14";
        else if (surface <= 300)
            return "15";
        else return "16";
    }
}
