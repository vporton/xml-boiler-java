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
package org.boiler.global;

import org.apache.jena.rdf.model.*;
import org.boiler.graph.*;

/**
 *
 * @author Victor Porton
 */
public class SubclassRelationLoader {

    static org.boiler.graph.AbstractGraph<Resource> loadSubclassGraph() {
        Graph<Resource> result = new Graph<Resource>();
        Model model = GlobalRDFLoader.read("/org/boiler/subclasses.ttl");
        Property predicate = ResourceFactory.createProperty(
                "http://www.w3.org/2000/01/rdf-schema#subClassOf");
        StmtIterator iter = model.listStatements(null, predicate, (RDFNode)null);
        while(iter.hasNext()) {
            Statement st = iter.next();
            // Let conversion exception on incorrect data
            result.addEdge(st.getSubject(), (Resource)st.getObject());
        }
        return result;
    }

}
