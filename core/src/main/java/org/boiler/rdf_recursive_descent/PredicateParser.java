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

/**
 *
 * @author Victor Porton
 */
public abstract class PredicateParser<T> {
    
    private final org.apache.jena.rdf.model.Property predicate;
    
    private final ErrorHandler onError; // TODO: Maybe move onError field into concrete derived types? (because it is not used everywhere)

    public PredicateParser(org.apache.jena.rdf.model.Property predicate) {
        this.predicate = predicate;
        this.onError = ErrorHandler.IGNORE;
    }
    
    public PredicateParser(org.apache.jena.rdf.model.Property predicate,
                           ErrorHandler onError) {
        this.predicate = predicate;
        this.onError = onError;
    }
    
    public final org.apache.jena.rdf.model.Property getPredicate() {
        return predicate;
    }
    
    public final ErrorHandler getErrorHandler() {
        return onError;
    }
    
    public abstract ParseResult<? extends T> parse(ParseContext context,
                                         org.apache.jena.rdf.model.Model model,
                                         org.apache.jena.rdf.model.Resource node)
                throws FatalParseError;

}
