package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface DataStoreRepository extends JpaRepository<DataBodyEntity, Long> {

    @Query("select d from DataBodyEntity d where d.dataHeaderEntity.blockType = :blockType")
    public List<DataBodyEntity> findByBlockType(@Param("blockType") BlockTypeEnum blockType);

    @Query("select d from DataBodyEntity d where d.dataHeaderEntity.name = :blockName")
    public Optional<DataBodyEntity> findByBlockName(@Param("blockName") String blockName);

}
