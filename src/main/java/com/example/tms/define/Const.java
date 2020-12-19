package com.example.tms.define;

public final class Const {

    public static class SystemConfig {
        public static final Integer SEARCH_MAX_SIZE = 10000;
        public static final String SEARCH_MAX_SIZE_STRING = "10000";
        public static final Integer DOWNLOAD_MAX_SIZE = 10000;
        public static final String DOWNLOAD_MAX_SIZE_STRING = "10000";
    }

    public static class Message {
        public static final String NOT_FOUND = "Not Found";
        public static final String OPTIMISTIC_LOCK_EXCEPTION = "Changed by othes";
    }
}
