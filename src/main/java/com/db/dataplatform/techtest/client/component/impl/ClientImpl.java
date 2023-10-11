package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.RestTemplateConfiguration;
import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.client.exception.DataClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

    @Autowired
    RestTemplateConfiguration restTemplateConfiguration;

    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
        try {
            RestTemplate restTemplate = restTemplateConfiguration.createRestTemplate(
                    new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter());

            ResponseEntity responseEntity = restTemplate.exchange(URI_PUSHDATA, HttpMethod.POST,
                    new HttpEntity<>(dataEnvelope), String.class);
            log.info("responseEntity {}" + responseEntity);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug(responseEntity.getBody().toString());
                throw new DataClientException("Non 200 response from Server");
            }
        }catch (RestClientException ex){
            throw new DataClientException(ex);
        }
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        try {
            RestTemplate restTemplate = restTemplateConfiguration.createRestTemplate(
                    new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter());

            final ParameterizedTypeReference<List<DataEnvelope>> response  = new ParameterizedTypeReference<List<DataEnvelope>>(){
            };

            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("blockType", blockType);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URI_GETDATA.toString());

            final ResponseEntity<List<DataEnvelope>> responseEntity = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.GET,
                    null, response);
            log.info("responseEntity {}" + responseEntity);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug(responseEntity.getBody().toString());
                throw new DataClientException("Non 200 response from Server");
            }
        }catch (RestClientException ex){
            throw new DataClientException(ex);
        }
        return null;
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        try {
            RestTemplate restTemplate = restTemplateConfiguration.createRestTemplate(
                    new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter());

            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("name", blockName);
            urlParams.put("newBlockType", newBlockType);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URI_PATCHDATA.toString());

            ResponseEntity responseEntity = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri() , HttpMethod.PATCH,
                    null, String.class);
            log.info("responseEntity {}" + responseEntity);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.debug(responseEntity.getBody().toString());
                throw new DataClientException("Non 200 response from Server");
            }
        }catch (RestClientException ex){
            throw new DataClientException(ex);
        }
        return true;
    }


}
