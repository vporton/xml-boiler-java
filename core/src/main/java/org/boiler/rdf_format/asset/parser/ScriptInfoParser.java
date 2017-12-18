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

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.rdf_format.Base;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_format.asset.*;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;
import org.boiler.rdf_recursive_descent.compound.ZeroOnePredicate;
import org.boiler.rdf_recursive_descent.literal.DoubleLiteral;

/**
 *
 * @author Victor Porton
 */
class ScriptInfoParser extends NodeParser<Asset.ScriptInfo> {

    private Asset.ScriptKindEnum scriptKind;

    private ScriptInfoParser(Asset.ScriptKindEnum scriptKind) {
        this.scriptKind = scriptKind;
    }

    @Override
    public ParseResult<? extends Asset.ScriptInfo>
    parse(ParseContext context, Model model, Resource node) throws FatalParseError {
        // TODO
    }

    private class BaseScriptInfoParser extends NodeParser<Asset.ScriptInfo> {

        @Override
        public ParseResult<? extends Asset.ScriptInfo>
        parse(ParseContext context, Model model, Resource node) throws FatalParseError {
            Asset.ScriptInfo result = new Asset.ScriptInfo();
            // TODO: Check 0..1 range
            DoubleLiteral doubleParser = new DoubleLiteral(ErrorHandler.WARNING);
            ZeroOnePredicate<Double> preservanceParser =
                    new ZeroOnePredicate<Double>(
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "preservance"),
                            doubleParser,
                            1.0d,
                            ErrorHandler.WARNING);
            ZeroOnePredicate<Double> stabilityParser =
                    new ZeroOnePredicate<Double>(
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "stability"),
                            doubleParser,
                            1.0d,
                            ErrorHandler.WARNING);
            ZeroOnePredicate<Double> preferenceParser =
                    new ZeroOnePredicate<Double>(
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "prefernce"),
                            doubleParser,
                            1.0d,
                            ErrorHandler.WARNING);
            result.preservance = preservanceParser.parse(context, model, node).getResult();
            result.stability   = stabilityParser  .parse(context, model, node).getResult();
            result.preference  = preferenceParser .parse(context, model, node).getResult();
            // TODO
        }
    }

}
