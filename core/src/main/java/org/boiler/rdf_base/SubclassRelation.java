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
package org.boiler.rdf_base;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.rdf.model.*;
import org.boiler.ExecutionContext;
import org.boiler.graph.*;

/**
 *
 * @author Victor Porton
 */
public class SubclassRelation extends org.boiler.graph.Connectivity<Resource> {

    private ExecutionContext context;

    private Property relation;

    public SubclassRelation(ExecutionContext context) {
        this.context  = context;
        this.relation = org.apache.jena.vocabulary.RDFS.subClassOf;
    }

    public SubclassRelation(ExecutionContext context, Model model) {
        this.context  = context;
        this.relation = org.apache.jena.vocabulary.RDFS.subClassOf;
        addModel(model);
    }

    public SubclassRelation(ExecutionContext context, Property relation) {
        this.context  = context;
        this.relation = relation;
    }

    public SubclassRelation(ExecutionContext context, Model model, Property relation) {
        this.context  = context;
        this.relation = relation;
        addModel(model);
    }

    public ExecutionContext getContext() {
        return context;
    }

    /*
     * Return false if there were errors.
     */
    public boolean addModel(Model model) {
        Graph<Resource> result = new Graph<Resource>();
        StmtIterator iter = model.listStatements(null,
                                                 relation,
                                                 (RDFNode)null);
        boolean wereErrors = false;
        while(iter.hasNext()) {
            Statement st = iter.next();
            Resource subject = st.getSubject();
            RDFNode object = st.getObject();
            if(object.isURIResource()) {
                if(checkTypes(model, subject, (Resource)object))
                    result.addEdge(subject, (Resource)object);
            } else {
                wereErrors = true;
                context.getLogger().log(
                        Level.WARNING,
                        java.text.MessageFormat.format(
                            context.getLocalized("ShouldBeIRI_error"),
                            st.getObject()));
            }
        }
        addGraph(result);
        return !wereErrors;
    }

    protected boolean checkTypes(Model model, Resource from, Resource to) {
        return true;
    }

}
