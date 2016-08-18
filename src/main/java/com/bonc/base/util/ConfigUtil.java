package com.bonc.base.util;

//~--- non-JDK imports --------------------------------------------------------


import com.bonc.base.entity.ConfigurationItem;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * 读写配置文件，支持xml格式，properties格式,list格式
 * list格式为列表格式，如slaves配置
 *
 *
 * @version        16/08/17
 * @author         tianbaochao    
 */
public class ConfigUtil {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * 读取xml配置文件
     * @param absolutePath
     * @return
     */
    public static List<ConfigurationItem> readXml(String absolutePath) {
        List<ConfigurationItem> configurationItems = new ArrayList<ConfigurationItem>();

        // 创建解析对象
        SAXReader reader = new SAXReader();
        Document  doc;

        try {

            // 读取xml文件
            doc = reader.read(new File(absolutePath));

            // 获取根节点
            Element     root = doc.getRootElement();
            Iterator<?> iter = root.elementIterator("property");

            while (iter.hasNext()) {
                Element           prop              = (Element) iter.next();
                ConfigurationItem configurationItem = new ConfigurationItem();

                configurationItem.setName(prop.elementTextTrim("name"));
                configurationItem.setValue(prop.elementTextTrim("value"));

                if (prop.element("desc") != null) {
                    configurationItem.setDesc(prop.elementTextTrim("description"));
                }

                if (prop.element("final") != null) {
                    configurationItem.setStrFinal(prop.elementTextTrim("final"));
                }

                configurationItems.add(configurationItem);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return configurationItems;
    }

    /**
     * 将配置项集合写入xml文件中
	 * 注意：modelFile必须原来就是xml格式文件
     * @param modelFile
     * @param configurationItems
     */
    public static void saveXml(String modelFile, List<ConfigurationItem> configurationItems) {
        SAXReader reader = new SAXReader();
        Document  doc;
        XMLWriter writer = null;

        try {
            doc = reader.read(new File(modelFile));

            Element root = doc.getRootElement();

            root.clearContent();

            for (ConfigurationItem configurationItem : configurationItems) {
                Element el     = DocumentHelper.createElement("property");
                Element nameEl = el.addElement("name");

                nameEl.setText(configurationItem.getName());

                Element valueEl = el.addElement("value");

                valueEl.setText(configurationItem.getValue());

                Element descEl = el.addElement("description");

                descEl.setText(configurationItem.getDesc());

                Element finalEl = el.addElement("final");

                finalEl.setText(configurationItem.getStrFinal());
                root.add(el);
            }

            OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding("UTF-8");
            writer = new XMLWriter(new FileOutputStream(new File(modelFile)), format);
            writer.write(doc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    writer = null;
                }
            }
        }
    }

    /**
     * 读取properties配置文件
     * @param absolutePath
     * @return
     */
    public static List<ConfigurationItem> readProperties(String absolutePath) {
        List<ConfigurationItem> configurationItems = new ArrayList<ConfigurationItem>();
        Properties              prop               = new Properties();
        Map<String, String>     map                = new HashMap<String, String>();

        try {
            FileInputStream in = new FileInputStream(absolutePath);

            prop.load(in);

            for (Object obj : prop.keySet()) {
                log.debug(obj.toString() + ":::::" + prop.get(obj).toString());

                ConfigurationItem configurationItem = new ConfigurationItem();

                configurationItem.setName(obj.toString());
                configurationItem.setValue(prop.get(obj).toString());
                configurationItems.add(configurationItem);
            }
        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("error," + absolutePath);

            return null;
        }

        return configurationItems;
    }

    /**
     * 将配置项集合写入properties文件中
     * @param modelFile
     * @param configurationItems
     *
     * @throws java.io.IOException
     */
    public static void saveProperties(String modelFile, List<ConfigurationItem> configurationItems) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(modelFile)));

        for (ConfigurationItem entry : configurationItems) {
            writer.write(entry.getName() + "=" + entry.getValue());
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

	/**
	 * 读取list配置文件
	 * @param absolutePath
	 * @return
	 */
	public static List<ConfigurationItem> readList(String absolutePath) {
		List<ConfigurationItem> configurationItems = new ArrayList<ConfigurationItem>();
		Properties              prop               = new Properties();
		Map<String, String>     map                = new HashMap<String, String>();

		try {
			FileInputStream in = new FileInputStream(absolutePath);

			prop.load(in);

			for (Object obj : prop.keySet()) {
				log.debug(obj.toString());

				ConfigurationItem configurationItem = new ConfigurationItem();

				configurationItem.setName(obj.toString());
				configurationItems.add(configurationItem);
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("error," + absolutePath);

			return null;
		}

		return configurationItems;
	}

	/**
	 * 将配置项集合写入list文件中
	 * @param modelFile
	 * @param configurationItems
	 *
	 * @throws java.io.IOException
	 */
	public static void saveList(String modelFile, List<ConfigurationItem> configurationItems) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(modelFile)));

		for (ConfigurationItem entry : configurationItems) {
			writer.write( entry.getName());
			writer.newLine();
		}

		writer.flush();
		writer.close();
	}
    /**
     * Method description
     *
     *
     * @param args
     */
    public static void main(String[] args) {

      	ConfigUtil configUtil = new ConfigUtil();
//		List<ConfigurationItem> configurationItems = xmlUtil.readXml("D:\\bigdata\\beh-manager\\etc\\hadoop\\hdfs-site.xml");
//    	System.out.println(JSON.toJSONString(configurationItems));
//
//		xmlUtil.saveXml("D:\\bigdata\\beh-manager\\etc\\hadoop\\hdfs-site2.xml", configurationItems);
//
//		List<ConfigurationItem> configurationItems = xmlUtil.readProperties("D:\\bigdata\\beh-manager\\etc\\hadoop\\zoo.cfg");
//		System.out.println(JSON.toJSONString(configurationItems));
//
//		try {
//			xmlUtil.saveProperties("D:\\bigdata\\beh-manager\\etc\\hadoop\\zoo2.cfg", configurationItems);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		List<ConfigurationItem> configurationItems = configUtil.readList("D:\\bigdata\\beh-manager\\etc\\hadoop\\slaves");
//		System.out.println(JSON.toJSONString(configurationItems));
		try {
			configUtil.saveList("D:\\bigdata\\beh-manager\\etc\\hadoop\\slave2.cfg", configurationItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


//~ Formatted by Jindent --- http://www.jindent.com
