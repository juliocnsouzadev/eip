package br.com.juliocnsouza.java.integration.jms.testing.topic.selector;

import br.com.juliocnsouza.java.integration.jms.config.SessionBuilder;
import br.com.juliocnsouza.java.integration.jms.listeners.ThirdPartyMessageListener;
import java.util.Scanner;

/**
 *
 * @author julio
 */
public class TestConsumerTopicSelector {

    public static void main( String[] args ) {

        SessionBuilder builder = new SessionBuilder( TestConsumerTopicSelector.class.getSimpleName() )
                .createTopic( "topic.thirdpartyselector" )
                .consumingWith( new ThirdPartyMessageListener() , "sync=false" );

        new Scanner( System.in ).nextLine();

        builder.close();
    }

}
