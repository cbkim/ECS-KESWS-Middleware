/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.test;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import org.kephis.ecs_kesws.entities.CustomPersistence;
import org.kephis.ecs_kesws.entities.InternalProductcodes;
import org.kephis.ecs_kesws.entities.MessageTypes;
import org.kephis.ecs_kesws.entities.RecCdFileMsg;
import org.kephis.ecs_kesws.entities.controllers.EcsKeswsEntitiesControllerCaller;
import org.kephis.ecs_kesws.entities.controllers.MessageTypesJpaController;
import org.kephis.ecs_kesws.entities.controllers.RecCdFileMsgJpaController;
import org.kephis.ecs_kesws.entities.controllers.EcsEntitiesControllerCaller;
/**
 *
 * @author kim
 */
public class EcsKeswsEntititesControllerCallerTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here 
        /***
        InternalProductcodes IPCObj = new InternalProductcodes();
        Date date = new java.sql.Date(2010, 10, 10);
        IPCObj.setAggregateIPCCodeLevel(0);
        IPCObj.setInternalProductCode("00000000000000");
        IPCObj.setHscodeDesc("Default mapping");
        IPCObj.setDateDeactivated(date);
        EcsKeswsEntitiesControllerCaller cntr = new EcsKeswsEntitiesControllerCaller();
        try {
            cntr.createInternalProductcode(IPCObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        * **/
        
           EcsEntitiesControllerCaller cntr = new EcsEntitiesControllerCaller();
        cntr.getSimpleConnection();
        // System.err.println(cntr.getInternalProductcodes("00000000000000").getIpcId());
       // InternalProductcodes internalProductcodes=cntr.getInternalProductcodes("06014262216147");
        //   cntr.findPricelistIPCDocMapEntitiesbyIPC(internalProductcodes);
           //cntr.updateCreateInternalProductcodePriceDocMappings(internalProductcodes);
     
        
    }

}
