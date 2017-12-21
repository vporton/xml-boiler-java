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

import java.util.HashMap;
import com.google.inject.name.Named;
import com.google.inject.assistedinject.Assisted;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.rdf_recursive_descent.EnumParser;
import org.boiler.rdf_recursive_descent.ErrorHandler;
import org.boiler.rdf_recursive_descent.FatalParseError;
import org.boiler.rdf_recursive_descent.ParseContext;
import org.boiler.rdf_recursive_descent.ParseResult;
import org.boiler.rdf_recursive_descent.PredicateParserWithError;
import org.boiler.rdf_recursive_descent.compound.OnePredicate;
import org.boiler.rdf_recursive_descent.compound.ZeroOnePredicate;
import org.boiler.rdf_recursive_descent.compound.Choice;
import org.boiler.rdf_recursive_descent.literal.*;
import org.boiler.rdf_recursive_descent.type.CheckNodeClass;
import org.boiler.rdf_format.asset.Asset.TransformerKindEnum;
import org.boiler.rdf_format.asset.Asset.ValidatorKindEnum;
import org.boiler.rdf_format.Base;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_format.asset.Asset;
import org.boiler.graph.AbstractGraph;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;

/**
 *
 * @author Victor Porton
 */
public class ScriptInfoParser extends NodeParser<Asset.ScriptInfo> {

    private final AbstractGraph<Resource> subclasses;

    private final Asset.ScriptKindEnum scriptKind;

    public interface Factory {
        ScriptInfoParser create(Asset.ScriptKindEnum scriptKind);
    }

    private ScriptInfoParser(@Named("subclasses") AbstractGraph<Resource> subclasses,
                             @Assisted Asset.ScriptKindEnum scriptKind) {
        this.subclasses = subclasses;
        this.scriptKind = scriptKind;
    }

    @Override
    public ParseResult<? extends Asset.ScriptInfo>
    parse(ParseContext context, Model model, Resource node) throws FatalParseError {
        MyBaseParser[] choices = {new CommandScriptInfoParser(subclasses, scriptKind),
                                  new WebServiceScriptInfoParser(subclasses, scriptKind)};
        NodeParser<Asset.ScriptInfo> realParser = new Choice<Asset.ScriptInfo>(choices);
        return realParser.parse(context, model, node);
    }

    private static abstract class MyBaseParser extends NodeParser<Asset.ScriptInfo> { }

    private static class BaseScriptInfoParser extends MyBaseParser {

        private final Asset.ScriptKindEnum scriptKind;

        private static NodeParser<TransformerKindEnum> transformerKindParser = null;
        private static NodeParser<ValidatorKindEnum>   validatorKindParser   = null;

        private static NodeParser<TransformerKindEnum> getTransformerKindParser() {
            if(transformerKindParser != null)
                return transformerKindParser;
            HashMap<Resource, TransformerKindEnum> map = new HashMap<Resource, TransformerKindEnum>();
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "entire"), TransformerKindEnum.ENTIRE);
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "sequential"), TransformerKindEnum.SEQUENTIAL);
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "upDown"), TransformerKindEnum.UP_DOWN);
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "downUp"), TransformerKindEnum.DOWN_UP);
            return transformerKindParser = new EnumParser<TransformerKindEnum>(map);
        }

        private static NodeParser<ValidatorKindEnum> getValidatorKindParser() {
            if(validatorKindParser != null)
                return validatorKindParser;
            HashMap<Resource, ValidatorKindEnum> map = new HashMap<Resource, ValidatorKindEnum>();
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "entire"), ValidatorKindEnum.ENTIRE);
            map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "parts"), ValidatorKindEnum.PARTS);
            return validatorKindParser = new EnumParser<ValidatorKindEnum>(map);
        }

        BaseScriptInfoParser(Asset.ScriptKindEnum scriptKind) {
            this.scriptKind = scriptKind;
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
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "preference"),
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
                                    getTransformerKindParser(),
                                    ErrorHandler.WARNING);
                    ParseResult<? extends TransformerKindEnum> transformerKind =
                            transformerKindParser.parse(context, model, node);
                    if(!transformerKind.getSuccess())
                        return new ParseResult<>();
                    result.transformerKind = transformerKind.getResult();
                    break;
                case VALIDATOR:
                    ParseResult<? extends ValidatorKindEnum> validatorKind =
                            validatorKindParser.parse(context, model, node);
                    if(!validatorKind.getSuccess())
                        return new ParseResult<>();
                    result.validatorKind = validatorKind.getResult();
                    break;
            }
            NodeParser<String> okResultNodeParser =
                    new org.boiler.rdf_recursive_descent.literal.StringLiteral(ErrorHandler.WARNING);
            PredicateParserWithError<String> okResultParser =
                    new ZeroOnePredicate<String>(
                            ResourceFactory.createProperty(Base.MAIN_NAMESPACE + "okResult"),
                            okResultNodeParser,
                            ErrorHandler.WARNING);
            result.okResult = okResultParser.parse(context, model, node).getResult();
            return new ParseResult<Asset.ScriptInfo>(result);
        }
    }

    private static class CommandScriptInfoParser extends MyBaseParser {

        private final AbstractGraph<Resource> subclasses;

        private final Asset.ScriptKindEnum scriptKind;

        CommandScriptInfoParser(AbstractGraph<Resource> subclasses, Asset.ScriptKindEnum scriptKind) {
            this.subclasses = subclasses;
            this.scriptKind = scriptKind;
        }

        @Override
        public ParseResult<? extends Asset.CommandScriptInfo>
        parse(ParseContext context, Model model, Resource node) throws FatalParseError {
            Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "Command");
            if(!CheckNodeClass.check(subclasses, context, model, node, klass, ErrorHandler.IGNORE))
                return new ParseResult<>();
            ParseResult<? extends Asset.ScriptInfo> base =
                    new BaseScriptInfoParser(scriptKind).parse(context, model, node);
            if(!base.getSuccess()) return new ParseResult<>();

            Asset.CommandScriptInfo result = new Asset.CommandScriptInfo(base.getResult());

            Property pred1 = ResourceFactory.createProperty(MAIN_NAMESPACE + "scriptURL");
            PredicateParserWithError<String> str1Parser =
                    new ZeroOnePredicate<String>(pred1, new StringLiteral(), ErrorHandler.WARNING);
            String str1 = str1Parser.parse(context, model, node).getResult();
            Property pred2 = ResourceFactory.createProperty(MAIN_NAMESPACE + "commandString");
            PredicateParserWithError<String> str2Parser =
                    new ZeroOnePredicate<String>(pred2, new StringLiteral(), ErrorHandler.WARNING);
            String str2 = str2Parser.parse(context, model, node).getResult();
            if(str1 == null && str2 == null)
                return context.raise(
                        ErrorHandler.WARNING,
                        java.text.MessageFormat.format(context.getLocalized("CommandScriptBothMissing_error"), node));
            if(str1 != null && str2 != null)
                return context.raise(
                        ErrorHandler.WARNING,
                        java.text.MessageFormat.format(context.getLocalized("CommandScriptBothPresent_error"), node));
            result.scriptURL     = str1;
            result.commandString = str2;

            Property minVersionPred = ResourceFactory.createProperty(MAIN_NAMESPACE + "minVersion");
            PredicateParserWithError<String> minParser =
                    new ZeroOnePredicate<String>(minVersionPred, new StringLiteral(ErrorHandler.WARNING));
            result.minVersion = minParser.parse(context, model, node).getResult();
            Property maxVersionPred = ResourceFactory.createProperty(MAIN_NAMESPACE + "maxVersion");
            PredicateParserWithError<String> maxParser =
                    new ZeroOnePredicate<String>(maxVersionPred, new StringLiteral(ErrorHandler.WARNING));
            result.maxVersion = maxParser.parse(context, model, node).getResult();

            Property languagePred = ResourceFactory.createProperty(MAIN_NAMESPACE + "language");
            PredicateParserWithError<Resource> languageParser =
                    new OnePredicate<>(languagePred, new IRILiteral(ErrorHandler.WARNING));
            ParseResult<? extends Resource> language = languageParser.parse(context, model, node);
            if(!language.getSuccess()) return new ParseResult<>();
            result.language = language.getResult();

            return new ParseResult<>(result);
        }

    }

    private static class WebServiceScriptInfoParser extends MyBaseParser {

        private final AbstractGraph<Resource> subclasses;

        private final Asset.ScriptKindEnum scriptKind;

        WebServiceScriptInfoParser(AbstractGraph<Resource> subclasses, Asset.ScriptKindEnum scriptKind) {
            this.subclasses = subclasses;
            this.scriptKind = scriptKind;
        }

        @Override
        public ParseResult<? extends Asset.WebServiceScriptInfo>
        parse(ParseContext context, Model model, Resource node) throws FatalParseError {
            Resource klass = ResourceFactory.createProperty(MAIN_NAMESPACE + "WebService");
            if(!CheckNodeClass.check(subclasses, context, model, node, klass, ErrorHandler.IGNORE))
                return new ParseResult<>();
            ParseResult<? extends Asset.ScriptInfo> base =
                    new BaseScriptInfoParser(scriptKind).parse(context, model, node);
            if(!base.getSuccess()) return new ParseResult<>();

            Asset.WebServiceScriptInfo result = new Asset.WebServiceScriptInfo(base.getResult());

            Property actionPred = ResourceFactory.createProperty(MAIN_NAMESPACE + "action");
            PredicateParserWithError<Resource> actionParser =
                    new OnePredicate<>(actionPred, new IRILiteral(ErrorHandler.WARNING));
            ParseResult<? extends Resource> action = actionParser.parse(context, model, node);
            if(!action.getSuccess()) return new ParseResult<>();
            result.action = action.getResult();

            Property methodPred = ResourceFactory.createProperty(MAIN_NAMESPACE + "method");
            PredicateParserWithError<String> methodParser =
                    new OnePredicate<String>(methodPred, new StringLiteral(ErrorHandler.WARNING));
            ParseResult<? extends String> method = methodParser.parse(context, model, node);
            if(!method.getSuccess()) return new ParseResult<>();
            result.method = method.getResult();

            Property xmlFieldPred = ResourceFactory.createProperty(MAIN_NAMESPACE + "xmlField");
            PredicateParserWithError<String> xmlFieldParser =
                    new OnePredicate<String>(xmlFieldPred, new StringLiteral(ErrorHandler.WARNING));
            ParseResult<? extends String> xmlField = xmlFieldParser.parse(context, model, node);
            if(!xmlField.getSuccess()) return new ParseResult<>();
            result.xmlField = xmlField.getResult();

            return new ParseResult<>(result);
        }

    }

}
