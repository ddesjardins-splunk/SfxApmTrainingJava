SignalFX Java Training examples


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

mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverterInstrumented

#Not Instrumented version.

mvn exec:java -Dexec.mainClass="com.signalfx.training.SfxCurrencyConverter
