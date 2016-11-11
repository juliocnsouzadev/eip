package br.com.juliocnsouza.java.integration.jms.testing.topic;

import br.com.juliocnsouza.java.integration.jms.config.SessionBuilder;
import br.com.juliocnsouza.java.integration.jms.listeners.ThirdPartyMessageListener;
import java.util.Scanner;

/**
 *
 * @author julio
 */
public class TestConsumerTopic01 {

    public static void main( String[] args ) {

        SessionBuilder builder = new SessionBuilder( TestConsumerTopic01.class.getSimpleName() )
                .createTopic( "topic.thirdparty" )
                .consumingWith( new ThirdPartyMessageListener() );

        new Scanner( System.in ).nextLine();

        builder.close();
    }

}
