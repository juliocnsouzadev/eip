package br.com.juliocnsouza.eip.file_transfer;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Main {

    public static void main( String[] args )
            throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes( new RouteBuilder() {

            @Override
            public void configure()
                    throws Exception {
                errorHandler( deadLetterChannel( "file:error" )
                        .logExhaustedMessageHistory( true )
                        .maximumRedeliveries( 2 )
                        .redeliveryDelay( 2000 )
                        .onRedelivery( ( Exchange exchange ) -> {
                            int counter = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_COUNTER );
                            int max = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_MAX_COUNTER );
                            System.out.println( "Redelivery - " + counter + "/" + max );
                        } ) );

                String ftpEipFrom = "ftp://eip_from@portalprogress.com@portalprogress.com?password=abcd1234&ftpClient.dataTimeout=30000";

                String ftpEipTo = "ftp://eip_to@portalprogress.com@portalprogress.com?password=abcd1234&ftpClient.dataTimeout=30000";

                //from this project to ftp one
//                from( "file:pedidos?delay=5s&noop=true" ) //sets the orign of the file and a delay for reading de folder in each 5 seconds
//                        .log( "From local to FTP ${body}" )
//                        .to( ftpEipFrom );
                from( ftpEipFrom )
                        .log( "From FTP to FTP ${body}" )
                        .to( ftpEipTo );
            }
        } );
        context.start();
        Thread.sleep( 60000 );
        context.stop();
    }
}
