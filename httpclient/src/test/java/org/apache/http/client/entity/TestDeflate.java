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

package org.apache.http.client.entity;

import java.util.zip.Deflater;

import tink.org.apache.http.Consts;
import tink.org.apache.http.HttpEntity;
import tink.org.apache.http.entity.ByteArrayEntity;
import tink.org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestDeflate {

    @Test
    public void testCompressDecompress() throws Exception {

        final String s = "some kind of text";
        final byte[] input = s.getBytes(Consts.ASCII);

        // Compress the bytes
        final byte[] compressed = new byte[input.length * 2];
        final Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        final int len = compresser.deflate(compressed);

        final HttpEntity entity = new DeflateDecompressingEntity(new ByteArrayEntity(compressed, 0, len));
        Assert.assertEquals(s, EntityUtils.toString(entity));
    }

}
