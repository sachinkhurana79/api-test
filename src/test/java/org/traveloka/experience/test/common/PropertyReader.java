package org.traveloka.experience.test.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * // TODO Comment
 */
public class PropertyReader {

  static Properties prop;

  public PropertyReader() {
    prop = new Properties();
    InputStream input = null;

    try {
      //System.out.println("Working Directory = " + System.getProperty("user.dir"));
      String filename = System.getProperty("user.dir") + "/resources/config.properties";
      prop.load(new FileInputStream(filename.trim()));

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public void printThemAll(){

    try {
      Enumeration<?> e = prop.propertyNames();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        String value = prop.getProperty(key);
        System.out.println("Key : " + key + ", Value : " + value);
      }

    } catch (Exception ex) {
          ex.printStackTrace();
    }
  }


  public static void main (String[]args){
    PropertyReader pReader = new PropertyReader();
    pReader.printThemAll();
    System.out.println(pReader.prop.get("share-stage.url"));
  }

}
