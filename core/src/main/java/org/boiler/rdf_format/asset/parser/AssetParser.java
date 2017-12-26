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
package org.boiler.rdf_format.asset.parser;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import static org.apache.jena.vocabulary.RDFS.seeAlso;
import org.boiler.graph.AbstractGraph;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.type.ClassForestParser;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.ParseContext;

/**
 *
 * @author Victor Porton
 */
public class AssetParser {

    private AbstractGraph<Resource> subclasses;

    private AssetParser(@Named("subclasses") AbstractGraph<Resource> subclasses) {
        this.subclasses = subclasses;
    }

    @Inject
    public Asset.AssetInfo parse(Model model) throws FatalParseError {
        Asset.AssetInfo result = new Asset.AssetInfo();

        ParseContext context = new ParseContext();

        Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Transformer");
        ClassForestParser<Asset.Transformer> transformerParser =
                new ClassForestParser<Asset.Transformer>(
                        new TransformerParser(subclasses), klass, subclasses);
        result.transformers = transformerParser.parse(context, model).getResult();

        result.seeAlsoTransform = scanSeeAlso(context, model, MAIN_NAMESPACE + "transform");
        result.seeAlsoValidate  = scanSeeAlso(context, model, MAIN_NAMESPACE + "validate" );

        return result;
    }

    private List<Resource> scanSeeAlso(ParseContext context, Model model, String kindURI)
            throws FatalParseError
    {
        Resource kind = ResourceFactory.createResource(kindURI);
        List<? super Resource> seeAlsoNodes =
                model.listObjectsOfProperty(kind, seeAlso).toList();
        if(seeAlsoNodes.size() > 1) {
            String str = java.text.MessageFormat.format(
                    context.getLocalized("Asset_multiple_seeAlso"),
                    model);
            context.getLogger().log(Level.SEVERE, str);
            throw new FatalParseError(str);
        }
        Resource seeAlsoHead = (Resource)seeAlsoNodes.get(0);
        // TODO: Separate the below into a special class
        RDFList list = seeAlsoHead.as(RDFList.class);
        if(!list.isValid()) {
            String str = java.text.MessageFormat.format(
                    context.getLocalized("RDFListError"),
                    seeAlsoHead);
            context.getLogger().log(Level.SEVERE, str);
            throw new FatalParseError(str);
        }
        Iterator<RDFNode> iter = list.iterator();
        ArrayList<Resource> result = new ArrayList<Resource>();
        while(iter.hasNext()) {
            try {
                result.add((Resource)iter.next());
            }
            catch(ClassCastException e) {
                String str = java.text.MessageFormat.format(
                        context.getLocalized("RDFListError"),
                        seeAlsoHead);
                context.getLogger().log(Level.SEVERE, str);
                throw new FatalParseError(str);
            }
        }
        return result;
    }

}
