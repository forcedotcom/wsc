package com.sforce.bulk;

/**
 * This class represents
 * <p/>
 * User: mcheenath
 * Date: Dec 15, 2010
 */
public class UpdateResult {
    private String id;
    private boolean success;
    private boolean created;
    private String error;

    public UpdateResult(String id, boolean success, boolean created, String error) {
        this.id = id;
        this.success = success;
        this.created = created;
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isCreated() {
        return created;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "UpdateResult{" +
                "id='" + id + '\'' +
                ", success=" + success +
                ", created=" + created +
                ", error='" + error + '\'' +
                '}';
    }
}


