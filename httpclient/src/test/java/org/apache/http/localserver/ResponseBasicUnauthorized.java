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

package org.apache.http.localserver;

import java.io.IOException;

import tink.org.apache.http.HttpException;
import tink.org.apache.http.HttpResponse;
import tink.org.apache.http.HttpResponseInterceptor;
import tink.org.apache.http.HttpStatus;
import tink.org.apache.http.auth.AUTH;
import tink.org.apache.http.protocol.HttpContext;

public class ResponseBasicUnauthorized implements HttpResponseInterceptor {

    @Override
    public void process(
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            if (!response.containsHeader(AUTH.WWW_AUTH)) {
                response.addHeader(AUTH.WWW_AUTH, "Basic realm=\"test realm\"");
            }
        }
    }

}
