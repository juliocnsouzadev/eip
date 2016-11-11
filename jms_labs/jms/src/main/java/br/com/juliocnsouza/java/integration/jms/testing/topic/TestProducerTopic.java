package br.com.juliocnsouza.java.integration.jms.testing.topic;

import br.com.juliocnsouza.java.integration.jms.config.SessionBuilder;
import br.com.juliocnsouza.java.integration.jms.models.MessageProperty;
import br.com.juliocnsouza.java.integration.jms.models.Person;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author julio
 */
public class TestProducerTopic {

    public static void main( String[] args ) {

        SessionBuilder sessionBuilder = new SessionBuilder()
                .createTopic( "topic.thirdpartyselector" );
        Gson gson = new Gson();
        Random r = new Random();

        for ( int i = 0 ; i < 10 ; i++ ) {
            Person p = new Person();
            int nextInt = r.nextInt( 10 );
            p.setAge( i * ( nextInt + i ) );
            String nameBase = getNames().get( i ) + " " + getNames().get( nextInt );
            String[] split = nameBase.split( " " );
            String name = split[0] + " " + split[split.length - 1] + " " + split[1];
            p.setName( name );

            sessionBuilder.producingFor( gson.toJson( p ) , new MessageProperty( "sync" , Boolean.FALSE , MessageProperty.Type.BOOLEAN ) );
        }
        System.out.println( "press Enter to finish..." );
        new Scanner( System.in ).nextLine();

        sessionBuilder.close();
    }

    private static List<String> getNames() {
        return Arrays.asList( "Joao Silva" , "Maria Soares" , "Josefina Dias" , "Carlos Ferreira" , "Cleiton Carvalho" ,
                              "Mara Pinto" , "Jose Souza" , "Milton Magalhaes" , "Mila Smith" , "Marta Pires" );
    }

}
