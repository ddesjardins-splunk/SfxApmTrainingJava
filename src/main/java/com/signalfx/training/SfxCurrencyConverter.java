/**
 * 
 */
package com.signalfx.training;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author ddesjardins
 *
 */


import java.math.BigDecimal;
import java.util.Map;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class SfxCurrencyConverter {
		
	
	protected ArrayList<String> m_Results = new ArrayList<String>();
	
    protected static Map<String, String> fromMap = Map.ofEntries(
    		  new AbstractMap.SimpleEntry<String, String>("United states", "USD"),
    		  new AbstractMap.SimpleEntry<String, String>("Great Britain", "GBP"),
    		  new AbstractMap.SimpleEntry<String, String>("India", "INR"),
    		  new AbstractMap.SimpleEntry<String, String>("China", "CNY"),
    		  new AbstractMap.SimpleEntry<String, String>("Singapore", "SGD")
    		  
    		);
    
    protected static Map<String, String> toMap = Map.ofEntries(
    		  new AbstractMap.SimpleEntry<String, String>("France", "EUR"),
    		  new AbstractMap.SimpleEntry<String, String>("Switzerland", "CHF"),
    		  new AbstractMap.SimpleEntry<String, String>("Germany", "EUR"),
    		  new AbstractMap.SimpleEntry<String, String>("Malaysia", "MYR"),
    		  new AbstractMap.SimpleEntry<String, String>("Japan", "JPY")
    		);
    
    
    private void doConversion ( BigDecimal amount, String fromCurrency, String fromLocale,  String toCurrency, String toLocale) {
    	MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency).setNumber(amount).create();
		 CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);
		 MonetaryAmount convertedCurrency = fromAmount.with(conversion);
		 
		 MonetaryAmountFormat formatUS = MonetaryFormats.getAmountFormat(Locale.US);
		 String formatted = formatUS.format(convertedCurrency);
		 
		 m_Results.add( amount + " in " + fromLocale + " (" + fromCurrency + ") is equivalent to " + formatted + " in " + toLocale );	
    }

    protected void convertMyAmount(BigDecimal amount) throws Exception {
    	m_Results.clear();
    	for (Map.Entry<String,String> from : fromMap.entrySet())  {
    		 
    		 for (Map.Entry<String,String> to : toMap.entrySet())  {
   
    			 doConversion ( amount, from.getValue(),  from.getKey(), to.getValue(), to.getKey() );
    		 }
    	 }
    }
    public  ArrayList<String> getResults() {
    	return m_Results;
    }
    
    public static void main(String[] args) throws Exception {
    	System.out.println("Please enter conversion amount: ");
    	
    	try {
        		//Enter data using BufferReader 
            	BufferedReader reader =  
                       new BufferedReader(new InputStreamReader(System.in)); 
             
            	// Reading data using readLine 
            	String valueToConvert = reader.readLine(); 
            	if ( null == valueToConvert ) {
            		System.err.println("Please enter an amount to convert !!");
            		System.exit(1);
            	} else {
            		SfxCurrencyConverter converter = new SfxCurrencyConverter();
            		converter.convertMyAmount(new BigDecimal(valueToConvert));
            		for (Iterator<String> it = converter.getResults().iterator() ; it.hasNext() ; ) {
            			System.out.println(it.next());
            		}
            		
            	}   
        } catch (Exception e) {
        	
        }finally { System.exit(0);}
    }
    
}


