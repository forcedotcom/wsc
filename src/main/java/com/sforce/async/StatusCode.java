/*
 * Copyright (c) 2013, salesforce.com, inc.
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

package com.sforce.async;

/**
 * StatusCode --
 *
 * @author mcheenath
 * @since 160
 */
public enum StatusCode {

   ALL_OR_NONE_OPERATION_ROLLED_BACK,


  /**
   * Enumeration  : ALREADY_IN_PROCESS
   */
   ALREADY_IN_PROCESS,

  /**
   * Enumeration  : ASSIGNEE_TYPE_REQUIRED
   */
   ASSIGNEE_TYPE_REQUIRED,

  /**
   * Enumeration  : BAD_CUSTOM_ENTITY_PARENT_DOMAIN
   */
   BAD_CUSTOM_ENTITY_PARENT_DOMAIN,

  /**
   * Enumeration  : BCC_NOT_ALLOWED_IF_BCC_COMPLIANCE_ENABLED
   */
   BCC_NOT_ALLOWED_IF_BCC_COMPLIANCE_ENABLED,

  /**
   * Enumeration  : CANNOT_CASCADE_PRODUCT_ACTIVE
   */
   CANNOT_CASCADE_PRODUCT_ACTIVE,

  /**
   * Enumeration  : CANNOT_CHANGE_FIELD_TYPE_OF_APEX_REFERENCED_FIELD
   */
   CANNOT_CHANGE_FIELD_TYPE_OF_APEX_REFERENCED_FIELD,

  /**
   * Enumeration  : CANNOT_CREATE_ANOTHER_MANAGED_PACKAGE
   */
   CANNOT_CREATE_ANOTHER_MANAGED_PACKAGE,

  /**
   * Enumeration  : CANNOT_DEACTIVATE_DIVISION
   */
   CANNOT_DEACTIVATE_DIVISION,

  /**
   * Enumeration  : CANNOT_DELETE_LAST_DATED_CONVERSION_RATE
   */
   CANNOT_DELETE_LAST_DATED_CONVERSION_RATE,

  /**
   * Enumeration  : CANNOT_DELETE_MANAGED_OBJECT
   */
   CANNOT_DELETE_MANAGED_OBJECT,

  /**
   * Enumeration  : CANNOT_DISABLE_LAST_ADMIN
   */
   CANNOT_DISABLE_LAST_ADMIN,

  /**
   * Enumeration  : CANNOT_ENABLE_IP_RESTRICT_REQUESTS
   */
   CANNOT_ENABLE_IP_RESTRICT_REQUESTS,

  /**
   * Enumeration  : CANNOT_INSERT_UPDATE_ACTIVATE_ENTITY
   */
   CANNOT_INSERT_UPDATE_ACTIVATE_ENTITY,

  /**
   * Enumeration  : CANNOT_MODIFY_MANAGED_OBJECT
   */
   CANNOT_MODIFY_MANAGED_OBJECT,

  /**
   * Enumeration  : CANNOT_RENAME_APEX_REFERENCED_FIELD
   */
   CANNOT_RENAME_APEX_REFERENCED_FIELD,

  /**
   * Enumeration  : CANNOT_RENAME_APEX_REFERENCED_OBJECT
   */
   CANNOT_RENAME_APEX_REFERENCED_OBJECT,

  /**
   * Enumeration  : CANNOT_REPARENT_RECORD
   */
   CANNOT_REPARENT_RECORD,

  /**
   * Enumeration  : CANNOT_UPDATE_CONVERTED_LEAD
   */
   CANNOT_UPDATE_CONVERTED_LEAD,

  /**
   * Enumeration  : CANT_DISABLE_CORP_CURRENCY
   */
   CANT_DISABLE_CORP_CURRENCY,

  /**
   * Enumeration  : CANT_UNSET_CORP_CURRENCY
   */
   CANT_UNSET_CORP_CURRENCY,

  /**
   * Enumeration  : CHILD_SHARE_FAILS_PARENT
   */
   CHILD_SHARE_FAILS_PARENT,

  /**
   * Enumeration  : CIRCULAR_DEPENDENCY
   */
   CIRCULAR_DEPENDENCY,

  /**
   * Enumeration  : CUSTOM_CLOB_FIELD_LIMIT_EXCEEDED
   */
   CUSTOM_CLOB_FIELD_LIMIT_EXCEEDED,

  /**
   * Enumeration  : CUSTOM_ENTITY_OR_FIELD_LIMIT
   */
   CUSTOM_ENTITY_OR_FIELD_LIMIT,

  /**
   * Enumeration  : CUSTOM_FIELD_INDEX_LIMIT_EXCEEDED
   */
   CUSTOM_FIELD_INDEX_LIMIT_EXCEEDED,

  /**
   * Enumeration  : CUSTOM_INDEX_EXISTS
   */
   CUSTOM_INDEX_EXISTS,

  /**
   * Enumeration  : CUSTOM_LINK_LIMIT_EXCEEDED
   */
   CUSTOM_LINK_LIMIT_EXCEEDED,

  /**
   * Enumeration  : CUSTOM_TAB_LIMIT_EXCEEDED
   */
   CUSTOM_TAB_LIMIT_EXCEEDED,

  /**
   * Enumeration  : DELETE_FAILED
   */
   DELETE_FAILED,

  /**
   * Enumeration  : DELETE_REQUIRED_ON_CASCADE
   */
   DELETE_REQUIRED_ON_CASCADE,

  /**
   * Enumeration  : DEPENDENCY_EXISTS
   */
   DEPENDENCY_EXISTS,

  /**
   * Enumeration  : DUPLICATE_CASE_SOLUTION
   */
   DUPLICATE_CASE_SOLUTION,

  /**
   * Enumeration  : DUPLICATE_COMM_NICKNAME
   */
   DUPLICATE_COMM_NICKNAME,

  /**
   * Enumeration  : DUPLICATE_CUSTOM_ENTITY_DEFINITION
   */
   DUPLICATE_CUSTOM_ENTITY_DEFINITION,

  /**
   * Enumeration  : DUPLICATE_CUSTOM_TAB_MOTIF
   */
   DUPLICATE_CUSTOM_TAB_MOTIF,

  /**
   * Enumeration  : DUPLICATE_DEVELOPER_NAME
   */
   DUPLICATE_DEVELOPER_NAME,

  /**
   * Enumeration  : DUPLICATE_EXTERNAL_ID
   */
   DUPLICATE_EXTERNAL_ID,

  /**
   * Enumeration  : DUPLICATE_MASTER_LABEL
   */
   DUPLICATE_MASTER_LABEL,

  /**
   * Enumeration  : DUPLICATE_USERNAME
   */
   DUPLICATE_USERNAME,

  /**
   * Enumeration  : DUPLICATE_VALUE
   */
   DUPLICATE_VALUE,

  /**
   * Enumeration  : EMAIL_NOT_PROCESSED_DUE_TO_PRIOR_ERROR
   */
   EMAIL_NOT_PROCESSED_DUE_TO_PRIOR_ERROR,

  /**
   * Enumeration  : EMPTY_SCONTROL_FILE_NAME
   */
   EMPTY_SCONTROL_FILE_NAME,

  /**
   * Enumeration  : ENTITY_FAILED_IFLASTMODIFIED_ON_UPDATE
   */
   ENTITY_FAILED_IFLASTMODIFIED_ON_UPDATE,

  /**
   * Enumeration  : ENTITY_IS_ARCHIVED
   */
   ENTITY_IS_ARCHIVED,

  /**
   * Enumeration  : ENTITY_IS_DELETED
   */
   ENTITY_IS_DELETED,

  /**
   * Enumeration  : ENTITY_IS_LOCKED
   */
   ENTITY_IS_LOCKED,

  /**
   * Enumeration  : ERROR_IN_MAILER
   */
   ERROR_IN_MAILER,

  /**
   * Enumeration  : FAILED_ACTIVATION
   */
   FAILED_ACTIVATION,

  /**
   * Enumeration  : FIELD_CUSTOM_VALIDATION_EXCEPTION
   */
   FIELD_CUSTOM_VALIDATION_EXCEPTION,

  /**
   * Enumeration  : FIELD_INTEGRITY_EXCEPTION
   */
   FIELD_INTEGRITY_EXCEPTION,

  /**
   * Enumeration  : HTML_FILE_UPLOAD_NOT_ALLOWED
   */
   HTML_FILE_UPLOAD_NOT_ALLOWED,

  /**
   * Enumeration  : IMAGE_TOO_LARGE
   */
   IMAGE_TOO_LARGE,

  /**
   * Enumeration  : INACTIVE_OWNER_OR_USER
   */
   INACTIVE_OWNER_OR_USER,

  /**
   * Enumeration  : INSUFFICIENT_ACCESS_ON_CROSS_REFERENCE_ENTITY
   */
   INSUFFICIENT_ACCESS_ON_CROSS_REFERENCE_ENTITY,

  /**
   * Enumeration  : INSUFFICIENT_ACCESS_OR_READONLY
   */
   INSUFFICIENT_ACCESS_OR_READONLY,

  /**
   * Enumeration  : INVALID_ACCESS_LEVEL
   */
   INVALID_ACCESS_LEVEL,

  /**
   * Enumeration  : INVALID_ARGUMENT_TYPE
   */
   INVALID_ARGUMENT_TYPE,

  /**
   * Enumeration  : INVALID_ASSIGNEE_TYPE
   */
   INVALID_ASSIGNEE_TYPE,

  /**
   * Enumeration  : INVALID_ASSIGNMENT_RULE
   */
   INVALID_ASSIGNMENT_RULE,

  /**
   * Enumeration  : INVALID_BATCH_OPERATION
   */
   INVALID_BATCH_OPERATION,

  /**
   * Enumeration  : INVALID_CREDIT_CARD_INFO
   */
   INVALID_CREDIT_CARD_INFO,

  /**
   * Enumeration  : INVALID_CROSS_REFERENCE_KEY
   */
   INVALID_CROSS_REFERENCE_KEY,

  /**
   * Enumeration  : INVALID_CROSS_REFERENCE_TYPE_FOR_FIELD
   */
   INVALID_CROSS_REFERENCE_TYPE_FOR_FIELD,

  /**
   * Enumeration  : INVALID_CURRENCY_CONV_RATE
   */
   INVALID_CURRENCY_CONV_RATE,

  /**
   * Enumeration  : INVALID_CURRENCY_CORP_RATE
   */
   INVALID_CURRENCY_CORP_RATE,

  /**
   * Enumeration  : INVALID_CURRENCY_ISO
   */
   INVALID_CURRENCY_ISO,

  /**
   * Enumeration  : INVALID_EMAIL_ADDRESS
   */
   INVALID_EMAIL_ADDRESS,

  /**
   * Enumeration  : INVALID_EMPTY_KEY_OWNER
   */
   INVALID_EMPTY_KEY_OWNER,

  /**
   * Enumeration  : INVALID_FIELD
   */
   INVALID_FIELD,

  /**
   * Enumeration  : INVALID_FIELD_FOR_INSERT_UPDATE
   */
   INVALID_FIELD_FOR_INSERT_UPDATE,

  /**
   * Enumeration  : INVALID_FIELD_WHEN_USING_TEMPLATE
   */
   INVALID_FIELD_WHEN_USING_TEMPLATE,

  /**
   * Enumeration  : INVALID_FILTER_ACTION
   */
   INVALID_FILTER_ACTION,

  /**
   * Enumeration  : INVALID_GOOGLE_DOCS_URL
   */
   INVALID_GOOGLE_DOCS_URL,

  /**
   * Enumeration  : INVALID_ID_FIELD
   */
   INVALID_ID_FIELD,

  /**
   * Enumeration  : INVALID_INET_ADDRESS
   */
   INVALID_INET_ADDRESS,

  /**
   * Enumeration  : INVALID_LINEITEM_CLONE_STATE
   */
   INVALID_LINEITEM_CLONE_STATE,

  /**
   * Enumeration  : INVALID_MASTER_OR_TRANSLATED_SOLUTION
   */
   INVALID_MASTER_OR_TRANSLATED_SOLUTION,

  /**
   * Enumeration  : INVALID_OPERATION
   */
   INVALID_OPERATION,

  /**
   * Enumeration  : INVALID_OPERATOR
   */
   INVALID_OPERATOR,

  /**
   * Enumeration  : INVALID_OR_NULL_FOR_RESTRICTED_PICKLIST
   */
   INVALID_OR_NULL_FOR_RESTRICTED_PICKLIST,

  /**
   * Enumeration  : INVALID_PARTNER_NETWORK_STATUS
   */
   INVALID_PARTNER_NETWORK_STATUS,

  /**
   * Enumeration  : INVALID_PERSON_ACCOUNT_OPERATION
   */
   INVALID_PERSON_ACCOUNT_OPERATION,

  /**
   * Enumeration  : INVALID_SAVE_AS_ACTIVITY_FLAG
   */
   INVALID_SAVE_AS_ACTIVITY_FLAG,

  /**
   * Enumeration  : INVALID_SESSION_ID
   */
   INVALID_SESSION_ID,

  /**
   * Enumeration  : INVALID_SETUP_OWNER
   */
   INVALID_SETUP_OWNER,

   /**
    * Enumeration : INVALID_SIGNUP_COUNTRY
    */
   INVALID_SIGNUP_COUNTRY,
   
   /**
    * Enumeration : INVALID_OAUTH_URL
    */
   INVALID_OAUTH_URL,
   
  /**
   * Enumeration  : INVALID_STATUS
   */
   INVALID_STATUS,

  /**
   * Enumeration  : INVALID_TYPE
   */
   INVALID_TYPE,

  /**
   * Enumeration  : INVALID_TYPE_FOR_OPERATION
   */
   INVALID_TYPE_FOR_OPERATION,

  /**
   * Enumeration  : INVALID_TYPE_ON_FIELD_IN_RECORD
   */
   INVALID_TYPE_ON_FIELD_IN_RECORD,

  /**
   * Enumeration  : IP_RANGE_LIMIT_EXCEEDED
   */
   IP_RANGE_LIMIT_EXCEEDED,

  /**
   * Enumeration  : LICENSE_LIMIT_EXCEEDED
   */
   LICENSE_LIMIT_EXCEEDED,

  /**
   * Enumeration  : LIMIT_EXCEEDED
   */
   LIMIT_EXCEEDED,

  /**
   * Enumeration  : MALFORMED_ID
   */
   MALFORMED_ID,

  /**
   * Enumeration  : MANAGER_NOT_DEFINED
   */
   MANAGER_NOT_DEFINED,

  /**
   * Enumeration  : MASSMAIL_RETRY_LIMIT_EXCEEDED
   */
   MASSMAIL_RETRY_LIMIT_EXCEEDED,

  /**
   * Enumeration  : MASS_MAIL_LIMIT_EXCEEDED
   */
   MASS_MAIL_LIMIT_EXCEEDED,

  /**
   * Enumeration  : MAXIMUM_CCEMAILS_EXCEEDED
   */
   MAXIMUM_CCEMAILS_EXCEEDED,

  /**
   * Enumeration  : MAXIMUM_DASHBOARD_COMPONENTS_EXCEEDED
   */
   MAXIMUM_DASHBOARD_COMPONENTS_EXCEEDED,

  /**
   * Enumeration  : MAXIMUM_HIERARCHY_LEVELS_REACHED
   */
   MAXIMUM_HIERARCHY_LEVELS_REACHED,

  /**
   * Enumeration  : MAXIMUM_SIZE_OF_ATTACHMENT
   */
   MAXIMUM_SIZE_OF_ATTACHMENT,

  /**
   * Enumeration  : MAXIMUM_SIZE_OF_DOCUMENT
   */
   MAXIMUM_SIZE_OF_DOCUMENT,

  /**
   * Enumeration  : MAX_ACTIONS_PER_RULE_EXCEEDED
   */
   MAX_ACTIONS_PER_RULE_EXCEEDED,

  /**
   * Enumeration  : MAX_ACTIVE_RULES_EXCEEDED
   */
   MAX_ACTIVE_RULES_EXCEEDED,

  /**
   * Enumeration  : MAX_APPROVAL_STEPS_EXCEEDED
   */
   MAX_APPROVAL_STEPS_EXCEEDED,

  /**
   * Enumeration  : MAX_FORMULAS_PER_RULE_EXCEEDED
   */
   MAX_FORMULAS_PER_RULE_EXCEEDED,

  /**
   * Enumeration  : MAX_RULES_EXCEEDED
   */
   MAX_RULES_EXCEEDED,

  /**
   * Enumeration  : MAX_RULE_ENTRIES_EXCEEDED
   */
   MAX_RULE_ENTRIES_EXCEEDED,

  /**
   * Enumeration  : MAX_TASK_DESCRIPTION_EXCEEEDED
   */
   MAX_TASK_DESCRIPTION_EXCEEEDED,

  /**
   * Enumeration  : MAX_TM_RULES_EXCEEDED
   */
   MAX_TM_RULES_EXCEEDED,

  /**
   * Enumeration  : MAX_TM_RULE_ITEMS_EXCEEDED
   */
   MAX_TM_RULE_ITEMS_EXCEEDED,

  /**
   * Enumeration  : MERGE_FAILED
   */
   MERGE_FAILED,

  /**
   * Enumeration  : MISSING_ARGUMENT
   */
   MISSING_ARGUMENT,

  /**
   * Enumeration  : MIXED_DML_OPERATION
   */
   MIXED_DML_OPERATION,

  /**
   * Enumeration  : NONUNIQUE_SHIPPING_ADDRESS
   */
   NONUNIQUE_SHIPPING_ADDRESS,

  /**
   * Enumeration  : NO_APPLICABLE_PROCESS
   */
   NO_APPLICABLE_PROCESS,

  /**
   * Enumeration  : NO_ATTACHMENT_PERMISSION
   */
   NO_ATTACHMENT_PERMISSION,

  /**
   * Enumeration  : NO_MASS_MAIL_PERMISSION
   */
   NO_MASS_MAIL_PERMISSION,

  /**
   * Enumeration  : NUMBER_OUTSIDE_VALID_RANGE
   */
   NUMBER_OUTSIDE_VALID_RANGE,

  /**
   * Enumeration  : NUM_HISTORY_FIELDS_BY_SOBJECT_EXCEEDED
   */
   NUM_HISTORY_FIELDS_BY_SOBJECT_EXCEEDED,

  /**
   * Enumeration  : OPTED_OUT_OF_MASS_MAIL
   */
   OPTED_OUT_OF_MASS_MAIL,

  /**
   * Enumeration  : PACKAGE_LICENSE_REQUIRED
   */
   PACKAGE_LICENSE_REQUIRED,

  /**
   * Enumeration  : PORTAL_USER_ALREADY_EXISTS_FOR_CONTACT
   */
   PORTAL_USER_ALREADY_EXISTS_FOR_CONTACT,

  /**
   * Enumeration  : PRIVATE_CONTACT_ON_ASSET
   */
   PRIVATE_CONTACT_ON_ASSET,

  /**
   * Enumeration  : RECORD_IN_USE_BY_WORKFLOW
   */
   RECORD_IN_USE_BY_WORKFLOW,

  /**
   * Enumeration  : REQUEST_RUNNING_TOO_LONG
   */
   REQUEST_RUNNING_TOO_LONG,

  /**
   * Enumeration  : REQUIRED_FIELD_MISSING
   */
   REQUIRED_FIELD_MISSING,

  /**
   * Enumeration  : SELF_REFERENCE_FROM_TRIGGER
   */
   SELF_REFERENCE_FROM_TRIGGER,

  /**
   * Enumeration  : SHARE_NEEDED_FOR_CHILD_OWNER
   */
   SHARE_NEEDED_FOR_CHILD_OWNER,

  /**
   * Enumeration  : STANDARD_PRICE_NOT_DEFINED
   */
   STANDARD_PRICE_NOT_DEFINED,

  /**
   * Enumeration  : STORAGE_LIMIT_EXCEEDED
   */
   STORAGE_LIMIT_EXCEEDED,

  /**
   * Enumeration  : STRING_TOO_LONG
   */
   STRING_TOO_LONG,

  /**
   * Enumeration  : TABSET_LIMIT_EXCEEDED
   */
   TABSET_LIMIT_EXCEEDED,

  /**
   * Enumeration  : TEMPLATE_NOT_ACTIVE
   */
   TEMPLATE_NOT_ACTIVE,

  /**
   * Enumeration  : TERRITORY_REALIGN_IN_PROGRESS
   */
   TERRITORY_REALIGN_IN_PROGRESS,

  /**
   * Enumeration  : TEXT_DATA_OUTSIDE_SUPPORTED_CHARSET
   */
   TEXT_DATA_OUTSIDE_SUPPORTED_CHARSET,

  /**
   * Enumeration  : TOO_MANY_APEX_REQUESTS
   */
   TOO_MANY_APEX_REQUESTS,

  /**
   * Enumeration  : TOO_MANY_ENUM_VALUE
   */
   TOO_MANY_ENUM_VALUE,

  /**
   * Enumeration  : TRANSFER_REQUIRES_READ
   */
   TRANSFER_REQUIRES_READ,

  /**
   * Enumeration  : UNABLE_TO_LOCK_ROW
   */
   UNABLE_TO_LOCK_ROW,

  /**
   * Enumeration  : UNAVAILABLE_RECORDTYPE_EXCEPTION
   */
   UNAVAILABLE_RECORDTYPE_EXCEPTION,

  /**
   * Enumeration  : UNDELETE_FAILED
   */
   UNDELETE_FAILED,

  /**
   * Enumeration  : UNKNOWN_EXCEPTION
   */
   UNKNOWN_EXCEPTION,

  /**
   * Enumeration  : UNSPECIFIED_EMAIL_ADDRESS
   */
   UNSPECIFIED_EMAIL_ADDRESS,

  /**
   * Enumeration  : UNSUPPORTED_APEX_TRIGGER_OPERATON
   */
   UNSUPPORTED_APEX_TRIGGER_OPERATON,

  /**
   * Enumeration  : WEBLINK_SIZE_LIMIT_EXCEEDED
   */
   WEBLINK_SIZE_LIMIT_EXCEEDED,
   
   
   WEBLINK_URL_INVALID,

  /**
   * Enumeration  : WRONG_CONTROLLER_TYPE
   */
   WRONG_CONTROLLER_TYPE,


}
