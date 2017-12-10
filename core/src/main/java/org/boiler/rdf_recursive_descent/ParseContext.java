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

import java.util.logging.Level;

/**
 *
 * @author Victor Porton
 */
public class ParseContext {
    
    private java.util.logging.Logger logger;
    
    public java.util.logging.Logger getLogger() {
        return logger;
    }

    public void setLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    <T> ParseResult<T> raise(ErrorHandler handler, org.boiler.util.StringCreator str)
            throws FatalParseError {
        switch(handler) {
            case IGNORE:
                return new ParseResult();
            case WARNING:
                if(logger != null)
                    logger.log(Level.WARNING, str.create());
                return new ParseResult();
            case FATAL:
                final String message = str.create();
                if(logger != null)
                    logger.log(Level.SEVERE, message);
                throw new FatalParseError(message);
        }
        return null; // avoid "missing return statement" warning
    }

    <T> ParseResult<T> raise(ErrorHandler handler, String message) throws FatalParseError {
        return raise(handler, ()->message);
    }
    
}
