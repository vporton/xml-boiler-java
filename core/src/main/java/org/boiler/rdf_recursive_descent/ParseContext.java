/*
 *  Copyright (c) 2017 Victor Porton,
 *  XML Boiler - http://freesoft.portonvictor.org
 *
 *  This file is part of XML Boiler.
 *
 *  XML Boiler is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.boiler.rdf_recursive_descent;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Locale;

/**
 *
 * @author Victor Porton
 */
public class ParseContext {
    
    private Logger logger;
    
    private java.util.ResourceBundle messages;
    
    public ParseContext() {
        this(new Locale("en", "US"));
    }
    
    public ParseContext(java.util.Locale locale) {
        messages = java.util.ResourceBundle.getBundle("Messages", locale);
    }
    
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public java.util.ResourceBundle getMessages() {
        return messages;
    }
    
    public <T> ParseResult<? extends T>
        raise(ErrorHandler handler, org.boiler.util.StringCreator str)
                throws FatalParseError {
        switch(handler) {
            case IGNORE:
                return new ParseResult<T>();
            case WARNING:
                if(logger != null)
                    logger.log(Level.WARNING, str.create());
                return new ParseResult<T>();
            case FATAL:
                final String message = str.create();
                if(logger != null)
                    logger.log(Level.SEVERE, message);
                throw new FatalParseError(message);
        }
        return null; // avoid "missing return statement" warning
    }

    public <T> ParseResult<? extends T> raise(ErrorHandler handler, String message)
            throws FatalParseError
    {
        return raise(handler, ()->message);
    }
    
}
