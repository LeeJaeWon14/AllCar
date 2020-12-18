package com.example.ghkdw.all_car;

import android.os.AsyncTask;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ghkdw on 2019-12-04.
 */

public class XmlParseTask extends AsyncTask<String, Void, Document> {
    TextView textView;

    public XmlParseTask(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected Document doInBackground(String... strings) {
        URL url;
        Document doc = null;
        try {
            url = new URL(strings[0]);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Document doc) {
        super.onPostExecute(doc);
        NodeList itemNodeList = doc.getElementsByTagName("OIL");

        textView.setText("");
        for(int i = 0; i < itemNodeList.getLength(); i++) {
            Node node = itemNodeList.item(i);
            Element element = (Element)node;

            NodeList nameNodeList = element.getElementsByTagName("PRODNM");
            String name = nameNodeList.item(0).getChildNodes().item(0).getNodeValue();
            NodeList priceNodeList = element.getElementsByTagName("PRICE");
            String price = priceNodeList.item(0).getChildNodes().item(0).getNodeValue();
            if(i == 0) {
                textView.setText(name + " : " + price + "원");
                continue;
            }
            textView.setText(textView.getText() + "\r\n" + name + " : " + price + "원");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
