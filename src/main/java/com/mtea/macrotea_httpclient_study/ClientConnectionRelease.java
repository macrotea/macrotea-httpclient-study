/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.mtea.macrotea_httpclient_study;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 这个例子说明如何把底层的连接交回给ConnectionManager
 */
public class ClientConnectionRelease {

    public final static void main(String[] args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("http://www.apache.org/");

            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = response.getEntity();

            //如果响应并不包含实体，也没有必要理会连接释放
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    instream.read();
                } catch (IOException ex) {
                	//若是IOException，则自动把底层的连接交回给ConnectionManager
                    throw ex;
                } catch (RuntimeException ex) {
                	//若是RuntimeException，则应该httpget.abort();
                    httpget.abort();
                    throw ex;
                } finally {
                    // instream.close() 会触发自动把底层的连接交回给ConnectionManager
                    try { instream.close(); } catch (Exception ignore) {}
                }
            }

        } finally {
			//若不再访问httpclient，则管理器连接管理器以释放系统资源
            httpclient.getConnectionManager().shutdown();
        }
    }

}

