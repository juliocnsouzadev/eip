package br.com.juliocnsouza.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

    public static void main( String[] args )
            throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes( new RouteBuilder() {

            @Override
            public void configure()
                    throws Exception {
//for treating a specific exception
//                onException( Exception.class ).
//                        handled( true ).
//                        maximumRedeliveries( 3 ).
//                        redeliveryDelay( 4000 ).
//                        onRedelivery(( Exchange exchange ) -> {
//                            int counter = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_COUNTER );
//                            int max = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_MAX_COUNTER );
//                            System.out.println( "Redelivery - " + counter + "/" + max );;
//                });
                errorHandler( deadLetterChannel( "file:error" )
                        .logExhaustedMessageHistory( true )
                        .maximumRedeliveries( 2 )
                        .redeliveryDelay( 2000 )
                        .onRedelivery( ( Exchange exchange ) -> {
                            int counter = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_COUNTER );
                            int max = ( int ) exchange.getIn().getHeader( Exchange.REDELIVERY_MAX_COUNTER );
                            System.out.println( "Redelivery - " + counter + "/" + max );
                        } ) );

                from( "file:pedidos?delay=5s&noop=true" ). //sets the orign of the file and a delay for reading de folder in each 5 seconds
                        routeId( "rota-pedidos" ).
                        to( "validator:pedido.xsd" );
//                        multicast().//spreads the call result (message) to all routes
//                        to( "direct:soap" ).
//                        to( "direct:http_get" ).
//                        to( "direct:http_post" );

                from( "direct:soap" ).
                        routeId( "rota-soap" ).
                        to( "xslt:pedido-para-soap.xslt" ).
                        log( "${body}" ).
                        setHeader( Exchange.CONTENT_TYPE , constant( "text/xml" ) ).
                        //to( "http4://localhost:8080/webservices/financeiro" );
                        to( "mock:soap" );//simulates an endpoint for testing

                from( "direct:http_post" )
                        .split().xpath( "/pedido/itens/item" )//splits the items
                        .filter().xpath( "/item/formato[text()='EBOOK']" ) //filter just ebooks inside the txml of the item

                        .marshal().xmljson()// converts de file from xml to json
                        .log( "${exchange.pattern}" ) //show the exchange pattern in action
                        .log( "${id} - ${body}" ) // just log on the console the id and body

                        //Endpoint Files Folder
                        //header for files changes the file extension getting the name with no extension and concating with .json
                        .setHeader( Exchange.FILE_NAME , simple( "${file:name.noext}-${header.CamelSplitIndex}.json" ) )
                        .to( "file:saida" )//sets the output folder

                        //Endpoint WS Rest POST
                        .setHeader( Exchange.HTTP_METHOD , HttpMethods.POST ) //header ws POST
                        .to( "http4://localhost:8080/webservices/ebook/item" ); //sending to ws

                from( "direct:http_get" ).
                        routeId( "rota-http" ).
                        setProperty( "pedidoId" , xpath( "/pedido/id/text()" ) ).
                        setProperty( "clienteId" , xpath( "/pedido/pagamento/email-titular/text()" ) ).
                        split().
                        xpath( "/pedido/itens/item" ).
                        filter().
                        xpath( "/item/formato[text()='EBOOK']" ).
                        setProperty( "ebookId" , xpath( "/item/livro/codigo/text()" ) ).
                        marshal().xmljson().
                        log( "${id} - ${body}" ).
                        setHeader( Exchange.HTTP_METHOD , HttpMethods.GET ).
                        setHeader( Exchange.HTTP_QUERY , simple(
                                           "ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}" ) ).
                        to( "http4://localhost:8080/webservices/ebook/item" );

            }
        } );
        context.start();
        Thread.sleep( 20000 );
        context.stop();
    }
}
