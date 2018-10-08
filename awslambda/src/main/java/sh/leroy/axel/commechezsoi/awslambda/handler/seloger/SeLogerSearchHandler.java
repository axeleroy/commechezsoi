package sh.leroy.axel.commechezsoi.awslambda.handler.seloger;

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractCriteresHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.Criteres;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeLogerSearchHandler extends AbstractCriteresHandler {
    private static final Logger logger = LogManager.getLogger(SeLogerSearchHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse getAnnonces(Criteres criteres) {

        URI uri;
        try {
            URIBuilder builder = new URIBuilder(Constants.SELOGER_SEARCH)
                .addParameter("idtt", (criteres.type == AnnonceType.Location) ? "1" : "2")
                .addParameter("px_loyermin", String.valueOf(criteres.minPrice))
                .addParameter("px_loyermax", String.valueOf(criteres.maxPrice))
                .addParameter("surfacemin", String.valueOf(criteres.minSurface))
                .addParameter("surfacemax", String.valueOf(criteres.maxSurface))
                .addParameter("nbpieces", IntStream.range(criteres.minRooms, criteres.maxRooms)
                        .mapToObj(Integer::toString).collect(Collectors.joining(",")))
                .addParameter("nbchambres", IntStream.range(criteres.minBedrooms, criteres.maxBedrooms)
                        .mapToObj(Integer::toString).collect(Collectors.joining(",")))
                .addParameter("ci", String.join(",", criteres.getInsees()))
                .addParameter("getDtCreationMax", "1");

            uri = builder.build();
            logger.info(uri.toString());
        } catch (URISyntaxException e) {
            return error(500, "Error building URL", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        List<String> ads = new ArrayList<>();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xml = builder.parse(response.getEntity().getContent());
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/recherche/annonces/annonce/idAnnonce").evaluate(xml, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength() ; i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    ads.add(nodeList.item(i).getTextContent());
                }
            }
        } catch (ParserConfigurationException|IOException|XPathExpressionException|SAXException e) {
            return error(500, "Error while parsing the response", e, logger);
        }

        return ApiGatewayResponse.builder()
                .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                .setObjectBody(ads.toArray())
                .build();
    }

}
