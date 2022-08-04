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
package com.sforce.ws;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a semantic java version that can parse a string into different semantic version components
 * (feature, interim, patch, update and build) and can compare two versions based on these components.
 *
 * @author arjun.mehrotra
 */
public class JavaVersion implements Comparable<JavaVersion> {

    //For java 8 and earlier versions the version format string follows pattern major.minor.patch_build https://www.oracle.com/java/technologies/javase/versioning-naming.html
    //For java 9 and later versions the version format string follows pattern feature.interim.update.patch https://openjdk.org/jeps/223
    private static final String JAVA_VERSION_REGEX = "^(?<FEATURE>[1-9][0-9]*)(?:\\.(?<INTERIM>[0-9]*))?(?:\\.(?<UPDATE>\\d+))?(?:\\.(?<PATCH>[0-9]*))?(?:_(?<BUILD>[0-9]*))?(.*)";
    private static final Pattern JAVA_VERSION_PATTERN = Pattern.compile(JAVA_VERSION_REGEX);
    protected static final String VERSION_PARSE_ERROR = "Cannot parse provided version string";
    protected static final String INVALID_FEATURE_VALUE_ERROR = "Invalid feature value in the provided version string";
    public static final String JAVA_VERSION_PROPERTY = "java.version";
    public static final String JDK_UPDATE_MESSAGE = "Please update the client to a JDK version that includes a fix for JDK-8209178. See https://trailblazer.salesforce.com/issues_view?id=a1p4V000002JfxSQAS for more details.";
    private final static JavaVersion JAVA_VERSION_14_0_0_0 =
            Builder.newBuilder().setFeature(14).build();
    private final static JavaVersion JAVA_VERSION_13_0_2 =
            Builder.newBuilder().setFeature(13).setInterim(0).setUpdate(2).build();
    private final static JavaVersion JAVA_VERSION_11_0_6 =
            Builder.newBuilder().setFeature(11).setInterim(0).setUpdate(6).build();
    private final static JavaVersion JAVA_VERSION_1_8_0_0_321 =
            Builder.newBuilder().setFeature(1).setInterim(8).setUpdate(0).setPatch(0).setBuild(321).build();
    private final int feature;
    private final int interim;
    private final int update;
    private final int patch;
    private final int build;

    private JavaVersion(Integer feature, int interim, int update, int patch, int build) {
        if (feature == null || feature.equals(0))
            throw new IllegalArgumentException(INVALID_FEATURE_VALUE_ERROR);
        this.feature = feature;
        this.interim = interim;
        this.update = update;
        this.patch = patch;
        this.build = build;
    }

    public static JavaVersion parse(String version) {
        if (version == null)
            throw new NullPointerException(VERSION_PARSE_ERROR);
        try {
            Matcher matcher = JAVA_VERSION_PATTERN.matcher(version);
            if (matcher.matches()) {
                return buildVersionFromNewPattern(matcher);
            }
        } catch (NumberFormatException e) {
            //do nothing
        }
        throw new IllegalArgumentException(VERSION_PARSE_ERROR);
    }

    private static JavaVersion buildVersionFromNewPattern(Matcher matcher) {
        Integer feature = getValue(matcher, "FEATURE", null);
        int interim = getValue(matcher, "INTERIM", 0);
        int update = getValue(matcher, "UPDATE", 0);
        int patch = getValue(matcher, "PATCH", 0);
        int build = getValue(matcher, "BUILD", 0);

        return new JavaVersion.Builder()
                .setFeature(feature)
                .setInterim(interim)
                .setUpdate(update)
                .setPatch(patch)
                .setBuild(build)
                .build();
    }

    private static Integer getValue(Matcher matcher, String group, Integer defaultValue) {
        String value = matcher.group(group);
        if (value == null)
            return defaultValue;
        else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Check if the provided java version has been patched for the bug JDK-8209178. Refer
     * <a href="https://bugs.openjdk.org/browse/JDK-8209178">JDK-8209178</a> to get the list of java versions that have
     * fixed the bug.
     *
     * @param runtimeVersion java version
     * @return true, if the java version has been patched for the bug, false otherwise
     */
    public static boolean javaVersionHasABug(String runtimeVersion) {
        try {
            JavaVersion javaVersion = parse(runtimeVersion);
            if (javaVersion.compareTo(JAVA_VERSION_14_0_0_0) >= 0) return false;
            else if (javaVersion.getFeature() == 13 && javaVersion.compareTo(JAVA_VERSION_13_0_2) >= 0) return false;
            else if (javaVersion.getFeature() == 11 && javaVersion.compareTo(JAVA_VERSION_11_0_6) >= 0) return false;
            else if (javaVersion.getFeature() == 1 && javaVersion.getInterim() == 8 && javaVersion.compareTo(JAVA_VERSION_1_8_0_0_321) >= 0) return false;
            else return true;
        } catch (Exception e) {
            // We were not able to determine the java version therefore, we default are assumption tha the current java
            // version is free from bug JDK-8209178
            return false;
        }
    }

    @Override
    public int compareTo(JavaVersion that) {
        if (that == null)
            throw new NullPointerException();
        if (!Objects.equals(getFeature(), that.getFeature()))
            return Integer.compare(getFeature(), that.getFeature());
        else if (!Objects.equals(getInterim(), that.getInterim()))
            return Integer.compare(getInterim(), that.getInterim());
        else if (!Objects.equals(getUpdate(), that.getUpdate()))
            return Integer.compare(getUpdate(), that.getUpdate());
        else if (!Objects.equals(getPatch(), that.getPatch()))
            return Integer.compare(getPatch(), that.getPatch());
        else if (!Objects.equals(getBuild(), that.getBuild()))
            return Integer.compare(getBuild(), that.getBuild());
        return 0;
    }

    public Integer getFeature() {
        return feature;
    }

    public int getInterim() {
        return interim;
    }

    public int getUpdate() {
        return update;
    }

    public int getPatch() {
        return patch;
    }

    public int getBuild() {
        return build;
    }

    @Override
    public String toString() {
        return "Version{" +
                "feature=" + feature +
                ", interim=" + interim +
                ", update=" + update +
                ", patch=" + patch +
                ", build=" + build +
                '}';
    }

    public static class Builder {
        private Integer feature;
        private int interim;
        private int update;
        private int patch;
        private int build;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setFeature(Integer feature) {
            this.feature = feature;
            return this;
        }

        public Builder setInterim(int interim) {
            this.interim = interim;
            return this;
        }

        public Builder setUpdate(int update) {
            this.update = update;
            return this;
        }

        public Builder setPatch(int patch) {
            this.patch = patch;
            return this;
        }

        public Builder setBuild(int build) {
            this.build = build;
            return this;
        }

        public JavaVersion build() {
            return new JavaVersion(feature, interim, update, patch, build);
        }
    }
}
