package org.spsochnev.vacancies.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CurrencySource {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencySource.class);

    @Value("${currency.source.url}")
    private String currencyURL;

    private HttpClient client = HttpClient.newHttpClient();

    public Map<String, BigDecimal> getCurrencyExchangeRates(Set<String> codes) {
        Map<String, BigDecimal> exchangeRates = new HashMap<>();
        if (codes.isEmpty()) return exchangeRates;

        String res = getDataFromURL(currencyURL);
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOG.error("Error while creating XML document builder occurred: " + e.getMessage());
        }
        Document doc = null;
        if (builder != null) {
            try {
                doc = builder.parse(new InputSource(new StringReader(res)));
            } catch (SAXException | IOException e) {
                LOG.error("Error while parsing XML occurred: " + e.getMessage());
            }
        }
        if (doc != null) {
            doc.getDocumentElement().normalize();
            NodeList currencyCodeNodes = doc.getElementsByTagName("CharCode");

            for (int i = 0; i < currencyCodeNodes.getLength(); i++) {
                Node codeNode = currencyCodeNodes.item(i);
                if (codes.contains(codeNode.getTextContent())) {
                    NodeList children = codeNode.getParentNode().getChildNodes();

                    for (int j = 0; j < children.getLength(); j++) {
                        Node child = children.item(j);
                        if (child.getNodeName().equals("Value")) {
                            String rateString = child.getTextContent();
                            rateString = rateString.replace(",", ".");
                            exchangeRates.put(codeNode.getTextContent(), new BigDecimal(rateString));
                        }
                    }
                }
            }
        }
        return exchangeRates;
    }

    private String getDataFromURL(String requestURL) {
        LOG.info("Getting info from: " + requestURL);
        URI requestURI = URI.create(requestURL);
        HttpRequest request = HttpRequest.newBuilder().
                uri(requestURI).
                build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
                thenApply(HttpResponse::body).
                join();
    }

}
