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

import java.util.List;
import com.google.inject.name.Named;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.compound.OnePredicate;
import org.boiler.rdf_recursive_descent.compound.ZeroOnePredicate;
import org.boiler.rdf_recursive_descent.compound.ZeroOrMorePredicate;
import org.boiler.rdf_recursive_descent.compound.OneOrMorePredicate;
import org.boiler.rdf_recursive_descent.type.CheckNodeClass;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;
import org.boiler.rdf_recursive_descent.PredicateParser;
import org.boiler.rdf_recursive_descent.literal.IRILiteral;
import org.boiler.rdf_recursive_descent.literal.BooleanLiteral;

/**
 *
 * @author Victor Porton
 */
public class TransformerParser extends NodeParser<Asset.Transformer> {

    private final org.boiler.SubclassRelation subclasses;

    public TransformerParser(org.boiler.SubclassRelation subclasses) {
        this.subclasses = subclasses;
    }

    @Override
    public ParseResult<? extends Asset.Transformer>
    parse(ParseContext context, Model model, Resource node) throws FatalParseError {
        Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Transformer");
        if(!CheckNodeClass.check(subclasses, context, model, node, klass, ErrorHandler.IGNORE))
            return new ParseResult<>();

        Asset.Transformer result = new Asset.Transformer();

        Property sourceNamespaceProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "sourceNamespace");
        NodeParser<Resource> sourceNamespacesNodeParser = new IRILiteral(ErrorHandler.WARNING);
        OneOrMorePredicate<Resource> sourceNamespacesParser =
                new OneOrMorePredicate<Resource>(sourceNamespaceProperty,
                                                 sourceNamespacesNodeParser,
                                                 ErrorHandler.WARNING);
        ParseResult<? extends List<Resource>> sourceNamespaces =
                sourceNamespacesParser.parse(context, model, node);
        if(!sourceNamespaces.getSuccess()) return new ParseResult<>();
        result.sourceNamespaces = new java.util.HashSet<Resource>(sourceNamespaces.getResult());

        Property targetNamespaceProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "targetNamespace");
        NodeParser<Resource> targetNamespacesNodeParser = new IRILiteral(ErrorHandler.WARNING);
        OneOrMorePredicate<Resource> targetNamespacesParser =
                new OneOrMorePredicate<Resource>(targetNamespaceProperty,
                                                 targetNamespacesNodeParser,
                                                 ErrorHandler.WARNING);
        ParseResult<? extends List<Resource>> targetNamespaces =
                targetNamespacesParser.parse(context, model, node);
        if(!targetNamespaces.getSuccess()) return new ParseResult<>();
        result.targetNamespaces = new java.util.HashSet<Resource>(targetNamespaces.getResult());

        Property precedenceProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "precedence");
        NodeParser<Resource> precedenceNodeParser = new IRILiteral(ErrorHandler.WARNING);
        OnePredicate<Resource> precedenceParser =
                new OnePredicate<>(precedenceProperty, precedenceNodeParser, ErrorHandler.WARNING);
        ParseResult<? extends Resource> precedence = precedenceParser.parse(context, model, node);
        if(!precedence.getSuccess()) return new ParseResult<>();
        result.precedence = precedence.getResult();

        Property ignoreTargetProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "ignoreTarget");
        NodeParser<Boolean> ignoreTargetNodeParser = new BooleanLiteral(ErrorHandler.WARNING);
        PredicateParser<Boolean> ignoreTargetParser =
                new ZeroOnePredicate<Boolean>(ignoreTargetProperty, ignoreTargetNodeParser, ErrorHandler.WARNING);
        ParseResult<? extends Boolean> ignoreTarget = ignoreTargetParser.parse(context, model, node);
        if(!ignoreTarget.getSuccess()) return new ParseResult<>();
        result.ignoreTarget = ignoreTarget.getResult();

        Property scriptProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "script");
        NodeParser<Asset.ScriptInfo> scriptNodeParser =
                new ScriptInfoParser(subclasses, Asset.ScriptKindEnum.TRANSFORMER);
        OneOrMorePredicate<Asset.ScriptInfo> scriptParser =
                new OneOrMorePredicate<>(scriptProperty, scriptNodeParser, ErrorHandler.WARNING);
        result.scripts = scriptParser.parse(context, model, node).getResult();

        return new ParseResult<>(result);
    }

}
