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
import org.boiler.ExecutionContext;
import org.boiler.rdf_base.SubclassRelation;
import org.boiler.rdf_base.SubclassRelationForType;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.type.ClassForestParser;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.compound.ListCollection;
import org.boiler.rdf_recursive_descent.literal.IRILiteral;

/**
 *
 * @author Victor Porton
 */
public class AssetParser {

    private final org.boiler.rdf_base.HardcodedSubclasses subclasses;

    @Inject
    private AssetParser(org.boiler.rdf_base.HardcodedSubclasses subclasses) {
        this.subclasses = subclasses;
    }

    @Inject
    public Asset.AssetInfo parse(Model model) throws FatalParseError {
        Asset.AssetInfo result = new Asset.AssetInfo();

        ParseContext context = new ParseContext();

        Resource transformerKlass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Transformer");
        ClassForestParser<Asset.Transformer> transformerParser =
                new ClassForestParser<Asset.Transformer>(
                        new TransformerParser(subclasses), transformerKlass, subclasses);
        result.transformers = transformerParser.parse(context, model).getResult();

        Resource nsKlass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Namespace");
        ClassForestParser<Asset.Namespace> nsParser =
                new ClassForestParser<Asset.Namespace>(
                        new NSParser(subclasses), nsKlass, subclasses);
        result.namespaces = nsParser.parse(context, model).getResult();

        result.seeAlsoTransform = scanSeeAlso(context, model, MAIN_NAMESPACE + "transform");
        result.seeAlsoValidate  = scanSeeAlso(context, model, MAIN_NAMESPACE + "validate" );

        Resource precedencesClass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Precedence");
        result.precedencesSubclasses =
                new SubclassRelationForType(context, model, org.apache.jena.vocabulary.RDF.type, precedencesClass);
        Property isHigherProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "higherThan");
        result.precedencesHigher =
                new SubclassRelationForType(context, model, isHigherProperty, precedencesClass);

        return result;
    }

    private List<Resource> scanSeeAlso(ParseContext context, Model model, String kindURI)
            throws FatalParseError
    {
        Resource kind = ResourceFactory.createResource(kindURI);
        List<? super Resource> seeAlsoNodes =
                model.listObjectsOfProperty(kind, seeAlso).toList();
        // Can be simplified using OnePredicate class
        if(seeAlsoNodes.size() > 1) {
            String str = java.text.MessageFormat.format(
                    context.getLocalized("Asset_multiple_seeAlso"),
                    model);
            context.getLogger().log(Level.SEVERE, str);
            throw new FatalParseError(str);
        }
        Resource seeAlsoHead = (Resource)seeAlsoNodes.get(0);
        ListCollection<Resource> parser = new ListCollection<>(new IRILiteral());
        return parser.parse(context, model, seeAlsoHead).getResult();
    }

}
