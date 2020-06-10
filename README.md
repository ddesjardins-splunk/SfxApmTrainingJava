SignalFX Java Training example

Assumes properly setup SignalFx Smart Agent as described here: https://docs.signalfx.com/en/latest/apm2/apm2-getting-started/apm2-smart-agent.html

How to build: ( linux )

1. Git Clone this Repository

2. Install Maven:

    sudo apt-get install maven 
    
3. Build from Local Repository directory ( where pom.xml is )

    sudo mvn install
    
4. Set Environment Variables

    #SIGNALFX_ENDPOINT_URL='http://localhost:9080/v1/trace' - only required if Agent is on another host
    
    SIGNALFX_INGEST_URL=https://ingest.YourRealm.signalfx.com
    SIGNALFX_SERVICE_NAME=SfxCurrencyConverterInstrumented
    SIGNALFX_TOKEN=<YourTokenHere>
     
    NOTE SIGNALFX_TRACE_METHODS is only required for Auto-Instrunentation of custom methods
    SIGNALFX_TRACE_METHODS= package.class[method1,method2] 

5. Run the Example:

    # Run the instrumented version here:

    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterInstrumented"

    # Not Instrumented version.

    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverter"
    
    # Optional web server version:
    
    Jetty - you can run this example with a Jetty webserver.
    
    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterServer"
    
    Then Goto: http://localhost:8888/?amt=amountToConvert. example http://localhost:8888/?amt=100
    
    # Auto-Instrumnentation of Custom Methods version:
    
     Set the Folowing Environment Variable:
     SIGNALFX_TRACE_METHODS=com.signalfx.training.SfxCurrencyConverterAuto[doConversion,convertMyAmount]

     mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterServerAuto"
     
     Then Goto:  http://localhost:8888/?amt=amountToConvert. example http://localhost:8888/?amt=100
