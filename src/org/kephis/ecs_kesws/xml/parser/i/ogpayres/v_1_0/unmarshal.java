/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kephis.ecs_kesws.xml.parser.i.ogpayres.v_1_0;
 
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
 
public class unmarshal {
	public static void main(String[] args) {
 
	 try {
 
		File file = new File("C:\\Users\\joshua\\Documents\\NetBeansProjects\\test_payment\\src\\CD_PAY_RES_v1\\CD_PAY_RES_v1.0.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(ConsignmentPaymentStatus.class);
 
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                
		ConsignmentPaymentStatus customer = (ConsignmentPaymentStatus) jaxbUnmarshaller.unmarshal(new File("C:\\Users\\joshua\\Documents\\NetBeansProjects\\test_payment\\src\\CD_PAY_RES_v1\\CD_PAY_RES_v1.0.xml"));
		System.out.println(customer);
 
	   } catch (JAXBException e) {
		e.printStackTrace();
	  }
 
	}
}
/**
 *
 * @author joshua
 */

    

