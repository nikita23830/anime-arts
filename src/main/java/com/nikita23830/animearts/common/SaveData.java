package com.nikita23830.animearts.common;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaveData {
    public static SaveData instance;
    public static HashMap<String, String> md5 = new HashMap<>();
    public static HashMap<String, String> md5A = new HashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    public static byte canStartingDownload = 0;
    private static NBTTagCompound mainNBT = null;

    public SaveData() {
        instance = this;
    }

    public void readData() {
        executor.execute(() -> {
            try {
                readDataThread();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void save() throws Exception {
        createXML();
    }

    public void readDataThread() throws Exception {
        XMLReader myReader = XMLReaderFactory.createXMLReader();
        myReader.parse(new InputSource(new URL("https://s3.streamcraft.net/uploads/arts/arts.xml").openStream()));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL("https://s3.streamcraft.net/uploads/arts/arts.xml").openStream());
        NodeList nList = doc.getElementsByTagName("art");
        for (int i = 0; i < nList.getLength(); ++i) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                md5.put(eElement.getAttribute("name"), eElement.getElementsByTagName("md5").item(0).getTextContent());
            }
        }
        NodeList nList1 = doc.getElementsByTagName("animated");
        for (int i = 0; i < nList1.getLength(); ++i) {
            Node node = nList1.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                md5A.put(eElement.getAttribute("name"), eElement.getElementsByTagName("md5").item(0).getTextContent());
            }
        }
        File f = new File("./config/arts");
        if (!f.exists())
            f.mkdir();
        readMainNBT();
        int i = 0;
        for (String key : md5.keySet()) {
            registerArt(key, ++i);
        }
        i = 0;
        for (String key : md5A.keySet()) {
            registerAnimated(key, ++i);
        }
        canStartingDownload = 1;
    }

    private void readMainNBT() throws Exception {
        File f = new File("./config/arts/gen.dat");
        if (!f.exists())
            f.createNewFile();
        mainNBT = new NBTTagCompound();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream("./config/arts/gen.dat"));
            NodeList nList = doc.getElementsByTagName("art");
            for (int i = 0; i < nList.getLength(); ++i) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    NBTTagCompound nbt = new NBTTagCompound();
                    String key = eElement.getAttribute("name");
                    nbt.setString("image", eElement.getElementsByTagName("image").item(0).getTextContent());
                    mainNBT.setTag(key, nbt);
                }
            }
        } catch (Exception e) {}
    }

    private void registerArt(String name, int id) throws Exception {
        RegisterAnimeArts.AnimeArt art = new RegisterAnimeArts.AnimeArt("https://s3.streamcraft.net/uploads/arts/" + name + ".png", name, RegisterAnimeArts.AnimeType.DEFAULT);
        boolean needDownload = true;
        if (mainNBT.hasKey(name)) {
            byte[] bytes = Base64.decodeBase64(mainNBT.getCompoundTag(name).getString("image").getBytes());
            String current = getMD5Checksum(bytes);
            String need = SaveData.md5.get(name);
            if (current.equals(need)) {
                needDownload = false;
                art.image = ImageIO.read(new ByteArrayInputStream(bytes));
            }
        }
        art.setId(id);
        art.needDownload = needDownload;
        RegisterAnimeArts.instance.arts.put(name, art);
    }

    private void registerAnimated(String name, int id) throws Exception {
        RegisterAnimeArts.AnimeArt art = new RegisterAnimeArts.AnimeArt("https://s3.streamcraft.net/uploads/arts/" + name + ".gif", name, RegisterAnimeArts.AnimeType.ANIMATED);
        boolean needDownload = true;
        File f = new File("./config/arts/" + name);
        if (f.exists()) {
            InputStream is = Files.newInputStream(Paths.get("./config/arts/" + name));
            String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
            if (md5.equals(md5A.get(name)))
                art.file = f;
        }
        art.setId(id);
        art.needDownload = needDownload;
        RegisterAnimeArts.instance.arts.put(name, art);
    }

    public BufferedImage addImage(String URL) throws Exception {
        BufferedImage image = ImageIO.read(new URL(URL));
        String key = URL.replace("https://s3.streamcraft.net/uploads/arts/", "").replace(".png", "");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        NBTTagCompound n = new NBTTagCompound();
        String k = new String(Base64.encodeBase64(baos.toByteArray()));
        n.setString("image", k);
        mainNBT.setTag(key, n);
        if (mainNBT.func_150296_c().size() == RegisterAnimeArts.getDefault().size()) {
            save();
        }
        return image;
    }

    private void createXML() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = dbFactory.newDocumentBuilder();
        Document document = dbuilder.newDocument();
        Element users = document.createElement("default");
        document.appendChild(users);

        Set<String> set = mainNBT.func_150296_c();
        for (String s : set) {
            NBTTagCompound n = mainNBT.getCompoundTag(s);
            Element user = document.createElement("art");
            user.setAttribute("name", s);
            Element lastName = document.createElement("image");
            lastName.appendChild(document.createTextNode(n.getString("image")));
            user.appendChild(lastName);
            users.appendChild(user);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("./config/arts/gen.dat"));
        transformer.transform(source, result);
    }

    public static byte[] createChecksum(byte[] bytes) throws Exception {
        InputStream fis =  new ByteArrayInputStream(bytes);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(byte[] bytes) throws Exception {
        byte[] b = createChecksum(bytes);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
