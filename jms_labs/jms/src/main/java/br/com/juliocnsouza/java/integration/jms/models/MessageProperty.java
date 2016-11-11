package br.com.juliocnsouza.java.integration.jms.models;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 *
 * @author julio
 */
public class MessageProperty {

    public enum Type {

        BOOLEAN,
        INT,
        BYTE,
        DOUBLE,
        LONG,
        FLOAT,
        SHORT,
        STRING,
        OBJECT
    }

    private final String name;

    private final Object value;

    private final Type type;

    public MessageProperty( String name , Object value , Type type ) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public void buildProperty( Message msg )
            throws JMSException {
        if ( msg == null || this.type == null ) {
            return;
        }
        switch ( this.type ) {
            case BOOLEAN:
                msg.setBooleanProperty( this.name , ( Boolean ) value );
                break;
            case BYTE:
                msg.setByteProperty( this.name , ( Byte ) value );
                break;
            case DOUBLE:
                msg.setDoubleProperty( this.name , ( Double ) value );
                break;
            case FLOAT:
                msg.setFloatProperty( this.name , ( Float ) value );
                break;
            case INT:
                msg.setIntProperty( this.name , ( Integer ) value );
                break;
            case LONG:
                msg.setLongProperty( this.name , ( Long ) value );
                break;
            case OBJECT:
                msg.setObjectProperty( this.name , value );
                break;
            case SHORT:
                msg.setShortProperty( this.name , ( Short ) value );
                break;
            case STRING:
                msg.setStringProperty( this.name , value.toString() );
                break;
        }
    }

}
