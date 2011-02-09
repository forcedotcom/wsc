package com.sforce.async;

/**
 * AsyncExceptionCode --
 *
 * @author mcheenath
 * @since 160
 */

public enum AsyncExceptionCode {
    Unknown,
    InvalidSessionId,
    InvalidOperation,
    InvalidUrl,
    InvalidUser,
    InvalidXml,
    FeatureNotEnabled,
    ExceededQuota,
    InvalidJob,
    InvalidJobState,
    InvalidBatch,
    InternalServerError,
    ClientInputError,
    Timeout,
    TooManyLockFailure,
    InvalidVersion,
    HttpsRequired,
    UnsupportedContentType
}
