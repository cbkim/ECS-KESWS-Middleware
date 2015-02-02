package org.kephis.ecs_kesws.xml.validator;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.kephis.ecs_kesws.utilities.UtilityClass;
import org.kephis.ecs_kesws.xml.parser.i.ogcdsubi.v_1_0.ConsignmentDocument;
import org.xml.sax.SAXException;

public class XmlFileValidator {

    private String ErrorDetails;

    public XmlFileValidator() {
        ErrorDetails = " ";
    }

    public String getErrorDetails() {
        return ErrorDetails;
    }

    public void setErrorDetails(String ErrorDetails) {
        this.ErrorDetails = ErrorDetails;
    }

    /**
     * *
     *
     * @param SourceXml
     * @param xsd
     * @param xsd1
     * @return valid (true )or invalid (false) status
     */
    public final boolean validateAgainstXSD(String SourceXml, String xsd, String xsd1) {
        boolean isvalid = false;
        try {
            Source[] schemaFile = new Source[2];
            schemaFile[0] = new StreamSource(new File(xsd));
            schemaFile[1] = new StreamSource(new File(xsd1));
            Source xmlFile = new StreamSource(new File(SourceXml));
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);

            Validator validator = schema.newValidator();

            try {
                validator.validate(xmlFile);
                isvalid = true;
            } catch (SAXException e) {
                // System.out.println(xmlFile.getSystemId() + " is NOT valid");
                System.out.println("Reason: " + e.getLocalizedMessage().replace("'", " "));
                setErrorDetails("Reason: " + e.getLocalizedMessage().replace("'", " "));
                isvalid = false;
            } catch (IOException ex) {
                // Logger.getLogger(XmlFileValidator.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Reason: " + ex.getLocalizedMessage().replace("'", " "));
                setErrorDetails("Reason: " + ex.getLocalizedMessage().replace("'", " "));
                isvalid = false;
            }
        } catch (SAXException ex) {
            //Logger.getLogger(XmlFileValidator.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("Reason: " + ex.getLocalizedMessage().replace("'", " "));
            setErrorDetails("Reason: " + ex.getLocalizedMessage().replace("'", " "));
            isvalid = false;
        } finally {
            return isvalid;
        }
    }

    public boolean validinternalProductDetails(ConsignmentDocument doc) {
        boolean vpcode = false;
        UtilityClass uc = new UtilityClass();
        List<ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails> itemdetails;
        itemdetails = doc.getDocumentDetails().getConsignmentDocDetails().getCDProductDetails().getItemDetails();

        for (Iterator<ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails> iterator = itemdetails.iterator(); iterator
                .hasNext();) {
            ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails itemDetails2 = (ConsignmentDocument.DocumentDetailsType.ConsignmentDocDetails.ProductDetailsType.ItemDetails) iterator.next();
            vpcode = uc.checkNull(itemDetails2.getCDProduct1().getInternalProductNo());
            if (vpcode == false) {
                return vpcode;
            }
        }
        return vpcode;

    }

    public boolean isValidOgCdSubIFile(String receivedFileName) {
        boolean isvalid = false;
        isvalid = validateAgainstXSD(receivedFileName, "/ecs_kesws/service/xsd_v_1_1/MDA_CommonTypes1.xsd", "/ecs_kesws/service/xsd_v_1_1/CONDOC1.xsd");
        return isvalid;
    }

    public boolean isValidOgCdSubIFileForProcessing(ConsignmentDocument conDoc) {
        boolean isvalid = false;
        isvalid = validinternalProductDetails(conDoc);
        return isvalid;
    }

    public boolean isValidOgCdResOFileForResponse(ConsignmentDocument keswsConsignmentDocumentObj) {
        return true;
    }

    public boolean isValidOgCdResOFile(String string) {
          return true;
    }
    /**
     * check inspection location check producer details check internal product
     * details
     *
     */
}
