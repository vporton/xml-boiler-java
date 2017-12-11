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

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.boiler.rdf_recursive_descent.*;

/**
 * This class implements a special case of defaultValue
 * (only for the case of max one predicate).
 * I do not implement more general case (in another class)
 * because I not found concrete uses for it.
 *
 * @author Victor Porton
 */
public class ZeroOnePredicate<T> extends PredicateParser<T> {

    private final NodeParser<T> child;

    private final T defaultValue;

    public ZeroOnePredicate(org.apache.jena.rdf.model.Property predicate,
                            NodeParser<T> child)
    {
        super(predicate);
        this.child = child;
        this.defaultValue = null;
    }

    public ZeroOnePredicate(org.apache.jena.rdf.model.Property predicate,
                            NodeParser<T> child,
                            ErrorHandler onError)
    {
        super(predicate, onError);
        this.child = child;
        this.defaultValue = null;
    }

    public ZeroOnePredicate(org.apache.jena.rdf.model.Property predicate,
                            NodeParser<T> child,
                            T defaultValue)
    {
        super(predicate);
        this.child = child;
        this.defaultValue = defaultValue;
    }

    public ZeroOnePredicate(org.apache.jena.rdf.model.Property predicate,
                            NodeParser<T> child,
                            T defaultValue,
                            ErrorHandler onError)
    {
        super(predicate, onError);
        this.child = child;
        this.defaultValue = defaultValue;
    }

    public NodeParser<T> getChild() {
        return child;
    }

    @Override
    public ParseResult<? extends T>
        parse(ParseContext context,
              org.apache.jena.rdf.model.Model model,
              org.apache.jena.rdf.model.Resource node)
                throws FatalParseError
    {
        final NodeIterator iter = model.listObjectsOfProperty(node, getPredicate());
        // Not very efficient if iter.toList() > 1, but this does not happen usually:
        java.util.List<RDFNode> list = iter.toList();
        if(list.isEmpty())
            return new ParseResult<T>(defaultValue);
        if(list.size() > 1) {
            org.boiler.util.StringCreator str =
                () -> java.text.MessageFormat.format(
                        context.getLocalized("ZeroOnePredicate_error"),
                        getPredicate(), node);
            return context.raise(getErrorHandler(), str);
        }
        return child.parse(context, model, (Resource)list.get(0));
    };

}
