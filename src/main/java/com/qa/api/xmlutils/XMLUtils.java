package com.qa.api.xmlutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.qa.api.fileutils.*;
import org.apache.commons.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class XMLUtils {

    private static Logger logger = LoggerFactory.getLogger(XMLUtils.class);

    @Step("Creating XML Document from file in resource folder")
    public static Document getXMLDocumentFromFile(String filePath) {

        try {
            InputStream readFileFromResource = FileUtility.readFileFromResourceAsStream(filePath);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(readFileFromResource);
            return xmlDocument;
        } catch (ParserConfigurationException e) {
            Allure.step("Exception occurred while creating XML document from file in resource folder. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while creating XML document from file in resource folder. Exception:- {}",
                    e.getMessage());
        } catch (SAXException e) {
            Allure.step("Exception occurred while creating XML document from file in resource folder. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while creating XML document from file in resource folder. Exception:- {}",
                    e.getMessage());
        } catch (IOException e) {
            Allure.step("Exception occurred while creating XML document from file in resource folder. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while creating XML document from file in resource folder. Exception:- {}",
                    e.getMessage());
        }
        return null;
    }

    @Step("Creating XML Document from string")
    public static Document getXMLDocumentFromString(String xmlString) {

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(IOUtils.toInputStream(xmlString));
            return xmlDocument;
        } catch (ParserConfigurationException e) {
            Allure.step("Exception occurred while creating XML document from xml string. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while creating XML document from xml string. Exception:- {}",
                    e.getMessage());
        } catch (SAXException e) {
            Allure.step("Exception occurred while creating XML document from xml string. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while creating XML document from xml string. Exception:- {}",
                    e.getMessage());
        } catch (IOException e) {
            Allure.step("Exception occurred while creating XML document from xml string. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while creating XML document from xml string. Exception:- {}",
                    e.getMessage());
        }
        return null;
    }

    @Step("Converting XML Document to string")
    public static String getStringFromXMLDocument(Document xmlDocument) {

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
            String xmlString = writer.getBuffer().toString();
            return xmlString;
        } catch (TransformerConfigurationException e) {
            Allure.step("Exception occurred while converting XML document to xml string. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while converting XML document to xml string. Exception:- {}",
                    e.getMessage());
        } catch (TransformerException e) {
            Allure.step("Exception occurred while converting XML document to xml string. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error("ERROR: Exception occurred while converting XML document to xml string. Exception:- {}",
                    e.getMessage());
        }
        return null;
    }

    @Step("Fetching xml node value based on xml xpath")
    public static String getXMLNodeValue(Document xmlDocument, String xmlXPath) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            return xPath.compile(xmlXPath).evaluate(xmlDocument);
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while fetching xml node value. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while fetching xml node value. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    getStringFromXMLDocument(xmlDocument), xmlXPath, e.getMessage());
        }
        return null;
    }

    @Step("Fetching xml node value based on xml xpath")
    public static String getXMLNodeValue(String xmlString, String xmlXPath) {
        try {
            Document xmlDom = getXMLDocumentFromString(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            return xPath.compile(xmlXPath).evaluate(xmlDom);
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while fetching xml node value. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while fetching xml node value. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    xmlString, xmlXPath, e.getMessage());
        }
        return null;
    }

    @Step("Update xml node value based on xml xpath")
    public static Document updateXMLNodeValue(Document xmlDocument, String xmlXPath, String newNodeValue) {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODESET);
            Node node;
            for (int i = 0, len = nodelist.getLength(); i < len; i++) {
                node = nodelist.item(i);
                node.setTextContent(newNodeValue);
            }
            return xmlDocument;
        } catch (XPathExpressionException e) {
            Allure.step(
                    "Exception occurred while updating xml node value in xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while updating xml node value in xml document. \nXmlString:- {} \nxmlXPath:- {} \nnewNodeValue:- {} \nException:- {}",
                    getStringFromXMLDocument(xmlDocument), xmlXPath, newNodeValue, e.getMessage());
        }
        return null;
    }

    @Step("Update xml node value based on xml xpath")
    public static String updateXMLNodeValue(String xmlString, String xmlXPath, String newNodeValue) {

        try {
            Document xmlDocument = getXMLDocumentFromString(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODESET);
            Node node;
            for (int i = 0, len = nodelist.getLength(); i < len; i++) {
                node = nodelist.item(i);
                node.setTextContent(newNodeValue);
            }
            return getStringFromXMLDocument(xmlDocument);
        } catch (XPathExpressionException e) {
            Allure.step(
                    "Exception occurred while updating xml node value in xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while updating xml node value in xml document. \nXmlString:- {} \nxmlXPath:- {} \nnewNodeValue:- {} \nException:- {}",
                    xmlString, xmlXPath, newNodeValue, e.getMessage());
        }
        return null;
    }

    @Step("Append xml node based on xml xpath in xml document")
    public static String appendNode(String xmlString, String xmlXPath, Node xmlNode) {

        try {
            Document xmlDocument = getXMLDocumentFromString(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODESET);
            Object E = nodelist.item(0);
            ((org.w3c.dom.Node) E).appendChild(xmlDocument.adoptNode(xmlNode.cloneNode(true)));
            return getStringFromXMLDocument(xmlDocument);
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while appending xml node in xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while appending xml node in xml document. \nXmlString:- {} \nxmlXPath:- {} \nxmlNode:- {} \nException:- {}",
                    xmlString, xmlXPath, xmlNode, e.getMessage());
        }
        return null;
    }

    @Step("Append xml node based on xml xpath in xml document")
    public static Document appendNode(Document xmlDocument, String xmlXPath, Node xmlNode) {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodelist = (NodeList) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODESET);
            Object E = nodelist.item(0);
            ((org.w3c.dom.Node) E).appendChild(xmlDocument.adoptNode(xmlNode.cloneNode(true)));
            return xmlDocument;
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while appending xml node in xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while appending xml node in xml document. \nXmlString:- {} \nxmlXPath:- {} \nxmlNode:- {} \nException:- {}",
                    getStringFromXMLDocument(xmlDocument), xmlXPath, xmlNode, e.getMessage());
        }
        return null;
    }

    @Step("Checking if xml node is present in xml document")
    public static boolean isNodePresent(Document xmlDocument, String xmlXPath) {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Object node = xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODE);
            return node != null;
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while checking if node is present in xml document. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while checking if node is present in xml document. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    getStringFromXMLDocument(xmlDocument), xmlXPath, e.getMessage());
        }
        return false;
    }

    @Step("Checking if xml node is present in xml document")
    public static boolean isNodePresent(String xmlString, String xmlXPath) {

        try {
            Document xmlDocument = getXMLDocumentFromString(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            Object node = xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODE);
            return node != null;
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while checking if node is present in xml document. Exception:- "
                    + e.getMessage(), Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while checking if node is present in xml document. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    xmlString, xmlXPath, e.getMessage());
        }
        return false;
    }

    @Step("Delete xml node from xml document")
    public static Document deleteNode(Document xmlDocument, String xmlXPath) {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODE);
            node.getParentNode().removeChild(node);
            return xmlDocument;
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while deleting node from xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while deleting node from xml document. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    getStringFromXMLDocument(xmlDocument), xmlXPath, e.getMessage());
        }
        return null;
    }

    @Step("Delete xml node from xml document")
    public static String deleteNode(String xmlString, String xmlXPath) {

        try {
            Document xmlDocument = getXMLDocumentFromString(xmlString);
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile(xmlXPath).evaluate(xmlDocument, XPathConstants.NODE);
            node.getParentNode().removeChild(node);
            return getStringFromXMLDocument(xmlDocument);
        } catch (XPathExpressionException e) {
            Allure.step("Exception occurred while deleting node from xml document. Exception:- " + e.getMessage(),
                    Status.FAILED);
            logger.error(
                    "ERROR: Exception occurred while deleting node from xml document. \nXmlString:- {} \nxmlXPath:- {} \nException:- {}",
                    xmlString, xmlXPath, e.getMessage());
        }
        return null;
    }
}