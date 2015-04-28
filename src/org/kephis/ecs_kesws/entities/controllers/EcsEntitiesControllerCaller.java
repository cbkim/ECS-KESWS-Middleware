/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.entities.controllers;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.ApplicationConfigurationXMLMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;

/**
 *
 * @author kim
 */
public class EcsEntitiesControllerCaller {

    UtilityClass utilclass;

    String JDBC_DRIVER = "";
    String DB_URL = "";
    String USER = "";
    String PASS = "";
    String FIN_DB_URL = "";
    String FIN_USER = "";
    String FIN_PASS = "";

    String DB_URL_ECS_KESWS;
    String USER_ECS_KESWS;
    String PASS_ECS_KESWS;

    public EcsEntitiesControllerCaller() {
        }

    public EcsEntitiesControllerCaller(ApplicationConfigurationXMLMapper applicationConfigurationXMLMapper) {
        utilclass = new UtilityClass();
        JDBC_DRIVER = "com.mysql.jdbc.Driver";
        DB_URL = applicationConfigurationXMLMapper.getECSDatabaseUrl();
        USER = applicationConfigurationXMLMapper.getECSDatabaseuser();
        PASS = applicationConfigurationXMLMapper.getECSDatabasepassword();
        
        FIN_DB_URL = applicationConfigurationXMLMapper.getIntergrationDatabaseUrl();
        FIN_USER = applicationConfigurationXMLMapper.getIntergrationDatabaseuser();
        FIN_PASS = applicationConfigurationXMLMapper.getIntergrationDatabasepassword();

        //THIS IS THE CONNECTION TO ECS_KESWS database
        DB_URL_ECS_KESWS = applicationConfigurationXMLMapper.getECS_Kesws_DbUrl();
        USER_ECS_KESWS = applicationConfigurationXMLMapper.getECS_Kesws_user();
        PASS_ECS_KESWS = applicationConfigurationXMLMapper.getECS_Kesws_pass();

    }

    public boolean ecsTransactionCommit(RecCdFileMsg recCdFileMsg, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController, ConsignmentDocument keswsConsignmentDocumentObj, ECSConsignmentDoc ecsConsignmentDocumentObj, int ClientId) {
        boolean transactioniscomplete = false;
        int CreatedECSconsignmentId = 0;
        CreatedECSconsignmentId = createECSconsignmentdetails(ecsConsignmentDocumentObj, ClientId,
                recCdFileMsg, ecsKeswsEntitiesController);
        if (CreatedECSconsignmentId != 0) {
            transactioniscomplete = createECSconsignmentVariatyQuantities(recCdFileMsg, keswsConsignmentDocumentObj,
                    ecsConsignmentDocumentObj, CreatedECSconsignmentId, ClientId, ecsKeswsEntitiesController);
            transactioniscomplete = createECSconsignmentPhytoCertificate(CreatedECSconsignmentId,
                    recCdFileMsg, ecsKeswsEntitiesController);
            transactioniscomplete = createConsignmentInvoice(CreatedECSconsignmentId, ClientId, ecsKeswsEntitiesController);
        }

        return transactioniscomplete;
    }

    public boolean ecsTransactionRollBack(String receivedFile) {
        return false;
    }

    private int createECSconsignmentdetails(ECSConsignmentDoc desObject,
        int clientId, RecCdFileMsg recCdFileMsg, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController) {
        BoneCP connectionPool = null;
        Connection connection = null;
        int ConsignmentId = 0;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                String p_cgt_id;
                int p_moc_id;
                int p_ilo_id;
                String p_csg_id;
                int p_clt_id;
                int p_cca_id;
                int p_cty_id;
                int p_cfo_id;
                String p_iap_id;
                String p_prefinspdate;
                String p_prefinsptime = "";
                String p_departuredate;
                String p_departuretime = new SimpleDateFormat("HH:mm").format(new java.util.Date());;
                String p_invoice_nr;
                String p_pointofentry;
                String p_addinfo;
                String p_dismarks;
                String p_status;
                String p_status_date;
                String p_cnt_id;
                int p_cft_id;
                int p_crt_id;
                String p_appdate;
                String p_apptime;
                long p_cgt_weight;
                p_cgt_id = " ";
                p_moc_id = desObject.getMeansofconveyance();
                p_ilo_id = desObject.getInspectionlocation();
                p_csg_id = "" + desObject.getConsigneeID() + "";
                p_clt_id = clientId;
                p_cca_id = 0;
                p_cty_id = 0;
                p_cfo_id = 0;
                p_iap_id = "";
                p_prefinspdate = desObject.getPreferredinspectiondate();
                if (desObject.getPreferredinspectiontime().length() >= 12 && desObject.getTimeofdeparture().length() >= 12) {

                    p_prefinsptime = desObject.getPreferredinspectiontime().substring(
                            8, 10)
                            + ":"
                            + desObject.getPreferredinspectiontime().substring(10, 12);
                    p_departuretime = desObject.getTimeofdeparture().substring(8, 10)
                            + ":" + desObject.getTimeofdeparture().substring(10, 12);
                } else if (desObject.getPreferredinspectiontime().length() <= 12) {
                    p_prefinsptime = new SimpleDateFormat("HH:mm").format(new java.util.Date());
                    ecsKeswsEntitiesController.logError(recCdFileMsg.getFileName(), "CONSIGNMENT PREFERRED INSPECTION TIME NOT SET ");
                    ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CONSIGNMENT PREFERRED INSPECTION TIME SET TO " + p_prefinsptime);
                } else if (desObject.getTimeofdeparture().length() <= 12) {
                    p_departuretime = new SimpleDateFormat("HH:mm").format(new java.util.Date());
                    ecsKeswsEntitiesController.logError(recCdFileMsg.getFileName(), "CONSIGNMENT PREFERRED DEPARTURE TIME NOT SET ");
                    ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CONSIGNMENT PREFERRED DEPARTURE TIME SET TO " + p_prefinsptime);
                }
                p_departuredate = "" + desObject.getDateofdeparture();
                p_invoice_nr = desObject.getInvoicenr() + "";
                p_pointofentry = desObject.getPointofentry() + " ";
                p_addinfo = desObject.getAdditionalinformation() + " ";
                p_dismarks = desObject.getDistinguishingmarks() + " ";
                p_status = "SAVED";
                p_status_date = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date());
                if (desObject.getCountryofdestination().contains("KE")) {
                    p_cnt_id = "KE";
                } else {
                    p_cnt_id = desObject.getCountryofdestination();
                }
                p_cft_id = 0;
                p_crt_id = 0;
                p_appdate = p_status_date;
                p_apptime = "00:00";
                p_cgt_weight = (long) desObject.getConsignementweight();
                String sql = "{call splient_insertIAPCGTinfo_foruploadedCGTXML(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                CallableStatement stmt = connection.prepareCall(sql);
                stmt.setString(1, p_cgt_id);
                stmt.setInt(2, p_moc_id);
                stmt.setInt(3, p_ilo_id);
                stmt.setString(4, p_csg_id);
                stmt.setInt(5, p_clt_id);
                stmt.setInt(6, p_cca_id);
                stmt.setInt(7, p_cty_id);
                stmt.setInt(8, p_cfo_id);
                stmt.setString(9, p_iap_id);
                stmt.setString(10, p_prefinspdate);
                stmt.setString(11, p_prefinsptime);
                stmt.setString(12, p_departuredate);
                stmt.setString(13, p_departuretime); /* 13 Depature time */

                stmt.setString(14, p_invoice_nr); /* 14 Invoice Number */

                stmt.setString(15, p_pointofentry); /* 15 Point of entry */

                stmt.setString(16, p_addinfo); /* 16 Additional info */

                stmt.setString(17, p_dismarks); /* 15 Dismarks */

                stmt.setString(18, p_status); /* 15 Status SAVED SUBMITTED */

                stmt.setString(19, p_status_date); /* 15 p_status_date null */

                stmt.setString(20, p_cnt_id); /* 16 p_cnt_id country id 10 NLD */

                stmt.setInt(21, p_cft_id); /* 17 p_cft_id 0 */

                stmt.setInt(22, p_crt_id); /* 18 p_crt_id 0 */

                stmt.setString(23, p_appdate); /* 19 p_appdate */

                stmt.setString(24, p_apptime); /* 20 p_apptime */

                stmt.setFloat(25, p_cgt_weight); /* 21 p_cgt_weight */

                stmt.executeUpdate();
                ConsignmentId = getCreatedECSconsignmentId(p_invoice_nr);
                recCdFileMsg.setECSCONSIGNEMENTIDRef(ConsignmentId);
                recCdFileMsg.setInvoiceNO(p_invoice_nr);
                ecsKeswsEntitiesController.updateRecCDFileMsg(recCdFileMsg);
                ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CONSIGNMENT STATUS SAVED");
                p_status = "SUBMITTED";
                String p_statusdate = p_status_date;
                String p_app_date = p_appdate;
                String p_app_time = p_apptime;
                sql = "{call spclient_updateconsignementstatus(?,?,?,?,?)}";
                stmt = connection.prepareCall(sql);
                stmt.setString(1, p_status);
                stmt.setString(2, p_statusdate);
                stmt.setInt(3, ConsignmentId);
                stmt.setString(4, p_app_date);
                stmt.setString(5, p_app_time);
                stmt.executeUpdate();
                ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CONSIGNMENT STATUS SUBMITTED");
            }
            connectionPool.shutdown();
        } catch (Exception ex) {
            //  ECSKESWSFileLogger.Log(e.toString(), "SEVERE"); 
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ConsignmentId;
    }

    /**
     * *
     *
     * @param SourceDoc Source document details
     * @param Clientid Client Id
     * @return Created consignee id of client
     */
    public int createClientConsignee(ConsignmentDocument SourceDoc, int Clientid) {
        BoneCP connectionPool = null;
        Connection connection = null;
        int consigneeid = 0;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                int p_flag = 1;
                int p_csg_id;
                String p_firmname;
                String p_streetname1;
                String p_streetname2;
                String p_postalcode;
                String p_town;
                String p_cnt_id;
                String p_number;
                String p_shortname;
                String p_clt_id;
                /* Initialize variables * */
                p_csg_id = 0;
                p_firmname = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDConsignee().getName()
                        + "";
                p_streetname1 = " "
                        + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDConsignee().getPhysicalAddress();
                if (SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPostalAddress().contentEquals(p_streetname1)) {
                    p_streetname2 = " ";
                } else {
                    p_streetname2 = " " + SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPostalAddress();
                }
                p_postalcode = "  ";
                //    + SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPostalAddress();
                p_town = " ";
                //  SourceDoc.getDocumentDetails().getConsignmentDocDetails() .getCDConsignee().getPhysicalAddress();
                if (SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDConsignee().getPhyCountry().contains("KE")) {
                    p_cnt_id = "KE";
                } else {
                    p_cnt_id = SourceDoc.getDocumentDetails()
                            .getConsignmentDocDetails().getCDConsignee().getPhyCountry();
                }
                p_number = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDConsignee().getTeleFax();
                p_shortname = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDConsignee().getName().substring(0, 3);
                p_clt_id = "" + Clientid + "";

                CallableStatement stmt1 = null;
                String sql2 = "{call spclient_insertupdateconsignee(?," + "?,"
                        + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?"
                        + ")}";
                System.out.println("{call spclient_insertupdateconsignee(" + p_flag + "," + p_csg_id + ","
                        + p_firmname + "," + p_streetname1 + "," + p_streetname2 + "," + p_postalcode + ","
                        + p_town + "," + p_cnt_id + "," + p_number + "," + p_shortname + ","
                        + p_clt_id + ""
                        + ")}");
                stmt1 = connection.prepareCall(sql2);
                stmt1.setInt(1, p_flag);
                stmt1.setInt(2, p_csg_id);
                stmt1.setString(3, p_firmname);
                stmt1.setString(4, p_streetname1);
                stmt1.setString(5, p_streetname2);
                stmt1.setString(6, p_postalcode);
                stmt1.setString(7, p_town);
                stmt1.setString(8, p_cnt_id);
                stmt1.setString(9, p_number);
                stmt1.setString(10, p_shortname);
                stmt1.setString(11, p_clt_id);
                stmt1.setString(12, "");
                stmt1.executeUpdate();
            }
            connectionPool.shutdown();
        } catch (Exception ex) {
            //  ECSKESWSFileLogger.Log(e.toString(), "SEVERE"); 
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return consigneeid = getConsigneeId(SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                .getCDConsignee().getName(), Clientid, SourceDoc.getDocumentDetails()
                .getConsignmentDocDetails().getCDConsignee().getPhyCountry(), 0);
    }

    public void createClientDetails(ConsignmentDocument SourceDoc) {
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                String p_cltid;
                int p_tocid;
                String p_firmname;
                String p_contact;
                String p_poststreetname;
                String p_postnumber;
                String p_postpostalcode;
                String p_posttown;
                String p_postcntid;
                String p_phone;
                String p_fax;
                String p_mail;
                String p_exLicNR;
                String p_VatNR;
                String p_pin;
                String p_status;
                String p_cltid_subchar;
                /* Initialize variables * */
                p_cltid = " ";
                p_tocid = 1;
                p_firmname = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getName();
                p_contact = " Kentrade kESWS Contact details ";
                p_poststreetname = " "
                        + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getPhysicalAddress();
                p_postnumber = " "
                        + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getPostalAddress();
                p_postpostalcode = "  "
                        + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getPostalAddress();
                p_posttown = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getPhyCountry();
                if (SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getPhyCountry().contains("KE")) {
                    p_postcntid = "KE";
                } else {
                    p_postcntid = SourceDoc.getDocumentDetails()
                            .getConsignmentDocDetails().getCDExporter().getPhyCountry();
                }
                p_phone = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getTeleFax()
                        + "  ";
                p_fax = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getTeleFax()
                        + "  ";
                p_mail = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getEmail()
                        + "  ";
                p_exLicNR = "HCDA No  ";
                p_VatNR = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getTIN()
                        + "  ";
                p_pin = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getTIN()
                        + "  ";
                p_status = "KESWS";
                p_cltid_subchar = SourceDoc.getDocumentDetails()
                        .getConsignmentDocDetails().getCDExporter().getName()
                        .substring(0, 1);

                CallableStatement stmt1 = null;
                String sql2 = "{call spclient_insert_kesws(?," + "?," + "?," + "?,"
                        + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?," + "?," + "?," + "?" + ")}";
                stmt1 = connection.prepareCall(sql2);

                stmt1.setString(1, p_cltid); /* Client id */

                stmt1.setInt(2, p_tocid); /* Type of client 1 exporter 2 */

                stmt1.setString(3, p_firmname);/* Firm or Client Name */

                stmt1.setString(4, p_contact); /* Contact not specified */

                stmt1.setString(5, p_poststreetname); /* 5 client id */

                stmt1.setString(6, p_postnumber); /* 6 p_cca_id insert 0 */

                stmt1.setString(7, p_postpostalcode); /* 7 Commodity type id insert 0 */

                stmt1.setString(8, p_posttown); /* 8 Commodity form id 0 */

                stmt1.setString(9, p_postcntid); /*
                 * 9 Inserted inspection application
                 * * value not used
                 */

                stmt1.setString(10, p_phone); /*
                 * 10 Prefered inspection date
                 * 10-04-2013
                 */

                stmt1.setString(11, p_fax); /* 11 Prefered inspection time 20:10:10 */

                stmt1.setString(12, p_mail); /*
                 * 12 Depature date 2013-05-10-
                 * 2011-04-26
                 */

                stmt1.setString(13, p_exLicNR); /* 13 Depature time */

                stmt1.setString(14, p_VatNR); /* 14 Invoice Number */

                stmt1.setString(15, p_pin); /* 15 Point of entry */

                stmt1.setString(16, p_status); /* 16 Additional info */

                stmt1.setString(17, p_cltid_subchar); /* 15 Dismarks */

                stmt1.executeUpdate();
            }
            connectionPool.shutdown();
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public boolean createECSconsignmentPhytoCertificate(Integer CGT_ID, RecCdFileMsg recCdFileMsgId, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesControllerCaller) {
        BoneCP connectionPool = null;
        Connection connection = null;
        boolean transactioniscomplete = false;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                Statement stmt = connection.createStatement();
                String sql = " insert into certificate (CGT_ID, CRT_ID) values ("
                        + CGT_ID.toString() + "," + "1" + ");";
                stmt.executeUpdate(sql);
                connection.close();
                ecsKeswsEntitiesControllerCaller.logInfo(recCdFileMsgId.getFileName(),
                        "CERTIFICATE ID FOR CONSIGNEMENT ID " + CGT_ID.toString() + " CREATED");
                transactioniscomplete = true;

            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            ecsKeswsEntitiesControllerCaller.logInfo(recCdFileMsgId.getFileName(),
                    "CERTIFICATE ID FOR CONSIGNEMENT ID " + CGT_ID.toString() + " NOT CREATED " + ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return transactioniscomplete;
    }

    public boolean createClientProducer(ConsignmentDocument SourceDoc, String producerName, int ClientId) {
        BoneCP connectionPool = null;
        Connection connection = null;
        boolean iscreated = false;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 

            int p_flag = 1;
            int p_prd_id;
            String p_firmname;
            String p_streetname1;
            String p_streetname2;
            String p_postalcode;
            String p_town;
            String p_cnt_id;
            String p_number;
            String p_shortname;
            String p_clt_id;
            String p_vatnr;
            String p_phone;
            /* Initialize variables * */
            p_prd_id = getLastProducerId();

            if (producerName == null) {
                p_firmname = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                        .getCDExporter().getName();
            } else {
                p_firmname = "" + producerName + " ";
            }

            p_streetname1 = " "
                    + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getPhysicalAddress();
            p_streetname2 = " "
                    + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getPostalAddress();
            p_postalcode = "  "
                    + SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getPostalAddress();
            p_town = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getPhysicalAddress();
            if (SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getPhyCountry().contains("KE")) {
                p_cnt_id = "KE";
            } else {
                p_cnt_id = SourceDoc.getDocumentDetails()
                        .getConsignmentDocDetails().getCDExporter().getPhyCountry();
            }
            p_number = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getTeleFax();
            p_shortname = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getName().substring(0, 3);
            p_clt_id = "" + ClientId + "";
            p_vatnr = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getTIN();
            p_phone = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                    .getCDExporter().getTeleFax();
            if (connection != null) {

                CallableStatement stmt1 = null;
                String sql2 = "{call spclient_insertupdateproducer(?," + "?,"
                        + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?," + "?"
                        + ")}";
                stmt1 = connection.prepareCall(sql2);
                stmt1.setInt(1, p_flag);
                stmt1.setInt(2, p_prd_id);
                stmt1.setString(3, p_firmname);
                stmt1.setString(4, p_streetname1);
                stmt1.setString(5, p_streetname2);
                stmt1.setString(6, p_postalcode);
                stmt1.setString(7, p_town);
                stmt1.setString(8, p_cnt_id);
                stmt1.setString(9, p_number);
                stmt1.setString(10, p_shortname);
                stmt1.setString(11, p_clt_id);
                stmt1.setString(12, p_vatnr);
                stmt1.setString(13, p_phone);
                iscreated = stmt1.execute();
            }
            connectionPool.shutdown(); // shutdown connection pool.

        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            // ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return iscreated;
    }

    /**
     * *
     *
     * @param srcConsDoc the source document from KESWS
     * @param desObject the destination document to KESWS
     * @param consignmentId the consignment id
     */
    private boolean createECSconsignmentVariatyQuantities(RecCdFileMsg recCdFileMsg,
            ConsignmentDocument srcConsDoc, ECSConsignmentDoc desObject,
            int consignmentId, int ClientId, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController) {

        boolean transactioniscomplete = false;
        List<ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails> itemdetails = srcConsDoc
                .getDocumentDetails().getConsignmentDocDetails()
                .getCDProductDetails().getItemDetails();

        for (Iterator<ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails> iterator = itemdetails.iterator(); iterator
                .hasNext();) {
            transactioniscomplete = false;
            ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails itemDetails2 = (ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails) iterator.next();
            BoneCP connectionPool = null;
            Connection connection = null;
            try {
                // load the database driver (make sure this is in your classpath!)
                Class.forName(JDBC_DRIVER);
                // setup the connection pool
                BoneCPConfig config = new BoneCPConfig();
                config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(USER);
                config.setPassword(PASS);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                connectionPool = new BoneCP(config); // setup the connection pool 
                connection = connectionPool.getConnection(); // fetch a connection 
                if (connection != null) {
                    Statement stmt = null;
                    int producerId = 0;
                    String producerName = " ";
                    String producerdetails = " ";
                    String TreatmentInformation = " ";
                    String chemicalsActiveIngredients = " ";
                    String durationsAndTemperature = " ";
                    String concentrationActiveIngredients = " ";
                    int ConsigneeId = desObject.getConsigneeID();
                    String InternalProductNoCommodityCategory = "060";
                    String InternalProductNoCommodityTypeId = "1426";//1426 live dev "1414" 
                    String InternalProductNoCommodityVarId = "2216";//2216 live dev "2188" 
                    String ItemComFormId = "147";//147 live dev 145 
                    String PackageId = "801";//801 live dev 854 
                    if (itemDetails2.getCDProduct1().getInternalProductNo() != null) {
                        if (itemDetails2.getCDProduct1().getInternalProductNo().length() != 0) {
                            InternalProductNoCommodityCategory = itemDetails2.getCDProduct1().getInternalProductNo().substring(0, 3);
                            InternalProductNoCommodityTypeId = itemDetails2.getCDProduct1().getInternalProductNo().substring(3, 7);
                            InternalProductNoCommodityVarId = itemDetails2.getCDProduct1().getInternalProductNo().substring(7, 11);
                            ItemComFormId = itemDetails2.getCDProduct1().getInternalProductNo().substring(11, 14);
                            PackageId = getTypeOfPackageingId(InternalProductNoCommodityCategory, itemDetails2.getCDProduct1()
                                    .getInternalProductNo().substring(3, 7), itemDetails2.getCDProduct1().getPackageTypeDesc());
                        }
                    }

                    final BigDecimal ItemQty = itemDetails2.getCDProduct1().getQuantity().getQty();
                    String UnitId = getVAQUnitId(itemDetails2.getCDProduct1().getQuantity().getUnitOfQty());

                    if (Integer.parseInt(getVAQUnitId(UnitId)) == 0) {
                        createUnit(itemDetails2.getCDProduct1().getQuantity().getUnitOfQty());
                        ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CREATED UNIT " + itemDetails2.getCDProduct1().getQuantity().getUnitOfQty());
                    } else {
                        UnitId = getVAQUnitId(itemDetails2.getCDProduct1().getQuantity().getUnitOfQty());

                    }
                    float packQty = itemDetails2.getCDProduct1().getPackageQty().floatValue();
                    try {
                        producerName = itemDetails2.getCDItemCommodity().getProducerDetails();
                        producerdetails = itemDetails2.getCDItemCommodity().getProducerDetails();
                    } catch (Exception ex) {
                        producerName = srcConsDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getName();
                        producerdetails = srcConsDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getName();
                        ecsKeswsEntitiesController.logError(recCdFileMsg.getFileName(), "PRODUCER DETAILS NOT SPECIFIED");
                    }
                    producerId = getProducerId(producerName, ClientId,
                            itemDetails2.getCDProduct1().getCountryOfOrigin());
                    if (producerId == 0) {
                        try {
                            if (itemDetails2
                                    .getCDItemCommodity().getProducerDetails() != null) {
                                createClientProducer(srcConsDoc, itemDetails2
                                        .getCDItemCommodity().getProducerDetails(), ClientId);
                                ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CREATED PRODUCER " + itemDetails2
                                        .getCDItemCommodity().getProducerDetails());

                            }

                        } catch (Exception ex) {
                            ecsKeswsEntitiesController.logError(recCdFileMsg.getFileName(), "PRODUCER NOT SPECIFIED " + ex.getMessage().toUpperCase());

                        }
                    }

                    producerId = getProducerId(producerdetails, ClientId,
                            itemDetails2.getCDProduct1().getCountryOfOrigin());

                    String TreatmentDate = "";

                    try {
                        if (itemDetails2.getCDItemCommodity().getTreatmentDate() != null && itemDetails2.getCDItemCommodity().getTreatmentDate().length() != 0) {

                            TreatmentDate = itemDetails2.getCDItemCommodity().getTreatmentDate();
                        }
                        TreatmentInformation = itemDetails2.getCDItemCommodity()
                                .getTreatmentInformation();
                        chemicalsActiveIngredients = itemDetails2.getCDItemCommodity()
                                .getChemicalsActiveIngredients();
                        durationsAndTemperature = itemDetails2.getCDItemCommodity()
                                .getDurationsAndTemperature() + " ";
                        concentrationActiveIngredients = itemDetails2.getCDItemCommodity()
                                .getConcentrationActiveIngredients();
                    } catch (Exception ex) {
                        TreatmentInformation = " ";
                        durationsAndTemperature = " ";
                        concentrationActiveIngredients = " ";
                    }

                    stmt = connection.createStatement();
                    if (TreatmentDate.length() == 0) {
                        String sql = "insert into varietyquantity (CSG_ID,CTY_ID,CVA_ID,QUANTITY_ORIGINAL,UNIT_ID,NUMBER_OF_PACKAGES,TOP_ID,TREATMENT_DETAILS,TREATMENT_DATE,CHEMICAL_INGREDIANTS,DURATION_TEMPARATURE,CONCENTRATION,ORIGIN_CNT_ID,PRD_ID,CGT_ID,CFO_ID) values ('"
                                + ConsigneeId
                                + "','"
                                + InternalProductNoCommodityTypeId
                                + "','"
                                + InternalProductNoCommodityVarId
                                + "','"
                                + ItemQty.floatValue()
                                + "','"
                                + UnitId
                                + "','"
                                + packQty
                                + "','"
                                + PackageId
                                + "',"
                                + "NULL"
                                + ","
                                + "NULL"
                                + ","
                                + "NULL"
                                + ","
                                + "NULL"
                                + ","
                                + "NULL"
                                + ",'"
                                + itemDetails2.getCDProduct1().getCountryOfOrigin()
                                + "','"
                                + producerId
                                + "','"
                                + consignmentId
                                + "','"
                                + ItemComFormId + "');";

                        stmt.executeUpdate(sql);
                        //validate existing ipc code 
                        // if not available create ipc code and log
                        //InternalProductcodesJpaController ipcontr=new InternalProductcodesJpaController(null)
                        InternalProductcodes IPCObj = null;
                        Double weight = (Double) ItemQty.doubleValue();
                        System.err.println("ERROR LOG");
                        if (!ecsKeswsEntitiesController.internalProductCodesExist(itemDetails2.getCDProduct1().getInternalProductNo())) {
                            IPCObj = new InternalProductcodes();
                            Date date = new java.sql.Date(2010, 10, 10);
                            IPCObj.setAggregateIPCCodeLevel(0);
                            IPCObj.setInternalProductCode(itemDetails2.getCDProduct1().getInternalProductNo());
                            IPCObj.setHscode(itemDetails2.getCDProduct1().getItemHSCode());
                            IPCObj.setHscodeDesc("System generated " + itemDetails2.getCDProduct1().getHSDescription() + ":" + itemDetails2.getCDProduct1().getItemDescription());
                            IPCObj.setDateDeactivated(date);
                            System.err.println("ERROR LOG2 " + itemDetails2.getCDProduct1().getInternalProductNo());
                            ecsKeswsEntitiesController.createInternalProductcode(IPCObj);
                            ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CREATED IPC ENTRY ON ECSKESWSDB");

                        }
                        IPCObj = ecsKeswsEntitiesController.getInternalProductcodes(itemDetails2.getCDProduct1().getInternalProductNo());
                        System.err.println("ERROR LOG3 " + IPCObj.getInternalProductCode());
                        ecsKeswsEntitiesController.updateCreateInternalProductcodePriceDocMappings(recCdFileMsg, IPCObj);
                        ecsKeswsEntitiesController.recCDFileMsgDetails(recCdFileMsg, IPCObj, weight);
                        transactioniscomplete = true;

                    } else {
                        String sql = "insert into varietyquantity (CSG_ID,CTY_ID,CVA_ID,QUANTITY_ORIGINAL,UNIT_ID,NUMBER_OF_PACKAGES,TOP_ID,TREATMENT_DETAILS,TREATMENT_DATE,CHEMICAL_INGREDIANTS,DURATION_TEMPARATURE,CONCENTRATION,ORIGIN_CNT_ID,PRD_ID,CGT_ID,CFO_ID) values ('"
                                + ConsigneeId
                                + "','"
                                + InternalProductNoCommodityTypeId
                                + "','"
                                + InternalProductNoCommodityVarId
                                + "','"
                                + ItemQty.floatValue()
                                + "','"
                                + UnitId
                                + "','"
                                + packQty
                                + "','"
                                + PackageId
                                + "','"
                                + TreatmentInformation
                                + "','"
                                + utilclass.formatDateString(TreatmentDate)
                                + "','"
                                + chemicalsActiveIngredients
                                + "','"
                                + durationsAndTemperature
                                + "','"
                                + concentrationActiveIngredients
                                + "','"
                                + itemDetails2.getCDProduct1().getCountryOfOrigin()
                                + "','"
                                + producerId
                                + "','"
                                + consignmentId
                                + "','"
                                + ItemComFormId + "');";
                        stmt.executeUpdate(sql);
                        InternalProductcodes IPCObj = null;
                        Double weight = (Double) ItemQty.doubleValue();
                        if (!ecsKeswsEntitiesController.internalProductCodesExist(itemDetails2.getCDProduct1().getInternalProductNo())) {
                            IPCObj = new InternalProductcodes();
                            Date date = new java.sql.Date(2010, 10, 10);
                            IPCObj.setAggregateIPCCodeLevel(0);
                            IPCObj.setInternalProductCode(itemDetails2.getCDProduct1().getInternalProductNo());
                            IPCObj.setHscode(itemDetails2.getCDProduct1().getItemHSCode());
                            IPCObj.setHscodeDesc("System generated " + itemDetails2.getCDProduct1().getHSDescription() + ":" + itemDetails2.getCDProduct1().getItemDescription());
                            IPCObj.setDateDeactivated(date);
                            ecsKeswsEntitiesController.createInternalProductcode(IPCObj);
                            ecsKeswsEntitiesController.logInfo(recCdFileMsg.getFileName(), "CREATED IPC ENTRY ON ECSKESWSDB");
                        }
                        IPCObj = ecsKeswsEntitiesController.getInternalProductcodes(itemDetails2.getCDProduct1().getInternalProductNo());
                        ecsKeswsEntitiesController.updateCreateInternalProductcodePriceDocMappings(recCdFileMsg, IPCObj);
                        ecsKeswsEntitiesController.recCDFileMsgDetails(recCdFileMsg, IPCObj, weight);
                        transactioniscomplete = true;
                    }
                }
                connectionPool.shutdown(); // shutdown connection pool.
            } catch (Exception ex) {
                Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                transactioniscomplete = false;
                //   ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return transactioniscomplete;

    }

    public boolean createTypeOfPackage(String commodityTypeId,
            String commodityCategoryId, String packageTypeDesc) {

        boolean iscreated = false;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                int p_id = 0;
                String p_name;
                String p_cty_id;
                String p_startdate;
                String p_enddate;
                String p_desc;
                int p_cca_id;
                p_name = packageTypeDesc;
                p_cty_id = commodityTypeId.replaceFirst("^0*", "");
                p_startdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                p_enddate = "2099-09-09";
                p_desc = " ";
                p_cca_id = Integer.parseInt(commodityCategoryId);

                /* Initialize variables * */
                Statement stmt;

                stmt = connection.createStatement();

                String sql = "SELECT   id  FROM  `TYPEOFPACKAGING` ORDER BY id DESC LIMIT  0, 1";

                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    p_id = rs.getInt("id") + 1;
                }

                CallableStatement stmt1 = null;
                String sql2 = "{call spTOP_insert(?," + "?,"
                        + "?," + "?," + "?," + "?," + "?)}";
                stmt1 = connection.prepareCall(sql2);
                stmt1.setInt(1, p_id);
                stmt1.setString(2, p_name);
                stmt1.setString(3, p_cty_id);
                stmt1.setString(4, p_startdate);
                stmt1.setString(5, p_enddate);
                stmt1.setString(6, p_desc);
                stmt1.setInt(7, p_cca_id);
                iscreated = stmt1.execute();
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            connectionPool.shutdown(); // shutdown connection pool.
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return iscreated;

    }

    private boolean createUnit(String unitOfQty) {
        //  //  //  Connection conn = dao.ECSDBConnectorService();
        //  Connection conn = dao.ECSDBConnectorService();
        BoneCP connectionPool = null;
        Connection connection = null;
        boolean iscreated = false;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 

            int p_id;
            String p_name;
            String p_startdate;
            String p_enddate;
            String p_desc;
            /* Initialize variables * */
            p_id = 0;
            p_name = unitOfQty;
            p_startdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            p_enddate = "2099-09-09";
            p_desc = " ";
            if (connection != null) {
                Statement stmt;
                stmt = connection.createStatement();

                String sql = "SELECT   id  FROM  `commodityunit` ORDER BY id DESC LIMIT  0, 1";

                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    p_id = rs.getInt("id") + 1;
                }
                CallableStatement stmt1 = null;
                String sql2 = "{call spcommunit_insert(?," + "?,"
                        + "?," + "?," + "?,?)}";
                stmt1 = connection.prepareCall(sql2);
                stmt1.setInt(1, p_id);
                stmt1.setString(2, p_name);
                stmt1.setString(3, p_startdate);
                stmt1.setString(4, p_enddate);
                stmt1.setString(5, p_desc);
                stmt1.setInt(6, 0);
                iscreated = stmt1.execute();
            }
            connectionPool.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return iscreated;

    }

    /**
     *
     * @param warehouseCode
     * @param warehouseLocation
     * @return
     */
    private int createECSInlocId(String warehouseCode, String warehouseLocation) {
        int Inlocid = 0;
        Statement stmt;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                String dateString = "2099-09-09";
                java.sql.Date date = null;
                try {
                    date = java.sql.Date.valueOf(dateString);
                } catch (Exception e) {
                    //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                }

                String sql = "INSERT INTO `inspectionlocation` ( `SHORT_NAME`, `NAME`, `STARTDATE`, `ENDDATE`, `DESCRIPTION`, `STREETNAME`, `NUMBER`, `POSTAL_CODE`, `TOWN`, `CONTACT_PERSON`, `PHONE`, `FAX`, `MAIL`)"
                        + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt;
                pstmt = connection.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                // pstmt.setString(1,id autogenarated);
                pstmt.setString(1, warehouseCode);
                pstmt.setString(2, warehouseCode + " " + warehouseLocation);
                pstmt.setDate(3, utilclass.getCurrentDate());
                pstmt.setDate(4, date);
                pstmt.setString(5, warehouseCode + " " + warehouseLocation);
                pstmt.setString(6, warehouseCode + " " + warehouseLocation);
                pstmt.setDouble(7, 233344);
                pstmt.setString(8, "00");
                pstmt.setString(9, warehouseLocation);
                pstmt.setString(10, "KESWS ");
                pstmt.setString(11, "0000");
                pstmt.setString(12, "0000");
                pstmt.setString(13, "kesws@kephis.org");
                pstmt.executeUpdate();
                // prest.executeUpdate(query,
                // PreparedStatement.RETURN_GENERATED_KEYS); Throws an error
                // prest.executeQuery(); Throws an error
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Inlocid = rs.getInt(1);
                }
                pstmt.close();
            }

            connectionPool.shutdown();
        } catch (Exception ex) {
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return Inlocid;
    }

    /**
     *
     * @param SourceDoc
     * @param ecsConsDoc
     * @param ecsKeswsEntitiesController
     * @param ClientId
     * @return
     */
    public ECSConsignmentDoc createDatabaseMappings(ConsignmentDocument SourceDoc, ECSConsignmentDoc ecsConsDoc, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController, int ClientId) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int ConsigneeId = ecsConsDoc.getConsigneeID();
        String COD = ecsConsDoc.getCountryofdestination();
        int MOCId = ecsConsDoc.getMeansofconveyance();
        int InLocId = ecsConsDoc.getInspectionlocation();
        if (ConsigneeId != 0) {
            ecsConsDoc.setConsigneeID(createClientConsignee(SourceDoc, ClientId));
        }
        if (!COD.isEmpty()) {
            //Log error of COD
            //ecsKeswsEntitiesController.logError(COD, COD);
        }
        if (MOCId != 0) {
            //Log error of MOCId
        }
        String wareHouseCode = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseCode();
        String wareHouseLocation = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseLocation();
        InLocId = createECSInlocId(wareHouseCode, wareHouseLocation);
        if (InLocId != 0) {
            ecsConsDoc.setInspectionlocation(InLocId);
        }

        return ecsConsDoc;
    }

    /**
     *
     * @param SourceDoc
     * @param ecsConsDoc
     * @param ClientId
     * @return
     */
    public ECSConsignmentDoc getDatabaseMappings(ConsignmentDocument SourceDoc, ECSConsignmentDoc ecsConsDoc, int ClientId) {
        int ConsigneeId = 0;
        String COD = "";
        int MOCId = 0;
        int InLocId = 0;
        ConsigneeId = mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(SourceDoc, ClientId);
        if (ConsigneeId != 0) {
            ecsConsDoc.setConsigneeID(ConsigneeId);
        }
        COD = mapKESWSConsignmentCODtoECSConsignmentCOD(SourceDoc);
        if (!COD.isEmpty()) {
            ecsConsDoc.setCountryofdestination(COD);
        }
        MOCId = mapKESWSConsignmentMOCtoECSClientConsignmentMOC(SourceDoc);
        if (MOCId != 0) {
            ecsConsDoc.setMeansofconveyance(MOCId);
        }
        InLocId = mapKESWSConsignmentILtoECSClientConsignmentIL(SourceDoc);
        if (InLocId != 0) {
            ecsConsDoc.setInspectionlocation(InLocId);//
        }
        return ecsConsDoc;
    }

    public RecCdFileMsg getLatestRecCdFileMsg() {
        return null;
    }

    public int getLastTransactionId() {
        int lastid = 0;
//       //  //  Connection conn = dao.ECSDBConnectorService();
        //      //  Connection conn = dao.ECSDBConnectorService();
        Connection conn = null;
        Statement stmt = null;
        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select MAX(ID)  FROM INTKESWSECSTRANSACTIONS");
            if (rs.next()) {
                lastid = rs.getInt(1);
            }

            return lastid;
        } catch (SQLException ex) {
//            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);

            return lastid;
        }
    }

    public double getTransactionCost() {
        return 0.0;
    }

    /**
     *
     * @param pin Client KRA Pin
     * @return returns the client ECS id for registered clients based on ECS
     * pin/ KESWS PIN
     */
    public int getClientId(String pin) {
        int clientId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                sql = "SELECT `ID` FROM `client` WHERE `PIN` LIKE '%" + pin + "%' AND STATUS='REGISTERED' ";
                ResultSet rs = stmt.executeQuery(sql); // do something with the connection.
                if (rs.next()) {
                    clientId = rs.getInt("ID");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return clientId;
    }

    private String getTypeOfPackageingId(String commodityCategoryId,
            String commodityTypeId, String packageTypeDesc) {
        //  //  Connection conn = dao.ECSDBConnectorService();
        //  Connection conn = dao.ECSDBConnectorService();
        int packageId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                Statement stmt = connection.createStatement();
                String sql;
                // Check using name and client id
                sql = "SELECT `ID`  FROM `typeofpackaging` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                        + commodityTypeId.replaceFirst("^0*", "")
                        + " AND `DESCRIPTION` LIKE '%"
                        + packageTypeDesc
                        + "%' AND `CCA_ID`="
                        + commodityCategoryId.replaceFirst("^0*", "") + ";";
                //System.out.println("Sql cca" + sql.toString());
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    packageId = rs.getInt("ID");
                } else {
                    sql = "SELECT `ID`  FROM `typeofpackaging` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                            + commodityTypeId.replaceFirst("^0*", "")
                            + " AND `CCA_ID`="
                            + commodityCategoryId.replaceFirst("^0*", "") + ";";
                    // System.out.println("Sql cca" + sql.toString());
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        packageId = rs.getInt("ID");
                    }
                    if (createTypeOfPackage(commodityTypeId,
                            commodityCategoryId, packageTypeDesc)) {
                        sql = "SELECT `ID`  FROM `typeofpackaging` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                                + commodityTypeId.replaceFirst("^0*", "")
                                + " AND `CCA_ID`="
                                + commodityCategoryId.replaceFirst("^0*", "") + ";";
                        // System.out.println("Sql cca" + sql.toString());
                        rs = stmt.executeQuery(sql);
                    }
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "" + packageId + "";

    }

    /**
     *
     * @param producerName
     * @param clientId
     * @param productCountry
     * @return
     */
    private int getProducerId(String producerName, int clientId, String productCountry) {

        int producerId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 

            if (connection != null) {
                Statement stmt;

                stmt = connection.createStatement();
                String sql;
                sql = "SELECT `ID`  FROM `producer` WHERE `FIRM_NAME` LIKE '%" + producerName + "%' AND `CNT_ID` LIKE '%" + productCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    producerId = rs.getInt("ID");
                } else {
                    sql = "SELECT `ID`  FROM `producer` WHERE `CNT_ID` LIKE '%" + productCountry + "%' AND `CLT_ID`=" + clientId + ";";
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        producerId = rs.getInt("ID");
                    }
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.  
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        // dao.ecsEntitiesControllerCallerCloseECSDBConnectorService();
        return producerId;
    }

    /**
     *
     * @param consigneeName The name of the consignee
     * @param clientId The client id with the consignee name
     * @param consigneeCountry The country of the consignee
     * @param ConsigneeIdRef The id reference if specifiable
     * @return
     */
    private int getConsigneeId(String consigneeName, int clientId,
            String consigneeCountry, Integer ConsigneeIdRef) {
        int consigneeId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                stmt = connection.createStatement();

                // Check using name and client id
                consigneeName = consigneeName.replaceAll("[^A-Za-z0-9 ]", " ");
                String qString = consigneeName;
                String[] parts = qString.split(" ");
                String firstWord = " ";
                String secondWord = " ";
                String thirdWord = " ";
                String forthWord = " ";
                if (parts.length > 1) {
                    String lastWord = parts[parts.length - 1];
                    //   System.out.println(lastWord); // "sentence"
                }
                if (parts.length == 1) {
                    firstWord = parts[0].trim();
                    //    System.out.println(firstWord);
                }
                if (parts.length > 2) {
                    secondWord = parts[1].trim();
                    //   System.out.println(secondWord);
                }
                if (parts.length > 3) {
                    thirdWord = parts[2].trim();
                    //    System.out.println(thirdWord);
                }
                if (parts.length > 4) {
                    forthWord = parts[3].trim();
                    //   System.out.println(forthWord);
                }
                // "sentence"
                sql = "SELECT `ID`  FROM `consignee` WHERE `FIRM_NAME` LIKE '%"
                        + consigneeName + "%' AND `CNT_ID` LIKE '%"
                        + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    consigneeId = rs.getInt("ID");
                }
                if (consigneeId == 0) {
                    sql = "SELECT `ID`  FROM `consignee` WHERE `FIRM_NAME` LIKE '%"
                            + consigneeName.substring(0, (consigneeName.length() / 2)) + "%' AND `CNT_ID` LIKE '%"
                            + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                    ResultSet rs2 = stmt.executeQuery(sql);
                    if (rs2.next()) {
                        consigneeId = rs2.getInt("ID");
                    }
                }
                if (consigneeId == 0) {
                    sql = "SELECT `ID`  FROM `consignee` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + " " + thirdWord.trim() + " " + forthWord.trim() + "%' AND `CNT_ID` LIKE '%"
                            + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                    ResultSet rs2 = stmt.executeQuery(sql);
                    //        System.out.println(" CONSIGNEE 2 " + sql);
                    if (rs2.next()) {
                        consigneeId = rs2.getInt("ID");
                    }
                }
                if (consigneeId == 0) {
                    sql = "SELECT `ID`  FROM `consignee` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + " " + thirdWord.trim() + "%' AND `CNT_ID` LIKE '%"
                            + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                    ResultSet rs2 = stmt.executeQuery(sql);
                    // System.out.println(" CONSIGNEE 2 " + sql);
                    if (rs2.next()) {
                        consigneeId = rs2.getInt("ID");
                    }
                }
                if (consigneeId == 0) {
                    sql = "SELECT `ID`  FROM `consignee` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + "%' AND `CNT_ID` LIKE '%"
                            + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                    ResultSet rs2 = stmt.executeQuery(sql);
                    //  System.out.println(" CONSIGNEE 2 " + sql);
                    if (rs2.next()) {
                        consigneeId = rs2.getInt("ID");
                    }
                }
            }
            connectionPool.shutdown();

        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        if (ConsigneeIdRef.intValue() == consigneeId) {
            return consigneeId;
        }
        if (ConsigneeIdRef.intValue() != 0) {
            return ConsigneeIdRef;
        }
        if (consigneeId != 0) {
            return consigneeId;
        }

        return consigneeId;
    }

    /**
     *
     * @param MOC Means of covenance
     * @return the means Id i.e Air 1 , Sea 2, Water 3
     */
    private int getMOCId(String MOC) {
        int MeansOfConveyance = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                // Check pin over 11 char
                sql = " SELECT `ID` FROM `meansofconveyance`  WHERE `ENDDATE` > NOW() AND NAME LIKE '%"
                        + MOC + "%' ";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    MeansOfConveyance = rs.getInt("ID");
                }
            }
            connectionPool.shutdown();
        } catch (Exception ex) {
            //     ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return MeansOfConveyance;
    }

    private String getVAQUnitId(String unitOfQty) {
        int VAQUnitId = 0;
        if (unitOfQty.contentEquals("STM")) {
            unitOfQty = "Stems";
        }
        if (unitOfQty.contentEquals("KG")) {
            unitOfQty = "Kgs";
        }
        if (unitOfQty.contentEquals("KGM")) {
            unitOfQty = "Kgs";
        }
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                Statement stmt = connection.createStatement();
                String sql = "SELECT  ID  FROM  commodityunit  WHERE `ENDDATE` > NOW() AND `NAME` LIKE '%"
                        + unitOfQty + "%' ORDER BY id DESC LIMIT  0, 1";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    VAQUnitId = rs.getInt("ID");
                }
            }

            connectionPool.shutdown(); // shutdown connection pool.

        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "" + VAQUnitId + "";
    }

    public String getECSconsignmentInspectionFindings(int ConsId) {
        BoneCP connectionPool = null;
        Connection connection = null;
        String eCSConsignmentInspectionFinding = " ";
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql = "SELECT  `COMMODITYVARIETY`.`NAME`,`QUANTITY_REJECTED`,`IDENTIFIED_PATHEGEN`   FROM  `VARIETYINSPECTIONFINDINGS`,`VARIETYQUANTITY`,COMMODITYVARIETY WHERE `VARIETYQUANTITY`.`CGT_ID`='" + ConsId + "' AND  `VARIETYQUANTITY`.`ID`=`VQA_ID` AND `CVA_ID`=`COMMODITYVARIETY`.`ID`";

                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    eCSConsignmentInspectionFinding += rs.getString("NAME");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.

        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eCSConsignmentInspectionFinding;
    }

    public String getECSconsignmentStatus(String InvoiceNumber, int ConsignmentId) {
        String eCSconsignmentStatus = "HOLD";
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql = "SELECT  STATUS   FROM    `CONSIGNEMENT` WHERE ID='" + ConsignmentId + "' ORDER BY id DESC LIMIT  0, 1";
                ResultSet rs = stmt.executeQuery(sql);
                System.out.println("" + sql);
                if (rs.next()) {
                    System.err.println(" STATUS " + rs.getString("STATUS"));
                    eCSconsignmentStatus = rs.getString("STATUS");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool. 
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eCSconsignmentStatus;
    }

    public String getECSFinalconsignmentInspectionResult(int ConsignmentId) {
        String eCSconsignmentStatus = "";
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql = "SELECT  FINAL_RESULT,REASON_OF_REJECTION  FROM    `CONSIGNEMENTINSPECTIONRESULT` WHERE CGT_ID='" + ConsignmentId + "' ORDER BY id DESC LIMIT  0, 1";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    eCSconsignmentStatus += rs.getString("FINAL_RESULT");
                    //eCSconsignmentStatus += rs.getString("REASON_OF_REJECTION");
                }

            }
            connectionPool.shutdown(); // shutdown connection pool. 
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eCSconsignmentStatus;
    }

    /**
     * public String getECSconsignmentStatus(String InvoiceNumber, int
     * ConsignmentId) { String eCSconsignmentStatus = "HOLD"; BoneCP
     * connectionPool = null; Connection connection = null; try { // load the
     * database driver (make sure this is in your classpath!)
     * Class.forName(JDBC_DRIVER); // setup the connection pool BoneCPConfig
     * config = new BoneCPConfig(); config.setJdbcUrl(DB_URL); // jdbc url
     * specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
     * config.setUsername(USER); config.setPassword(PASS);
     * config.setMinConnectionsPerPartition(5);
     * config.setMaxConnectionsPerPartition(10); config.setPartitionCount(1);
     * connectionPool = new BoneCP(config); // setup the connection pool
     * connection = connectionPool.getConnection(); // fetch a connection if
     * (connection != null) { Statement stmt = connection.createStatement();
     * String sql = "SELECT STATUS FROM `CONSIGNEMENT` WHERE ID='" +
     * ConsignmentId + "' ORDER BY id DESC LIMIT 0, 1"; ResultSet rs =
     * stmt.executeQuery(sql); System.out.println(""+sql); if (rs.next()) {
     * System.err.println(" STATUS " + rs.getString("STATUS"));
     * eCSconsignmentStatus = rs.getString("STATUS"); } }
     * connectionPool.shutdown(); // shutdown connection pool. } catch
     * (Exception ex) { // ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
     * Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE,
     * null, ex); } finally { if (connection != null) { try {
     * connection.close(); } catch (SQLException e) { e.printStackTrace(); } } }
     * return eCSconsignmentStatus; }
*
     */
    public String getECSConsignmentsForBilling(int ConsignmentId) {
        String eCSconsignmentStatus = "";
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql = "SELECT  FINAL_RESULT,REASON_OF_REJECTION  FROM    `CONSIGNEMENTINSPECTIONRESULT` WHERE CGT_ID='" + ConsignmentId + "' ORDER BY id DESC LIMIT  0, 1";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    eCSconsignmentStatus += rs.getString("FINAL_RESULT");
                    //eCSconsignmentStatus += rs.getString("REASON_OF_REJECTION");
                }

            }
            connectionPool.shutdown(); // shutdown connection pool. 
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eCSconsignmentStatus;
    }

    public String getECSCertificateDetails(String InvoiceNumber, int eCSconsignmentId) {

        String eCSconsignmentDocuments = "";
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();

                String sql = "SELECT  DOCUMENT_NR   FROM   `certificate` WHERE CGT_ID=" + eCSconsignmentId + " ORDER BY id DESC LIMIT  0, 1";

                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    eCSconsignmentDocuments = rs.getString("DOCUMENT_NR");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool. 
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return eCSconsignmentDocuments;
    }

    /**
     * This method maps id to warehouse locations
     *
     * @param warehouseCode
     * @param warehouseLocation
     * @return The Inspection Location Id
     */
    private int getECSInlocId(String warehouseCode, String warehouseLocation) {

        int inLocId = 0;

        //KUEHNE+NAGEL LIMITED
        if (warehouseCode.contains("BNBI493") || warehouseLocation.contains("BNBI493")) {
            inLocId = 1;
        } //Airflo Ltd
        else if (warehouseCode.contains("AIRFLOL") || warehouseLocation.contains("AIRFLOL")) {
            inLocId = 2;
        } //Skytrain Ltd
        else if (warehouseCode.contains("SKYTRAI") || warehouseLocation.contains("SKYTRAI")) {
            inLocId = 3;
        } //Sunripe (1976) Ltd
        else if (warehouseCode.contains("SUNRIPE") || warehouseLocation.contains("SUNRIPE")) {
            inLocId = 4;
        } //Total Touch Cargo Ltd
        else if (warehouseCode.contains("TTCCARL") || warehouseLocation.contains("TTCCARL")) {
            inLocId = 5;
        } //Swissport Cargo Services Ltd
        else if (warehouseCode.contains("SWISSPO") || warehouseLocation.contains("SWISSPO")) {
            inLocId = 6;
        } //Air Connection Ltd
        else if (warehouseCode.contains("Air Connection") || warehouseLocation.contains("Air Connection")) {
            inLocId = 7;
        } //Flowerwings (K) Ltd
        else if (warehouseCode.contains("FLOWING") || warehouseLocation.contains("FLOWING")) {
            inLocId = 8;
        } //Schenker Kenya Ltd
        else if (warehouseCode.contains("SCHENKE") || warehouseLocation.contains("SCHENKE")) {
            inLocId = 9;
        } //General Freighters Ltd
        else if (warehouseCode.contains("GENFREI") || warehouseLocation.contains("GENFREI")) {
            inLocId = 10;
        } //Greenlands Agroproducers (EPZ) Ltd
        else if (warehouseCode.contains("GREENLA") || warehouseLocation.contains("GREENLA")) {
            inLocId = 12;
        } //Makindu Growers and Packers Ltd
        else if (warehouseCode.contains("MAKINDU") || warehouseLocation.contains("MAKINDU")) {
            inLocId = 13;
        } //East Africa Growers Kenya Ltd
        else if (warehouseCode.contains("EAGAAFR") || warehouseLocation.contains("EAGAAFR")) {
            inLocId = 14;
        } //Wilham (K) Ltd
        else if (warehouseCode.contains("WILHAM") || warehouseLocation.contains("WILHAM")) {
            inLocId = 15;
        } //Vegpro(K) Ltd
        else if (warehouseCode.contains("VEGPROK") || warehouseLocation.contains("VEGPROK")) {
            inLocId = 16;
        } //African Cargo Handling Ltd
        else if (warehouseCode.contains("TJKA001") || warehouseLocation.contains("TJKA001")) {
            inLocId = 17;
        } //Kenya Airfreight Handling Ltd
        else if (warehouseCode.contains("KAHL") || warehouseLocation.contains("KAHL")) {
            inLocId = 18;
        } //Transglobal Cargo Centre Ltd
        else if (warehouseCode.contains("TRANSG") || warehouseLocation.contains("TRANSG")) {
            inLocId = 19;
        } //Everest enterprises Ltd
        else if (warehouseCode.contains("EVEREST") || warehouseLocation.contains("EVEREST")) {
            inLocId = 20;
        } //KAKUZI
        else if (warehouseCode.contains("KAKUZI") || warehouseLocation.contains("KAKUZI")) {
            inLocId = 21;
        } //KEPHIS Naivasha
        else if (warehouseCode.contains("NAIVASHA") || warehouseLocation.contains("NAIVASHA")) {
            inLocId = 22;
        } //C. DORMAN
        else if (warehouseCode.contains("DORMAN") || warehouseLocation.contains("DORMAN")) {
            inLocId = 23;
        } //AFRICOFF TRADING CO LTD
        else if (warehouseCode.contains("AFRICOFF") || warehouseLocation.contains("AFRICOFF")) {
            inLocId = 24;
        } //TAYLORWINCH COFFEE LTD
        else if (warehouseCode.contains("TAYLORW") || warehouseLocation.contains("TAYLORW")) {
            inLocId = 25;
        } //*************************************SDV NAIROBI DOES NOT EXIST IN THE***************************************************************
        else if (warehouseCode.contains("SDV NAIROBI") || warehouseLocation.contains("SDV NAIROBI")) {
            inLocId = 26;
        } //Plant Quarantine and Biosafety Station
        else if (warehouseCode.contains("PQBSMUG") || warehouseLocation.contains("PQBSMUG")) {
            inLocId = 27;
        } //KEPHIS MOMBASA********************************KEPIS MOMBASA DOES NOT EXIST IN THE LIST******************************************************CHECK THIS
        else if (warehouseCode.contains("KEPHIS MOMBASA") || warehouseLocation.contains("KEPHIS MOMBASA")) {
            inLocId = 28;
        } //KEPHIS Nakuru
        else if (warehouseCode.contains("NAKURU") || warehouseLocation.contains("NAKURU")) {
            inLocId = 29;
        } //Equatorial Nut Processors
        else if (warehouseCode.contains("EQUATOR") || warehouseLocation.contains("EQUATOR")) {
            inLocId = 30;
        } //DIAMOND COFFEE CO LTD
        else if (warehouseCode.contains("DIAMOND") || warehouseLocation.contains("DIAMOND")) {
            inLocId = 31;
        } //KEPHIS Headquarters
        else if (warehouseCode.contains("KEPHISHQ") || warehouseLocation.contains("KEPHISHQ")) {
            inLocId = 32;
        } //Kenya Nut Company
        else if (warehouseCode.contains("KNC") || warehouseLocation.contains("KNC")) {
            inLocId = 33;
        } //KEPHIS Kitale
        else if (warehouseCode.contains("KITALE") || warehouseLocation.contains("KITALE")) {
            inLocId = 34;
        } //Eldoret	Eldoret Airport ( KEPHIS)
        else if (warehouseCode.contains("ELDORET") || warehouseLocation.contains("ELDORET")) {
            inLocId = 35;
        } //KEPHIS JKIA
        else if (warehouseCode.contains("PIUJKIA") || warehouseLocation.contains("PIUJKIA")) {
            inLocId = 36;
        } //SDV Mombasa	SDV Transami Mombasa********************************************
        else if (warehouseCode.contains("SDV MOMBASA")) {
            inLocId = 37;
        }
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {

                Statement stmt = connection.createStatement();
                String sql;
                // Check using name and client id
                sql = "SELECT `ID`  FROM `inspectionlocation` WHERE `NAME` LIKE '%"
                        + warehouseCode + "%' OR `TOWN` LIKE '%"
                        + warehouseLocation + "%';";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    inLocId = rs.getInt("ID");
                }
            }
            connectionPool.shutdown();

        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return inLocId;

    }

    public int getLastProducerId() {

        int producerId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                // Check using name and client id
                sql = "SELECT COUNT(*) as ID  FROM `PRODUCER`";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    producerId = rs.getInt("ID");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool. 
        } catch (Exception ex) {
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return producerId + 1;
    }

    /**
     *
     * @param COD Country of destination
     * @return
     */
    private String getECSCOD(String COD) {

        String CODISOCODE = null;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                sql = "SELECT `ISOCODE` FROM `country` WHERE `ENDDATE` > NOW() AND `ISOCODE` LIKE '%"
                        + COD + "%';";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    CODISOCODE = rs.getString("ISOCODE");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            //  ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return CODISOCODE;
    }

    /**
     *
     * @param SourceDoc
     * @return
     */
    // map client pin to client id
    public int mapKESWSClientPintoECSClientId(ConsignmentDocument SourceDoc) {
        String pin;
        int clientid;
        pin = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getTIN();
        clientid = getClientId(pin);
        if (clientid == 0) {
            createClientDetails(SourceDoc);
        }
        clientid = getClientId(pin);
        return clientid;
    }

    /**
     *
     * @param SourceDoc
     * @return
     */
    // map client application date and time
    private int mapKESWSConsignmentMOCtoECSClientConsignmentMOC(ConsignmentDocument SourceDoc) {
        String MOC = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getModeOfTransportDesc();
        int MOCId = getMOCId(MOC);
        return MOCId;
    }

    private String mapKESWSConsignmentCODtoECSConsignmentCOD(ConsignmentDocument SourceDoc) {
        String COD;
        COD = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPhyCountry();
        return getECSCOD(COD);
    }

    // map client id to client id
    private int mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(ConsignmentDocument SourceDoc, int ClientId) {
        int consigneeId = 0;
        String consigneeName;
        Integer CongineeIdRef;
        CongineeIdRef = 0;
        consigneeName = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getName();
        String consigneeCountry = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPosCountry();
        try {
            CongineeIdRef = CongineeIdRef.parseInt(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getMDARefNo());
        } catch (Exception e) {
            CongineeIdRef = 0;
        }
        consigneeId = getConsigneeId(consigneeName, ClientId, consigneeCountry, CongineeIdRef);
        if (consigneeId == 0) {
            createClientConsignee(SourceDoc, ClientId);
        }
        consigneeId = getConsigneeId(consigneeName, ClientId, consigneeCountry, CongineeIdRef);
        return consigneeId;
    }

    /**
     *
     * @param SourceDoc
     * @return
     */
    private int mapKESWSConsignmentILtoECSClientConsignmentIL(ConsignmentDocument SourceDoc) {
        int inspectionLocationId = 0;

        inspectionLocationId = getECSInlocId(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseCode(), SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseLocation());

        return inspectionLocationId;
    }

    private int getCreatedECSconsignmentId(String InvoiceNo) {
        int createdECSconsignmentId = 0;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql = "SELECT   id  FROM    `consignement` where "
                        + "`INVOICE_NR` LIKE '%" + InvoiceNo + "%' ORDER BY id DESC  LIMIT  0, 1";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    createdECSconsignmentId = rs.getInt("id");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return createdECSconsignmentId;
    }

    public List<Integer> getSubmittedConsignementIds() {
        
        List<Integer> consignmentIds = new ArrayList<Integer>() {
        };
        BoneCP connectionPool = null;
        Connection connection = null;
        try {

            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            //config.setJdbcUrl("jdbc:mysql://192.168.0.9:3306/ECS_KESWS"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setJdbcUrl(DB_URL_ECS_KESWS);
            config.setUsername(USER_ECS_KESWS);
            config.setPassword(PASS_ECS_KESWS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                String simpleProc = "{ call GET_SUBMITTED_CONDOC(?) }";
                CallableStatement cs = connection.prepareCall(simpleProc);
                cs.setString(1, "SUBMITTED");
                cs.execute();
                ResultSet rs1 = cs.getResultSet();

                while (rs1.next()) {
                    consignmentIds.add((Integer) rs1.getInt("consignment_id"));
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            //  ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return consignmentIds;
    }

    /**
     *
     * @param ECSconsignmentId
     * @param ClientId
     * @param Amount
     * @param AccpacInvoiceNo
     * @param AccpacInvoiceRefNo
     * @param ecsKeswsEntitiesController
     * @param inseertupdateflag if set to 1 create invoice otherwise update
     * @param Flow "ECS-KESWS","KESWS-ECS" if ecs kews call to update invoice
     * cost FINANCE to ECS after billing update actual invoice No
     *
     * @return
     */
    public boolean InvoiceConsignmentonECS(int ECSconsignmentId, String ClientId, float Amount, String AccpacInvoiceNo, String AccpacInvoiceRefNo, String InvoiceDescription, String ServiceType, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController, int inseertupdateflag, String Flow) {

        boolean iscreated = false;
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            
          // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            
            if (connection != null) {

                int p_flag = inseertupdateflag;
                int p_emp_id = getECSEmpId(ECSconsignmentId);
                String p_changed_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String p_clt_id = ClientId;
                int p_iap_id = getECSEmpId(ECSconsignmentId);
                String p_iap_date = getECSIAPDate(ECSconsignmentId);
                String p_insp_date = getECSIinspDate(ECSconsignmentId);
                float p_amount_payable = Amount;
                int p_cgt_id = ECSconsignmentId;
                String p_accpac_inv_id = AccpacInvoiceNo;
                String p_clk_inv_id = AccpacInvoiceRefNo;

                /* Initialize variables * */
                Statement stmt;

                stmt = connection.createStatement();

                String sql2 = "call ecsfortesting.sprecordinspres_insertupdateinvoicedetails_middleware("
                        + " ?,"
                        + " ?,"
                        + " ?,"
                        + " ?, "
                        + " ?, "
                        + "?,"
                        + " ?,"
                        + "?, "
                        + "?,"
                        + "?,"
                        + "?, "
                        + "?,"
                        + "?,"
                        + "?"
                        + ")";

                CallableStatement stmt1 = null;
                stmt1 = connection.prepareCall(sql2);
                stmt1.setInt(1, p_flag);
                System.out.println(p_flag);
                stmt1.setInt(2, p_emp_id);
                System.out.println(p_emp_id);
                stmt1.setString(3, p_changed_date);
                System.out.println(p_changed_date);
                stmt1.setString(4, p_clt_id);
                System.out.println(p_clt_id);
                stmt1.setInt(5, p_iap_id);
                System.out.println(p_iap_id);
                stmt1.setString(6, p_iap_date);
                System.out.println(p_iap_date);
                stmt1.setString(7, p_insp_date);
                System.out.println(p_insp_date);
                stmt1.setFloat(8, p_amount_payable);
                System.out.println(p_amount_payable);
                stmt1.setInt(9, p_cgt_id);
                System.out.println(p_cgt_id);
                stmt1.setString(10, p_accpac_inv_id.trim());
                System.out.println(p_accpac_inv_id.trim());
                stmt1.setString(11, p_clk_inv_id.trim());
                System.out.println(p_clk_inv_id);
                stmt1.setString(12, ServiceType.trim());
                System.out.println(ServiceType);
                if (InvoiceDescription.length() > 450) {
                    stmt1.setString(13, InvoiceDescription.trim().substring(0, 450));
                } else {
                    stmt1.setString(13, InvoiceDescription.trim());
                }
                stmt1.setString(14, p_changed_date);

                System.out.println("[call ecsfortesting.sprecordinspres_insertupdateinvoicedetails_middleware("
                        + " ?,"
                        + " ?,"
                        + " ?,"
                        + " ?, "
                        + " ?, "
                        + "?,"
                        + " ?,"
                        + "?, "
                        + "?,"
                        + "?,"
                        + "?, "
                        + "?,"
                        + "?,"
                        + p_changed_date
                        + ");]");
                iscreated = stmt1.execute();

            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            connectionPool.shutdown(); // shutdown connection pool.
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return iscreated;
    }

    public boolean InvoiceConsignmentonAccpac(int CreatedECSconsignmentId, String ClientPin, float Amount, String AccpacInvoiceNo, String AccpacInvoiceRefNo, String InvoiceDescription, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController) {

        String custAccpacId = getClientACCPACId(ClientPin);
        boolean iscreated = false;
        Connection connection = null;

        // load the database driver (make sure this is in your classpath!)
             try {
       Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
    }
       catch (Exception ex){
       System.out.print("Check classpath. Cannot load db driver");
    }
        
        try {
            ResultSet rs = null;
            ResultSet temp = null;
            ResultSet temp2 = null;
            ResultSet temp1 = null;

            Statement stmt = null;
            connection = DriverManager.getConnection(FIN_DB_URL, FIN_USER, FIN_PASS);
            stmt = connection.createStatement();
            if (connection != null) {
                //  SELECT COUNT(*) FROM  [dbo].[ECS_INVOICE_DETAILS]
                // int INV_ID = CreatedECSconsignmentId;  
                String INV_DESCRIPTION = InvoiceDescription;
                float INV_AMOUNT = Amount;
                String INV_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String TRANSACTION_TYPE = "";
                int CUR_CODE = 1;
                int STATUS = 0;
                String BATCHNUMBER = "";
                String ENTRYNUMBER = "";
                int Ispaid = 0;

                String USER_ID = "";
                String RECEIPT_NUMBER = "";
                String CUST_PIN = ClientPin;
                String ECS_ID_REF = "" + CreatedECSconsignmentId;
                String ECS_STATUS_FLAG = "1";
                String REFERENCE_NUMBER = "ECS " + CreatedECSconsignmentId;

                if (stmt.execute("SET ANSI_WARNINGS  OFF;"
                        + "INSERT INTO dbo.ECS_INVOICE_DETAILS ("
                        //+ "INV_ID"
                        + " CUST_NUMBER"
                        + ",REFERENCE_NUMBER"
                        + ",INV_DESCRIPTION"
                        + ",INV_AMOUNT"
                        + ",INV_DATE"
                        + ",TRANSACTION_TYPE"
                        + ",CUR_CODE"
                        + ",STATUS"
                        + ",BATCHNUMBER"
                        + ",ENTRYNUMBER"
                        + ",Ispaid"
                        + ",USER_ID"
                        + ",RECEIPT_NUMBER"
                        + ",CUST_PIN"
                        + ",ECS_ID_REF"
                        + ",ECS_STATUS_FLAG"
                        + ") VALUES  ("
                        //     + "" + INV_ID + ","
                        + "'" + custAccpacId + "',"
                        + "'" + REFERENCE_NUMBER + "',"
                        + "'" + INV_DESCRIPTION + "',"
                        + "" + INV_AMOUNT + ","
                        + "'" + INV_DATE + "',"
                        + "'" + TRANSACTION_TYPE + "',"
                        + "'" + CUR_CODE + "',"
                        + "'" + STATUS + "',"
                        + "'" + BATCHNUMBER + "',"
                        + "'" + ENTRYNUMBER + "',"
                        + "" + Ispaid + ","
                        + "'" + USER_ID + "',"
                        + "'" + RECEIPT_NUMBER + "',"
                        + "'" + CUST_PIN + "',"
                        + "'" + ECS_ID_REF + "',"
                        + "'" + ECS_STATUS_FLAG + "');"
                        + "SET ANSI_WARNINGS ON;")) {
                    System.out.println("INSERTED TRANSACTION : AMOUNT = " + INV_AMOUNT);
                    // Return  False  Log transaction error
                    iscreated = true;
                } else {
                    //Return Accpac invoice Refrence 
                    iscreated = false;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            //Log error 
//            ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return iscreated;
    }

    private boolean createConsignmentInvoice(int CreatedECSconsignmentId, int ClientId, EcsKeswsEntitiesControllerCaller ecsKeswsEntitiesController) {
        boolean iscreated = false;
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return iscreated;
    }

    private int getECSEmpId(int ECSconsignmentId) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return 1;
    }

    private String getECSIAPDate(int ECSconsignmentId) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getECSIinspDate(int ECSconsignmentId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public boolean CreateClientinACCPAC(String ClientPin, String ClientName) {

        String custNu = getLikelyClientACCPACId(ClientPin, ClientName);

        boolean iscreated = false;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        // Query details from accpac database for customer number
        // K@ph1$67q
        // load the database driver (make sure this is in your classpath!)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        } catch (Exception ex) {
            System.out.print("Check classpath. Cannot load db driver");
        }
        try {
            //System.out.println("-" + FIN_DB_URL + "-" + FIN_USER + "-" + FIN_PASS);
            connection = DriverManager.getConnection(FIN_DB_URL, FIN_USER, FIN_PASS);

            if (connection != null) {
                stmt = connection.createStatement();
                //increment to get  INDCUST2 unique  number  from ARCUS table
                rs = stmt.executeQuery("SELECT CUST_PIN FROM  ECS_CUSTOMER_INFO WHERE CUST_PIN LIKE '%" + ClientPin + "%'");
                if (rs.next()) {
                    iscreated = true;

                } else {
                    stmt = connection.createStatement();
                    stmt.execute("SET ANSI_WARNINGS  OFF;"
                            + " INSERT INTO  dbo.ECS_CUSTOMER_INFO("
                            + "           CUST_NAME,CUST_NUMBER"
                            + "           ,CUST_PIN"
                            + ") VALUES  ("
                            + "'" + ClientName + "',"
                            + "'" + custNu + "',"
                            + "'" + ClientPin + "'"
                            + " );"
                            + "SET ANSI_WARNINGS  ON;");
                    iscreated = CreateClientinACCPAC(ClientPin, ClientName);
                }
                return iscreated;
            }
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return iscreated;
    }

    String getClientACCPACId(String ClientPin) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        String CUST_NUMBER = "";

        // load the database driver (make sure this is in your classpath!)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        } catch (Exception ex) {
            System.out.print("Check classpath. Cannot load db driver");
        }
        try {
            connection = DriverManager.getConnection(FIN_DB_URL, FIN_USER, FIN_PASS);

            if (connection != null) {
                stmt = connection.createStatement();
                //increment to get  INDCUST2 unique  number  from ARCUS table
                rs = stmt.executeQuery("SELECT CUST_NUMBER FROM  ECS_CUSTOMER_INFO WHERE CUST_PIN LIKE '%" + ClientPin + "%'");
                while (rs.next()) {
                    CUST_NUMBER = rs.getString("CUST_NUMBER");

                }
            }//Call CustomerDetails DOA and create customer details
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            return "";

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return CUST_NUMBER;
    }

    public String getLikelyClientACCPACId(String ClientPin, String ClientName) {
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection connection = null;
        String INDCUST2 = "";
        // load the database driver (make sure this is in your classpath!)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        } catch (Exception ex) {
            System.out.print("Check classpath. Cannot load db driver");
        }
        try {

            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver()); 
            connection = DriverManager.getConnection("jdbc:sqlserver://192.168.0.3;databaseName=KEPHIS60", "sa", "K@ph1$67q");

            if (connection != null) {
                stmt = connection.createStatement();
                //increment to get  INDCUST2 unique  number  from ARCUS table 
                Integer count = 1;
                Integer count2 = 0;
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
                Calendar cal = Calendar.getInstance();
                //increment to get  INDCUST2 unique  number  from ARCUS table
                rs = stmt.executeQuery("SELECT IDCUST FROM  ARCUS WHERE IDCUST LIKE '%" + ClientName.substring(0, 1) + "%'");
                while (rs.next()) {
                    count = count + 1;

                    INDCUST2 = "" + ClientName.substring(0, 1) + count + "";
                   // System.out.println(count + "  " + ClientName + "      " + INDCUST2);
                }
                //increment to get  INDCUST2 unique  number  from INTCUSTDT table
                rs2 = stmt.executeQuery("SELECT IDCUST FROM  INTCUSTDT WHERE IDCUST ='" + INDCUST2 + "'");
                while (rs2.next()) {
                    count = count + 1;
                    System.out.println(count);

                   // System.out.println(INDCUST2);

                }
                if (count < 10) {
                    INDCUST2 = "" + ClientName.substring(0, 1) + "00" + count + "";
                } else if (count < 100) {
                    INDCUST2 = "" + ClientName.substring(0, 1) + "0" + count + "";
                } else {
                    INDCUST2 = "" + ClientName.substring(0, 1) + count + "";
                }
                
//                ResultSet result = stmt.executeQuery("SELECT NAMECUST FROM ARCUS WHERE NAMECUST  LIKE '%" + ClientName + "%'");
//                
//                if(result.next()){
//                    System.out.println(result.getString(0));
//                }
                
                if (stmt.execute("SELECT NAMECUST FROM ARCUS WHERE NAMECUST  LIKE '%" + ClientName + "%'")) {
                    System.out.println("Client " + ClientName + " " + "with client ID" + INDCUST2 + " the customer is not present");
                }

                if (stmt.execute("SELECT CUSTOMERNAME FROM  INTCUSTDT WHERE CUSTOMERNAME  = '" + ClientName + "'")) {

                    System.out.println("Client " + ClientName + " " + "with client ID" + INDCUST2 + " the customer is not present");
                } else {
                    rs2 = stmt.executeQuery("SELECT IDCUST FROM  INTCUSTDT WHERE CUSTOMERNAME  = '" + ClientName + "'");
                    while (rs2.next()) {
                        INDCUST2 = rs2.getString("IDCUST");
                    }
                    System.out.println("Client " + ClientName + " " + "with client ID" + INDCUST2 + "can't be created since the customer is present");

                }//Call CustomerDetails DOA and create customer details
            }
        } catch (Exception ex) {
            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
            return "";

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return INDCUST2;
    }

    public String getClientCustomerId(String ClientPin) {
        String clientId = "";
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName(JDBC_DRIVER);
            // setup the connection pool
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DB_URL); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config); // setup the connection pool 
            connection = connectionPool.getConnection(); // fetch a connection 
            if (connection != null) {
                Statement stmt = connection.createStatement();
                String sql;
                sql = "SELECT `CLT_ID` FROM `client` WHERE `PIN` LIKE '%" + ClientPin + "%' AND STATUS='REGISTERED' ";
                ResultSet rs = stmt.executeQuery(sql); // do something with the connection.
                if (rs.next()) {
                    clientId = rs.getString("CLT_ID");
                }
            }
            connectionPool.shutdown(); // shutdown connection pool.
        } catch (Exception ex) {

            Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcsEntitiesControllerCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return clientId;
    }

    /**
     * Uses DriverManager.
     */
    public Connection getSimpleConnection() {
        //See your driver documentation for the proper format of this string :
        String DB_CONN_STRING = "jdbc:sqlserver://172.16.158.129\\MSSQLSERVER:1433;databaseName=INTERGRATION";
        //Provided by your driver documentation. In this case, a MySql driver is used :
        String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String USER_NAME = "ecsaccpac";
        String PASSWORD = "ecsaccpac";

        Connection result = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        } catch (Exception ex) {
            System.out.print("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
        }

        try {
            result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            System.out.print("Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
        }
        return result;
    }
}
