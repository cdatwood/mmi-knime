package com.mmiagency.knime.nodes.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class Util {

    public static Integer toInteger(String s_in, Integer iExc) {
        Integer iResult = iExc;
        do {
            if (s_in == null) {
                continue;
            }
            if (s_in.length() == 0) {
                continue;
            }
            try {
                String s = s_in.trim();
                StringBuffer results = new StringBuffer(s.length());
                boolean bErr = false;
                for (int i = 0; i < s.length() && !bErr; i++) {
                    char c = s.charAt(i);
                    if ((c < '0' || c > '9')) {
                        if (c == '-' && 0 == i) {
                            results.append(c);
                            continue;
                        } else if (c == '$') {
                            continue;
                        } else if (c == ',') {
                            continue;
                        } else {
                            bErr = true;
                            continue;
                        }
                    } else {
                        results.append(c);
                    }
                }
                if (bErr) {
                    continue;
                }

                iResult = new Integer(results.toString());
            } catch (Exception E) {
            }
        } while (false);
        return iResult;
    }

    /**
     * Given a collection, join the contents together using a string
     * For example, for a vector foo with values ("a", "b", "c") joined with "-"
     * using join("-", foo) results in "a-b-c"
     * @param The join string to use (ie. ",", "-", " and ", etc)
     * @param The collection to join
     */
    public static String join(String joinString, Collection<?> collection) {
        if (collection == null) return null;
        StringBuffer results = new StringBuffer();
        boolean firstValue = true;
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!firstValue) {
                results.append(joinString);
            } else {
                firstValue = false;
            }
            results.append(iterator.next());
        }
        return results.toString();
    }
    
    /**
     * Evaluates whether a string is blank or null
     * @param string to evaluate
     * @return boolean, true if blank or null, false if has a value
     */
    public static boolean isBlankOrNull(String s) {
        return(s == null || s.trim().length() == 0);
    }


    public static Date toDate(String dateFormat, String dateToParse, Date defaultDate)  {
    	if (Util.isBlankOrNull(dateToParse)) return defaultDate;
    	SimpleDateFormat format = new SimpleDateFormat(dateFormat);
       	try	{	
       		Date date = format.parse(dateToParse);
       		return date;
       	} catch (Exception e) {
       		return null;
       	}
    }

    public static String formatDate(String dateFormat, Date date, String defaultString)  {
    	if (date == null) return defaultString;
    	SimpleDateFormat format = new SimpleDateFormat(dateFormat);
       	try	{	
       		String formattedDate = format.format(date);
       		return formattedDate;
       	} catch (Exception e) {
       		return defaultString;
       	}
    }

    
    public static Date getDateDaysAgo(int daysAgo) throws Exception {
        Date today = new Date();
        return getDateDaysAgo(today, daysAgo);
    }

    public static Date getDateDaysAgo(Date date, int daysAgo) throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, daysAgo * -1);
        return cal.getTime();
    }    

    public static Date getDateYearsAgo(Date date, int yearsAgo) throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, yearsAgo * -1);
        return cal.getTime();
    }    

    
}



