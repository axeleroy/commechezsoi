package sh.leroy.axel.commechezsoi.awslambda.handler.seloger;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractStringHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.Annonce;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceurType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.Site;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class SeLogerDetailHandler extends AbstractStringHandler {
    public static final Logger logger = LogManager.getLogger(SeLogerDetailHandler.class);
    private String token;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        logger.info(request);
        token = ((Map<String, String>) request.get("queryStringParameters")).get("token");
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse handleString(String id) {
        ObjectMapper mapper = new ObjectMapper();

        URI adUri;
        try {
            adUri = new URI(Constants.SELOGER_DETAIL + id);
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(adUri);
        get.setHeader("AppToken", token);

        HttpResponse response;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        JsonNode ad;
        try {
            ad = mapper.readTree(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8));
        } catch (IOException e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        try {
            Annonce.Builder builder = Annonce.builder()
                .setId("seloger-" + ad.get("id").asText())
                .setSite(Site.SeLoger)
                .setAnnonceur(AnnonceurType.Agence)
                .setType((ad.get("realtyType").asInt() == 1) ? AnnonceType.Location : AnnonceType.Vente)
                .setCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(ad.get("created").asText()))
                .setTitle(ad.get("title").asText())
                .setCity(ad.get("city").asText())
                .setTelephone(ad.get("professionals").get(0).get("phoneNumber").asText())
                .setPrice(Math.round(ad.get("price").asDouble()))
                .setCharges(Math.round(ad.get("priceAnnuity").asDouble()))
                .setSurface(Math.round(ad.get("livingArea").asDouble()))
                .setRooms(ad.get("rooms").asInt())
                .setBedrooms(ad.get("bedrooms").asInt())
                .setLink(ad.get("permalink").asText())
                .setDescription(ad.get("description").asText().replace("\n", "<br />"))
                .setPictures(mapper.convertValue(ad.get("photos"), String[].class));

            return ApiGatewayResponse.builder()
                    .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                    .setObjectBody(builder.build())
                    .build();
        } catch (ParseException e) {
            return error(500, "Error while parsing date", e, logger);
        }

    }
}
