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
package org.boiler.rdf_format.asset;

import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Victor Porton
 */
public class Asset {

    enum ScriptKindEnum { TRANSFORMER, VALIDATOR }

    enum TransformerKindEnum { ENTIRE, SEQUENTIAL, UP_DOWN, DOWN_UP }

    enum ValidatorKindEnum { ENTIRE, PARTS }

    public static class ScriptInfo {
        public double preservance;
        public double stability;
        public double preference;
        public ScriptKindEnum scriptKind;
        public TransformerKindEnum transformerKind;
        public ValidatorKindEnum validatorKind;
        public String okResult; // null means none
    }

//    enum CommandScriptInvocation { COMMAND, URL }

    public static class CommandScriptInfo extends ScriptInfo {
        public Resource language;
        public String minVersion, maxVersion; // may be null
        public String commandString, scriptURL; // either of them should be null
    }

    public static class WebServiceScriptInfo extends ScriptInfo {
        public String action; // action URL
        public String method;
        public String xmlField;
    }

}
