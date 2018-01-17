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
package org.boiler.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Victor Porton
 */
public class AsSet {

    public static <T> Graph<T> union(Graph<T> a, Graph<T> b) {
        Set<T> source = new HashSet<>();
        source.addAll(a.adj.keySet());
        source.addAll(b.adj.keySet());
        HashMap<T, HashSet<T>> adj = new HashMap<>();
        for(T x : source) {
            Set<T> setA = a.adj.get(x);
            Set<T> setB = a.adj.get(x);
            if(!setA.isEmpty() || !setB.isEmpty()) {
                HashSet<T> set = new HashSet<>();
                if(!setA.isEmpty()) set.addAll(setA);
                if(!setB.isEmpty()) set.addAll(setB);
                adj.put(x, set);
            }
        }
        return new Graph<T>(adj);
    }

}
