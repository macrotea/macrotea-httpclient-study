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

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/**
 * 这个例子演示了使用本地HTTP上下文填入自定义属性。
 */
public class ClientCustomContext {

    public final static void main(String[] args) throws Exception {

        HttpClient httpclient = new DefaultHttpClient();
        try {
            // CookieStore 本地实例
            CookieStore cookieStore = new BasicCookieStore();

            // HttpContext 本地实例
            HttpContext localContext = new BasicHttpContext();
            
            //为HttpContext安装CookieStore
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpGet httpget = new HttpGet("http://www.google.com/");

            System.out.println("executing request " + httpget.getURI());

            // 根据本地上下文执行请求
            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            
            //获取访问后存储下来的cookie
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("Local cookie: " + cookies.get(i));
            }

           	//耗尽
            EntityUtils.consume(entity);

            System.out.println("----------------------------------------");

        } finally {
			//若不再访问httpclient，则管理器连接管理器以释放系统资源
            httpclient.getConnectionManager().shutdown();
        }
    }

}

