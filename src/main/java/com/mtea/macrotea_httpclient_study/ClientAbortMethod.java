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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 这个例子说明在它正常完成之前如何去终止一个http请求方法。
 */
public class ClientAbortMethod {

    public final static void main(String[] args) throws Exception {
    	//注意 DefaultHttpClient
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com/");

            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
			System.out.println("RequestLine:" + httpget.getRequestLine());
			System.out.println("RequestLine:" + response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            System.out.println("----------------------------------------");
        	//耗尽
            EntityUtils.consume(entity);            
            //若不再访问response的信息，则通过httpget终止
            httpget.abort();
        }catch (Exception e) {
        	e.printStackTrace();
		} finally {
			//若不再访问httpclient，则管理器连接管理器以释放系统资源
            httpclient.getConnectionManager().shutdown();
        }
    }

}

