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

import org.apache.jena.rdf.model.*;
import org.boiler.global.GlobalRDFLoader;
import org.boiler.graph.*;

/**
 *
 * @author Victor Porton
 */
public class SubclassRelation {

    private Graph<Resource> connectivity = new Graph<Resource>();

    public SubclassRelation() { }

    private SubclassRelation(Graph<Resource> graph) {
        addGraph(graph);
    }

    private SubclassRelation(Model model) {
        addModel(model);
    }

    public Graph<Resource> getConnectivity() {
        return connectivity;
    }

    public void addGraph(Graph<Resource> graph) {
        connectivity = TransitiveClosure.transitiveClosure(AsSet.union(connectivity, graph));
    }

    public void addModel(Model model) {
        Graph<Resource> result = new Graph<Resource>();
        StmtIterator iter = model.listStatements(null,
                                                 org.apache.jena.vocabulary.RDFS.subClassOf,
                                                 (RDFNode)null);
        while(iter.hasNext()) {
            Statement st = iter.next();
            // FIXME: conversion exception on incorrect data
            result.addEdge(st.getSubject(), (Resource)st.getObject());
        }
        addGraph(result);
    }

    public static org.boiler.SubclassRelation loadSubclassGraph() {
        Model model = GlobalRDFLoader.read("/org/boiler/subclasses.ttl");
        return new SubclassRelation(model);
    }

}
