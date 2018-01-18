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
package org.boiler.rdf_format.asset.parser;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.*;
import org.boiler.rdf_recursive_descent.compound.*;
import org.boiler.rdf_recursive_descent.type.CheckNodeClass;

/**
 *
 * @author Victor Porton
 */
public class NSParser extends NodeParser<Asset.Namespace> {

    private final org.boiler.rdf_base.SubclassRelation subclasses;

    public NSParser(org.boiler.rdf_base.SubclassRelation subclasses) {
        this.subclasses = subclasses;
    }

    @Override
    public ParseResult<? extends Asset.Namespace>
    parse(ParseContext context, Model model, Resource node) throws FatalParseError {
        Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Namespace");
        if(!CheckNodeClass.check(subclasses, context, model, node, klass, ErrorHandler.IGNORE))
            return new ParseResult<>();

        Asset.Namespace result = new Asset.Namespace();

        Property scriptProperty = ResourceFactory.createProperty(MAIN_NAMESPACE + "script");
        NodeParser<Asset.ScriptInfo> scriptNodeParser =
                new ScriptInfoParser(subclasses, Asset.ScriptKindEnum.TRANSFORMER);
        OneOrMorePredicate<Asset.ScriptInfo> scriptParser =
                new OneOrMorePredicate<>(scriptProperty, scriptNodeParser, ErrorHandler.WARNING);
        result.validators = scriptParser.parse(context, model, node).getResult();

        return new ParseResult<>(result);
    }

}
