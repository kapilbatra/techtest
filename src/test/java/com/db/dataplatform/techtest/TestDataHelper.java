package com.db.dataplatform.techtest;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;

import java.time.Instant;

public class TestDataHelper {

    public static final String TEST_NAME = "Test";
    public static final String TEST_NAME_EMPTY = "";
    public static final String DUMMY_DATA = "AKCp5fU4WNWKBVvhXsbNhqk33tawri9iJUkA5o4A6YqpwvAoYjajVw8xdEw6r9796h1wEp29D";
    public static final String CHECKSUM = "CECFD3953783DF706878AAEC2C22AA70";
    public static final String INVALID_CHECKSUM = "313131313131313";

    public static DataHeaderEntity createTestDataHeaderEntity(Instant expectedTimestamp) {
        DataHeaderEntity dataHeaderEntity = new DataHeaderEntity();
        dataHeaderEntity.setName(TEST_NAME);
        dataHeaderEntity.setBlockType(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity.setCreatedTimestamp(expectedTimestamp);
        return dataHeaderEntity;
    }

    public static DataBodyEntity createTestDataBodyEntity(DataHeaderEntity dataHeaderEntity) {
        DataBodyEntity dataBodyEntity = new DataBodyEntity();
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);
        dataBodyEntity.setDataBody(DUMMY_DATA);
        return dataBodyEntity;
    }

    public static DataEnvelope createTestDataEnvelopeApiObject() {
        DataBody dataBody = new DataBody(new Long(1), new DataHeader(), DUMMY_DATA, CHECKSUM, Instant.now().toString());
        DataHeader dataHeader = new DataHeader(new Long(1), TEST_NAME, BlockTypeEnum.BLOCKTYPEA, Instant.now());

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody);
        return dataEnvelope;
    }

    public static DataEnvelope createTestDataEnvelopeApiObject_InvalidChecksum() {
        DataBody dataBody = new DataBody(new Long(1), new DataHeader(), DUMMY_DATA, INVALID_CHECKSUM, Instant.now().toString());
        DataHeader dataHeader = new DataHeader(new Long(1), TEST_NAME, BlockTypeEnum.BLOCKTYPEA, Instant.now());

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody);
        return dataEnvelope;
    }

    public static DataEnvelope createTestDataEnvelopeApiObjectWithEmptyName() {
        DataBody dataBody = new DataBody(new Long(1), new DataHeader(), DUMMY_DATA, CHECKSUM, Instant.now().toString());
        DataHeader dataHeader = new DataHeader(new Long(1), TEST_NAME_EMPTY, BlockTypeEnum.BLOCKTYPEA, Instant.now());

        DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody);
        return dataEnvelope;
    }
}
