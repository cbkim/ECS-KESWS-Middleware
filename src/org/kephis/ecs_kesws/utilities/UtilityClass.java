/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kephis.ecs_kesws.utilities;

/**
 *
 * @author kim
 */
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author kim
 */
public class UtilityClass {

    public java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    /**
     *
     * @param a
     * @return
     */
    public boolean checkNull(String... strings) {
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return current date yyyyMMddHHmmss
     */
    public String getCurrentTime() {
        Date curdate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf1.format(curdate);
    }

    public String getCurrentYear() {
        Date curdate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        return sdf1.format(curdate);
    }

    public String getCurrentMonth() {
        Date curdate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        return sdf1.format(curdate);
    }

    public String getCurrentDay() {
        Date curdate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        return sdf1.format(curdate);
    }

    public String formatDateString2(String date) {
        try {
            if (date.length() >= 8) {
                String year = date.substring(4, 8);
                String month = date.substring(2, 4);
                String day = date.substring(0, 2);
                date = year + "-" + month + "-" + day;
            } else {
                date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                return date;
            }
        } catch (Exception e) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            return date;
        }
        return date;
    }

    /**
     *
     * @param date
     * @return date year + "-" + month + "-" + day;
     */
    public String formatDateStringT(String date) {
        date = 2015 + "-" + 1 + "-" + 1;
        String day = date.substring(6, 8);
        String month = date.substring(4, 6);
        String year = date.substring(0, 4);
        date = year + "-" + month + "-" + day;
        return date;
    }

    /**
     *
     * @param date
     * @return
     */

    public String convertDatetoStringyyyMMdd(Date date) { 
        Format formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date); 
    }
  public String convertDatetoStringyyyMMddhhss(Date date) { 
        Format formatter = new SimpleDateFormat("yyyyMMddHHmm");
        return formatter.format(date); 
    }
    public String convertDatetoStringddMMyyyhhmmss(Date date) { 
        Format formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        return formatter.format(date); 
    }
    public String formatDateString(Integer date) {
        if (date.toString(date).length() < 8) {
            date = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()).toString());
        }
        String day = date.toString().substring(6, 8);
        String month = date.toString().substring(4, 6);
        String year = date.toString().substring(0, 4);
        String rdate = year + "-" + month + "-" + day;
        return rdate;
    }

    public String formatDateString(String date) {
        try {
            if (date.length() >= 8) {
                String day = date.substring(6, 8);
                String month = date.substring(4, 6);
                String year = date.substring(0, 4);
                date = year + "-" + month + "-" + day;
            } else {
                date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                return date;
            }
        } catch (Exception e) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            return date;
        }
        return date;
    }

    public String formatDateStringTime(String time) {
        String hr = time.substring(8, 10);
        String min = time.substring(10, 12);
        time = hr + ":" + min;
        return time;
    }

    public XMLGregorianCalendar stringToXMLGregorianCalendar(String s) throws ParseException, DatatypeConfigurationException {
        String ss = s.substring(12, 14);
        String mm = s.substring(10, 12);
        String HH = s.substring(8, 10);
        String day = s.substring(0, 2);
        String month = s.substring(2, 4);
        String year = s.substring(4, 8);
        s = year + "-" + month + "-" + day + "T" + HH + ":" + mm + ":" + ss;
        XMLGregorianCalendar result = null;
        Date date;
        SimpleDateFormat simpleDateFormat;
        GregorianCalendar gregorianCalendar;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        date = simpleDateFormat.parse(s);
        gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        gregorianCalendar.setTime(date);
        result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        return result;
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */ }
        /* // If you have StringUtils, you can use it to calculate the edit distance:
         return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
         (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
    /*
     public void printSimilarity(String s, String t) {
     System.out.println(String.format(
     "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
     }

     public static void main(String[] args) {
     UtilityClass cl = new UtilityClass();
     cl.printSimilarity("", "");
     cl.printSimilarity("James Finlay", "James");
     cl.printSimilarity("JAMES FINLAY(ME)DMCC DTTC,", "JAMES FINLAY (ME) DMCC");
     cl.printSimilarity("JAMES FINLAY (ME) DMCC DTTC", "JAMES FINLAY (ME) DMCC");
     cl.printSimilarity("JAMES FINLAY(ME)DMCC", "AMES FINLAY (ME) DMCC");
     cl.printSimilarity("JAMAL LEBANON COMPANY", "JAMAL LEBANON COMPANY");
     cl.printSimilarity("JAMES FINLAY (ME) DMCC DTTC ", "JAMES FINLAY (ME) DMCC DTTC");
     cl.printSimilarity("47/2010", "472011");
     cl.printSimilarity("47/2010", "AB.CDEF");
     cl.printSimilarity("47/2010", "4B.CDEFG");
     cl.printSimilarity("47/2010", "AB.CDEFG");
     cl.printSimilarity("The quick fox jumped", "The fox jumped");
     cl.printSimilarity("The quick fox jumped", "The fox");
     cl.printSimilarity("kitten", "sitting");
     }
     **/

    public String LPad(String str, Integer length, char car) {
        return str
                + String.format("%" + (length - str.length()) + "s", "")
                .replace(" ", String.valueOf(car));
    }

    public String RPad(String str, Integer length, char car) {
        return String.format("%" + (length - str.length()) + "s", "")
                .replace(" ", String.valueOf(car))
                + str;
    }
}
