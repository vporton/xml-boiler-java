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
package org.boiler.graph;

import java.util.HashSet;
import java.util.Map.Entry;

/**
 *
 * @author porton
 */
public class Composition {

    /*
     * Note order of arguments!
     */
    public static <T> Graph<T> compose(Graph<T> b, Graph<T> a) {
        Graph<T> result = new Graph<T>();
        for(Entry<T, HashSet<T>> e: a.adj.entrySet()) {
            T x = e.getKey();
            for(T y : e.getValue()) {
                HashSet<T> e2 = b.adj.get(y);
                if(e2 != null)
                    for(T z : e2) result.addEdge(x, z);
            }
        }
        return result;
    }

}
