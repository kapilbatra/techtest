package com.db.dataplatform.techtest.server.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@JsonSerialize(as = DataBody.class)
@JsonDeserialize(as = DataBody.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataBody {

    @NotNull
    private Long dataStoreId;

    @NotNull
    private DataHeader dataHeaderEntity;

    @NotNull
    private String dataBody;

    @NotNull
    private String checksum;

    private String createdTimestamp;

}
