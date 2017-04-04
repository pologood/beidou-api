/**
 * ClientBusinessException.java
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
package com.baidu.api.client.core;

import com.baidu.api.sem.common.v2.ResHeader;

/**
 * Throw if Client operation failed due to header status is not success.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-9-13$
 * 
 */
public class ClientBusinessException extends RuntimeException {

    private static final long serialVersionUID = -7645114374520949036L;

    private ResHeader rheader;
    private Object response;

    /**
     * The constructor.
     * 
     * @param rheader
     *            The response header
     * @param response
     *            The response, the type will depend on the method you called.
     */
    public ClientBusinessException(ResHeader rheader, Object response) {
        super();
        this.rheader = rheader;
        this.response = response;
    }

    public ResHeader getRheader() {
        return rheader;
    }

    public Object getResponse() {
        return response;
    }

}
