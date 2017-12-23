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
package org.boiler.rdf_recursive_descent.compound;

import java.util.List;
import java.util.ArrayList;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.boiler.rdf_recursive_descent.*;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;
import org.boiler.rdf_recursive_descent.PredicateParserWithError;

/**
 *
 * @author Victor Porton
 */
public class OneOrMorePredicate<T> extends PredicateParserWithError<List<T>> {

    private final NodeParser<T> child;

    public OneOrMorePredicate(org.apache.jena.rdf.model.Property predicate,
                              NodeParser<T> child)
    {
        super(predicate);
        this.child = child;
    }

    public OneOrMorePredicate(org.apache.jena.rdf.model.Property predicate,
                              NodeParser<T> child,
                              ErrorHandler onError)
    {
        super(predicate, onError);
        this.child = child;
    }

    public NodeParser<T> getChild() {
        return child;
    }

    @Override
    public ParseResult<? extends List<T>>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        final NodeIterator iter = model.listObjectsOfProperty(node, getPredicate());
        ArrayList<T> result = new ArrayList<>();
        while(iter.hasNext()) {
            ParseResult<? extends T> elt = child.parse(context, model, (Resource)iter.next());
            if(elt.getSuccess())
                result.add(elt.getResult());
        }
        if(result.size() == 0) {
            org.boiler.util.StringCreator msg =
                () -> java.text.MessageFormat.format(
                        context.getLocalized("OneOrMorePredicate_error"),
                        getPredicate(), node);
            return context.raise(getErrorHandler(), msg);
        }
        return new ParseResult<ArrayList<T>>(result);
    };

}
