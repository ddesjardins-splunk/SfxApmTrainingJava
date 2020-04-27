package com.signalfx.training;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class SfxCurrencyConverterServer extends AbstractHandler {
	
	public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		String amount = request.getParameter("amt");
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		SfxCurrencyConverterInstrumented converter = new SfxCurrencyConverterInstrumented();
		converter.convertMyAmount(new BigDecimal(amount));
		for (Iterator<String> it = converter.getResults().iterator() ; it.hasNext() ; ) {
			response.getWriter().println(it.next());
		}
	
	}
	
	 public static void main(String[] args) throws Exception {
	    	
		 Server server = new Server(8888);
	     server.setHandler(new SfxCurrencyConverterServer());
	     server.start();
	     server.join();	    
	    	
	 }
}
