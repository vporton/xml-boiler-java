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

import javax.inject.*;
import com.google.inject.assistedinject.Assisted;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.rdf_format.Base;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_format.asset.*;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.NodeParserWithError;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;
import org.boiler.rdf_recursive_descent.PredicateParser;
import org.boiler.rdf_recursive_descent.PredicateParserWithError;
import org.boiler.rdf_recursive_descent.compound.OnePredicate;
import org.boiler.rdf_recursive_descent.compound.ZeroOnePredicate;
import org.boiler.rdf_recursive_descent.literal.DoubleLiteral;

/**
 *
 * @author Victor Porton
 */
// made public for Guice
public class ScriptInfoParser extends NodeParser<Asset.ScriptInfo> {

    private Asset.ScriptKindEnum scriptKind;

    private ScriptInfoParser(Asset.ScriptKindEnum scriptKind) {
        this.scriptKind = scriptKind;
    }

    @Override
    public ParseResult<? extends Asset.ScriptInfo>
    parse(ParseContext context, Model model, Resource node) throws FatalParseError {
        // TODO
    }

    // made public for Guice
    public static class BaseScriptInfoParser extends NodeParser<Asset.ScriptInfo> {

        private Asset.ScriptKindEnum scriptKind;

        private final NodeParser<Asset.TransformerKindEnum> transformerKindNodeParser;
        private final NodeParser<Asset.ValidatorKindEnum>   validatorKindNodeParser;

        public interface Factory {
            BaseScriptInfoParser create(Asset.ScriptKindEnum scriptKind);
        }

        @Inject
        BaseScriptInfoParser(
                @Named("transformerKind") NodeParser<Asset.TransformerKindEnum> transformerKindNodeParser,
                @Named("validatorKind") NodeParser<Asset.ValidatorKindEnum> validatorKindNodeParser,
                @Assisted Asset.ScriptKindEnum scriptKind)
        {
            this.scriptKind = scriptKind;
            this.transformerKindNodeParser = transformerKindNodeParser;
            this.validatorKindNodeParser   = validatorKindNodeParser;
        }

        @Override
        public ParseResult<? extends Asset.ScriptInfo>
        parse(ParseContext context, Model model, Resource node) throws FatalParseError {
            Asset.ScriptInfo result = new Asset.ScriptInfo();
            result.scriptKind = scriptKind;
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
            switch(scriptKind) {
                case TRANSFORMER:
                    OnePredicate<Asset.TransformerKindEnum> transformerKindParser =
                            new OnePredicate<Asset.TransformerKindEnum>(
                                    ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "transformerKind"),
                                    transformerKindNodeParser,
                                    ErrorHandler.WARNING);
                    result.transformerKind = transformerKindParser.parse(context, model, node).getResult();
                    break;
                case VALIDATOR:
                    OnePredicate<Asset.ValidatorKindEnum> validatorKindParser =
                            new OnePredicate<Asset.ValidatorKindEnum>(
                                    ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "validatorKind"),
                                    validatorKindNodeParser,
                                    ErrorHandler.WARNING);
                    result.validatorKind = validatorKindParser.parse(context, model, node).getResult();
                    break;
            }
            NodeParser<String> okResultNodeParser =
                    new org.boiler.rdf_recursive_descent.literal.StringLiteral(ErrorHandler.WARNING);
            PredicateParserWithError<String> okResultParser =
                    new ZeroOnePredicate<String>(
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "okResult"),
                            okResultNodeParser,
                            ErrorHandler.WARNING);
            result.okResult = okResultParser.parse(context, model, node).getResult();;
            return new ParseResult<Asset.ScriptInfo>(result);
        }
    }

}
