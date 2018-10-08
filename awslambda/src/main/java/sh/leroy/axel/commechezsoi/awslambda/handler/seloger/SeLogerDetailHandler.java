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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sh.leroy.axel.commechezsoi.awslambda.Constants;
import sh.leroy.axel.commechezsoi.awslambda.handler.AbstractStringHandler;
import sh.leroy.axel.commechezsoi.awslambda.model.Annonce;
import sh.leroy.axel.commechezsoi.awslambda.model.ApiGatewayResponse;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.Site;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeLogerDetailHandler extends AbstractStringHandler {
    public static final Logger logger = LogManager.getLogger(SeLogerDetailHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> request, Context context) {
        return handleRequest(request, context, logger);
    }

    @Override
    public ApiGatewayResponse handleString(String id) {
        URI adUri;
        try {
            adUri = new URIBuilder(Constants.SELOGER_DETAIL)
                    .addParameter("idAnnonce", id)
                    .addParameter("noAudiotel", "1")
                    .build();
        } catch (URISyntaxException e) {
            return error(500, "Error building URI", e, logger);
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(adUri);

        HttpResponse response;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            return error(500, "Error executing request", e, logger);
        }

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xml = docBuilder.parse(response.getEntity().getContent());
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList photosNodeList = (NodeList) xPath.compile("//photos/photo/bigUrl").evaluate(xml, XPathConstants.NODESET);
            List<String> photos = new ArrayList<>();
            for (int i = 0 ; i < photosNodeList.getLength() ; i++) {
                Element node = (Element) photosNodeList.item(i);
                photos.add(node.getTextContent());
            }

            NodeList annonceNodes = (NodeList) xPath.compile("/detailAnnonce").evaluate(xml, XPathConstants.NODESET);
            Element el = (Element) annonceNodes.item(0);
            NodeList contactNodes = (NodeList) xPath.compile("/detailAnnonce/contact").evaluate(xml, XPathConstants.NODESET);
            Element contact = (Element) contactNodes.item(0);

            Annonce.Builder builder = Annonce.builder();
            builder
                .setId("seloger-" + 0)
                .setSite(Site.SeLoger)
                .setCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(el.getElementsByTagName("dtFraicheur").item(0).getTextContent()))
                .setTitle(el.getElementsByTagName("titre").item(0).getTextContent())
                .setCity(el.getElementsByTagName("ville").item(0).getTextContent())
                .setTelephone(contact.getElementsByTagName("telephone").item(0).getTextContent())
                .setPrice(Integer.parseInt(el.getElementsByTagName("prix").item(0).getTextContent()))
                .setCharges(Integer.parseInt(el.getElementsByTagName("charges").item(0).getTextContent()))
                .setSurface(Integer.parseInt(el.getElementsByTagName("surface").item(0).getTextContent()))
                .setRooms(Integer.parseInt(el.getElementsByTagName("nbPieces").item(0).getTextContent()))
                .setBedrooms(Integer.parseInt(el.getElementsByTagName("nbChambres").item(0).getTextContent()))
                .setLink(el.getElementsByTagName("permaLien").item(0).getTextContent())
                .setDescription(el.getElementsByTagName("descriptif").item(0).getTextContent())
                .setPictures(photos.toArray(new String[0]));

            return ApiGatewayResponse.builder()
                .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                .setObjectBody(builder.build())
                .build();

        } catch (ParserConfigurationException | IOException | XPathExpressionException | SAXException | ParseException e) {
            return error(500, "Error while parsing the response", e, logger);
        }
    }
}
