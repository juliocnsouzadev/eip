package br.com.juliocnsouza.java.integration.jms.testing.queue;

import br.com.juliocnsouza.java.integration.jms.config.SessionBuilder;
import br.com.juliocnsouza.java.integration.jms.listeners.ThirdPartyMessageListener;
import java.util.Scanner;

/**
 *
 * @author julio
 */
public class TestConsumerQueue {

    public static void main( String[] args ) {

        SessionBuilder sessionBuilder = new SessionBuilder()
                .createQueue( "queue.thirdparty" )
                .consumingWith( new ThirdPartyMessageListener() );

        new Scanner( System.in ).nextLine();

        sessionBuilder.close();
    }

}
