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
package org.boiler;

import java.util.logging.Logger;
import java.util.Locale;

/**
 *
 * @author Victor Porton
 */
public class ExecutionContext {

    private Logger logger;

    private java.util.ResourceBundle messages;

    public ExecutionContext() {
        this(Locale.getDefault());
    }

    public ExecutionContext(java.util.Locale locale) {
        messages = java.util.ResourceBundle.getBundle("org.boiler.Messages", locale);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public java.util.ResourceBundle getMessages() {
        return messages;
    }

    public String getLocalized(String str) {
        return messages.getString(str);
    }

}
