package io.shace.app.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melvin on 8/10/14.
 */
public class ApiError {
    private int code;
    private String type;
    private Return returns;
    private List<Parameter> parameters = new ArrayList<Parameter>();

    public static final int JSON_REQUIRED = 100;
    public static final int PARAMETERS_ERROR = 101;
    public static final int TOKEN_NOT_FOUND = 200;
    public static final int INVALID_IDS = 201;
    public static final int ALREADY_CONNECTED = 202;
    public static final int BETA_PROCESSING = 203;
    public static final int NO_INVITATIONS = 204;
    public static final int REQUEST_BETA_SENT = 205;
    public static final int ACCESS_TOKEN_REQUIRED = 206;
    public static final int NEED_AUTHENTICATION = 207;
    public static final int NEED_ANONYMOUS = 208;
    public static final int NEED_ADMIN = 209;
    public static final int ACCESS_TOKEN_ERROR = 210;
    public static final int LANGUAGE_NOT_FOUND = 300;
    public static final int EVENT_NOT_FOUND = 400;
    public static final int MEDIA_NOT_FOUND = 401;
    public static final int COMMENT_NOT_FOUND = 402;
    public static final int NEED_ADMINISTRATE = 403;
    public static final int NO_PASSWORD = 404;
    public static final int WRONG_PASSWORD = 405;
    public static final int READING_TOO_STRONG = 406;
    public static final int EMPTY_MEDIA_LIST = 407;
    public static final int NEED_OWNER = 408;
    public static final int BAD_FORMAT_IMAGE = 409;
    public static final int TAG_NOT_FOUND = 410;
    public static final int NEED_PASSWORD = 411;
    public static final int EVENT_FORBIDDEN = 412;
    public static final int USER_FORBIDDEN = 413;
    public static final int USER_NOT_FOUND = 500;

    public enum Return {
        BAD_REQUEST,
        NOT_FOUND,
        UNAUTHORIZED,
        FORBIDDEN
    }

    public boolean is(int code) {
        return this.code == code;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public Return getReturns() {
        return returns;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReturns(Return returns) {
        this.returns = returns;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    protected static class Parameter {
        public enum Type {
            REQUIRED,
            DUPLICATE,
            FORMAT
        }

        public Type type;
        public String field;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}