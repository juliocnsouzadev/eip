package br.com.juliocnsouza.java.integration.jms.listeners;

import br.com.juliocnsouza.java.integration.jms.models.Person;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author julio
 */
public class ThirdPartyMessageListener implements MessageListener {

    @Override
    public void onMessage( Message msg ) {
        //do something with the message
        try {
            TextMessage txtMsg = ( TextMessage ) msg;
            String text = txtMsg.getText();
            Gson gson = new Gson();
            Person person = gson.fromJson( text , Person.class );
            if ( person != null ) {
                System.out.println( "Receive a person named " + person.getName() + " and with the age of " + person.getAge() );
            }
            else {
                System.out.println( "No person converted from: " + text );
            }
        }
        catch ( JMSException |
                JsonSyntaxException e ) {
            System.out.println( "Something went wrong... Exception: "
                    + e.getClass().getSimpleName()
                    + ", msg: " + e.getMessage() );
        }

    }

}
