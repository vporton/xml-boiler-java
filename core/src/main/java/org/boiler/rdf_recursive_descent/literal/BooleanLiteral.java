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

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Literal;
import org.boiler.rdf_recursive_descent.*;

/**
 *
 * @author Victor Porton
 */
public class BooleanLiteral extends NodeParser<Boolean> {

    @Override
    public ParseResult<? extends Boolean>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        boolean fail = false;
        if(!node.isLiteral()) fail = true;
        Literal literal = null;
        if(!fail) {
            literal = node.asLiteral();
            if(literal.getDatatype() != org.apache.jena.vocabulary.XSD.xboolean)
                fail = true;
        }
        if(!fail)
            return new ParseResult<Boolean>(literal.getBoolean());
        org.boiler.util.StringCreator msg =
            () -> java.text.MessageFormat.format(
                    context.getLocalized("BooleanLiteral_error"),
                    node);
        return context.raise(getErrorHandler(), msg);
    }


}
