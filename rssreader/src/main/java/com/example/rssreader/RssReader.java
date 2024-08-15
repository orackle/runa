package com.example.rssreader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RssReader {

    private static final Logger logger = LoggerFactory.getLogger(RssReader.class);

    public static void main(String[] args) {
        String feedUrl = "https://www.pannchoa.com/feeds/posts/default"; // Change this URL to test Atom feeds

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            URL url = new URL(feedUrl);
            Document document = saxBuilder.build(url);

            Element rootElement = document.getRootElement();
            String rootName = rootElement.getName();

            if ("rss".equals(rootName)) {
                parseRssFeed(rootElement);
            } else if ("feed".equals(rootName)) {
                parseAtomFeed(rootElement);
            } else {
                logger.warn("Unsupported feed format: {}", rootName);
            }
        } catch (IOException e) {
            logger.error("I/O error occurred while reading the feed.", e);
        } catch (JDOMException e) {
            logger.error("Error parsing the feed.", e);
        }
    }

    private static void parseRssFeed(Element rootElement) {
        Element channel = rootElement.getChild("channel");
        List<Element> items = channel.getChildren("item");

        if (items.isEmpty()) {
            logger.info("No items found in the RSS feed.");
        } else {
            for (Element item : items) {
                String title = item.getChildText("title");
                String link = item.getChildText("link");
                String description = item.getChildText("description");
                String pubDate = item.getChildText("pubDate");

                logger.info("Title: {}", title);
                logger.info("Link: {}", link);
                logger.info("Description: {}", description);
                logger.info("Published Date: {}", pubDate);
                logger.info("----------------------------------------------------");
            }
        }
    }

    private static void parseAtomFeed(Element rootElement) {
        List<Element> entries = rootElement.getChildren("entry");

        if (entries.isEmpty()) {
            logger.info("No entries found in the Atom feed.");
        } else {
            for (Element entry : entries) {
                String title = entry.getChildText("title");
                String link = entry.getChild("link").getAttributeValue("href");
                String summary = entry.getChildText("summary");
                String published = entry.getChildText("published");

                logger.info("Title: {}", title);
                logger.info("Link: {}", link);
                logger.info("Summary: {}", summary);
                logger.info("Published Date: {}", published);
                logger.info("-------------------------------------");
            }
        }
    }
}
