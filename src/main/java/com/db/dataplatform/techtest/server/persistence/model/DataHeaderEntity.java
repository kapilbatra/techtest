package com.db.dataplatform.techtest.server.persistence.model;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
        name = "DATA_HEADER",
        uniqueConstraints = @UniqueConstraint(columnNames="NAME")
)
@Setter
@Getter
public class DataHeaderEntity {

    @Id
    @SequenceGenerator(name = "dataHeaderSequenceGenerator", sequenceName = "SEQ_DATA_HEADER", allocationSize = 1)
    @GeneratedValue(generator = "dataHeaderSequenceGenerator")
    @Column(name = "DATA_HEADER_ID", insertable = false, updatable = false)
    private Long dataHeaderId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BLOCK_TYPE")
    @Enumerated(EnumType.STRING)
    private BlockTypeEnum blockType;


    @Column(name = "CREATED_TIMESTAMP")
    @JsonIgnore
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
        DataHeaderEntity dataHeaderEntity = (DataHeaderEntity) o;
        return Objects.equals(dataHeaderId, dataHeaderEntity.dataHeaderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataHeaderId);
    }
}
