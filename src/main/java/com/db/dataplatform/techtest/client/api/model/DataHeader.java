package com.db.dataplatform.techtest.client.api.model;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@JsonSerialize(as = DataHeader.class)
@JsonDeserialize(as = DataHeader.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataHeader {

    private Long dataHeaderId;

    @NotBlank
    private String name;

    @NotBlank
    private BlockTypeEnum blockType;

    private Instant createdTimestamp;

}
