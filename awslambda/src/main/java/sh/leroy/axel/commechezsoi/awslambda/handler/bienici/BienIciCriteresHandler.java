package sh.leroy.axel.commechezsoi.awslambda.handler.bienici;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractCriteresHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.Annonce;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceurType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.Site;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BienIciCriteresHandler extends AbstractCriteresHandler {
    private static final Logger logger = LogManager.getLogger(BienIciCriteresHandler.class);
    private String token;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        Map<String, String> parameters = (Map<String, String>) request.get("queryStringParameters");
        token = parameters.get("token");
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse getAnnonces(Criteres criteres) {
        ObjectMapper mapper = new ObjectMapper();

        JSONObject criteresJson;
        try {
             criteresJson = new JSONObject()
                    .put("size", 2400)
                    .put("from", 0)
                    .put("filterType", (criteres.type == AnnonceType.Location) ? "rent" : "buy")
                    .put("minPrice", criteres.minPrice)
                    .put("maxPrice", criteres.maxPrice)
                    .put("minRooms", criteres.minRooms)
                    .put("maxRooms", criteres.maxRooms)
                    .put("minArea", criteres.minSurface)
                    .put("maxArea", criteres.maxSurface)
                    .put("minBedrooms", criteres.minBedrooms)
                    .put("maxBedrooms", criteres.maxBedrooms)
                    .put("propertyType", new JSONArray()
                            .put("house").put("flat"))
                    .put("maxAuthorizedResults", 2400)
                    .put("resultsPerPage", 2400)
                    .put("zoneIdsByTypes", new JSONObject()
                            .put("zoneIds", new JSONArray(Arrays.asList(criteres.getZoneIds()))))
                    .put("onTheMarket", new JSONArray().put(true));
        } catch (JSONException e) {
            return error(500, "Error creating JSON", e, logger);
        }



        URI uri;
        try {
            uri = new URIBuilder(Constants.BIENICI_SEARCH)
                .addParameter("filter", criteresJson.toString()).build();
            logger.info(uri);
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpGet get = new HttpGet(uri);
        get.setHeader("Authorization", "Bearer " + token);

        HttpResponse response;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        JsonNode results;
        try {
            results = mapper.readTree(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8));
        } catch (Exception e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        List<Annonce> ads = new ArrayList<>();

        for (JsonNode ad : results.get("realEstateAds")) {
            if (!ad.get("status").get("onTheMarket").asBoolean()) {
                logger.info("Skipping ad: " + ad.toString());
                continue;
            }

            List<String> photos = new ArrayList<>();
            for (JsonNode photo : ad.get("photos")) {
                photos.add(photo.get("url").asText());
            }

            Date date = null;
            try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    .parse(ad.get("publicationDate").asText());
            } catch (ParseException e) {
                logger.error("Error parsing date:", e);
            } catch (NullPointerException e) {
                logger.error("Date not found:", ad);
            }

            Annonce.Builder builder = Annonce.builder()
                .setId("bienici-" + ad.get("id").asText())
                .setSite(Site.BienIci)
                .setAnnonceur((ad.get("adCreatedByPro").asBoolean()) ? AnnonceurType.Agence : AnnonceurType.Particulier)
                .setType((ad.get("adType").asText().equals("rent")) ? AnnonceType.Location : AnnonceType.Vente)
                .setCreated(date)
                .setTitle(ad.get("title").asText())
                .setCity(ad.get("city").asText())
                .setPrice(Math.round(ad.get("price").asDouble()))
                .setCharges((ad.get("charges") != null) ? ad.get("charges").asInt() : 0)
                .setSurface(Math.round(ad.get("surfaceArea").asDouble()))
                .setRooms(ad.get("roomsQuantity").asInt())
                .setBedrooms(ad.get("bedroomsQuantity").asInt())
                .setDescription(ad.get("description").asText())
                .setLink("https://www.bienici.com/annonce/" + ad.get("id").asText())
                .setPictures(photos.toArray(new String[0]));
            ads.add(builder.build());


        }

        return ApiGatewayResponse.builder()
                .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                .setObjectBody(ads.toArray())
                .build();
    }
}
