/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws.types;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionTest {

    @Test
    public void testVersionParsingWithInvalidVersion() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Version.parse("a.b.c");
        });
        assertEquals("Invalid version parsing should throw an exception with appropriate message", exception.getMessage(), Version.VERSION_PARSE_ERROR);
    }

    @Test
    public void testVersionParsingWithNullVersion() {
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> {
            Version.parse(null);
        });
        assertEquals("Invalid version parsing should throw an exception with appropriate message", exception.getMessage(), Version.VERSION_PARSE_ERROR);
    }

    @Test
    public void testVersionParsingWithAllComponents() {
        Version version = Version.parse("11.2.6.1_123");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 2);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 6);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 1);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 123);
    }

    @Test
    public void testVersionParsingWithAdditionalComponents() {
        Version version = Version.parse("11.1.6.1_123-GA");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 1);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 6);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 1);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 123);
    }

    @Test
    public void testVersionParsingWithMissingBuildValue() {
        Version version = Version.parse("11.2.6.1");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 2);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 6);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 1);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 0);
    }

    @Test
    public void testVersionParsingWithMissingPatchValue() {
        Version version = Version.parse("11.2.6");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 2);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 6);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 0);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 0);
    }

    @Test
    public void testVersionParsingWithMissingUpdateValue() {
        Version version = Version.parse("11.2");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 2);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 0);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 0);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 0);
    }

    @Test
    public void testVersionParsingWithMissingInterimValue() {
        Version version = Version.parse("11");
        assertEquals("Invalid parsing of feature value from the version string", version.getFeature(), Integer.valueOf(11));
        assertEquals("Invalid parsing of interim value from the version string", version.getInterim(), 0);
        assertEquals("Invalid parsing of update value from the version string", version.getUpdate(), 0);
        assertEquals("Invalid parsing of patch value from the version string", version.getPatch(), 0);
        assertEquals("Invalid parsing of build value from the version string", version.getBuild(), 0);
    }

    @Test
    public void testVersionParsingWithInvalidFeatureValue() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Version.parse("0");
        });
        assertEquals("Invalid version parsing should throw an exception with appropriate message", exception.getMessage(), Version.VERSION_PARSE_ERROR);
    }

    @Test
    public void testVersionParsingWithZeroFeatureValue() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Version.Builder.newBuilder().setFeature(0).build();
        });
        assertEquals("Invalid version parsing should throw an exception with appropriate message", exception.getMessage(), Version.INVALID_FEATURE_VALUE_ERROR);
    }

    @Test
    public void testVersionParsingWithNullFeatureValue() {
        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            Version.Builder.newBuilder().setInterim(1).build();
        });
        assertEquals("Invalid version parsing should throw an exception with appropriate message", exception.getMessage(), Version.INVALID_FEATURE_VALUE_ERROR);
    }

    @Test
    public void testCompareToWithSimilarVersionObjects() {
        Version objA = Version.Builder.newBuilder().setFeature(12).build();
        Version objB = Version.Builder.newBuilder().setFeature(12).build();
        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB), 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).build();
        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB), 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).build();
        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB), 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).build();
        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB), 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).setBuild(123).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).setBuild(123).build();
        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB), 0);

        assertEquals("Invalid result returned on comparing two comparable version objects", objA.compareTo(objA), 0);
        assertEquals("Invalid result returned on comparing two comparable version objects", objB.compareTo(objB), 0);
    }

    @Test
    public void testCompareToWithDifferentVersionObjects() {

        Version objA = Version.Builder.newBuilder().setFeature(14).build();
        Version objB = Version.Builder.newBuilder().setFeature(12).build();
        assertTrue("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB) > 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(2).build();
        assertTrue("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB) < 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(3).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).build();
        assertTrue("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB) > 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(2).build();
        assertTrue("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB) < 0);

        objA = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).setBuild(124).build();
        objB = Version.Builder.newBuilder().setFeature(12).setInterim(1).setUpdate(2).setPatch(1).setBuild(123).build();
        assertTrue("Invalid result returned on comparing two comparable version objects", objA.compareTo(objB) > 0);
    }

    @Test
    public void testCompareToWithANullValue() {
        Version objA = Version.Builder.newBuilder().setFeature(12).build();
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> {
            objA.compareTo(null);
        });
    }
}
