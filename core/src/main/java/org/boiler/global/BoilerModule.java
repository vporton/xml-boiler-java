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
package org.boiler.global;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.jena.rdf.model.Resource;
import org.boiler.rdf_format.asset.parser.ScriptInfoParser;

/**
 *
 * @author Victor Porton
 */
public class BoilerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(org.boiler.SubclassRelation.class);
//        install(new FactoryModuleBuilder()
//            .build(ScriptInfoParser.Factory.class));
    }

    @Provides @Named("hardcodedSubclasses") @Singleton
    org.boiler.SubclassRelation provideSubclassesGraph() {
        return org.boiler.SubclassRelation.loadSubclassGraph();
    }

}
