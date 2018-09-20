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
package org.apache.http.osgi.impl;

import tink.org.apache.http.impl.client.HttpClientBuilder;
import tink.org.apache.http.osgi.services.ProxyConfiguration;

import java.util.List;

final class HttpClientBuilderConfigurator {

    private final OSGiCredentialsProvider credentialsProvider;

    private final OSGiHttpRoutePlanner routePlanner;

    HttpClientBuilderConfigurator(final List<ProxyConfiguration> proxyConfigurations) {
        credentialsProvider = new OSGiCredentialsProvider(proxyConfigurations);
        routePlanner = new OSGiHttpRoutePlanner(proxyConfigurations);
    }

    <T extends HttpClientBuilder> T configure(final T clientBuilder) {
        clientBuilder
                .setDefaultCredentialsProvider(credentialsProvider)
                .setRoutePlanner(routePlanner);
        return clientBuilder;
    }
}
