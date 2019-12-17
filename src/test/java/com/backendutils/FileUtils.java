package com.backendutils;

import com.google.common.io.Resources;

public class FileUtils {
    public static String getFullResourcePath(String resourceFileName){
        return Resources.getResource(resourceFileName).toString().substring(6);
    }
}
