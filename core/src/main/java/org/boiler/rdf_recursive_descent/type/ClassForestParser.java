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
package org.boiler.rdf_recursive_descent.type;

import java.util.List;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.boiler.rdf_recursive_descent.*;

/**
 *
 * @author Victor Porton
 */
public class ClassForestParser<T> {

    final NodeParser<T> nodeParser;

    final Resource klass;

    final org.boiler.graph.AbstractGraph<Resource> subclassesGraph;

    public ClassForestParser(NodeParser<T> nodeParser,
                             Resource klass,
                             org.boiler.graph.AbstractGraph<Resource> subclassesGraph)
    {
        this.nodeParser = nodeParser;
        this.klass = klass;
        this.subclassesGraph = subclassesGraph;
    }

    public ParseResult<List<? extends T>>
        parse(ParseContext context, org.apache.jena.rdf.model.Model model)
                throws FatalParseError
    {
        java.util.ArrayList<T> result = new java.util.ArrayList<T>();
        // TODO: Not the fastest algorithm
        StmtIterator iter = model.listStatements(null,
                                                 org.apache.jena.vocabulary.RDF.type,
                                                 (RDFNode)null);
        while(iter.hasNext()) {
            final Statement st = iter.next();
            final Resource node = st.getSubject();
            if(CheckNodeClass.check(subclassesGraph, context, model, node, klass, ErrorHandler.IGNORE)) {
                ParseResult<? extends T> subResult = nodeParser.parse(context, model, node);
                if(subResult.getSuccess()) result.add(subResult.getResult());
            }
        }
        return new ParseResult<java.util.List<? extends T>>(result);
    }

}
