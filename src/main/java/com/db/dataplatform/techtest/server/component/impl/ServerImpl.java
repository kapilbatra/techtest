package com.db.dataplatform.techtest.server.component.impl;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.common.MD5;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.DataNotFoundException;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    public static final String URI_HADOOP_DATA_LAKE = "http://localhost:8090/hadoopserver/pushbigdata";

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope, RestTemplate restTemplate) throws NoSuchAlgorithmException {
        // Check if passed md5Checksum is valid or not
        String md5Checksum = MD5.calculateMD5Checksum(envelope.getDataBody().getDataBody());
        log.info("MD5 checksum calculated: {}", md5Checksum);
        if (!envelope.getDataBody().getChecksum().equals(md5Checksum)) {
            return false;
        }
        // Save to persistence.
        persist(envelope);

        log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());

        restTemplate.postForEntity(URI_HADOOP_DATA_LAKE, envelope, String.class);

        return true;
    }

    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        List<DataBodyEntity> dataBodyEntityList = dataBodyServiceImpl.getDataByBlockType(BlockTypeEnum.valueOf(blockType));
        List<DataEnvelope> dataEnvelopList = new ArrayList<>();
        dataBodyEntityList.stream()
                .forEach(getDataBodyEntityConsumer(dataEnvelopList));
        return dataEnvelopList;
    }

    private Consumer<DataBodyEntity> getDataBodyEntityConsumer(List<DataEnvelope> dataEnvelopList) {
        return p -> {
            DataBody dataBody = modelMapper.map(p, DataBody.class);
                DataEnvelope dataEnvelope = new DataEnvelope();
                dataEnvelope.setDataBody(dataBody);
                dataEnvelope.setDataHeader(dataBody.getDataHeaderEntity());
                dataEnvelopList.add(dataEnvelope);
        };
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        if (StringUtils.isEmpty(blockName) || blockName.equals("null")) {
            throw new ConstraintViolationException("blockName is null", null);
        }
        Optional<DataBodyEntity> dataBody = dataBodyServiceImpl.getDataByBlockName(blockName);
        if (dataBody.isPresent()) {
            DataHeaderEntity dataHeaderEntity = dataBody.get().getDataHeaderEntity();
            dataHeaderEntity.setBlockType(BlockTypeEnum.valueOf(newBlockType));

            dataBody.get().setDataHeaderEntity(dataHeaderEntity);

            saveData(dataBody.get());
            return true;
        } else {
            throw new DataNotFoundException("No Data found for given blockName!!");
        }
    }

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }


}
