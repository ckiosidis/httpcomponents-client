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
package org.apache.http.impl.client.integration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import tink.org.apache.http.Header;
import tink.org.apache.http.HttpException;
import tink.org.apache.http.HttpHost;
import tink.org.apache.http.HttpRequest;
import tink.org.apache.http.HttpResponse;
import tink.org.apache.http.HttpStatus;
import tink.org.apache.http.client.methods.HttpGet;
import tink.org.apache.http.client.protocol.HttpClientContext;
import tink.org.apache.http.entity.StringEntity;
import tink.org.apache.http.impl.client.HttpClients;
import tink.org.apache.http.localserver.LocalServerTestBase;
import tink.org.apache.http.protocol.HttpContext;
import tink.org.apache.http.protocol.HttpRequestHandler;
import tink.org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Client protocol handling tests.
 */
public class TestMinimalClientRequestExecution extends LocalServerTestBase {

    private static class SimpleService implements HttpRequestHandler {

        public SimpleService() {
            super();
        }

        @Override
        public void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {
            response.setStatusCode(HttpStatus.SC_OK);
            final StringEntity entity = new StringEntity("Whatever");
            response.setEntity(entity);
        }
    }

    @Test
    public void testNonCompliantURI() throws Exception {
        this.serverBootstrap.registerHandler("*", new SimpleService());
        this.httpclient = HttpClients.createMinimal();
        final HttpHost target = start();

        final HttpClientContext context = HttpClientContext.create();
        for (int i = 0; i < 10; i++) {
            final HttpGet request = new HttpGet("/");
            final HttpResponse response = this.httpclient.execute(target, request, context);
            EntityUtils.consume(response.getEntity());
            Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            final HttpRequest reqWrapper = context.getRequest();
            Assert.assertNotNull(reqWrapper);

            final Header[] headers = reqWrapper.getAllHeaders();
            final Set<String> headerSet = new HashSet<String>();
            for (final Header header: headers) {
                headerSet.add(header.getName().toLowerCase(Locale.ROOT));
            }
            Assert.assertEquals(3, headerSet.size());
            Assert.assertTrue(headerSet.contains("connection"));
            Assert.assertTrue(headerSet.contains("host"));
            Assert.assertTrue(headerSet.contains("user-agent"));
        }
    }

}
