package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.TableBuilder;
import de.koanam.dbtester.core.entity.TableBuilderFactory;
import de.koanam.dbtester.core.entity.TableObject;
import de.koanam.dbtester.core.entity.TableParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XMLTableParser implements TableParser {

    private static final String SCHEMA_LOCATION = "./src/main/resources/xsd/xml.xsd";
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private TableBuilderFactory tableBuilderFactory;

    static {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(SCHEMA_LOCATION));
            DOCUMENT_BUILDER_FACTORY.setSchema(schema);
            DOCUMENT_BUILDER_FACTORY.setValidating(true);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public XMLTableParser(TableBuilderFactory tableBuilderFactory){
        this.tableBuilderFactory = tableBuilderFactory;
    }

    @Override
    public Collection<TableObject> parseTables(InputStream input) throws IOException {
        try {
            List<TableObject> tables = new ArrayList<>();

            Element element = this.parseElement(input);

            NodeList nodeList = element.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    TableObject table = this.parseTable(node);
                    tables.add(table);
                }
            }
            return tables;
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private TableObject parseTable(Node tableNode) {
        TableBuilder tableBuilder = this.tableBuilderFactory.getBuilder();

        String tableName = tableNode.getAttributes().getNamedItem("name").getNodeValue();
        List<String> columnNames = new ArrayList<>();

        NodeList nodeList = tableNode.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if("column".equalsIgnoreCase(node.getNodeName())){
                    String nodeValue = node.getChildNodes().item(0).getNodeValue();
                    columnNames.add(nodeValue);
                } else if("row".equalsIgnoreCase(node.getNodeName())){
                    List<String> values = this.parseRow(node);
                    tableBuilder.addRow(values);
                }
            }
        }

        tableBuilder.setName(tableName);
        tableBuilder.setColumnNames(columnNames);

        return tableBuilder.build();
    }

    private Element parseElement(InputStream input) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        Document doc = db.parse(input);
        Element element = doc.getDocumentElement();
        return element;
    }

    private List<String> parseRow(Node rowNode){
        List<String> values = new ArrayList<>();

        NodeList nodeList = rowNode.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if("null".equalsIgnoreCase(node.getNodeName())){
                    values.add("");
                } else{
                    values.add(node.getChildNodes().item(0).getNodeValue());
                }
            }
        }
        return values;
    }


}
