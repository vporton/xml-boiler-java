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

import java.util.logging.Level;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.rdf_recursive_descent.*;

/**
 *
 * @author Victor Porton
 */
public class CheckNodeClass {

    public static boolean check(org.boiler.graph.AbstractGraph<Resource> graph,
                                ParseContext context,
                                org.apache.jena.rdf.model.Model model,
                                org.apache.jena.rdf.model.Resource node,
                                org.apache.jena.rdf.model.Resource klass,
                                ErrorHandler handler)
            throws FatalParseError
    {
        final Property property = org.apache.jena.vocabulary.RDF.type;
        final NodeIterator iter = model.listObjectsOfProperty(node, property);
        while(iter.hasNext()) {
            final RDFNode klass2 = iter.next();
            if(klass2.isResource() && graph.connected(klass2.asResource(), klass))
                return true;
        }
        final java.util.logging.Logger logger = context.getLogger();
        org.boiler.util.StringCreator msg =
            () -> java.text.MessageFormat.format(
                    context.getLocalized("RDFType_error"),
                    node, klass);
        switch(handler) {
            case WARNING:
                if(logger != null)
                    logger.log(Level.WARNING, msg.create());
                break;
            case FATAL:
                final String message = msg.create();
                if(logger != null)
                    logger.log(Level.SEVERE, message);
                throw new FatalParseError(message);
        }
        return false;
    }

}
