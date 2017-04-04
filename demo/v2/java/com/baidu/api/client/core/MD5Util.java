/**
 * MD5Util.java
 *
 * Copyright 2011 Baidu, Inc.
 *
 * Baidu licenses this file to you under the Apache License, version 2.0
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

import java.security.MessageDigest;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-9-2$
 * 
 */
public abstract class MD5Util {
    
    public static final boolean md5check(byte [] data, String md5) {
        String sum = md5sum(data);
        return sum.equalsIgnoreCase(md5);
    }

    public static final String md5sum(byte [] data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data);
            return byteToBase16(md.digest());
        } catch (Exception e) {
            throw new ClientInternalException("MD5 not supported.", e);
        }
    }
    
    public static final String byteToBase16(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("The parameter should not be null!");
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xF0) >> 4));
            sb.append(Integer.toHexString(b & 0x0F));
        }
        return sb.toString();
    }
}
