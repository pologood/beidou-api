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


/**
 * The current version configuration class, used to configure QName and service address.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2010-7-30$
 * 
 */
public class V2 extends Version {
	private static final String NAMESPACE_COMMON = "http://api.baidu.com/sem/common/v2";
	
	public static final V2 v = new V2();

	private V2() {}

	@Override
	public String getVersion() {
		return "V2";
	}

	@Override
	public <T> String getAddr(Class<T> cls) {
	    String pkgName = cls.getPackage().getName();
	    if (pkgName.contains("sms"))
	        return "/sem/sms/v2/" + cls.getSimpleName();
	    else
	        return "/sem/nms/v2/" + cls.getSimpleName();
	}

	@Override
	public String getHeaderNameSpace() {
		return NAMESPACE_COMMON;
	}

}
