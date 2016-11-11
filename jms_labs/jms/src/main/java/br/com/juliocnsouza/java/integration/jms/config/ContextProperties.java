package br.com.juliocnsouza.java.integration.jms.config;

import java.util.Properties;
import javax.naming.Context;

/**
 *
 * @author julio
 */
public class ContextProperties {

    private static final Properties props = new Properties();

    public static Properties get() {
        props.setProperty( Context.INITIAL_CONTEXT_FACTORY , "org.apache.activemq.jndi.ActiveMQInitialContextFactory" );
        props.setProperty( Context.PROVIDER_URL , "tcp://localhost:61616" );
        return props;
    }

}
