package ru.evteev.converter.models;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.evteev.converter.entities.Currency;
import ru.evteev.converter.entities.ExchangeRate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParserDOM implements XMLParser {

    private final List<Currency> currenciesDOM;
    private final List<ExchangeRate> exchangeRatesDOM;

    public XMLParserDOM() {
        this.currenciesDOM = new ArrayList<>();
        this.exchangeRatesDOM = new ArrayList<>();
    }

    @Override
    public List<ExchangeRate> parse(String url)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(url);

        NodeList elements = doc.getDocumentElement().getElementsByTagName("Valute");

        for (int i = 0; i < elements.getLength(); i++) {
            Node rate = elements.item(i);

            NamedNodeMap attributes = rate.getAttributes();
            Node valute = attributes.getNamedItem("ID");

            NodeList childNodes = rate.getChildNodes();

            // Currency
            String id = valute.getNodeValue();
            String numCode = childNodes.item(0).getTextContent();
            String charCode = childNodes.item(1).getTextContent();
            Integer nominal = Integer.parseInt(childNodes.item(2).getTextContent());
            String name = childNodes.item(3).getTextContent();

            // Exchange rate
            double value = Double.parseDouble(childNodes.item(4).getTextContent()
                    .replace(",", "."));

            Currency currency = new Currency(id, numCode, charCode, nominal, name);
            currenciesDOM.add(currency);
            exchangeRatesDOM.add(new ExchangeRate(currency, value));
        }
        return exchangeRatesDOM;
    }
}
