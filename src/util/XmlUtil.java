package util;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

    private Document m_doc;
    private String m_cfgFile;

    public XmlUtil(String cfgFile) {
        this.m_cfgFile = cfgFile;
        File file = new File(cfgFile);
        try {
            if (file.exists()) {
                SAXReader reader = new SAXReader();
                // 读取XML文件
                m_doc = reader.read(file);
            }
        } catch (Exception e) {
            m_doc = null;
        }
    }

    public void save() {
        if (m_doc == null) return;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileOutputStream(m_cfgFile), format);
            writer.write(m_doc);
            writer.close();
        } catch (Exception e) {
            m_doc = null;
        }
    }

    public void write(String key, String value) {
        if (m_doc == null) return;
        try {
            Node node = m_doc.selectSingleNode(key);
            if (node == null) {
                return;
            }
            node.setText(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readAsString(String key) {
        if (m_doc == null) {
            return null;
        }
        try {
            Node node = m_doc.selectSingleNode(key);
            if (node == null) {
                return null;
            }
            return node.getText();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer readAsInteger(String key) {
        String str = readAsString(key);
        if (str == null) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }

    public Double readAsDouble(String key) {
        String str = readAsString(key);
        if (str == null) {
            return null;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer readAsInteger(String key, Integer defaultValue) {
        String str = readAsString(key);
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Double readAsDouble(String key, Double defaultValue) {
        String str = readAsString(key);
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public List<Integer> readInts(String key, String separators) {
        String str = this.readAsString(key);
        String[] arrs = str.split(separators);

        List<Integer> ls = new ArrayList<>();
        for (String arr : arrs) {
            if (arr.length() > 0) {
                int i = Integer.parseInt(arr);
                if (!ls.contains(i)) {
                    ls.add(i);
                }
            }
        }
        return ls;
    }

}
