package br.com.juliocnsouza.java.integration.jms.testing.queue;

import br.com.juliocnsouza.java.integration.jms.config.SessionBuilder;
import br.com.juliocnsouza.java.integration.jms.models.Person;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author julio
 */
public class TestProducerQueue {

    public static void main( String[] args ) {

        SessionBuilder sessionBuilder = new SessionBuilder()
                .createQueue( "queue.thirdparty" );
        Gson gson = new Gson();

        for ( int i = 0 ; i < 10 ; i++ ) {
            Person p = new Person();
            p.setAge( i * 3 );
            p.setName( getNames().get( i ) );

            sessionBuilder.producingFor( gson.toJson( p ) );
        }

        new Scanner( System.in ).nextLine();

        sessionBuilder.close();
    }

    private static List<String> getNames() {
        return Arrays.asList( "Joao Silva" , "Maria Soares" , "Josefina Dias" , "Carlos Ferreira" , "Cleiton Carvalho" ,
                              "Mara Silva" , "Jose Soares" , "Milton Dias" , "Mila Ferreira" , "Marta Carvalho" );
    }

}
