package com.miniproject.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;

public class PropertiesTask {

   public static String getPropertiesValue(String propertiesFileName, String key) throws IOException {
      Properties prop = new Properties();
      
      Reader reader = Resources.getResourceAsReader(propertiesFileName);
      prop.load(reader);
      
      return (String)prop.get(key);
   }
}