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

import java.util.Iterator;
import java.util.Locale;

import com.signalfx.tracing.api.Trace;





public class SfxCurrencyConverterAuto extends SfxCurrencyConverter {
		
	
	@Trace(operationName = "doConversion")
    private void doConversion ( BigDecimal amount, String fromCurrency, String fromLocale,  String toCurrency, String toLocale) {
        MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency).setNumber(amount).create();
		CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);
		MonetaryAmount convertedCurrency = fromAmount.with(conversion);

		MonetaryAmountFormat formatUS = MonetaryFormats.getAmountFormat(Locale.US);
		String formatted = formatUS.format(convertedCurrency);

		m_Results.add(amount + " in " + fromLocale + " (" + fromCurrency + ") is equivalent to " + formatted + " in " + toLocale );	
   }

   @Trace(operationName = "convertMyAmount")
   protected void convertMyAmount(BigDecimal amount) {
	   m_Results.clear();
	  
		for (Map.Entry<String,String> from : fromMap.entrySet())  { 
			for (Map.Entry<String,String> to : toMap.entrySet())  {
				doConversion ( amount, from.getValue(),  from.getKey(), to.getValue(), to.getKey() );
			}
		}
	
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
        		SfxCurrencyConverterAuto converter = new SfxCurrencyConverterAuto();
        		converter.convertMyAmount(new BigDecimal(valueToConvert));
        		for (Iterator<String> it = converter.getResults().iterator() ; it.hasNext() ; ) {
        			System.out.println(it.next());
        		}
        		// The sleep is here below because the Tracer object is not fully shutdown when the app exits, thus throwing exeception
        		// in production situations this will not be  the case as this is a short-lived application.
        		Thread.sleep(3000);  	
	      }   
	    } catch (Exception e) {
	    	
	    }finally { System.exit(0);}
	}
}




