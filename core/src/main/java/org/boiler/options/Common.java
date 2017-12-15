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
package org.boiler.options;

import java.util.LinkedHashSet;

/**
 *
 * @author Victor Porton
 */
public class Common {

    public enum WorklowKind { TRANSFORMATION, VALIDATION }

    public enum RecursiveDownload { NONE, DEPTH_FIRST, BREADTH_FIRST }

    public enum RecursiveRetrievalPriorityOrderElement { SOURCES, TARGETS }

    // Too much honor to use this advanced data structure
    public static class RecursiveRetrievalPriority extends
            LinkedHashSet<RecursiveRetrievalPriorityOrderElement>
    {
        // intentionally left blank
    }

    public static final class RecursiveDownloadOptions {
        public RecursiveDownload recursiveDownload;
        public RecursiveRetrievalPriority retrievalPriority;
    }

    // In this version the same options are applied to all elements of the
    // workflow, but in future we may increase "granularity" to have different
    // options for different elements.
    public static class BaseAutomaticWorkflowElementOptions {
        public Common.WorklowKind kind;
        public RecursiveDownloadOptions recursiveOptions;
    }

}
