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
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.graph.AbstractGraph;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.type.ClassForestParser;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;
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
    public Asset.AssetInfo parse(org.apache.jena.rdf.model.Model model) throws FatalParseError {
        Asset.AssetInfo result = new Asset.AssetInfo();

        ParseContext context = new ParseContext();

        Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Transformer");
        ClassForestParser<Asset.Transformer> transformerParser =
                new ClassForestParser<Asset.Transformer>(
                        new TransformerParser(subclasses), klass, subclasses);
        result.transformers = transformerParser.parse(context, model).getResult();

        // TODO: seeAlso

        return result;
    }

}
