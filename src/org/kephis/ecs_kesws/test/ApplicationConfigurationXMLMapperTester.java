/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.test;

import java.util.Iterator;
import java.util.List;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;

/**
 *
 * @author kim
 */
public class ApplicationConfigurationXMLMapperTester {
      public static void main(String[] args) {
          
          ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper=new ApplicationConfigurationXMLMapper();
          List<String> filesTypestoReceive = applicationConfigurationXMLMapper.getFilesTypestoReceive();
          for (Iterator<String> iterator = filesTypestoReceive.iterator(); iterator.hasNext();) {
              String next = iterator.next();
              System.out.println(next);
              
          }
      }
}
