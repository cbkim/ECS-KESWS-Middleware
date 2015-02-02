package org.kephis.ecs_kesws.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 



public class HashKeyGenerator {

	 

	public static String generate(String agencyKey) {
		MessageDigest md = null;
		StringBuffer hashAgencyToken = null;
                if (agencyKey != null && !agencyKey.equals("")) {
//				md = MessageDigest.getInstance(SHA_ALGORITHM);
                    
                    md.update(agencyKey.getBytes());
                    
                    byte byteData[] = md.digest();
                    
                    // convert the byte to hex format method 1
                    hashAgencyToken = new StringBuffer();
                    for (int i = 0; i < byteData.length; i++) {
                        hashAgencyToken.append(Integer.toString(
                                (byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    System.out.println("Hex format : " + hashAgencyToken.toString());
                    //		logger.info("Hex format : " + hashAgencyToken.toString());
                    return hashAgencyToken.toString();
                }
		return null;
	}
	
	public static void main(String[] args) {
		String ucrNumber = "UCR201400032547";
		String pgaSystemCode = "TBK";
		String pgaSystemPass = "TBK2014";
		
		String token = HashKeyGenerator.generate(ucrNumber.trim()+pgaSystemCode+pgaSystemPass);
		
		System.out.println("Generated Token ::"+token);
		
	}
}