package com.db.dataplatform.techtest.api.model;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static com.db.dataplatform.techtest.TestDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataEnvelopeTests {

    @Test
    public void assignDataHeaderFieldsShouldWorkAsExpected() {
        DataHeader dataHeader = new DataHeader(new Long(1), TEST_NAME, BlockTypeEnum.BLOCKTYPEA, Instant.now());
        DataBody dataBody = new DataBody(new Long(1), new DataHeader(), DUMMY_DATA, CHECKSUM, Instant.now().toString());

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody);

        assertThat(dataEnvelope).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isNotNull();
        assertThat(dataEnvelope.getDataBody()).isNotNull();
        assertThat(dataEnvelope.getDataHeader()).isEqualTo(dataHeader);
        assertThat(dataEnvelope.getDataHeader()).isEqualTo(dataHeader);
        assertThat(dataBody.getDataBody()).isEqualTo(DUMMY_DATA);
        assertThat(dataBody.getChecksum()).isEqualTo(CHECKSUM);
    }
}
