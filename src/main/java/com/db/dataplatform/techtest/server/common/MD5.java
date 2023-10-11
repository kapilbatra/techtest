package com.db.dataplatform.techtest.server.common;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

@Slf4j
public class MD5 {

    public static final String MD5 = "MD5";

    public static String calculateMD5Checksum(String inputBody) throws NoSuchAlgorithmException {
        if(inputBody != null){
            log.info("Calculation MD5 checksum for input");
            MessageDigest md = getInstance(MD5);
            md.update(inputBody.getBytes());
            byte[] digest = md.digest();
            String md5Checksum = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            return md5Checksum;
        }
        return null;

    }
}
