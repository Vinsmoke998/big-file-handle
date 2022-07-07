package com.example.demo.common;

public class Endpoints {

    public static final String API = "/api";
    public static final String FILE = "/file";
    public static final String EXPORT = "/export";
    public static final String FILE_EXTENSION = "/csv";
    public static final String IMPORT = "/import";
    public static final String ROLE = "/role";
    public static final String UPLOAD = "/upload";

    // file
    public final static String URL_FILE_EXPORT = API + FILE + EXPORT + FILE_EXTENSION;

    public final static String URL_FILE_IMPORT = API + FILE + IMPORT + FILE_EXTENSION;

    public final static String URL_FILE_UPLOAD = API + FILE + UPLOAD;

    // role
    public final static String URL_ROLE = API + ROLE;
}
