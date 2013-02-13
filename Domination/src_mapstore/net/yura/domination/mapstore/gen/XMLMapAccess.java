package net.yura.domination.mapstore.gen;
import net.yura.domination.mapstore.Category;
import net.yura.domination.mapstore.Map;
import net.yura.mobile.io.ServiceLink.Task;
import java.util.Hashtable;
import java.util.Vector;
import net.yura.mobile.io.kxml2.KXmlParser;
import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;
import net.yura.mobile.io.XMLUtil;
/**
 * THIS FILE IS GENERATED, DO NOT EDIT
 */
public class XMLMapAccess extends XMLUtil {
    public XMLMapAccess() {
    }
    protected void saveObject(XmlSerializer serializer,Object object) throws IOException {
        if (object instanceof Category) {
            serializer.startTag(null,"Category");
            saveCategory(serializer,(Category)object);
            serializer.endTag(null,"Category");
        }
        else if (object instanceof Map) {
            serializer.startTag(null,"Map");
            saveMap(serializer,(Map)object);
            serializer.endTag(null,"Map");
        }
        else if (object instanceof Task) {
            serializer.startTag(null,"Task");
            saveTask(serializer,(Task)object);
            serializer.endTag(null,"Task");
        }
        else {
            super.saveObject(serializer, object);
        }
    }
    protected void saveCategory(XmlSerializer serializer,Category object) throws IOException {
        if (object.getName()!=null) {
            serializer.attribute(null,"name", object.getName() );
        }
        if (object.getId()!=null) {
            serializer.attribute(null,"id", object.getId() );
        }
        if (object.getIconURL()!=null) {
            serializer.attribute(null,"iconURL", object.getIconURL() );
        }
    }
    protected void saveMap(XmlSerializer serializer,Map object) throws IOException {
        if (object.getName()!=null) {
            serializer.attribute(null,"name", object.getName() );
        }
        if (object.getId()!=null) {
            serializer.attribute(null,"id", object.getId() );
        }
        if (object.getVersion()!=null) {
            serializer.attribute(null,"version", object.getVersion() );
        }
        if (object.getDescription()!=null) {
            serializer.attribute(null,"description", object.getDescription() );
        }
        if (object.getAuthorId()!=null) {
            serializer.attribute(null,"authorId", object.getAuthorId() );
        }
        if (object.getAuthorName()!=null) {
            serializer.attribute(null,"authorName", object.getAuthorName() );
        }
        if (object.getDateAdded()!=null) {
            serializer.attribute(null,"dateAdded", object.getDateAdded() );
        }
        if (object.getMapHeight()!=null) {
            serializer.attribute(null,"mapHeight", object.getMapHeight() );
        }
        if (object.getMapUrl()!=null) {
            serializer.attribute(null,"mapUrl", object.getMapUrl() );
        }
        if (object.getMapWidth()!=null) {
            serializer.attribute(null,"mapWidth", object.getMapWidth() );
        }
        if (object.getNumberOfDownloads()!=null) {
            serializer.attribute(null,"numberOfDownloads", object.getNumberOfDownloads() );
        }
        if (object.getNumberOfRatings()!=null) {
            serializer.attribute(null,"numberOfRatings", object.getNumberOfRatings() );
        }
        if (object.getPreviewUrl()!=null) {
            serializer.attribute(null,"previewUrl", object.getPreviewUrl() );
        }
        if (object.getRating()!=null) {
            serializer.attribute(null,"rating", object.getRating() );
        }
    }
    protected void saveTask(XmlSerializer serializer,Task object) throws IOException {
        if (object.getMethod()!=null) {
            serializer.attribute(null,"method", object.getMethod() );
        }
        serializer.startTag(null,"object");
        saveObject(serializer, object.getObject() );
        serializer.endTag(null,"object");
    }
    protected Object readObject(KXmlParser parser) throws Exception {
        String name = parser.getName();
        if ("Category".equals(name)) {
            return readCategory(parser);
        }
        else if ("Map".equals(name)) {
            return readMap(parser);
        }
        else if ("Task".equals(name)) {
            return readTask(parser);
        }
        else {
            return super.readObject(parser);
        }
    }
    protected Category readCategory(KXmlParser parser) throws Exception {
        Category object = new Category();
        int count = parser.getAttributeCount();
        for (int c=0;c<count;c++) {
            String key = parser.getAttributeName(c);
            String value = parser.getAttributeValue(c);
            if ("name".equals(key)) {
                object.setName(value);
            }
            else if ("id".equals(key)) {
                object.setId(value);
            }
            else if ("iconURL".equals(key)) {
                object.setIconURL(value);
            }
            else {
                System.out.println("unknown item found "+key);
            }
        }
        parser.skipSubTree();
        return object;
    }
    protected Map readMap(KXmlParser parser) throws Exception {
        Map object = new Map();
        int count = parser.getAttributeCount();
        for (int c=0;c<count;c++) {
            String key = parser.getAttributeName(c);
            String value = parser.getAttributeValue(c);
            if ("name".equals(key)) {
                object.setName(value);
            }
            else if ("version".equals(key)) {
                object.setVersion(value);
            }
            else if ("description".equals(key)) {
                object.setDescription(value);
            }
            else if ("id".equals(key)) {
                object.setId(value);
            }
            else if ("authorId".equals(key)) {
                object.setAuthorId(value);
            }
            else if ("authorName".equals(key)) {
                object.setAuthorName(value);
            }
            else if ("dateAdded".equals(key)) {
                object.setDateAdded(value);
            }
            else if ("mapHeight".equals(key)) {
                object.setMapHeight(value);
            }
            else if ("mapUrl".equals(key)) {
                object.setMapUrl(value);
            }
            else if ("mapWidth".equals(key)) {
                object.setMapWidth(value);
            }
            else if ("numberOfDownloads".equals(key)) {
                object.setNumberOfDownloads(value);
            }
            else if ("numberOfRatings".equals(key)) {
                object.setNumberOfRatings(value);
            }
            else if ("previewUrl".equals(key)) {
                object.setPreviewUrl(value);
            }
            else if ("rating".equals(key)) {
                object.setRating(value);
            }
            else {
                System.out.println("unknown item found "+key);
            }
        }
        parser.skipSubTree();
        return object;
    }
    protected Task readTask(KXmlParser parser) throws Exception {
        Task object = new Task();
        int count = parser.getAttributeCount();
        for (int c=0;c<count;c++) {
            String key = parser.getAttributeName(c);
            String value = parser.getAttributeValue(c);
            if ("method".equals(key)) {
                object.setMethod(value);
            }
            else {
                System.out.println("unknown item found "+key);
            }
        }
        while (parser.nextTag() != KXmlParser.END_TAG) {
            String name = parser.getName();
            if ("object".equals(name)) {
                Object obj = null;
                while (parser.nextTag() != KXmlParser.END_TAG) {
                    if (obj!=null) { throw new IOException(); }
                    obj = readObject(parser);
                }
                object.setObject( (Object)obj );
            }
            else {
                System.out.println("unknown section: "+name);
                parser.skipSubTree();
            }
        }
        return object;
    }
}
