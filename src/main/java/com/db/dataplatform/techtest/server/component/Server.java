package com.db.dataplatform.techtest.server.component;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Server {
    boolean saveDataEnvelope(DataEnvelope envelope, RestTemplate restTemplate) throws IOException, NoSuchAlgorithmException;

    List<DataEnvelope> getData(String blockType);

    boolean updateData(String blockName, String newBlockType) throws UnsupportedEncodingException;
}
