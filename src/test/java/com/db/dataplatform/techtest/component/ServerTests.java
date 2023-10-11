package com.db.dataplatform.techtest.component;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.exception.DataNotFoundException;
import com.db.dataplatform.techtest.server.mapper.ServerMapperConfiguration;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.impl.ServerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject_InvalidChecksum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServerTests {

    @Mock
    private DataBodyService dataBodyServiceImpl;

    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    @Mock
    private RestTemplate restTemplate;

    private Server server;

    public static final String URI_HADOOP_DATA_LAKE = "http://localhost:8090/hadoopserver/pushbigdata";


    @Before
    public void setup() {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));

        server = new ServerImpl(dataBodyServiceImpl, modelMapper);

        when(restTemplate.postForEntity(URI_HADOOP_DATA_LAKE, testDataEnvelope, String.class)).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));

    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException {
        testDataEnvelope = createTestDataEnvelopeApiObject_InvalidChecksum();
        boolean success = server.saveDataEnvelope(testDataEnvelope, restTemplate);
        assertThat(success).isFalse();
        verify(dataBodyServiceImpl, times(0)).saveDataBody(eq(expectedDataBodyEntity));
    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected_CheckSumNotMatching() throws NoSuchAlgorithmException, IOException {
        boolean success = server.saveDataEnvelope(testDataEnvelope, restTemplate);
        assertThat(success).isTrue();
        verify(dataBodyServiceImpl, times(1)).saveDataBody(eq(expectedDataBodyEntity));
    }

    @Test
    public void shouldGetDataEnvelopeAsExpected() throws Exception {
        String blockType = "BLOCKTYPEA";
        List list = new ArrayList();
        list.add(expectedDataBodyEntity);
        when(dataBodyServiceImpl.getDataByBlockType(BlockTypeEnum.valueOf(blockType))).thenReturn(list);
        List<DataEnvelope> dataEnvelopList = server.getData(blockType);
        assertTrue(dataEnvelopList.size() > 0);
        verify(dataBodyServiceImpl, times(1)).getDataByBlockType(eq(BlockTypeEnum.BLOCKTYPEA));
    }

    @Test
    public void shouldUpdateDataAsExpected() throws Exception {
        String blockName = "Test";
        String blockType = "BLOCKTYPEB";
        when(dataBodyServiceImpl.getDataByBlockName(blockName)).thenReturn(Optional.of(expectedDataBodyEntity));
        boolean result = server.updateData(blockName, blockType);

        assertThat(result).isTrue();
        verify(dataBodyServiceImpl, times(1)).getDataByBlockName(blockName);
    }

    @Test(expected = DataNotFoundException.class)
    public void shouldUpdateDataAsExpected_DataNotFoundException() throws Exception {
        String blockName = "Test";
        String blockType = "BLOCKTYPEB";
        server.updateData(blockName, blockType);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldUpdateDataAsExpected_BlockNameIsNull() throws Exception {
        String blockName = null;
        String blockType = "BLOCKTYPEB";
        server.updateData(blockName, blockType);
    }
}
