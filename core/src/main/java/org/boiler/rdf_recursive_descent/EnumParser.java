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
package org.boiler.rdf_recursive_descent;

import java.util.HashMap;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Victor Porton
 */
// We don't use that T extends Enum<T>, "T extends Enum<T>" can be safely removed
public class EnumParser<T extends Enum<T>> extends NodeParser<T> {

    HashMap<Resource, T> map;

    // TODO: constructor

    @Override
    public ParseResult<T>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        // FIXME: What to do if Is_Blank(Node)?
        if(!node.isResource()) {
            org.boiler.util.StringCreator msg =
                () -> java.text.MessageFormat.format(
                        context.getLocalized("ShouldBeIRI_error"),
                        node);
            return context.raise(getErrorHandler(), msg);
        }
        Resource resource = node.asResource();
        T value = map.get(resource);
        if(value == null) {
            org.boiler.util.StringCreator msg =
                () -> java.text.MessageFormat.format(
                        context.getLocalized("UnknownIRI_error"),
                        resource);
            return context.raise(getErrorHandler(), msg);
        }
        return new ParseResult<T>(value);
    }

}
