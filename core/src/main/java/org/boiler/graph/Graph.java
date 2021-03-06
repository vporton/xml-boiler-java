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
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Directed graph. Used to check connectivity between two vertices.
 *
 * @author Victor Porton
 */
public class Graph<T> implements AbstractGraph<T> {

    // In fact we have no duplicating entries. So we could use a faster type
    HashMap<T, HashSet<T>> adj;

    Graph(HashMap<T, HashSet<T>> adj) {
        this.adj = adj;
    }

    public Graph() {
        adj = new HashMap<T, HashSet<T>>();
    }

    @Override
    public int hashCode() {
        return adj.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Graph)) return false;
        return adj.equals(((Graph<?>)obj).adj);
    }

    @Override
    public Graph<T> clone() {
        return new Graph<>(adj);
    }

    public void addEdge(T from, T to) {
        HashSet<T> value = adj.get(from);
        if(value == null) {
            value = new HashSet<T>();
            value.add(to);
            adj.put(from, value);
        } else {
            value.add(to);
        }
    }

    @Override
    public boolean adjanced(T from, T to) {
        HashSet<T> set = adj.get(from);
        if(set == null) return false;
        return set.contains(to);
    }

    Graph<T> reverse() {
        Graph<T> result = new Graph<T>();
        for(Entry<T, HashSet<T>> e: adj.entrySet())
            for(T y : e.getValue())
                result.addEdge(y, e.getKey());
        return result;
    }

// I use depth first search (probably should cache results)
//    public boolean connected(T from, T to) {
//        if(from == to) return true;
//        // discovered is not strictly required because we expect no cycles in our data, but
//        java.util.HashSet<T> discovered = new java.util.HashSet<T>();
//        java.util.Stack<T> stack = new java.util.Stack<T>();
//        stack.push(from);
//        while(!stack.isEmpty()) {
//            T node = stack.pop();
//            if(!discovered.contains(node)) discovered.add(node);
//            HashSet<T> set = adj.get(node);
//            if(set != null)
//                for(T v : set) {
//                    if(v == to) return true;
//                    discovered.add(v);
//                }
//        }
//        return false;
//    }

}
