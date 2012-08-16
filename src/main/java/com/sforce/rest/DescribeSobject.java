/*
 * Copyright (c) 2011, salesforce.com, inc.
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
package com.sforce.rest;

import java.util.Map;

/**
 * This class represents a JSON response from /services/data/v{version}/sobjects/{sobjectName}
 * 
 * @author gwester
 * @since 172
 */
public class DescribeSobject {
    private SobjectDescribe objectDescribe;

    public SobjectDescribe getObjectDescribe() {
        return objectDescribe;
    }
    
    /**
     * This class represents part of a JSON response from high level resource describe calls.
     * 
     * @author gwester
     * @since 170
     */
    public static class SobjectDescribe {
        private String name;
        private String label;
        private Boolean custom;
        private String keyPrefix;
        private String labelPlural;
        private Boolean layoutable;
        private Boolean activateable;
        private Boolean updateable;
        private Map<String, String> urls;
        private Boolean createable;
        private Boolean deletable;
        private Boolean feedEnabled;
        private Boolean queryable;
        private Boolean replicateable;
        private Boolean retrieveable;
        private Boolean undeletable;
        private Boolean triggerable;
        private Boolean mergeable;
        private Boolean deprecatedAndHidden;
        private Boolean customSetting;
        private Boolean searchable;
        
        public String getName() {
            return name;
        }
        public String getLabel() {
            return label;
        }
        public Boolean isCustom() {
            return custom;
        }
        public String getKeyPrefix() {
            return keyPrefix;
        }
        public String getLabelPlural() {
            return labelPlural;
        }
        public Boolean isLayoutable() {
            return layoutable;
        }
        public Boolean isActivateable() {
            return activateable;
        }
        public Boolean isUpdateable() {
            return updateable;
        }
        public Map<String, String> getUrls() {
            return urls;
        }
        public Boolean isCreateable() {
            return createable;
        }
        public Boolean isDeletable() {
            return deletable;
        }
        public Boolean isFeedEnabled() {
            return feedEnabled;
        }
        public Boolean isQueryable() {
            return queryable;
        }
        public Boolean isReplicateable() {
            return replicateable;
        }
        public Boolean isRetrieveable() {
            return retrieveable;
        }
        public Boolean isUndeletable() {
            return undeletable;
        }
        public Boolean isTriggerable() {
            return triggerable;
        }
        public Boolean isMergeable() {
            return mergeable;
        }
        public Boolean isDeprecatedAndHidden() {
            return deprecatedAndHidden;
        }
        public Boolean isCustomSetting() {
            return customSetting;
        }
        public Boolean isSearchable() {
            return searchable;
        }
    }
}
