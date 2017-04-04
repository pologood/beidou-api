/**
 * Configer.java
 *
 * Copyright @year@ @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.api.client.version;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The abstract version configuration class, used to configure QName and service address.
 * Internal use only.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-7-30$
 * 
 */
public abstract class Version {
    protected static final Log log = LogFactory.getLog(Version.class);

    private static Map<String, Version> verions = new HashMap<String, Version>();

    public abstract <T> String getAddr(Class<T> cls);

    public abstract String getVersion();

    public abstract String getHeaderNameSpace();

    /**
     * Add a new version into the version map.
     * 
     * @param v
     *            The version to add.
     */
    protected static void addVersion(Version v) {
        verions.put(v.getVersion().toUpperCase(), v);
    }

    /**
     * Get a version named by the key string.
     * 
     * @param key
     *            The version name.
     * @return The version, null if not found.
     */
    public static Version getVersion(String key) {
        return verions.get(key.toUpperCase());
    }

    protected Version() {
        Version.addVersion(this);
        log.info("Version [" + this.getVersion() + "] has been loaded.");
    }
}
