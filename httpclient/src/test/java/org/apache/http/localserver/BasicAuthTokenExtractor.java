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

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import tink.org.apache.http.Header;
import tink.org.apache.http.HttpException;
import tink.org.apache.http.HttpRequest;
import tink.org.apache.http.ProtocolException;
import tink.org.apache.http.auth.AUTH;
import tink.org.apache.http.util.EncodingUtils;

public class BasicAuthTokenExtractor {

    public String extract(final HttpRequest request) throws HttpException {
        String auth = null;

        final Header h = request.getFirstHeader(AUTH.WWW_AUTH_RESP);
        if (h != null) {
            final String s = h.getValue();
            if (s != null) {
                auth = s.trim();
            }
        }

        if (auth != null) {
            final int i = auth.indexOf(' ');
            if (i == -1) {
                throw new ProtocolException("Invalid Authorization header: " + auth);
            }
            final String authscheme = auth.substring(0, i);
            if (authscheme.equalsIgnoreCase("basic")) {
                final String s = auth.substring(i + 1).trim();
                try {
                    final byte[] credsRaw = EncodingUtils.getAsciiBytes(s);
                    final BinaryDecoder codec = new Base64();
                    auth = EncodingUtils.getAsciiString(codec.decode(credsRaw));
                } catch (final DecoderException ex) {
                    throw new ProtocolException("Malformed BASIC credentials");
                }
            }
        }
        return auth;
    }

}
