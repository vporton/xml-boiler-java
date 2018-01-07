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
package org.boiler;

import org.jgrapht.alg.ConnectivityInspector;
import org.apache.jena.rdf.model.*;
import org.boiler.global.GlobalRDFLoader;

/**
 *
 * @author Victor Porton
 */
public class SubclassRelation extends ConnectivityInspector<Resource, Void> {

    private SubclassRelation(org.jgrapht.Graph<Resource, Void> base) {
        super(base);
    }

    // TODO: http://jgrapht.org/javadoc/org/jgrapht/alg/ConnectivityInspector.html
    // says "The inspected graph is specified at construction time and cannot be modified."
    // It is inefficient as we may add new precedences to the inspected graph
    // multiple times.
    public static org.boiler.SubclassRelation calculateSubclassGraph(Model model) {
        org.jgrapht.Graph<Resource, Void> result =
                new org.jgrapht.graph.DefaultDirectedGraph<Resource, Void>(Void.class);
        StmtIterator iter = model.listStatements(null,
                                                 org.apache.jena.vocabulary.RDFS.subClassOf,
                                                 (RDFNode)null);
        while(iter.hasNext()) {
            Statement st = iter.next();
            // FIXME: conversion exception on incorrect data
            result.addEdge(st.getSubject(), (Resource)st.getObject());
        }
        return new org.boiler.SubclassRelation(result);
    }

    public static org.boiler.SubclassRelation loadSubclassGraph() {
        Model model = GlobalRDFLoader.read("/org/boiler/subclasses.ttl");
        return calculateSubclassGraph(model);
    }

}