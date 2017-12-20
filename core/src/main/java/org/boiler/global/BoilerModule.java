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

import java.util.HashMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.boiler.rdf_format.asset.Asset.TransformerKindEnum;
import org.boiler.rdf_format.asset.Asset.ValidatorKindEnum;
import org.boiler.rdf_recursive_descent.NodeParser;
import org.boiler.rdf_recursive_descent.EnumParser;
import static org.boiler.rdf_format.Base.MAIN_NAMESPACE;

/**
 *
 * @author Victor Porton
 */
public class BoilerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(org.boiler.graph.AbstractGraph.class);
        install(new FactoryModuleBuilder()
            .build(org.boiler.rdf_format.asset.parser.ScriptInfoParser.BaseScriptInfoParser.Factory.class));
    }

    @Provides @Named("subclasses") @Singleton
    org.boiler.graph.AbstractGraph provideSubclassesGraph() {
        return SubclassRelationLoader.loadSubclassGraph();
    }

    @Provides @Named("transformerKind") @Singleton
    NodeParser<TransformerKindEnum>
    provideTransformerKindParser() {
        HashMap<Resource, TransformerKindEnum> map = new HashMap<Resource, TransformerKindEnum>();
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "entire"), TransformerKindEnum.ENTIRE);
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "sequential"), TransformerKindEnum.SEQUENTIAL);
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "upDown"), TransformerKindEnum.UP_DOWN);
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "downUp"), TransformerKindEnum.DOWN_UP);
        return new EnumParser<TransformerKindEnum>(map);
    }

    @Provides @Named("validatorKind") @Singleton
    NodeParser<ValidatorKindEnum>
    provideValidatorKindParser() {
        HashMap<Resource, ValidatorKindEnum> map = new HashMap<Resource, ValidatorKindEnum>();
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "entire"), ValidatorKindEnum.ENTIRE);
        map.put(ResourceFactory.createResource(MAIN_NAMESPACE + "parts"), ValidatorKindEnum.PARTS);
        return new EnumParser<ValidatorKindEnum>(map);
    }

}
