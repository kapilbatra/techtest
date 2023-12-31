package com.db.dataplatform.techtest.server.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "DATA_STORE")
@Setter
@Getter
public class DataBodyEntity {

    @Id
    @SequenceGenerator(name = "dataStoreSequenceGenerator", sequenceName = "SEQ_DATA_STORE", allocationSize = 1)
    @GeneratedValue(generator = "dataStoreSequenceGenerator")
    @Column(name = "DATA_STORE_ID", insertable = false, updatable = false)
    private Long dataStoreId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "DATA_HEADER_ID")
    private DataHeaderEntity dataHeaderEntity;

    @Column(name = "DATA_BODY")
    private String dataBody;

    @Column(name = "CHECKSUM")
    private String checksum;

    @Column(name = "CREATED_TIMESTAMP")
    private Instant createdTimestamp;

    @PrePersist
    public void setTimestamps() {
        if (createdTimestamp == null) {
            createdTimestamp = Instant.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBodyEntity dataBodyEntity = (DataBodyEntity) o;
        return Objects.equals(dataStoreId, dataBodyEntity.dataStoreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataStoreId);
    }

    }
