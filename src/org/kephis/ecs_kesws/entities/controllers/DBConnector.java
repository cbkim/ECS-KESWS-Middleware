package org.kephis.ecs_kesws.entities.controllers;

import java.sql.Connection;
  
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.SQLException;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
 

public class DBConnector {

    BoneCP connectionPool = null;
    Connection conn = null;
 
 
    public Connection ECSDBConnectorService() {
        ApplicationConfigurationXMLMapper configMapper = new ApplicationConfigurationXMLMapper();


        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = configMapper.getECSDatabaseUrl();
        final String USER = configMapper.getECSDatabaseuser();
        final String PASS = configMapper.getECSDatabasepassword();
        try {

            Class.forName(JDBC_DRIVER);
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL);
            config.setUsername(USER);
            config.setPassword(PASS); 
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            config.setDetectUnclosedStatements(true);
            config.setMaxConnectionAgeInSeconds(30);
            config.setCloseOpenStatements(true);
            connectionPool = new BoneCP(config);
            conn = connectionPool.getConnection();
            
        } catch (Exception e) {
            
            e.printStackTrace();
          //  ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
         connectionPool.shutdown();
        return conn;
    }

    public void CloseECSDBConnectorService() {
        
        try {
            conn.close();
            connectionPool.shutdown();
        } catch (SQLException ex) {
          //  ECSKESWSFileLogger.Log(ex.toString(), "SEVERE");
        }
    }
}

 
