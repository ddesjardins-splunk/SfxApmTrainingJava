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

    SIGNALFX_INGEST_URL=https://ingest.us0.signalfx.com
    SIGNALFX_SERVICE_NAME=SfxCurrencyConverterInstrumented
    SIGNALFX_TOKEN=<YourTokenHere>

5. Run the Eaxmple

    # Run the instrumented version here:

    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterInstrumented"

    #Not Instrumented version.

    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverter"
    
    #Optional web server version:
    
    you can run this example with a Jetty webserver also.
    
    mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterServer"
    
    Then Goto: http://localhost:8888/?amt=amountToConvert. example http://localhost:8888/?amt=100
    
    
