package br.com.juliocnsouza.java.integration.jms.models;

import com.google.gson.Gson;
import org.junit.Test;

/**
 *
 * @author julio
 */
public class PersonTest {

    public PersonTest() {
    }

    @Test
    public void testSomeMethod() {
        Person p = new Person();
        p.setAge( 30 );
        p.setName( "Charles Watson" );
        Gson g = new Gson();
        System.out.println( g.toJson( p ) );
    }

}
