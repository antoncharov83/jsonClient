package com.antoncharov;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger log = Logger.getLogger(Main.class.getName());
    private static final String filename_devices = "devices.json";
    private static final String filename_document = "document.xml";

    public static void main(String[] args) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        // включаем поддержку пространства имен XML
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;

        try{
            builder = builderFactory.newDocumentBuilder();
            doc = builder.parse("Document.xml");

            XPathFactory xpathFactory = XPathFactory.newInstance();

            XPath xpath = xpathFactory.newXPath();

            com.antoncharov.Document document = new com.antoncharov.Document();

            document.setName(getValue(doc, xpath, "name"));
            document.setDescription(getValue(doc, xpath, "description"));
            document.setDate(getDate(doc, xpath));
            document.setConclusion(getValue(doc, xpath, "conclusion"));
            document.setFile(encodeFileToBase64Binary(filename_document));
            document.setFile_md5(getMD5(document.getFile()));

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename_devices));
            Type collectionType = new TypeToken<List<DeviceData>>(){}.getType();
            List<DeviceData> list = (List<DeviceData>) gson.fromJson(reader, collectionType);

            DevicesDataList devicesDataList = new DevicesDataList(list);

            Device first_device = new Device();
            first_device.setName(getValue(doc, xpath, "device"));
            int IdDevice = devicesDataList.getIdByName(first_device.getName());
            first_device.setId(IdDevice == -1 ? null : 0);

            List<Device> deviceList = new ArrayList<>();
            deviceList.add(first_device);
            document.setDevices(deviceList);

            Results result = new Results();

            result.setBp_diast(tryParse(getValue(doc, xpath, "bp_diast", "results"), 0));
            result.setBp_syst(tryParse(getValue(doc, xpath, "bp_syst", "results"), 0));
            result.setHeart_rate(tryParse(getValue(doc, xpath, "heart_rate", "results"), 0));

            Results result2 = new Results();

            result2.setBp_diast(123);
            result2.setBp_syst(85);
            result2.setHeart_rate(65);

            List<Results> resultsList = new ArrayList<>();
            resultsList.add(result);
            resultsList.add(result2);
            document.setResults(resultsList);

            System.out.println(document);

            String request_gson = gson.toJson(document, com.antoncharov.Document.class);

            System.out.println(request_gson);

            System.out.println(sendRequest(request_gson));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
        }
    }

    private static String sendRequest(String gsonString) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost request = new HttpPost("http://localhost:1234");
            StringEntity params = new StringEntity("document=" + gsonString, ContentType.APPLICATION_JSON);
            request.setEntity(params);

            CloseableHttpResponse response = httpClient.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return  result.toString();
        }catch (Exception ex) {
            return ex.toString();
        } finally {
            httpClient.close();
        }
    }

    private static int tryParse(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private static String getDate(Document doc, XPath xpath){
        String date;
        try {
            date = getValue(doc, xpath, "date").replace('T',' ');
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            date = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
        }
        return "";
    }

    private static String getValue(Document doc, XPath xpath, String atribute, String add){
        String value;
        if(add.length() != 0)
            add = '/' + add;
        try {
            XPathExpression xpathExpression = xpath.compile("/document"+add+"/item[@name='"+atribute+"']/text/text()");
            value = (String) xpathExpression.evaluate(doc, XPathConstants.STRING);
            return value;
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
        }
        return "";
    }

    private static String getValue(Document doc, XPath xpath, String atribute){
        return getValue(doc, xpath, atribute, "");
    }

    private static String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        String encoded = "";
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte fileData[] = new byte[(int) file.length()];
            inputStream.read(fileData);

            encoded = Base64.getEncoder().encodeToString(fileData);
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
            System.out.println("File not found" + ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
            System.out.println("Exception while reading the file " + ex);
        }
        return encoded;
    }

    private static String getMD5(String fileBASE64){
        String MD5String = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = fileBASE64.getBytes("UTF-8");
            MD5String = md.digest(bytes).toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            log.log(Level.SEVERE, "Exception: ", ex);
        }
        return MD5String;
    }

}
