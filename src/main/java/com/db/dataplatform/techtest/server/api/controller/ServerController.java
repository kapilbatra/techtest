package com.db.dataplatform.techtest.server.api.controller;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.ChecksumNotMatchingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {
    private final Server server;

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope, RestTemplate restTemplate) throws IOException, NoSuchAlgorithmException {

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope, restTemplate);

        if(!checksumPass){
            log.info("MD5 checksum passed is not matching with calculated checksum hence data envelope will not be persisted for attribute name: {}", dataEnvelope.getDataHeader().getName());
            throw new ChecksumNotMatchingException("MD5 checksum passed is not matching with calculated checksum hence data envelope will not be persisted for attribute name: "+ dataEnvelope.getDataHeader().getName());
        }

        log.info("Data envelope persisted in DB and hadoop data lake. Attribute name: {}", dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }

    @GetMapping(path = "/data/{blockType}")
    public List<DataEnvelope> getData(@PathVariable("blockType") final String blockType) {
        log.info("blockType received: {}", blockType);
        List<DataEnvelope> list = server.getData(blockType);
       return list;
    }


    @PatchMapping(path = "/update/{blockName}/{newBlockType}")
    public ResponseEntity<Boolean> updateData(@PathVariable("blockName") final String blockName, @PathVariable("newBlockType") final String newBlockType) throws IOException {
        log.info("blockName and newBlockType received: {} {}", blockName, newBlockType);
        boolean updated = server.updateData(blockName, newBlockType);
        return ResponseEntity.ok(updated);
    }
}
