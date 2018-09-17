package sh.leroy.axel.commechezsoi.awslambda;

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

public class MainTest {
    public static void main(String[] args) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xml = builder.parse(new FileInputStream("/home/axel/Téléchargements/rep.xml"));
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/detailAnnponce").evaluate(xml, XPathConstants.NODESET);

            System.out.println(nodeList.getLength());

            Element el = (Element) nodeList;

            List<String> photos = new ArrayList<>();
            NodeList photosNode = el.getElementsByTagName("photos");
            for (int i = 0 ; i < photosNode.getLength() ; i++) {
                Element node = (Element) photosNode.item(i);
                photos.add(node.getElementsByTagName("bigUrl").item(0).getTextContent());
            }

            /* Element contact = (Element) el.getElementsByTagName("contact");
            String phone = contact.getElementsByTagName("telephone").item(0).getTextContent();
            */

            System.out.println(photos);
            // System.out.println(phone);
        } catch (IOException | ClassCastException | SAXException | XPathExpressionException | ParserConfigurationException e) {
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
