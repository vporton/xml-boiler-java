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
package org.boiler.rdf_recursive_descent.literal;

import org.boiler.rdf_recursive_descent.*;

/**
 *
 * @author Victor Porton
 */
public class StringLiteral extends NodeParserWithError<String> {

    public StringLiteral() { }

    public StringLiteral(ErrorHandler onError) { super(onError); }

    @Override
    public ParseResult<? extends String>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        // TODO: xsd:normalizedString support
        if(!node.isLiteral() || node.asLiteral().getDatatype() != org.apache.jena.vocabulary.XSD.xstring) {
            org.boiler.util.StringCreator msg =
                () -> java.text.MessageFormat.format(
                        context.getLocalized("StringLiteral_error"),
                        node);
            return context.raise(getErrorHandler(), msg);

        }
        return new ParseResult<String>(node.asLiteral().getString());
    }


}
