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
 *
 * @author Victor Porton
 */
public class OnePredicate<T> extends PredicateParser<T> {
    
    private final NodeParser<T> child;
    
    public OnePredicate(org.apache.jena.rdf.model.Property predicate,
                        NodeParser<T> child) {
        super(predicate);
        this.child = child;
    }
    
    public OnePredicate(org.apache.jena.rdf.model.Property predicate,
                        NodeParser<T> child,
                        ErrorHandler onError) {
        super(predicate, onError);
        this.child = child;
    }
    
    public NodeParser<T> getChild() {
        return child;
    }
    
    @Override
    public ParseResult<? extends T> parse(ParseContext context,
                                org.apache.jena.rdf.model.Model model, 
                                org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        final NodeIterator iter = model.listObjectsOfProperty(node, getPredicate());
        // Not very efficient if iter.toList() > 1, but this does not happen usually:
        java.util.List<RDFNode> list = iter.toList();
        if(list.size() != 1) {
            final org.apache.jena.rdf.model.Resource node2 = node;
            final org.apache.jena.rdf.model.Resource predicate2 = getPredicate();
            org.boiler.util.StringCreator msg =
                () -> java.text.MessageFormat.format(
                        context.getMessages().getString("OnePredicate_error"),
                        predicate2, node2);
            return context.raise(getErrorHandler(), msg);
        }
        return child.parse(context, model, (Resource)list.get(0));
    };
    
}
