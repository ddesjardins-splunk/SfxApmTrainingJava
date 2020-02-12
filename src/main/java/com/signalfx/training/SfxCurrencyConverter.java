/**
 * 
 */
package com.signalfx.training;

/**
 * @author ddesjardins
 *
 */

import java.io.IOException;
import java.io.PrintWriter;
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

import com.signalfx.tracing.api.Trace;
  
import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;


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
    
    
    // TODO take parameter
    public static void main(String[] args) throws Exception {
       if (args.length < 1) {
    	   System.err.println("Please enter an amount to convert !!");
    	   System.exit(1);
      } else {
        	converterInstance.convertMyAmount(new BigDecimal(args[0]));
      }   
    }
    
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


