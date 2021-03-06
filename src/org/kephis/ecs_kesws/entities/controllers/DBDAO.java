package org.kephis.ecs_kesws.entities.controllers;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.kephis.ecs_kesws.entities.CdFileDetails;
import org.kephis.ecs_kesws.entities.EcsResCdFileMsg;
//import org.kephis.ecs_kesws.entities.CustomPersistence;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.LogTypes;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.PaymentInfoLog;
import org.kephis.ecs_kesws.entities.Pricelist;
import org.kephis.ecs_kesws.entities.PricelistInternalProductcodeDocumentMap;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.RecErrorFileMsg;
import org.kephis.ecs_kesws.entities.ResCdFileMsg;
import org.kephis.ecs_kesws.entities.TransactionLogs;
import org.kephis.ecs_kesws.entities.controllers.exceptions.IllegalOrphanException;
import org.kephis.ecs_kesws.entities.controllers.exceptions.NonexistentEntityException;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.XmlFileMapper;
import org.kephis.ecs_kesws.xml.parser.ECSConsignmentDoc;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails;

public class DBDAO {

    static UtilityClass utilclass;
    // DBConnector dao;

    static EntityManagerFactory emf;
    static EntityManager em;

    public DBDAO() {

        utilclass = new UtilityClass();
        Properties p = new Properties(System.getProperties());
        p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF"); // or any other
        System.setProperties(p);
        org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.WARNING); //or whatever level you need
        emf = Persistence.createEntityManagerFactory("ECS-KESWS-MiddlewarePU");
        em = emf.createEntityManager();

    }

    public int createECSconsignmentdetails(ECSConsignmentDoc desObject,
            int clientId, RecCdFileMsg recCdFileMsg) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        CallableStatement stmt;
        int ConsignmentId = 0;

        try {
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

            // TO DO REMOVE Validate desObjectDetails AND MOVE TO XML FILE MAPPER
            if (desObject.getPreferredinspectiontime().length() >= 12 && desObject.getTimeofdeparture().length() >= 12) {

                p_prefinsptime = desObject.getPreferredinspectiontime().substring(
                        8, 10)
                        + ":"
                        + desObject.getPreferredinspectiontime().substring(10, 12);
                p_departuretime = desObject.getTimeofdeparture().substring(8, 10)
                        + ":" + desObject.getTimeofdeparture().substring(10, 12);
                //TO DO Time of inpection check
            } else if (desObject.getPreferredinspectiontime().length() <= 12) {
                p_prefinsptime = new SimpleDateFormat("HH:mm").format(new java.util.Date());
                //TO DO Time of inpection set default time manually and log error 
                logPreferredInspectionTimeNotSet("PREFERRED INSPECTION TIME AUTO SET TO [" + p_prefinsptime + "] FOR INVOICE NO [" + desObject.getInvoicenr() + "]", recCdFileMsg.getRECCDFileID());
            } else if (desObject.getTimeofdeparture().length() <= 12) {
                p_departuretime = new SimpleDateFormat("HH:mm").format(new java.util.Date());
                //TO DO Time of inpection set default time manually and log error  
                logTimeofdepartureTimeNotSet("DEPATURE TIME AUTO SET TO [" + p_departuretime + "] FOR INVOICE NO [" + desObject.getInvoicenr() + "]", recCdFileMsg.getRECCDFileID());
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
            stmt = conn.prepareCall(sql);
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

            System.out.println("Stored procedure" + sql);
            try {
                stmt.executeUpdate();
                ConsignmentId = getCreatedECSconsignmentId();
                recCdFileMsg.setECSCONSIGNEMENTIDRef(ConsignmentId);
                recCdFileMsg.setInvoiceNO(p_invoice_nr);
                updateRecCDFileMsg(recCdFileMsg);
                logSavedConsignementDetails("SAVED CONSIGNMENTID [" + ConsignmentId + "] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " INVOICE NO [" + desObject.getInvoicenr() + "]", recCdFileMsg.getRECCDFileID());

            } catch (Exception e) {

                e.printStackTrace();
                //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                logErrorSavingConsignementDetails("ERROR SAVING CONSIGNMENT INVOICE NO [" + desObject.getInvoicenr() + " ] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " ERROR DETAILS [" + e.toString() + "]", recCdFileMsg.getRECCDFileID());
            }
            try {
                p_status = "SUBMITTED";
                String p_statusdate = p_status_date;
                String p_app_date = p_appdate;
                String p_app_time = p_apptime;
                sql = "{call spclient_updateconsignementstatus(?,?,?,?,?)}";
                stmt = conn.prepareCall(sql);
                stmt.setString(1, p_status);
                stmt.setString(2, p_statusdate);
                stmt.setInt(3, ConsignmentId);
                stmt.setString(4, p_app_date);
                stmt.setString(5, p_app_time);
                System.out.println("Stored procedure spclient_updateconsignementstatus" + stmt.toString());
                stmt.executeUpdate();
                logSubmittedConsignementDetails("SUBMITTED CONSIGNMENTID " + getECSconsignmentId(desObject.getInvoicenr()) + "AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " INVOICE NO [" + desObject.getInvoicenr() + "]", recCdFileMsg.getRECCDFileID());

            } catch (Exception e) {
                e.printStackTrace();
                //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                logErrorSubmittingConsignementDetails("ERROR SUBMITTING CONSIGNMENT ID [" + ConsignmentId + "]INVOICE NO [" + desObject.getInvoicenr() + " ] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " ERROR DETAILS [" + e.toString() + "]", recCdFileMsg.getRECCDFileID());

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            logErrorCreatingConsignementDetails("ERROR CREATING CONSIGNMENTID [" + ConsignmentId + "] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " INVOICE NO [" + desObject.getInvoicenr() + "]" + " ERROR DETAILS [" + e.toString() + "]", recCdFileMsg.getRECCDFileID());

        }
        return ConsignmentId;
    }

    public double getTransactionCost() {
        return 0.0;
    }

    public int getClientId(String pin) {
        // //DBConnector dao = new DBConnector();
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        int clientId = 0;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
            // Check pin over 11 char
            sql = "SELECT `ID` FROM `CLIENT` WHERE `PIN` LIKE '%" + pin + "%' AND STATUS='REGISTERED' ";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                clientId = rs.getInt("ID");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }

        return clientId;
    }

    public void createClientConsignee(ConsignmentDocument SourceDoc) {

        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
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
        p_clt_id = "" + mapKESWSClientPintoECSClientId(SourceDoc) + "";

        try {
            CallableStatement stmt1 = null;
            String sql2 = "{call spclient_insertupdateconsignee(?," + "?,"
                    + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                    + "?"
                    + ")}";
            stmt1 = conn.prepareCall(sql2);
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
            System.out.println("CREATE CONSIGNEE" + stmt1.toString() + ":" + p_flag + ":" + p_csg_id + ":" + p_firmname + ":" + p_streetname1 + ":" + p_streetname2 + ":"
                    + p_postalcode + ":" + p_town + ":" + p_cnt_id + ":" + p_number + ":" + p_shortname + ":" + p_clt_id);
            stmt1.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
    }

    public void createClientDetails(ConsignmentDocument SourceDoc) {
        DBConnector dao = new DBConnector();

        Connection conn = dao.ECSDBConnectorService();

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

        try {
            CallableStatement stmt1 = null;
            String sql2 = "{call spclient_insert_kesws(?," + "?," + "?," + "?,"
                    + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                    + "?," + "?," + "?," + "?," + "?" + ")}";
            stmt1 = conn.prepareCall(sql2);

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

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();

            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }

    }

    public int getConsigneeId(String consigneeName, int clientId,
            String consigneeCountry, Integer ConsigneeIdRef) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();

        int consigneeId = 0;

        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
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
                System.out.println(lastWord); // "sentence"
            }
            if (parts.length == 1) {
                firstWord = parts[0].trim();
                System.out.println(firstWord);
            }
            if (parts.length > 2) {
                secondWord = parts[1].trim();
                System.out.println(secondWord);
            }
            if (parts.length > 3) {
                thirdWord = parts[2].trim();
                System.out.println(thirdWord);
            }
            if (parts.length > 4) {
                forthWord = parts[3].trim();
                System.out.println(forthWord);
            }
            // "sentence"
            sql = "SELECT `ID`  FROM `CONSIGNEE` WHERE `FIRM_NAME` LIKE '%"
                    + consigneeName + "%' AND `CNT_ID` LIKE '%"
                    + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
            System.out.println(" CONSIGNEE 1 " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                consigneeId = rs.getInt("ID");
            }
            if (consigneeId == 0) {
                sql = "SELECT `ID`  FROM `CONSIGNEE` WHERE `FIRM_NAME` LIKE '%"
                        + consigneeName.substring(0, (consigneeName.length() / 2)) + "%' AND `CNT_ID` LIKE '%"
                        + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                System.out.println(" CONSIGNEE 2 " + sql);
                if (rs2.next()) {
                    consigneeId = rs2.getInt("ID");
                    System.out.println(" CONSIGNEE ID " + consigneeId);
                }
            }
            if (consigneeId == 0) {
                sql = "SELECT `ID`  FROM `CONSIGNEE` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + " " + thirdWord.trim() + " " + forthWord.trim() + "%' AND `CNT_ID` LIKE '%"
                        + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                System.out.println(" CONSIGNEE 2 " + sql);
                if (rs2.next()) {
                    consigneeId = rs2.getInt("ID");
                    System.out.println(" CONSIGNEE ID " + consigneeId);
                }
            }
            if (consigneeId == 0) {
                sql = "SELECT `ID`  FROM `CONSIGNEE` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + " " + thirdWord.trim() + "%' AND `CNT_ID` LIKE '%"
                        + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                System.out.println(" CONSIGNEE 2 " + sql);
                if (rs2.next()) {
                    consigneeId = rs2.getInt("ID");
                    System.out.println(" CONSIGNEE ID " + consigneeId);
                }
            }
            if (consigneeId == 0) {
                sql = "SELECT `ID`  FROM `CONSIGNEE` WHERE `FIRM_NAME` LIKE'%" + firstWord.trim() + " " + secondWord.trim() + "%' AND `CNT_ID` LIKE '%"
                        + consigneeCountry + "%' AND `CLT_ID`=" + clientId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                System.out.println(" CONSIGNEE 2 " + sql);
                if (rs2.next()) {
                    consigneeId = rs2.getInt("ID");
                    System.out.println(" CONSIGNEE ID " + consigneeId);
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
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

    public boolean issendResponseDetails1(String fileName) {

        boolean responsestatus = false;
        int response1status = 1;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {
            Statement stmt = null;
            String sql;

            stmt = conn.createStatement();
            sql = "SELECT RESPONSE1STATUS FROM `INTKESWSECSTRANSACTIONS` WHERE `RECEIVEDFILEDETAILS` LIKE '%"
                    + fileName + "%';";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.first()) {
                response1status = rs.getInt("RESPONSE1STATUS");
            }

            if (response1status == 0) {
                responsestatus = true;
            }
            if (response1status == 1) {
                responsestatus = false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responsestatus;
    }

    public boolean issendResponseDetails2(String fileName) {

        boolean responsestatus = false;
        int response1status = 0;
        int response2status = 0;
        String response1time = utilclass.getCurrentTime();
        String response2time = utilclass.getCurrentTime();
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {
            Statement stmt = null;
            String sql;

            stmt = conn.createStatement();
            sql = "SELECT RESPONSE1STATUS,REPONSE1TIME,RESPONSE2STATUS,REPONSE2TIME FROM `INTKESWSECSTRANSACTIONS` WHERE `RECEIVEDFILEDETAILS` LIKE '%"
                    + fileName + "%';";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.first()) {
                response1status = rs.getInt("RESPONSE1STATUS");
                response2status = rs.getInt("RESPONSE2STATUS");
                if (rs.getString("REPONSE1TIME") != null) {
                    if (rs.getString("REPONSE1TIME").length() > 11) {
                        response1time = rs.getString("REPONSE1TIME");
                    }
                }
                if (rs.getString("REPONSE2TIME") != null) {
                    if (rs.getString("REPONSE2TIME").length() > 11) {
                        response2time = rs.getString("REPONSE2TIME");
                    }
                }
            }

            if (response2status == 1) {
                return false;
            }
            if (response2status == 0) {
                return true;
            }
            if ((response2status == 1) && (response1status == 1)) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Date date1 = new Date(Integer.parseInt(response1time.substring(0, 4)), Integer.parseInt(response1time.substring(4, 6)), Integer.parseInt(response1time.substring(6, 8)), Integer.parseInt(response1time.substring(8, 10)), Integer.parseInt(response1time.substring(10, 12)));
            Date date2 = new Date(Integer.parseInt(response2time.substring(0, 4)), Integer.parseInt(response2time.substring(4, 6)), Integer.parseInt(response2time.substring(6, 8)), Integer.parseInt(response2time.substring(8, 10)), Integer.parseInt(response2time.substring(10, 12)));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
            date1 = sdf.parse(sdf.format(date1));
            date2 = sdf.parse(sdf.format(date2));
            long diff = date2.getTime() - date1.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffMinutes < 0) {
                diffMinutes = diffMinutes * -1;
            }
            if (diffHours < 0) {
                diffHours = diffHours * -1;
            }
            if (diffDays < 0) {
                diffDays = diffDays * -1;
            }
            if ((diffMinutes > 9) && (response2status == 0)) {
                responsestatus = true;
            }
            if ((diffHours > 1) && (response2status == 0)) {
                responsestatus = true;
            }
            if ((diffDays > 1) && (response2status == 0)) {
                responsestatus = true;
            }

        } catch (ParseException ex) {
            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responsestatus;
    }

    public void setResponseStatusDetails(int responsestatus, String fileDetails, String respFileDetails, String response) {
        Statement stmt;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        if (response.startsWith("RESPONSE1STATUS")) {
            try {
                stmt = conn.createStatement();
                String sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET"
                        + "`RESPONSE1STATUS` = " + responsestatus + ","
                        + "`RESPONSEFILE1DETAILS` = '" + respFileDetails + "',"
                        + "`REPONSE1TIME` = '" + utilclass.getCurrentTime() + "'"
                        + " WHERE `RECEIVEDFILEDETAILS` = '" + fileDetails + "';";

                stmt.executeUpdate(sql);

            } catch (SQLException ex) {
                Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (response.startsWith("RESPONSE2STATUS")) {
            try {
                stmt = conn.createStatement();
                String sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET"
                        + "`RESPONSE2STATUS` = " + responsestatus + ","
                        + "`RESPONSEFILE2DETAILS` = '" + respFileDetails + "',"
                        + "`REPONSE2TIME` = '" + utilclass.getCurrentTime() + "'"
                        + " WHERE `RECEIVEDFILEDETAILS` = '" + fileDetails + "';";

                stmt.executeUpdate(sql);

            } catch (SQLException ex) {
                Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int trackTransactionDetails(String transactionType, String transactionDetails, int status, String KESWSID, String ECSInvoiceNo, String additionalTransactionDetails, String fileDetails) {
        Statement stmt;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        int insertkey = 1;
        try {

            if (transactionType.contains("PROCESS")) {
                stmt = conn.createStatement();
                String sql = "INSERT INTO `INTKESWSECSTRANSACTIONS` ("
                        + "`ID`,"
                        + "`TRANSACTIONTYPE`,"
                        + "`TRANSACTIONDETAILS`,"
                        + "`TRANSACTIONCOST`,"
                        + "`KESWSID`,"
                        + "`ECSCSGID`,"
                        + "`ECSINVOICENO`,"
                        + "`RECEIVESTATUS`,"
                        + "`RECEIVETIME`,"
                        + "`RECEIVEDFILEDETAILS`,"
                        + "`ISRECEIVEDFILEDETAILSVALID`,"
                        + "`INSPECTIONSTATUS`,"
                        + "`INSPECTIONTIME`,"
                        + "`ISSUESTATUS`,"
                        + "`ISSUETIME`,"
                        + "`RESPONSE1STATUS`,"
                        + "`REPONSE1TIME`,"
                        + "`RESPONSEFILE1DETAILS`,"
                        + "`RESPONSE2STATUS`,"
                        + "`REPONSE2TIME`,"
                        + "`RESPONSEFILE2DETAILS`,"
                        + "`ACHIVESTATUS`,"
                        + "`ACHIVETIME`,"
                        + "`ADDITIONALTRANSACTIONDETAILS`,"
                        + "`RELATEDTRANSACTIONID`"
                        + ") VALUES ("
                        + "NULL,"
                        + "'" + transactionType + "',"
                        + "'" + transactionDetails + "',"
                        + "'" + getTransactionCost() + "',"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "1,"
                        + "'" + utilclass.getCurrentTime() + "',"
                        + "'" + fileDetails + "',"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL,"
                        + "NULL"
                        + ");";

                stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                insertkey = rs.getInt(1);

            }
            if (transactionType.startsWith("FILESTATUS")) {
                stmt = conn.createStatement();
                String sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET"
                        + "`ISRECEIVEDFILEDETAILSVALID` = '" + status + "',"
                        + "`ADDITIONALTRANSACTIONDETAILS` = '" + additionalTransactionDetails + "'"
                        + " WHERE `RECEIVEDFILEDETAILS` = '" + fileDetails + "';";

                stmt.executeUpdate(sql);

            }

            return insertkey;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            return insertkey;
        }

    }

    public void trackTransactionDetails(String transactionType, int status, String fileName, String additionalDetails) {

        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        String Transactiontype = "";
        String transactionDetails = "";
        Integer inspectionStatus = -1;
        Integer issueStatus = -1;
        String additionalTransactionDetails = "";
        Integer relatedTransactionId = 0;
        String addetails1 = " ";
        String addetails2 = " ";
        String addetails3 = " ";
        String addetails4 = " ";
        String addetails5 = " ";
        String addetails6 = " ";
        String addetails7 = " ";
        String addetails8 = " ";
        String transTypedetails1 = " ";
        String transTypedetails2 = " ";
        String transTypedetails3 = " ";
        String transTypedetails4 = " ";
        String transTypedetails5 = " ";
        String transTypedetails6 = " ";
        String transTypedetails7 = " ";
        String transTypedetails8 = " ";
        try {
            Statement stmt = null;

            String sql;
            // Check using name and client id
            sql = "SELECT `TRANSACTIONTYPE`,`TRANSACTIONDETAILS`,`ADDITIONALTRANSACTIONDETAILS`,`INSPECTIONSTATUS` ,`ISSUESTATUS`,`RELATEDTRANSACTIONID` FROM `INTKESWSECSTRANSACTIONS` WHERE `RECEIVEDFILEDETAILS` ='"
                    + fileName + "';";
            // System.out.println(sql);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //System.out.println("roW 1"+rs.first());
            if (rs.first()) {
                Transactiontype = rs.getString("TRANSACTIONTYPE");
                transactionDetails = rs.getString("TRANSACTIONDETAILS");
                additionalTransactionDetails = rs.getString("ADDITIONALTRANSACTIONDETAILS");
                inspectionStatus = rs.getInt("INSPECTIONSTATUS");
                issueStatus = rs.getInt("ISSUESTATUS");
                relatedTransactionId = rs.getInt("RELATEDTRANSACTIONID");
                //System.out.println("Transactiontype" + Transactiontype+"transactionDetails"+transactionDetails+"additionalTransactionDetails"+additionalTransactionDetails+"inspectionStatus"+inspectionStatus+"relatedTransactionId"+relatedTransactionId);
            }
            if (additionalTransactionDetails.length() > 950) {
                additionalTransactionDetails = additionalTransactionDetails.substring(0, 800);
            }
            if (transactionType.contains("SUBMITTEDINSPECTIONSTATUS")) {

                Statement stmt2 = null;
                stmt2 = conn.createStatement();
                Date date1 = new Date(Integer.parseInt(additionalDetails.substring(0, 4)), Integer.parseInt(additionalDetails.substring(4, 6)), Integer.parseInt(additionalDetails.substring(6, 8)), Integer.parseInt(additionalDetails.substring(8, 10)), Integer.parseInt(additionalDetails.substring(10, 12)));
                Date date2 = new Date(Integer.parseInt(utilclass.getCurrentTime().substring(0, 4)), Integer.parseInt(utilclass.getCurrentTime().substring(4, 6)), Integer.parseInt(utilclass.getCurrentTime().substring(6, 8)), Integer.parseInt(utilclass.getCurrentTime().substring(8, 10)), Integer.parseInt(utilclass.getCurrentTime().substring(10, 12)));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                date1 = sdf.parse(sdf.format(date1));
                date2 = sdf.parse(sdf.format(date2));
                long diff = date2.getTime() - date1.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                //System.out.println("SUBMITTED STATUS  " + fileName + " relatedTransactionId " + relatedTransactionId.intValue());
                if (relatedTransactionId == 0) {
                    if (transTypedetails1.length() < 10) {
                        transTypedetails1 = transactionDetails;
                    }
                    if (transTypedetails1.length() > 10) {
                        transTypedetails1 = transTypedetails1;
                    }
                    addetails1 = "  WAITING FOR " + diffHours + "HRS " + diffMinutes + "MINS SINCE SUBMITED | VALID FILE";
                    transTypedetails1 = "APPLICATION WIATING FOR PLANNING | " + transactionDetails + "";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails1 + "',"
                            + "`RELATEDTRANSACTIONID` = '" + 1 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` ='" + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }
                if (relatedTransactionId == 1) {
                    if (addetails1.length() < 10) {
                        addetails1 = additionalTransactionDetails;
                    }
                    if (transTypedetails1.length() < 10) {
                        transTypedetails1 = transactionDetails;
                    }
                    addetails2 = "WAITING FOR " + diffHours + "HRS " + diffMinutes + "MIN FOR ASSIGNING TO OFFICER | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails1 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    // System.out.println("SUBMITTED STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }

            }
            if (transactionType.startsWith("PLANNEDINSPECTIONSTATUS")) {
                Statement stmt2 = null;
                Date date1 = new Date(Integer.parseInt(additionalDetails.substring(0, 4)), Integer.parseInt(additionalDetails.substring(4, 6)), Integer.parseInt(additionalDetails.substring(6, 8)), Integer.parseInt(additionalDetails.substring(8, 10)), Integer.parseInt(additionalDetails.substring(10, 12)));
                Date date2 = new Date(Integer.parseInt(utilclass.getCurrentTime().substring(0, 4)), Integer.parseInt(utilclass.getCurrentTime().substring(4, 6)), Integer.parseInt(utilclass.getCurrentTime().substring(6, 8)), Integer.parseInt(utilclass.getCurrentTime().substring(8, 10)), Integer.parseInt(utilclass.getCurrentTime().substring(10, 12)));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                date1 = sdf.parse(sdf.format(date1));
                date2 = sdf.parse(sdf.format(date2));
                long diff = date2.getTime() - date1.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                stmt2 = conn.createStatement();
                if (relatedTransactionId == 1) {
                    if (addetails2.length() < 10) {
                        addetails2 = additionalTransactionDetails;
                        addetails1 = "";
                    }
                    if (transTypedetails1.length() < 10) {
                        transTypedetails1 = transactionDetails;
                    }
                    if (addetails2.length() > 10) {
                        addetails2 = addetails2;
                    }
                    if (transTypedetails1.length() > 10) {
                        transTypedetails1 = transTypedetails1;
                    }
                    addetails3 = "ISSUED TO OFFICER FOR INSPECTION AT " + utilclass.getCurrentTime() + " |";
                    transTypedetails2 = "APPLICATION WIATING FOR INSPECTION | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails2 + transTypedetails1 + "',"
                            + "`RELATEDTRANSACTIONID` = '" + 2 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails3 + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    //System.out.println("PLANNEDINSPECTION STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }
                if (relatedTransactionId == 2) {
                    if (addetails3.length() < 10) {
                        addetails3 = additionalTransactionDetails;
                        addetails2 = "";
                        addetails1 = "";
                    }
                    if (transTypedetails2.length() < 10) {
                        transTypedetails2 = transactionDetails;
                        transTypedetails1 = "";
                    }
                    if (addetails3.length() > 10) {
                        addetails3 = addetails3;
                    }
                    if (transTypedetails2.length() > 10) {
                        transTypedetails2 = transTypedetails2;
                    }
                    addetails4 = "WAITING FOR " + diffHours + "HRS " + diffMinutes + "MIN FOR INSPECTION BY OFFICER | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails2 + transTypedetails1 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails4 + addetails3 + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    //System.out.println("PLANNEDINSPECTION STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }

            }
            if (transactionType.startsWith("PENDINGINSPECTIONSTATUS")) {

                Statement stmt2 = null;
                Date date1 = new Date(Integer.parseInt(additionalDetails.substring(0, 4)), Integer.parseInt(additionalDetails.substring(4, 6)), Integer.parseInt(additionalDetails.substring(6, 8)), Integer.parseInt(additionalDetails.substring(8, 10)), Integer.parseInt(additionalDetails.substring(10, 12)));
                Date date2 = new Date(Integer.parseInt(utilclass.getCurrentTime().substring(0, 4)), Integer.parseInt(utilclass.getCurrentTime().substring(4, 6)), Integer.parseInt(utilclass.getCurrentTime().substring(6, 8)), Integer.parseInt(utilclass.getCurrentTime().substring(8, 10)), Integer.parseInt(utilclass.getCurrentTime().substring(10, 12)));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                date1 = sdf.parse(sdf.format(date1));
                date2 = sdf.parse(sdf.format(date2));
                long diff = date2.getTime() - date1.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                stmt2 = conn.createStatement();
                if (relatedTransactionId == 2) {
                    if (addetails4.length() < 10) {
                        addetails4 = additionalTransactionDetails;
                        addetails3 = "";
                        addetails2 = "";
                        addetails1 = "";
                    }
                    if (transTypedetails2.length() < 10) {
                        transTypedetails2 = transactionDetails;
                        transTypedetails2 = "";
                    }
                    if (addetails4.length() > 10) {
                        addetails4 = addetails4;
                    }
                    if (transTypedetails2.length() > 10) {
                        transTypedetails2 = transTypedetails2;
                    }
                    addetails5 = "INSPECTED BY OFFICER AT " + utilclass.getCurrentTime() + " |";
                    transTypedetails3 = "APPLICATION WIATING FOR ISSUE OF CERTIFICATE | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails3 + transTypedetails2 + transTypedetails1 + "',"
                            + "`INSPECTIONSTATUS` = '" + status + "',"
                            + "`INSPECTIONTIME` = '" + utilclass.getCurrentTime() + "',"
                            + "`RELATEDTRANSACTIONID` = '" + 3 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails5 + addetails4 + addetails3 + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    // System.out.println("PENDINGINSPECTIONS STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }
                if (relatedTransactionId == 3) {
                    if (addetails5.length() < 10) {
                        addetails5 = additionalTransactionDetails;
                        addetails4 = "";
                        addetails3 = "";
                        addetails2 = "";
                        addetails1 = "";
                    }
                    if (transTypedetails3.length() < 10) {
                        transTypedetails3 = transactionDetails;
                        transTypedetails2 = "";
                        transTypedetails1 = "";
                    }
                    if (addetails5.length() > 10) {
                        addetails5 = addetails5;
                    }
// compair last digits and truncate 
                    addetails6 = "WAITING FOR " + diffHours + "HRS " + diffMinutes + "MIN FOR CERTIFICATE ISSUE | ";
                    String transdetails = addetails6 + addetails5 + addetails4 + addetails3 + addetails2 + addetails1;
                    if (transdetails.length() > 900) {
                        transdetails = transdetails.substring(0, 850);
                    }
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails3 + transTypedetails2 + transTypedetails1 + "',"
                            + "`INSPECTIONSTATUS` = '" + status + "',"
                            + "`INSPECTIONTIME` = '" + utilclass.getCurrentTime() + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + transdetails + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    //System.out.println("PENDINGINSPECTIONS STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }

            }
            if (transactionType.startsWith("REJECTEDINSPECTIONSTATUS")) {
                Statement stmt2 = null;
                stmt2 = conn.createStatement();
                Date date1 = new Date(Integer.parseInt(additionalDetails.substring(0, 4)), Integer.parseInt(additionalDetails.substring(4, 6)), Integer.parseInt(additionalDetails.substring(6, 8)), Integer.parseInt(additionalDetails.substring(8, 10)), Integer.parseInt(additionalDetails.substring(10, 12)));
                Date date2 = new Date(Integer.parseInt(utilclass.getCurrentTime().substring(0, 4)), Integer.parseInt(utilclass.getCurrentTime().substring(4, 6)), Integer.parseInt(utilclass.getCurrentTime().substring(6, 8)), Integer.parseInt(utilclass.getCurrentTime().substring(8, 10)), Integer.parseInt(utilclass.getCurrentTime().substring(10, 12)));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                date1 = sdf.parse(sdf.format(date1));
                date2 = sdf.parse(sdf.format(date2));
                long diff = date2.getTime() - date1.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                if (relatedTransactionId == 2) {
                    if (addetails6.length() < 10) {
                        addetails6 = additionalTransactionDetails;
                        addetails5 = "";
                        addetails4 = "";
                        addetails3 = "";
                        addetails2 = "";
                        addetails1 = "";
                    }
                    if (transTypedetails3.length() < 10) {
                        transTypedetails3 = transactionDetails;
                        transTypedetails2 = "";
                        transTypedetails1 = "";
                    }
                    if (addetails6.length() > 10) {
                        addetails6 = addetails6;
                    }
                    addetails7 = "APPLICATION REJECTED AT " + diffHours + "HRS " + diffMinutes + "MIN |";
                    transTypedetails4 = "APPLICATION REJECTED  | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails4 + transTypedetails3 + transTypedetails2 + transTypedetails1 + "',"
                            + "`INSPECTIONSTATUS` = '" + status + "',"
                            + "`INSPECTIONTIME` = '" + utilclass.getCurrentTime() + "',"
                            + "`RELATEDTRANSACTIONID` = '" + 5 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails7 + addetails6 + addetails5 + addetails4 + addetails3 + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    //System.out.println("REJECTEDINSPECTION STATUS  " + fileName + " sql " + sql);
                    stmt2.executeUpdate(sql);
                    System.out.println(sql);
                }

            }
            if (transactionType.startsWith("ISSUEDINSPECTIONSTATUS")) {
                Statement stmt2 = null;
                stmt2 = conn.createStatement();
                Date date1 = new Date(Integer.parseInt(additionalDetails.substring(0, 4)), Integer.parseInt(additionalDetails.substring(4, 6)), Integer.parseInt(additionalDetails.substring(6, 8)), Integer.parseInt(additionalDetails.substring(8, 10)), Integer.parseInt(additionalDetails.substring(10, 12)));
                Date date2 = new Date(Integer.parseInt(utilclass.getCurrentTime().substring(0, 4)), Integer.parseInt(utilclass.getCurrentTime().substring(4, 6)), Integer.parseInt(utilclass.getCurrentTime().substring(6, 8)), Integer.parseInt(utilclass.getCurrentTime().substring(8, 10)), Integer.parseInt(utilclass.getCurrentTime().substring(10, 12)));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
                date1 = sdf.parse(sdf.format(date1));
                date2 = sdf.parse(sdf.format(date2));
                long diff = date2.getTime() - date1.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                if (relatedTransactionId == 3) {
                    if (addetails6.length() < 10) {
                        addetails6 = additionalTransactionDetails;
                        addetails5 = "";
                        addetails4 = "";
                        addetails3 = "";
                        addetails2 = "";
                        addetails1 = "";
                    }
                    if (transTypedetails4.length() < 10) {
                        transTypedetails4 = transactionDetails;
                        transTypedetails3 = "";
                        transTypedetails2 = "";
                        transTypedetails1 = "";
                    }
                    if (addetails6.length() > 10) {
                        addetails6 = addetails6;
                    }
                    addetails7 = "CERTIFICATE ISSUED AT TIME " + utilclass.getCurrentTime() + "  |";
                    transTypedetails5 = "APPLICATION APPROVED | ";
                    sql = "UPDATE `INTKESWSECSTRANSACTIONS` SET "
                            + "`TRANSACTIONDETAILS` ='" + transTypedetails5 + transTypedetails4 + transTypedetails3 + transTypedetails2 + transTypedetails1 + "',"
                            + "`ISSUESTATUS` = '" + status + "',"
                            + "`ISSUETIME` = '" + utilclass.getCurrentTime() + "',"
                            + "`RELATEDTRANSACTIONID` = '" + 4 + "',"
                            + "`ADDITIONALTRANSACTIONDETAILS` = '" + addetails7 + addetails6 + addetails5 + addetails4 + addetails3 + addetails2 + addetails1 + "'"
                            + " WHERE `RECEIVEDFILEDETAILS` = '" + fileName + "';";
                    stmt2.executeUpdate(sql);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecCdFileMsg getLatestRecCdFileMsg() {
        return null;
    }

    public int getLastTransactionId() {
        int lastid = 0;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        Statement stmt = null;
        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select MAX(ID)  FROM INTKESWSECSTRANSACTIONS");
            if (rs.next()) {
                lastid = rs.getInt(1);
            }

            return lastid;
        } catch (SQLException ex) {
            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);

            return lastid;
        }
    }

    public void createECSconsignmentCertificate(Integer CGT_ID, int recCdFileMsgId) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = " insert into CERTIFICATE (CGT_ID, CRT_ID) values ("
                    + CGT_ID.toString() + "," + "1" + ");";
            System.out.print(sql);
            stmt.executeUpdate(sql);
            logCreatedConsignementCertificate("SAVED CERTIFICATE FOR CONSIGNEMENT[" + CGT_ID + "] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime(), recCdFileMsgId);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            logErrorCreatingConsignementCertificate("ERROR CREATING CERTIFICATE FOR CONSIGNEMENT[" + CGT_ID + "] AT " + utilclass.getCurrentDate() + " " + utilclass.getCurrentTime() + " ERROR DETAILS [" + e.toString() + "]", recCdFileMsgId);

        }
    }

    public int getLastProducerId() {

        int producerId = 0;

        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
            // Check using name and client id
            sql = "SELECT COUNT(*) as ID  FROM `PRODUCER`";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                producerId = rs.getInt("ID");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return producerId + 1;
    }

    public boolean createClientProducer(ConsignmentDocument SourceDoc, String producerName) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        boolean iscreated = false;
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
        p_clt_id = "" + mapKESWSClientPintoECSClientId(SourceDoc) + "";
        p_vatnr = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                .getCDExporter().getTIN();
        p_phone = SourceDoc.getDocumentDetails().getConsignmentDocDetails()
                .getCDExporter().getTeleFax();

        try {
            CallableStatement stmt1 = null;
            String sql2 = "{call spclient_insertupdateproducer(?," + "?,"
                    + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                    + "?," + "?," + "?"
                    + ")}";
            stmt1 = conn.prepareCall(sql2);
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
            System.out.println("Stored procedure spclient_insertupdateproducer" + stmt1.toString());
            iscreated = stmt1.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
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
    public void createECSconsignmentVariatyQuantities(
            ConsignmentDocument srcConsDoc, ECSConsignmentDoc desObject,
            int consignmentId) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        List<ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails> itemdetails = srcConsDoc
                .getDocumentDetails().getConsignmentDocDetails()
                .getCDProductDetails().getItemDetails();

        for (Iterator<ItemDetails> iterator = itemdetails.iterator(); iterator
                .hasNext();) {
            ItemDetails itemDetails2 = (ItemDetails) iterator.next();

            try { 
                Statement stmt = null;
                int producerId = 0;
                String producerName = " ";
                String producerdetails = " ";
                String TreatmentInformation = " ";
                String chemicalsActiveIngredients = " ";
                String durationsAndTemperature = " ";
                String concentrationActiveIngredients = " ";
                int ConsigneeId = 0;
                if (desObject.getConsigneeID() == 0) {
                    ConsigneeId = mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(srcConsDoc);

                } else {
                    ConsigneeId = desObject.getConsigneeID();

                }
                if (ConsigneeId == 0) {
                    System.out.println("wrong " + ConsigneeId);
                }
                String InternalProductNoCommodityCategory = "060";
                // String InternalProductNoCommodityTypeId = "1414";//1426 live dev "1414"
                String InternalProductNoCommodityTypeId = "1426";//1426 live dev "1414"
                // String InternalProductNoCommodityVarId = "2188";//2216 live dev "2188"
                String InternalProductNoCommodityVarId = "2216";//2216 live dev "2188"
                //  String ItemComFormId = "145";//147 live dev 145
                String ItemComFormId = "147";//147 live dev 145
                // String PackageId="854";//801 live dev 854 
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
                if ("KGM".equals(itemDetails2.getCDProduct1().getQuantity().getUnitOfQty())) {
                    UnitId = getVAQUnitId("Kgs");
                }

                float packQty = itemDetails2.getCDProduct1().getPackageQty().floatValue();

                try {
                    producerName = itemDetails2
                            .getCDItemCommodity().getProducerDetails();
                    producerdetails = itemDetails2
                            .getCDItemCommodity().getProducerDetails();
                } catch (Exception e) {

                    producerName = srcConsDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getName();
                    producerdetails = srcConsDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getName();
                }
                //producerId = EcsEntitiesControllerCaller.getProducerId(producerName, mapKESWSClientPintoECSClientId(srcConsDoc),
              //          itemDetails2.getCDProduct1().getCountryOfOrigin());
                System.out.println(" producer id" + producerId);
                if (producerId == 0) {
                    try {
                        if (itemDetails2
                                .getCDItemCommodity().getProducerDetails() != null) {
                            createClientProducer(srcConsDoc, itemDetails2
                                    .getCDItemCommodity().getProducerDetails());
                        }

                    } catch (Exception e) {
                        createClientProducer(srcConsDoc, "not_available");

                    }
                }
//                producerId = EcsEntitiesControllerCaller.getProducerId(producerdetails, mapKESWSClientPintoECSClientId(srcConsDoc),
           //             itemDetails2.getCDProduct1().getCountryOfOrigin());

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
                } catch (Exception e) {

                    TreatmentInformation = " ";
                    durationsAndTemperature = " ";
                    concentrationActiveIngredients = " ";
                }

                stmt = conn.createStatement();
                if (TreatmentDate.length() == 0) {
                    String sql = "insert into VARIETYQUANTITY (CSG_ID,CTY_ID,CVA_ID,QUANTITY_ORIGINAL,UNIT_ID,NUMBER_OF_PACKAGES,TOP_ID,TREATMENT_DETAILS,TREATMENT_DATE,CHEMICAL_INGREDIANTS,DURATION_TEMPARATURE,CONCENTRATION,ORIGIN_CNT_ID,PRD_ID,CGT_ID,CFO_ID) values ('"
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
                    System.out.println("Sql " + sql.toString());
                    stmt.executeUpdate(sql);
                } else {
                    String sql = "insert into VARIETYQUANTITY (CSG_ID,CTY_ID,CVA_ID,QUANTITY_ORIGINAL,UNIT_ID,NUMBER_OF_PACKAGES,TOP_ID,TREATMENT_DETAILS,TREATMENT_DATE,CHEMICAL_INGREDIANTS,DURATION_TEMPARATURE,CONCENTRATION,ORIGIN_CNT_ID,PRD_ID,CGT_ID,CFO_ID) values ('"
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
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
            }

        }

    }

    public int getMOCId(String MOC) {
        // TODO Auto-generated method stub

        int MeansOfConveyance = 0;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
            // Check pin over 11 char
            sql = " SELECT `ID` FROM `MEANSOFCONVEYANCE`  WHERE `ENDDATE` > NOW() AND NAME LIKE '%"
                    + MOC + "%' ";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                MeansOfConveyance = rs.getInt("ID");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");

        }

        return MeansOfConveyance;
    }

    private String getTypeOfPackageingId(String commodityCategoryId,
            String commodityTypeId, String packageTypeDesc) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        int packageId = 0;

        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
            // Check using name and client id
            sql = "SELECT `ID`  FROM `TYPEOFPACKAGING` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                    + commodityTypeId.replaceFirst("^0*", "")
                    + " AND `DESCRIPTION` LIKE '%"
                    + packageTypeDesc
                    + "%' AND `CCA_ID`="
                    + commodityCategoryId.replaceFirst("^0*", "") + ";";
            System.out.println("Sql cca" + sql.toString());
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                packageId = rs.getInt("ID");
            } else {
                sql = "SELECT `ID`  FROM `TYPEOFPACKAGING` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                        + commodityTypeId.replaceFirst("^0*", "")
                        + " AND `CCA_ID`="
                        + commodityCategoryId.replaceFirst("^0*", "") + ";";
                System.out.println("Sql cca" + sql.toString());
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    packageId = rs.getInt("ID");
                }
                if (createTypeOfPackage(commodityTypeId,
                        commodityCategoryId, packageTypeDesc)) {
                    sql = "SELECT `ID`  FROM `TYPEOFPACKAGING` WHERE `ENDDATE` > NOW() AND `CTY_ID`="
                            + commodityTypeId.replaceFirst("^0*", "")
                            + " AND `CCA_ID`="
                            + commodityCategoryId.replaceFirst("^0*", "") + ";";
                    System.out.println("Sql cca" + sql.toString());
                    rs = stmt.executeQuery(sql);
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return "" + packageId + "";

    }

    public boolean createTypeOfPackage(String commodityTypeId,
            String commodityCategoryId, String packageTypeDesc) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        boolean iscreated = false;
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
        try {
            stmt = conn.createStatement();

            String sql = "SELECT   id  FROM  `TYPEOFPACKAGING` ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                p_id = rs.getInt("id") + 1;
            }

            CallableStatement stmt1 = null;
            String sql2 = "{call spTOP_insert(?," + "?,"
                    + "?," + "?," + "?," + "?," + "?)}";
            stmt1 = conn.prepareCall(sql2);
            stmt1.setInt(1, p_id);
            stmt1.setString(2, p_name);
            stmt1.setString(3, p_cty_id);
            stmt1.setString(4, p_startdate);
            stmt1.setString(5, p_enddate);
            stmt1.setString(6, p_desc);
            stmt1.setInt(7, p_cca_id);

            System.out.println("Stored procedure spclient_insertupdateproducer" + stmt1.toString());
            iscreated = stmt1.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return iscreated;

    }

    private boolean createUnit(String unitOfQty) {
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        boolean iscreated = false;
        int p_id;
        String p_name;
        String p_startdate;
        String p_enddate;
        String p_desc;
        /* Initialize variables * */
        p_id = 0;
        Statement stmt;
        try {
            stmt = conn.createStatement();

            String sql = "SELECT   id  FROM  `COMMODITYUNIT` ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                p_id = rs.getInt("id") + 1;
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        p_name = unitOfQty;
        p_startdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        p_enddate = "2099-09-09";
        p_desc = " ";

        try {
            CallableStatement stmt1 = null;
            String sql2 = "{call spcommunit_insert(?," + "?,"
                    + "?," + "?," + "?)}";
            stmt1 = conn.prepareCall(sql2);
            stmt1.setInt(1, p_id);
            stmt1.setString(2, p_name);
            stmt1.setString(3, p_startdate);
            stmt1.setString(4, p_enddate);
            stmt1.setString(5, p_desc);

            System.out.println("Stored procedure spcommunit_insert" + stmt1.toString());
            iscreated = stmt1.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return iscreated;

    }

    private String getVAQUnitId(String unitOfQty) {
        int VAQUnitId = 0;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        if (unitOfQty.contentEquals("STM")) {
            unitOfQty = "Stems";
        }
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  ID  FROM  COMMODITYUNIT  WHERE `ENDDATE` > NOW() AND `NAME` LIKE '%"
                    + unitOfQty + "%' ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                VAQUnitId = rs.getInt("ID");

            } else {
                createUnit(unitOfQty);
                sql = "SELECT  ID  FROM  COMMODITYUNIT  WHERE `ENDDATE` > NOW() AND `NAME` LIKE '%"
                        + unitOfQty + "%' ORDER BY id DESC LIMIT  0, 1";

                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    VAQUnitId = rs.getInt("ID");

                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        System.out.print("UNIT ID " + VAQUnitId + " Unit" + unitOfQty);
        return "" + VAQUnitId + "";
    }

    public String getECSconsignmentInspectionFindings(String InvoiceNumber) {
        String eCSConsignmentInspectionFinding = " ";
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  `COMMODITYVARIETY`.`NAME`,`QUANTITY_REJECTED`,`IDENTIFIED_PATHEGEN`   FROM  `VARIETYINSPECTIONFINDINGS`,`VARIETYQUANTITY`,COMMODITYVARIETY WHERE `VARIETYQUANTITY`.`CGT_ID`='" + getECSconsignmentId(InvoiceNumber) + "' AND  `VARIETYQUANTITY`.`ID`=`VQA_ID` AND `CVA_ID`=`COMMODITYVARIETY`.`ID`";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                eCSConsignmentInspectionFinding += rs.getString("NAME");
                //  eCSConsignmentInspectionFinding += rs.getString("QUANTITY_REJECTED");
                //  eCSConsignmentInspectionFinding += rs.getString("IDENTIFIED_PATHEGEN");

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return eCSConsignmentInspectionFinding;
    }

    /**
     * todo use mysql_insert_id()
     *
     * @return
     */
    public static int getCreatedECSconsignmentId() {
        int createdECSconsignmentId = 0;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT   id  FROM    `CONSIGNEMENT` ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                createdECSconsignmentId = rs.getInt("id");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return createdECSconsignmentId;
    }

    public String getECSconsignmentStatus(String InvoiceNumber, int ConsignmentId) {
        String eCSconsignmentStatus = "HOLD";
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  STATUS   FROM    `CONSIGNEMENT` WHERE INVOICE_NR='" + InvoiceNumber + "' ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.err.println(" STATUS " + rs.getString("STATUS"));
                eCSconsignmentStatus = rs.getString("STATUS");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return eCSconsignmentStatus;
    }

    public static int getECSconsignmentId(String InvoiceNumber) {
        int eCSconsignmentStatus = 0;
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  ID   FROM  `CONSIGNEMENT` WHERE INVOICE_NR='" + InvoiceNumber + "' ORDER BY id DESC LIMIT  0, 1";
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                eCSconsignmentStatus = rs.getInt("ID");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return eCSconsignmentStatus;
    }

    public static String getECSFinalconsignmentInspectionResult(String InvoiceNumber) {
        String eCSconsignmentStatus = "";
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  FINAL_RESULT,REASON_OF_REJECTION  FROM    `CONSIGNEMENTINSPECTIONRESULT` WHERE CGT_ID='" + getECSconsignmentId(InvoiceNumber) + "' ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {

                eCSconsignmentStatus += rs.getString("FINAL_RESULT");
                //eCSconsignmentStatus += rs.getString("REASON_OF_REJECTION");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return eCSconsignmentStatus;
    }

    public String getECSCertificateDetails(String InvoiceNumber) {
        int eCSconsignmentId = getECSconsignmentId(InvoiceNumber);
        String eCSconsignmentDocuments = "";
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT  DOCUMENT_NR   FROM   `CERTIFICATE` WHERE CGT_ID=" + eCSconsignmentId + " ORDER BY id DESC LIMIT  0, 1";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                eCSconsignmentDocuments = rs.getString("DOCUMENT_NR");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return eCSconsignmentDocuments;
    }

    /**
     * This method maps id to warehouse locations
     *
     * @param warehouseCode
     * @param warehouseLocation
     * @return
     */
    public int getECSInlocId(String warehouseCode, String warehouseLocation) {
        // TODO Auto-generated method stub
        DBConnector dao = new DBConnector();
        Connection conn = dao.ECSDBConnectorService();
        int inLocId = 1;

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
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;
            // Check using name and client id
            sql = "SELECT `ID`  FROM `INSPECTIONLOCATION` WHERE `NAME` LIKE '%"
                    + warehouseCode + "%' OR `TOWN` LIKE '%"
                    + warehouseLocation + "%';";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                inLocId = rs.getInt("ID");
            }
            if (inLocId == 0) {
                String dateString = "2099-09-09";
                java.sql.Date date = null;
                try {
                    date = java.sql.Date.valueOf(dateString);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
                }

                sql = "INSERT INTO `INSPECTIONLOCATION` ( `SHORT_NAME`, `NAME`, `STARTDATE`, `ENDDATE`, `DESCRIPTION`, `STREETNAME`, `NUMBER`, `POSTAL_CODE`, `TOWN`, `CONTACT_PERSON`, `PHONE`, `FAX`, `MAIL`)"
                        + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt;
                pstmt = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                // pstmt.setString(1,id autogenarated);
                pstmt.setString(2, warehouseCode);
                pstmt.setString(3, warehouseCode + " " + warehouseLocation);
                pstmt.setDate(4, utilclass.getCurrentDate());
                pstmt.setDate(5, date);
                pstmt.setString(6, warehouseCode + " " + warehouseLocation);
                pstmt.setString(7, warehouseCode + " " + warehouseLocation);
                pstmt.setDouble(8, 233344);
                pstmt.setString(9, "00");
                pstmt.setString(10, warehouseLocation);
                pstmt.setString(11, "KESWS ");
                pstmt.setString(12, "0000");
                pstmt.setString(13, "0000");
                pstmt.setString(14, "kesws@kephis.org");
                pstmt.executeUpdate();
                // prest.executeUpdate(query,
                // PreparedStatement.RETURN_GENERATED_KEYS); Throws an error
                // prest.executeQuery(); Throws an error
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    inLocId = rs.getInt(1);
                }

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return inLocId;

    }

    public String getECSCOD(String COD) {
        Connection conn;
        String CODISOCODE = null;
        DBConnector dao = new DBConnector();
        conn = dao.ECSDBConnectorService();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT `ISOCODE` FROM `COUNTRY` WHERE `ENDDATE` > NOW() AND `ISOCODE` LIKE '%"
                    + COD + "%';";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                CODISOCODE = rs.getString("ISOCODE");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //ECSKESWSFileLogger.Log(e.toString(), "SEVERE");
        }
        return CODISOCODE;
    }

    public void logDuplicateReceivedFile(String filename, int log_level) {

        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails("DUPLICATE FILE RECEIVED");
        transactionLog.setLogTypesLogIdLevel(logtype);
        transactionLog.setRecCdFileMsgRecCdFileId(findRecCdFileMsgbyFileName(filename));
        transactionLogsContr.create(transactionLog);

    }

    /**
    public RecCdFileMsg recCDFileMsg(String filename, int message_type) {

        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(message_type);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        RecCdFileMsg recCdFileMsg = new RecCdFileMsg();
        recCdFileMsg.setFileName(filename);
        recCdFileMsg.setMessageTypesMessageTypeId(messageType);
        if (findRecCdFileMsgbyFileName(filename) == null) {
            try {
                recCDFileMsgContr.create(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            return null;
        }
        return findRecCdFileMsgbyFileName(filename);

    }
**/
    public RecCdFileMsg getRecCDFileMsgbyFileName(String filename) {

        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (findRecCdFileMsgbyFileName(filename) == null) {
            return null;
        } else {
            return findRecCdFileMsgbyFileName(filename);
        }

    }

    public RecCdFileMsg OgSubCd1Msg(String receivedFileName, int message_type) {

        RecCdFileMsg recCdFileMsg = new RecCdFileMsg();

        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(message_type);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        recCdFileMsg.setFileName(receivedFileName);
        recCdFileMsg.setMessageTypesMessageTypeId(messageType);
        if (findRecCdFileMsgbyFileName(receivedFileName) == null) {
            try {
                recCDFileMsgContr.create(recCdFileMsg);
                recCdFileMsg = findRecCdFileMsgbyFileName(receivedFileName);
                return recCdFileMsg;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    //validate existing ipc code 
    // if not available create ipc code and log
/** 
    public CdFileDetails recCDFileMsgDetails(RecCdFileMsg CDFile, InternalProductcodes IPCCode, Double Weight, InternalProductcodes InternalProductCode) {
        CdFileDetails cdFileDetails = new CdFileDetails();
        Double itemweight = 0.00;
        itemweight = itemweight + Weight;
        if (IPCCode.getAggregateIPCCodeLevel() == 1) {
            // find cd files of the same consignment and update weight  and return id
            if (!CDFile.getCdFileDetailsCollection().isEmpty()) {
                for (Iterator iterator1 = CDFile.getCdFileDetailsCollection().iterator(); iterator1.hasNext();) {
                    cdFileDetails = (CdFileDetails) iterator1.next();
                    itemweight = itemweight + cdFileDetails.getWeight();
                    cdFileDetails.setWeight(itemweight);
                    for (Iterator iterator = InternalProductCode.getPricelistInternalProductcodeDocumentMapCollection().iterator(); iterator.hasNext();) {
                        PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                        PricelistJpaController pricelistController = new PricelistJpaController(emf);
                        Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                        if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                            Double temp_max_weight = 0.00;
                            Double temp_min_weight = 0.00;
                            int pricelistid1 = 0;
                            int pricelistid2 = 0;
                            //set default to 0
                            PricelistInternalProductcodeDocumentMap defaultdocprice = null;
                            if (temp_max_weight <= pricelist.getMaxWeight()) {
                                temp_max_weight = pricelist.getMaxWeight();
                                pricelistid1 = pricelist.getPriceID();
                                defaultdocprice = next;

                            }
                            if (temp_min_weight >= pricelist.getMinWeight()) {
                                temp_min_weight = pricelist.getMinWeight();
                                pricelistid2 = pricelist.getPriceID();
                            }
                            if (pricelistid2 == pricelistid1) {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(next);
                            } else {
                                cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                            }

                        }
                    }
                }
                return cdFileDetails;

            } else {

                CdFileDetailsJpaController cdFileDetailsController = new CdFileDetailsJpaController(emf);

                cdFileDetails.setIpcIdCode(IPCCode.getInternalProductCode());
                cdFileDetails.setAggregateIPCCodeLevel(IPCCode.getAggregateIPCCodeLevel());
                cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(CDFile);
                cdFileDetails.setWeight(itemweight);
                for (Iterator iterator = InternalProductCode.getPricelistInternalProductcodeDocumentMapCollection().iterator(); iterator.hasNext();) {
                    PricelistInternalProductcodeDocumentMap next = (PricelistInternalProductcodeDocumentMap) iterator.next();
                    PricelistJpaController pricelistController = new PricelistJpaController(emf);
                    Pricelist pricelist = pricelistController.findPricelist(next.getPRICELISTPriceIDRef().getPriceID());
                    if ((pricelist.getMaxWeight() >= cdFileDetails.getWeight()) && (cdFileDetails.getWeight() <= pricelist.getMinWeight())) {
                        Double temp_max_weight = 0.00;
                        Double temp_min_weight = 0.00;
                        int pricelistid1 = 0;
                        int pricelistid2 = 0;
                        //set default to 0
                        PricelistInternalProductcodeDocumentMap defaultdocprice = null;
                        if (temp_max_weight <= pricelist.getMaxWeight()) {
                            temp_max_weight = pricelist.getMaxWeight();
                            pricelistid1 = pricelist.getPriceID();
                            defaultdocprice = next;

                        }
                        if (temp_min_weight >= pricelist.getMinWeight()) {
                            temp_min_weight = pricelist.getMinWeight();
                            pricelistid2 = pricelist.getPriceID();
                        }
                        if (pricelistid2 == pricelistid1) {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(next);
                        } else {
                            cdFileDetails.setPRICELISTINTIPCDOCUMENTMAPPricelistIPCMAPIDRef(defaultdocprice);
                        }

                    }
                }
                cdFileDetailsController.create(cdFileDetails);
                for (Iterator<CdFileDetails> iterator = CDFile.getCdFileDetailsCollection().iterator(); iterator.hasNext();) {
                    cdFileDetails = iterator.next();

                }
                return cdFileDetails;
                //  if non is found  crate new cd file and return id
            }
            /
             * } else if (true) { //aggregate level is 11 aggregate on commodity
             * category // if non is founs } else if (true) { //aggregate level
             * is 111 aggregate on commodity type // if non is founs } else if
             * (true) { //aggregate level is 11 aggregate on commodity variety
             * // if non is founs }
             *
             

        }
        return cdFileDetails;
    }
**/
    public void setRecCDFileMsgisValid(RecCdFileMsg recCdFileMsg, int status) {

        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            recCdFileMsg = recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID());
            recCdFileMsg.setIsFileXMLValid(status);
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //  return recCdFileMsg;
    }

    public void logReceivedFile(String filename, int loglevel) {
        //TRANSACTION_LOGS
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ECS-KESWSPU");

        LogTypes logTypes = new LogTypes(loglevel);
        RecCdFileMsg recCdFileMsg = new RecCdFileMsg();
        RecCdFileMsgJpaController recControl = new RecCdFileMsgJpaController(emf);
        //get object using filename
        // recCdFileMsg = recControl.findRecCdFileMsg(filename);

        // TransactionLogs transactionLogs = new TransactionLogs(logTypes, recCdFileMsg, filename + ":Received" , new Date());
        //  TransactionLogsJpaController transactionLogsJpaController = new TransactionLogsJpaController(emf);
        //  transactionLogsJpaController.create(transactionLogs);
    }

    public static void updateRecCDFileMsg(RecCdFileMsg recCdFileMsg) {
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setRecCDFileMsgisValidForTransaction(RecCdFileMsg recCdFileMsg, int status) {

        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            recCdFileMsg.setIsFileXMLValidForTransaction(status);
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 
    public static void setRecCDFileMsgisInValid(RecCdFileMsg recCdFileMsg, String errorDetails, int status) {
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        if (recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID()) != null) {
            recCdFileMsg = recCDFileMsgContr.findRecCdFileMsg(recCdFileMsg.getRECCDFileID());
            recCdFileMsg.setIsFileXMLValidForTransaction(status);
            logErrorOnXMLFile(recCdFileMsg.getFileName(), errorDetails, 4);
            try {
                updateRecFileMsg(recCdFileMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createCDFileDetails(RecCdFileMsg recCdFileMsg, CdFileDetails cdFileDetails) {
        CdFileDetailsJpaController cdFileContr = new CdFileDetailsJpaController(emf);
        if (cdFileContr.findCdFileDetails(cdFileDetails.getId()) == null) {
            cdFileDetails.setRECCDFILEMSGRECCDFILEIDRef(recCdFileMsg);
            cdFileContr.create(cdFileDetails);
        }

    }

    public void setRecCDFilePath(String string) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logPreferredInspectionTimeNotSet(String string, int recCdFileMsgId) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logTimeofdepartureTimeNotSet(String string, int recCdFileMsgId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logSavedConsignementDetails(String string, int recCdFileMsgId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logSubmittedConsignementDetails(String string, int recCdFileMsgId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logCreatedConsignementCertificate(String string, int recCdFileMsgId) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logErrorSavingConsignementDetails(String string, int recCdFileMsgId) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logErrorSubmittingConsignementDetails(String string, int recCdFileMsgId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logErrorCreatingConsignementDetails(String string, int recCdFileMsgId) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void logErrorCreatingConsignementCertificate(String string, int recCdFileMsgId) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void logErrorOnXMLFile(String filename, String ErrorMessage, int log_level) {
        TransactionLogsJpaController transactionLogsContr = new TransactionLogsJpaController(emf);
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        TransactionLogs transactionLog = new TransactionLogs();
        LogTypes logtype = new LogTypes();
        logtype.setLogIdLevel(log_level);
        transactionLog.setLogDetails(ErrorMessage);
        transactionLog.setLogTypesLogIdLevel(logtype);
        transactionLog.setRecCdFileMsgRecCdFileId(findRecCdFileMsgbyFileName(filename));
        transactionLogsContr.create(transactionLog);
    }

    public void CloseDAOConnection() {
        try {

            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(DBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int mapKESWSConsignmentILtoECSClientConsignmentIL(ConsignmentDocument SourceDoc) {
        int inspectionLocationId;
        DBDAO dbdao = new DBDAO();
        try {
            inspectionLocationId = dbdao.getECSInlocId(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseCode(), SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDExporter().getWarehouseLocation());
        } catch (Exception e) {
            inspectionLocationId = 1;
        }
        return inspectionLocationId;
    }

    // map client application date and time
    public int mapKESWSConsignmentMOCtoECSClientConsignmentMOC(ConsignmentDocument SourceDoc) {
        DBDAO dbdao = new DBDAO();
        String MOC = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getModeOfTransportDesc();
        int MOCId = dbdao.getMOCId(MOC);
        dbdao.CloseDAOConnection();
        return MOCId;
    }

    // map client id to client id
    public int mapKESWSClientConsigneeIdtoECSClientIdConsigneeId(ConsignmentDocument SourceDoc) {
        int consigneeId = 0;
        String consigneeName;
        Integer CongineeIdRef;
        CongineeIdRef = 0;
        DBDAO dbdao = new DBDAO();
        consigneeName = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getName();
        String consigneeCountry = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPosCountry();
        try {
            CongineeIdRef = CongineeIdRef.parseInt(SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getMDARefNo());
        } catch (Exception e) {
            CongineeIdRef = 0;
        }
        consigneeId = dbdao.getConsigneeId(consigneeName, mapKESWSClientPintoECSClientId(SourceDoc), consigneeCountry, CongineeIdRef);
        if (consigneeId == 0) {
            dbdao.createClientConsignee(SourceDoc);
        }
        consigneeId = dbdao.getConsigneeId(consigneeName, mapKESWSClientPintoECSClientId(SourceDoc), consigneeCountry, CongineeIdRef);
        return consigneeId;
    }

    public String mapKESWSConsignmentCODtoECSConsignmentCOD(ConsignmentDocument SourceDoc) {
        String COD;
        DBDAO dbdao = new DBDAO();
        COD = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDConsignee().getPhyCountry();
        return dbdao.getECSCOD(COD);
    }

    public String mapCountryKESWSConsignmentPOEtoECSConsignmentPOE(ConsignmentDocument SourceDoc) {
        String POE;
        POE = SourceDoc.getDocumentDetails().getConsignmentDocDetails().getCDTransport().getPortOfArrivalDesc();
        return POE;
    }

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
        CloseDAOConnection();
        return clientid;
    }

    public ResCdFileMsg resCDFileMsg(RecCdFileMsg recCdFileMsg, int messageTypeId) {
        ResCdFileMsg resCdFileMsg = null;
        MessageTypesJpaController messagetypeContr = new MessageTypesJpaController(emf);
        MessageTypes messageType = messagetypeContr.findMessageTypes(messageTypeId);
        ResCdFileMsgJpaController resCDFileMsgContr = new ResCdFileMsgJpaController(emf);
        int isResMessageSent = 0;
        if (!(recCdFileMsg == null)) {
            isResMessageSent = recCdFileMsg.getResCdFileMsgCollection().size();
        }
        if (isResMessageSent > 0) {

            for (Iterator<ResCdFileMsg> iterator1 = recCdFileMsg.getResCdFileMsgCollection().iterator(); iterator1.hasNext();) {
                resCdFileMsg = iterator1.next();
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 1) {
                    return resCdFileMsg;
                }
                if (resCdFileMsg.getMessageTypesMessageTypeId().getMessageTypeId() == 2) {
                    return resCdFileMsg;
                }
            }

        } else {
            resCdFileMsg = new ResCdFileMsg();
            resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsg);
            resCdFileMsg.setMessageTypesMessageTypeId(messageType);
            createResCdFileMsg(resCdFileMsg, recCdFileMsg);
            return recCdFileMsg.getResCdFileMsgCollection().iterator().next();
        }
        return resCdFileMsg;

    }

    public static RecCdFileMsg findRecCdFileMsgbyFileName(String FileName) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            RecCdFileMsg recCdFileMsg = null;
            Query findRecCdFileMsgbyFileName = em.createNamedQuery("RecCdFileMsg.findByFileName");
            findRecCdFileMsgbyFileName.setParameter("fileName", FileName);
            List results = findRecCdFileMsgbyFileName.getResultList();
            if (!results.isEmpty()) {
                recCdFileMsg = (RecCdFileMsg) results.get(0);
            }
            return recCdFileMsg;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void updateRecFileMsg(RecCdFileMsg recCdFileMsg) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        RecCdFileMsgJpaController recCDFileMsgContr = new RecCdFileMsgJpaController(emf);
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            recCdFileMsg = em.merge(recCdFileMsg);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recCdFileMsg.getRECCDFileID();
                if (recCDFileMsgContr.findRecCdFileMsg(id) == null) {
                    throw new NonexistentEntityException("The recCdFileMsg with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void createResCdFileMsg(ResCdFileMsg resCdFileMsg, RecCdFileMsg recCdFileMsg) {
       if (resCdFileMsg.getRecErrorFileMsgCollection() == null) {
            resCdFileMsg.setRecErrorFileMsgCollection(new ArrayList<RecErrorFileMsg>());
        }
        if (resCdFileMsg.getPaymentInfoLogCollection() == null) {
            resCdFileMsg.setPaymentInfoLogCollection(new ArrayList<PaymentInfoLog>());
        }
        if (resCdFileMsg.getTransactionLogsCollection() == null) {
            resCdFileMsg.setTransactionLogsCollection(new ArrayList<TransactionLogs>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecCdFileMsg recCdFileMsgRecCdFileId = resCdFileMsg.getRecCdFileMsgRecCdFileId();
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId = em.getReference(recCdFileMsgRecCdFileId.getClass(), recCdFileMsgRecCdFileId.getRECCDFileID());
                resCdFileMsg.setRecCdFileMsgRecCdFileId(recCdFileMsgRecCdFileId);
            }
            EcsResCdFileMsg ECSRESCDFILEMSGRECCDFileID = resCdFileMsg.getECSRESCDFILEMSGRECCDFileID();
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID = em.getReference(ECSRESCDFILEMSGRECCDFileID.getClass(), ECSRESCDFILEMSGRECCDFileID.getRECCDFileID());
                resCdFileMsg.setECSRESCDFILEMSGRECCDFileID(ECSRESCDFILEMSGRECCDFileID);
            }
            MessageTypes messageTypesMessageTypeId = resCdFileMsg.getMessageTypesMessageTypeId();
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId = em.getReference(messageTypesMessageTypeId.getClass(), messageTypesMessageTypeId.getMessageTypeId());
                resCdFileMsg.setMessageTypesMessageTypeId(messageTypesMessageTypeId);
            }
        em.persist(resCdFileMsg);
            if (recCdFileMsgRecCdFileId != null) {
                recCdFileMsgRecCdFileId.getResCdFileMsgCollection().add(resCdFileMsg);
                recCdFileMsgRecCdFileId = em.merge(recCdFileMsgRecCdFileId);
            }
            if (ECSRESCDFILEMSGRECCDFileID != null) {
                ECSRESCDFILEMSGRECCDFileID.getResCdFileMsgCollection().add(resCdFileMsg);
                ECSRESCDFILEMSGRECCDFileID = em.merge(ECSRESCDFILEMSGRECCDFileID);
            }
            if (messageTypesMessageTypeId != null) {
                messageTypesMessageTypeId.getResCdFileMsgCollection().add(resCdFileMsg);
                messageTypesMessageTypeId = em.merge(messageTypesMessageTypeId);
            }
             
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        } 
    
    }

}
