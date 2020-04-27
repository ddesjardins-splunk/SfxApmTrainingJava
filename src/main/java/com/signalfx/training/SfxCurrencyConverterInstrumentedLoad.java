package com.signalfx.training;



import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 
 */

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

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import com.signalfx.tracing.api.Trace;




public class SfxCurrencyConverterInstrumentedLoad {
		
	private static SfxCurrencyConverterInstrumentedLoad converterInstance = new SfxCurrencyConverterInstrumentedLoad();
	private static final Tracer s_tracer = GlobalTracer.get();

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
 
private static void runLoad()  throws Exception  {

	for(int i=0; i<10; i++) {
		converterInstance.convertMyAmount( new BigDecimal(i));
	}
}
    
    
public static void main(String[] args) throws Exception {
	
	try {
		runLoad();
		Thread.sleep(6000);  	   
    } catch (Exception e) {
    	
    }finally { System.exit(0);}
}
 	

	@Trace(operationName = "doConversion")
    private void doConversion ( BigDecimal amount, String fromCurrency, String fromLocale,  String toCurrency, String toLocale) throws Exception {

		final Span span = s_tracer.buildSpan("doConversion").start();
   	  	try (Scope scope = s_tracer.scopeManager().activate(span)) {
   	  		span.setTag("span.kind", "SERVER");
   	    	span.setTag("userid","DerekD");
   	        MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency).setNumber(amount).create();
   			CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);
   			MonetaryAmount convertedCurrency = fromAmount.with(conversion);

   			 MonetaryAmountFormat formatUS = MonetaryFormats.getAmountFormat(Locale.US);
   			 String formatted = formatUS.format(convertedCurrency);

   			 System.out.println(amount + " in " + fromLocale + " (" + fromCurrency + ") is equivalent to " + formatted + " in " + toLocale );	
   		    
   	    } finally {
   	       span.finish();
   	    }
   	  
   }

   @Trace(operationName = "convertMyAmount")
   private void convertMyAmount(BigDecimal amount) throws Exception {

   	final Span span = s_tracer.buildSpan("convertMyAmount").start();
   		try (Scope scope = s_tracer.scopeManager().activate(span)) {
   			for (Map.Entry<String,String> from : fromMap.entrySet())  { 
   				//System.out.println("Key = " + from.getKey() + ", Value = " + from.getValue()); 
   				for (Map.Entry<String,String> to : toMap.entrySet())  {
   					//System.out.println("Key = " + to.getKey() +  ", Value = " + to.getValue());
   					doConversion ( amount, from.getValue(),  from.getKey(), to.getValue(), to.getKey() );
   				}
   			}
   		} finally {
   		span.finish();
  		}
   }
}




