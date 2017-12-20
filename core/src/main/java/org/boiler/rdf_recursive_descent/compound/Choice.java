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
package org.boiler.rdf_recursive_descent.compound;

import org.boiler.rdf_recursive_descent.*;

/**
 * TODO: If the node conforms to more than one choice, it does not conform to
 * the specification.
 *
 * @author Victor Porton
 */
public class Choice<T> extends NodeParser<T> {

    private NodeParser<T>[] choices;

    public Choice(NodeParser<T>[] choices) {
        this.choices = choices;
    }

    public NodeParser<T>[] getChoices() {
        return choices;
    }

    @Override
    public ParseResult<? extends T>
    parse(ParseContext context,
          org.apache.jena.rdf.model.Model model,
          org.apache.jena.rdf.model.Resource node)
            throws FatalParseError
    {
        for(NodeParser<T> variant: choices) {
            ParseResult<? extends T> elt = variant.parse(context, model, node);
            if(elt.getSuccess()) return elt;
        }
        return new ParseResult<T>();
    }

}
