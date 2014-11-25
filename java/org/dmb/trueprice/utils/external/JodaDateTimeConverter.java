/*
    This class is free provided by 
        https://www.blogger.com/profile/17534925504846430857
    The purpose of this class is to handle the convertion of dates from JodaTime lib 
    to be used with EclipseLink.
    
    It can be downloaded here :     
        http://jcodehelp.blogspot.be/2011/12/persist-joda-datetime-with-eclipselink.html

    This is not needed with Hibernate JPA's implementation
*/
package org.dmb.trueprice.utils.external;

import java.sql.Timestamp;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;

public class JodaDateTimeConverter implements Converter {

    private static final long serialVersionUID = 1L;

    @Override
    public Object convertDataValueToObjectValue( Object dataValue, Session session ) {
        return dataValue == null ? null : new DateTime( (Timestamp) dataValue );
    }

    @Override
    public Object convertObjectValueToDataValue( Object objectValue, Session session ) {
        return objectValue == null ? null : new Timestamp( ( (DateTime) objectValue ).getMillis() );
    }

    @Override
    public void initialize( DatabaseMapping mapping, Session session ) {
    }

    @Override
    public boolean isMutable() {
        return false;
    }

}