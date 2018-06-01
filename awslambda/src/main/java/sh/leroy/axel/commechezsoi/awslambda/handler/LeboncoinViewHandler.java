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
import org.jsoup.Jsoup;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.model.Annonce;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class LeboncoinViewHandler extends AbstractHandler {
    private static final Logger logger = LogManager.getLogger(LeboncoinViewHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        return handleRequest(request, context, logger);
    }

    @Override
    protected ApiGatewayResponse getAnnonce(String id) {
        ObjectMapper mapper = new ObjectMapper();

        URI adUri;
        try {
            adUri = new URIBuilder(Constants.LEBONCOIN_VIEW)
                    .addParameter("ad_id", id)
                    .build();
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(adUri);
        post.setHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.toString());
        post.setEntity(new StringEntity(Constants.LEBONCOIN_TOKEN, "UTF-8"));

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        JsonNode result;
        try {
            result = mapper.readTree(IOUtils.toString(response.getEntity().getContent(),"UTF-8"));
        } catch (Exception e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        try {
            double surface = 0.0;
            int rooms = 0;

            for (JsonNode node : result.get("parameters")) {
                if (node.get("id").asText().equals("rooms")) {
                    rooms = node.get("value").asInt();
                }
                if (node.get("id").asText().equals("square")) {
                    final String surfaceString = node.get("value").asText();
                    final String escapedSurface = surfaceString.replace("m&sup2;", "")
                            .replace("m²", "")
                            .trim();
                    surface = Double.parseDouble(escapedSurface);
                }
            }

            Annonce.Builder builder = Annonce.builder();
            builder
                .setId("leboncoin-" + result.get("list_id").asText())
                .setSite(Annonce.Site.Leboncoin)
                .setCreated(new SimpleDateFormat("dd/MM/yyyy '&agrave;' H'h'mm")
                        .parse(result.get("formatted_date").asText()))
                .setTitle(Jsoup.parse(result.get("subject").asText()).text())
                .setPrice(result.get("price").asInt())
                .setSurface(surface)
                .setRooms(rooms)
                .setCity(result.get("zipcode").asText())
                .setPictures(mapper.convertValue(result.get("images"), String[].class))
                .setLink("https://www.leboncoin.fr/vi/" + result.get("list_id").asText() + ".htm");

            if (result.get("body") != null)
                builder.setDescription(Jsoup.parse(result.get("body").asText()).text());
            if (result.get("phone") != null)
                builder.setTelephone(result.get("phone").asText());

            return ApiGatewayResponse.builder()
                    .setContentType(ContentType.APPLICATION_JSON.toString())
                    .setObjectBody(builder.build())
                    .build();
        } catch (ParseException e) {
            return error(500, "Error while parsing date", e, logger);
        }
    }

    @Override
    protected ApiGatewayResponse getAnnonces(Criteres criteres) {
        return null;
    }
}
