package com.signalfx.training;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.jetty.server.Server;


public class SfxCurrencyConverterGenerator extends SfxCurrencyConverterServer {
	public static void main(String[] args) throws Exception {
    	
		 Server server = new Server(8888);
		 server.setHandler(new SfxCurrencyConverterServer());
		 server.start();   
		 
		 for(int i=0; i<1000; i++) {
			
			 URL url = new URL("http://localhost:8888/?amt="+i);
			 InputStream is = url.openConnection().getInputStream();

			 BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );

			 String line = null;
			 while( ( line = reader.readLine() ) != null )  {
			    System.out.println(line);
			 }
			 reader.close();
		 }
	 }
}
