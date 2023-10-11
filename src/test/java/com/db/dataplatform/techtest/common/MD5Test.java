package com.db.dataplatform.techtest.common;

import com.db.dataplatform.techtest.server.common.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;

@RunWith(MockitoJUnitRunner.class)
public class MD5Test {

    @Test
    public void givenPassword_whenHashing_thenVerifying()
            throws NoSuchAlgorithmException {
        String checksum = "CECFD3953783DF706878AAEC2C22AA70";
        String inputBody = "AKCp5fU4WNWKBVvhXsbNhqk33tawri9iJUkA5o4A6YqpwvAoYjajVw8xdEw6r9796h1wEp29D";

        String calculateMD5Checksum = MD5.calculateMD5Checksum(inputBody);

        assertThat(calculateMD5Checksum.equals(checksum)).isTrue();
    }
}
