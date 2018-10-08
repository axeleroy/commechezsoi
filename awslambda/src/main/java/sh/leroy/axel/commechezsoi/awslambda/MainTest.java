package sh.leroy.axel.commechezsoi.awslambda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import sh.leroy.axel.commechezsoi.awslambda.model.Annonce;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.Site;

public class MainTest {
    public static void main(String[] args) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xml = builder.parse(new FileInputStream("/home/axel/Téléchargements/rep.xml"));
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

            Annonce.Builder annonceBuilder = Annonce.builder();
            annonceBuilder
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

            Annonce annonce = annonceBuilder.build();
            System.out.println(annonce.telephone);

        } catch (IOException | ClassCastException | SAXException | XPathExpressionException | ParserConfigurationException | ParseException e) {
            e.printStackTrace();
        }

    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
