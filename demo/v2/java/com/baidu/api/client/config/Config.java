/**
 * Config.java
 *
 * Copyright 2010 @company@, Inc.
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
package com.baidu.api.client.config;

/**
 * The Config interface
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-7-30$
 * 
 */
public interface Config {
    /**
     * Set configurations into Configer class
     * 
     * @param key
     *            The configuration key
     * @param value
     *            The configuration value
     */
    public void setConfig(String key, String value);
}
