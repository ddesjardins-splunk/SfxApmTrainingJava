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
import java.util.Locale;


public class SfxCurrencyConverter {
		
	private static SfxCurrencyConverter converterInstance = new SfxCurrencyConverter();
	
    private static Map<String, String> fromMap = Map.ofEntries(
    		  new AbstractMap.SimpleEntry<String, String>("United states", "USD"),
    		  new AbstractMap.SimpleEntry<String, String>("Great Britain", "GBP"),
    		  new AbstractMap.SimpleEntry<String, String>("India", "INR"),
    		  new AbstractMap.SimpleEntry<String, String>("China", "CNY"),
    		  new AbstractMap.SimpleEntry<String, String>("Singapore", "SGD")
    		  
    		);
    
    private static Map<String, String> toMap = Map.ofEntries(
    		  new AbstractMap.SimpleEntry<String, String>("France", "EUR"),
    		  new AbstractMap.SimpleEntry<String, String>("Switzerland", "CHF"),
    		  new AbstractMap.SimpleEntry<String, String>("Germany", "EUR"),
    		  new AbstractMap.SimpleEntry<String, String>("Malaysia", "MYR"),
    		  new AbstractMap.SimpleEntry<String, String>("Japan", "JPY")
    		);
    
    

public static void main(String[] args) throws Exception {
		System.out.println("Please enter conversion amount: ");
    	//Enter data using BufferReader 
        BufferedReader reader =  
                   new BufferedReader(new InputStreamReader(System.in)); 
         
        // Reading data using readLine 
        String valueToConvert = reader.readLine(); 
       if ( null == valueToConvert ) {
    	   System.err.println("Please enter an amount to convert !!");
    	   System.exit(1);
      } else {
        	converterInstance.convertMyAmount(new BigDecimal(valueToConvert));
        	// The sleep is here below because the Tracer object is not fully shutdown when the app exits, thus throwing exeception
        	// in production situations this will not be  the case as this is a short-lived application.
        	Thread.sleep(3000);
      }   
    }
 	
    /*
    public static void main(String[] args) throws Exception {
       if (args.length < 1) {
    	   System.err.println("Please enter an amount to convert !!");
    	   System.exit(1);
      } else {
        	converterInstance.convertMyAmount(new BigDecimal(args[0]));
        	// The sleep is here below because the Tracer object is not fully shutdown when the app exits, thus throwing exeception
        	// in production situations this will not be  the case as this is a short-lived application.
        	Thread.sleep(3000);
      }   
    }
 */
    private void doConversion ( BigDecimal amount, String fromCurrency, String fromLocale,  String toCurrency, String toLocale) {
    	MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency).setNumber(amount).create();
		 CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);
		 MonetaryAmount convertedCurrency = fromAmount.with(conversion);
		 
		 MonetaryAmountFormat formatUS = MonetaryFormats.getAmountFormat(Locale.US);
		 String formatted = formatUS.format(convertedCurrency);
		 
		 System.out.println(amount + " in " + fromLocale + " (" + fromCurrency + ") is equivalent to " + formatted + " in " + toLocale );	
    }

    private void convertMyAmount(BigDecimal amount) {
    	 for (Map.Entry<String,String> from : fromMap.entrySet())  {
    		 
    		 //System.out.println("Key = " + from.getKey() + ", Value = " + from.getValue()); 
    		 
    		 for (Map.Entry<String,String> to : toMap.entrySet())  {
    			 //System.out.println("Key = " + to.getKey() +  ", Value = " + to.getValue());
    			 
    			 doConversion ( amount, from.getValue(),  from.getKey(), to.getValue(), to.getKey() );
    		 }
    	 }
    }
    
}


