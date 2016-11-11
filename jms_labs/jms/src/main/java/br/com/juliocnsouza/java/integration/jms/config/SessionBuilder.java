package br.com.juliocnsouza.java.integration.jms.config;

import br.com.juliocnsouza.java.integration.jms.models.MessageProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author julio
 */
public class SessionBuilder {

    private final InitialContext context;
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Session session;
    private Destination destination;
    private boolean isDurable;

    public SessionBuilder() {
        this( null );
    }

    public SessionBuilder( String clientID ) {
        isDurable = clientID != null;
        try {
            context = new InitialContext( ContextProperties.get() );
            factory = ( ConnectionFactory ) context.lookup( "ConnectionFactory" );//lookup;
            connection = factory.createConnection();
            if ( isDurable ) {
                connection.setClientID( clientID );
            }
            session = connection.createSession( false , Session.AUTO_ACKNOWLEDGE );
        }
        catch ( NamingException |
                JMSException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );
            throw new RuntimeException( ex );
        }
    }

    public SessionBuilder createQueue( String queueName ) {
        try {
            initDestinationQueue( queueName );
        }
        catch ( JMSException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );
            throw new RuntimeException( ex );
        }

        return this;
    }

    public SessionBuilder createTopic( String topicName ) {
        try {
            initDestinationTopic( topicName );
        }
        catch ( JMSException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );
            throw new RuntimeException( ex );
        }

        return this;
    }

    private void initDestinationQueue( String queueName )
            throws JMSException {
        connection.start();
        destination = session.createQueue( queueName );
    }

    private void initDestinationTopic( String topicName )
            throws JMSException {
        connection.start();
        destination = session.createTopic( topicName );
    }

    public SessionBuilder consumingWith( MessageListener msgListener ) {
        consumingWith( msgListener , null );
        return this;
    }

    public SessionBuilder consumingWith( MessageListener msgListener , String messageSelector ) {
        try {
            if ( destination == null ) {
                throw new RuntimeException( "destination null" );
            }
            MessageConsumer consumer;
            if ( isDurable ) {
                if ( messageSelector != null ) {
                    consumer = session.createDurableSubscriber( ( Topic ) destination , "subscription" , messageSelector , false );
                }
                else {
                    consumer = session.createDurableSubscriber( ( Topic ) destination , "subscription" );
                }
            }
            else {
                consumer = session.createConsumer( destination );
            }

            consumer.setMessageListener( msgListener );
        }
        catch ( JMSException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );
            throw new RuntimeException( ex );
        }

        return this;
    }

    public SessionBuilder producingFor( String msg , MessageProperty... props ) {
        try {
            if ( destination == null ) {
                initDestinationQueue( "queue.default" );
            }
            MessageProducer producer = session.createProducer( destination );
            TextMessage txtMsg = session.createTextMessage( msg );
            if ( props != null ) {
                for ( MessageProperty prop : props ) {
                    prop.buildProperty( txtMsg );
                }
            }
            producer.send( txtMsg );
        }
        catch ( JMSException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );
            throw new RuntimeException( ex );
        }

        return this;
    }

    public void close() {
        try {
            session.close();
            connection.close();
            context.close();
        }
        catch ( JMSException |
                NamingException ex ) {
            Logger.getLogger( SessionBuilder.class.getName() ).log( Level.SEVERE , null , ex );

        }

    }

}
