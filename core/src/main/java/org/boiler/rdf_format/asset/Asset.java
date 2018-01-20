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

import java.util.AbstractSet;
import java.util.List;
import org.apache.jena.rdf.model.Resource;
import org.boiler.graph.Connectivity;
import org.boiler.graph.Graph;

/**
 *
 * @author Victor Porton
 */
public class Asset {

    public enum ScriptKindEnum { TRANSFORMER, VALIDATOR }

    public enum TransformerKindEnum { ENTIRE, SEQUENTIAL, UP_DOWN, DOWN_UP }

    public enum ValidatorKindEnum { ENTIRE, PARTS }

    // TODO: Distinguishing transformer and validator scripts does not conform to the specification
    public static class ScriptInfo {
        public double preservance;
        public double stability;
        public double preference;
        public ScriptKindEnum scriptKind;
        public TransformerKindEnum transformerKind;
        public ValidatorKindEnum validatorKind;
        public String okResult; // null means none

        public ScriptInfo() { }

        public ScriptInfo(ScriptInfo src) {
            preservance     = src.preservance;
            stability       = src.stability;
            preference      = src.preference;
            scriptKind      = src.scriptKind;
            transformerKind = src.transformerKind;
            validatorKind   = src.validatorKind;
            okResult        = src.okResult;
        }
    }

//    enum CommandScriptInvocation { COMMAND, URL }

    public static class CommandScriptInfo extends ScriptInfo {

        public CommandScriptInfo(ScriptInfo src) {
            super(src);
        }

        public Resource language;
        public String minVersion, maxVersion; // may be null
        public String commandString, scriptURL; // either of them should be null
    }

    public static class WebServiceScriptInfo extends ScriptInfo {
        public WebServiceScriptInfo(ScriptInfo src) {
            super(src);
        }

        public Resource action; // action URL
        public String method;
        public String xmlField;
    }

    public static class Transformer {
        public AbstractSet<Resource> sourceNamespaces, targetNamespaces;
        public boolean ignoreTarget;
        public Resource precedence;
        public List<? extends ScriptInfo> scripts;
    }

    public static class Namespace {
        public Resource URI;
        public List<? extends ScriptInfo> validators;
    }

    public static class AssetInfo {
        public List<? extends Transformer> transformers;
        public List<? extends Namespace> namespaces;
        public List<Resource> seeAlsoTransform, seeAlsoValidate;
        public Connectivity<Resource> precedencesSubclasses;
        public Connectivity<Resource> precedencesHigher;
    }

}
