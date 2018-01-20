/*
 *  Copyright (c) 2018 Victor Porton,
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_recursive_descent.NodeParserWithError;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;

/**
 *
 * @author Victor Porton
 */
public class ListCollection<T> extends NodeParserWithError<ArrayList<T>> {

    final NodeParser subparser;

    public ListCollection(NodeParser subparser) {
        this.subparser = subparser;
    }

    @Override
    public ParseResult<? extends ArrayList<T>>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        RDFList list = node.as(RDFList.class);
        if(!list.isValid()) {
            String str = java.text.MessageFormat.format(
                    context.getLocalized("RDFListError"),
                    node);
            context.getLogger().log(Level.SEVERE, str);
            throw new FatalParseError(str);
        }
        Iterator<RDFNode> iter = list.iterator();
        ArrayList<T> result = new ArrayList<T>();
        while(iter.hasNext()) {
            RDFNode subnode = iter.next();
            if(!subnode.isURIResource()) {
                String str = java.text.MessageFormat.format(
                        context.getLocalized("seeAlsoResource"),
                        node);
                context.getLogger().log(Level.SEVERE, str);
                throw new FatalParseError(str);
            }
            ParseResult<T> parsed = subparser.parse(context, model, (Resource)subnode);
            if(!parsed.getSuccess()) return new ParseResult<>();
            result.add(parsed.getResult());
        }
        return new ParseResult<ArrayList<T>>(result);
    }

}
