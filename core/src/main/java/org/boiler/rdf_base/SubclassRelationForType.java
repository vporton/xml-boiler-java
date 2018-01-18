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
package org.boiler.rdf_base;

import java.util.logging.Level;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.boiler.ExecutionContext;

/**
 * TODO: Use this class to read precedences info.
 *
 * @author Victor Porton
 */
public class SubclassRelationForType extends SubclassRelation {

    Resource nodeClass;

    public SubclassRelationForType(ExecutionContext context, Resource nodeClass) {
        super(context);
        this.nodeClass = nodeClass;
    }

    public SubclassRelationForType(ExecutionContext context, Model model, Resource nodeClass) {
        super(context, model);
        this.nodeClass = nodeClass;
    }

    public SubclassRelationForType(ExecutionContext context, Property relation, Resource nodeClass) {
        super(context, relation);
        this.nodeClass = nodeClass;
    }

    public SubclassRelationForType(ExecutionContext context, Model model, Property relation, Resource nodeClass) {
        super(context, model, relation);
        this.nodeClass = nodeClass;
    }

    @Override
    protected boolean checkTypes(Model model, Resource from, Resource to) {
        boolean fromOK = model.contains(from, org.apache.jena.vocabulary.RDF.type, nodeClass);
        boolean toOK   = model.contains(to  , org.apache.jena.vocabulary.RDF.type, nodeClass);
        if(fromOK ^ toOK)
            getContext().getLogger().log(
                    Level.WARNING,
                    java.text.MessageFormat.format(
                            getContext().getLocalized("BothOperands_error"),
                            nodeClass));
        return fromOK && toOK;
    }

}
