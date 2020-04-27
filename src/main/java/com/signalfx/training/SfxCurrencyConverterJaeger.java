package com.signalfx.training;


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
import java.util.Collections;
import java.util.Locale;

 

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import com.signalfx.tracing.api.Trace;
import com.signalfx.tracing.api.TraceSetting;

import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.zipkin.ZipkinV2Reporter;
import okhttp3.Request;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;


public class SfxCurrencyConverterJaeger {
		
	private static SfxCurrencyConverterJaeger converterInstance = new SfxCurrencyConverterJaeger();
	private static final Tracer s_tracer = createTracer();
	
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
  
	private static final String SIGNALFX_INGEST_URL = "SIGNALFX_INGEST_URL";
	private static final String SIGNALFX_TOKEN = "SIGNALFX_TOKEN";
	private static final String SIGNALFX_ENDPOINT_URL = "SIGNALFX_ENDPOINT_URL";
	
	private static io.opentracing.Tracer createTracer() {
        String accessToken = System.getenv(SIGNALFX_TOKEN);

        // Build the sender that does the HTTP request containing spans to our ingest server.
        OkHttpSender.Builder senderBuilder = OkHttpSender.newBuilder()
                .compressionEnabled(true).endpoint( System.getenv(SIGNALFX_INGEST_URL) + "/v1/trace");
                             

        // Add an interceptor to inject the SignalFx X-SF-Token auth header.
        senderBuilder.clientBuilder().addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("X-SF-Token", accessToken)
                    .build();

            return chain.proceed(request);
        });

        OkHttpSender sender = senderBuilder.build();

        // Build the Jaeger Tracer instance, which implements the opentracing Tracer interface.
        io.opentracing.Tracer tracer = new io.jaegertracing.Configuration("signalfx-jaeger-java-example")
                // We need to get a builder so that we can directly inject the
                // reporter instance.
                .getTracerBuilder()
                // This configures the tracer to send all spans, but you will probably want to use
                // something less verbose.
                .withSampler(new ConstSampler(true))
                // Configure the tracer to send spans in the Zipkin V2 JSON format instead of the
                // default Jaeger UDP protocol, which we do not support.
                .withReporter(new ZipkinV2Reporter(AsyncReporter.create(sender)))
                .build();

        // It is considered best practice to at least register the GlobalTracer instance, even if you
        // don't generally use it.
        //GlobalTracer.register(tracer);

        return tracer;
    }

	@Trace(operationName = "doConversion")
    private void doConversion ( BigDecimal amount, String fromCurrency, String fromLocale,  String toCurrency, String toLocale) {

   	 final Span span = s_tracer.buildSpan("doConverstion").start();
   	    try (Scope scope = s_tracer.scopeManager().activate(span)) {
   	    	span.setTag("Converting "+ amount.toString(),  " From:" + fromLocale + " To:"+toLocale);
   	        MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency).setNumber(amount).create();
   			CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);
   			MonetaryAmount convertedCurrency = fromAmount.with(conversion);

   			 MonetaryAmountFormat formatUS = MonetaryFormats.getAmountFormat(Locale.US);
   			 String formatted = formatUS.format(convertedCurrency);

   			 System.out.println(amount + " in " + fromLocale + " (" + fromCurrency + ") is equivalent to " + formatted + " in " + toLocale );	

   	    } finally {
   	       span.finish();
   	    }
   	 ((io.jaegertracing.internal.JaegerTracer)(s_tracer)).close();
   }

   @Trace(operationName = "convertMyAmount")
   private void convertMyAmount(BigDecimal amount) {

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
   
   public static void main(String[] args) throws Exception {
       if (args.length < 1) {
    	   System.err.println("Please enter an amount to convert !!");
    	   System.exit(1);
      } else {
    	  Span parentSpan = s_tracer.buildSpan("root").start();
        	converterInstance.convertMyAmount(new BigDecimal(args[0]));
      }   
    }
}




