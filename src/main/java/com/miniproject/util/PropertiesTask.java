package com.miniproject.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;

public class PropertiesTask {

   /**
 * @author Administrator
 * @data 2025. 2. 18.
 * @enclosing_method getPropertiesValue
 * @todo TODO
 * @param String propertiesFileName : 읽어낼 프로퍼티스 파일명
 * @param String key : 찾을 value에 대한 key값
 * @returnType String : 찾아온 value값
 */
public static String getPropertiesValue(String propertiesFileName, String key) throws IOException {
      Properties prop = new Properties();
      
      Reader reader = Resources.getResourceAsReader(propertiesFileName);
      prop.load(reader);
      
      return (String)prop.get(key);
   }
}