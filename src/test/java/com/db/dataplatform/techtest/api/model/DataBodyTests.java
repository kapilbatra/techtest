package com.db.dataplatform.techtest.api.model;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataBodyTests {

    public static final String DUMMY_DATA = "AKCp5fU4WNWKBVvhXsbNhqk33tawri9iJUkA5o4A6YqpwvAoYjajVw8xdEw6r9796h1wEp29D";

    public static final String CHECKSUM = "CECFD3953783DF706878AAEC2C22AA70";

    @Test
    public void assignDataBodyFieldsShouldWorkAsExpected() {
        DataBody dataBody = new DataBody(new Long(1), new DataHeader(), DUMMY_DATA, CHECKSUM, Instant.now().toString());

        assertThat(dataBody).isNotNull();
        assertThat(dataBody.getDataBody()).isEqualTo(DUMMY_DATA);
        assertThat(dataBody.getChecksum()).isEqualTo(CHECKSUM);
    }
}
