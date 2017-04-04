/**
 * JsonConnection.java
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.map.type.TypeFactory;


/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-5-10$
 * 
 */
public class JsonConnection {

    private HttpURLConnection connection;
    private int connectTimeout = -1; // 默认连接超时30秒
    private int readTimeout = -1; // 默认连接超时60秒

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @param url The request URL
     * @throws IOException
     * @throws MalformedURLException
     */
    public JsonConnection(String url) throws MalformedURLException, IOException {
        super();
        connection = (HttpURLConnection) new URL(url).openConnection();
    }

    /**
     * 向服务器发送信息
     * 
     * @throws ClientInternalException
     */

    protected OutputStream sendRequest() {
        OutputStream out = null;
        try {
            if (connectTimeout > 0) {
                connection.setConnectTimeout(connectTimeout);
            }
            if (readTimeout > 0) {
                connection.setReadTimeout(readTimeout);
            }
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/json; charset=utf-8");
            connection.connect();
            out = connection.getOutputStream();
            return out;
        } catch (Exception e) {
            throw new ClientInternalException(e);
        }
    }
    
    /**
     * 向服务器发送信息
     * @param body 向服务器发送的信封对象
     */
    public void sendRequest(JsonEnvelop<?, ?> body) {
        OutputStream out = sendRequest();
        try {
            JacksonUtil.writeObj(out, body);
        } catch (Exception e) {
            throw new ClientInternalException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new ClientInternalException(e);
                }
            }
        }
            
    }

    /**
     * 读取服务器返回的信息
     * 
     * @return 读取到的数据
     */
    protected InputStream readResponse() {
        InputStream in = null;
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
            } else {
                if (connection.getErrorStream() != null) {
                    in = connection.getErrorStream();
                } else {
                    in = connection.getInputStream();
                }
            }
            return in;
        } catch (IOException e) {
            throw new ClientInternalException(e);
        }
    }
    
    /**
     * 读取服务器返回的信息
     * @param <T> 返回头类型。
     * @param <K> 返回body类型。
     * @param t 返回头类型的class
     * @param k 返回body类型的class
     * @return 服务器返回的信封
     */
    public <T, K> JsonEnvelop<T, K> readResponse(Class<T> t, Class<K> k) {
        InputStream in = readResponse();
        try {
            return JacksonUtil.readObj(in, TypeFactory.parametricType(JsonEnvelop.class, t, k));
        } catch (IOException e) {
            throw new ClientInternalException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ClientInternalException(e);
                }
            }
        }
    }
}
