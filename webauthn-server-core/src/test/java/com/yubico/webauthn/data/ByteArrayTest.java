// Copyright (c) 2018, Yubico AB
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.yubico.webauthn.data;

import com.yubico.webauthn.data.exception.Base64UrlException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ByteArrayTest {

    @Test
    public void testEncodeBase64Url() {
        byte[] input = "Test".getBytes();
        String base64Data = new ByteArray(input).getBase64Url();

        // No padding.
        assertEquals("VGVzdA", base64Data);
    }

    @Test
    public void decodeTest() throws Base64UrlException {
        String base64Data = "VGVzdA";
        String base64DataWithPadding = "VGVzdA==";
        String base64DataEmpty = "";

        // Verify that Base64 data with and without padding ('=') are decoded correctly.
        String out1 = new String(ByteArray.fromBase64Url(base64Data).getBytes());
        String out2 = new String(ByteArray.fromBase64Url(base64DataWithPadding).getBytes());
        String out3 = new String(ByteArray.fromBase64Url(base64DataEmpty).getBytes());

        assertEquals(out1, out2);
        assertEquals(out1, "Test");
        assertEquals(out3, "");
    }

    @Test
    public void decodeMimeTest() {
        String base64 = "ab+/+/==";
        String base64WithoutPadding = "ab+/+/";
        String expectedRecoded = "ab-_-w";

        assertEquals(expectedRecoded, ByteArray.fromBase64(base64).getBase64Url());
        assertEquals(expectedRecoded, ByteArray.fromBase64(base64WithoutPadding).getBase64Url());
    }

    @Test(expected = Base64UrlException.class)
    public void decodeBadAlphabetTest() throws Base64UrlException {
        ByteArray.fromBase64Url("****");
    }

    @Test(expected = Base64UrlException.class)
    public void decodeBadPaddingTest() throws Base64UrlException {
        ByteArray.fromBase64Url("A===");
    }
}
