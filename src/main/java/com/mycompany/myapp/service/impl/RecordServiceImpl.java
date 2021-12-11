package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.service.RecordService;
import com.mycompany.myapp.service.dto.RecordDTO;
import com.mycompany.myapp.service.mapper.RecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Record}.
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);

    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

    public RecordServiceImpl(RecordRepository recordRepository, RecordMapper recordMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
    }

    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        log.debug("Request to save Record : {}", recordDTO);
        Record record = recordMapper.toEntity(recordDTO);
        record = recordRepository.save(record);
        return recordMapper.toDto(record);
    }

    @Override
    public Optional<RecordDTO> partialUpdate(RecordDTO recordDTO) {
        log.debug("Request to partially update Record : {}", recordDTO);

        return recordRepository
            .findById(recordDTO.getId())
            .map(existingRecord -> {
                recordMapper.partialUpdate(existingRecord, recordDTO);

                return existingRecord;
            })
            .map(recordRepository::save)
            .map(recordMapper::toDto);
    }

    @Override
    public Page<RecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Records");
        return recordRepository.findAll(pageable).map(recordMapper::toDto);
    }

    public Page<RecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recordRepository.findAllWithEagerRelationships(pageable).map(recordMapper::toDto);
    }

    @Override
    public Optional<RecordDTO> findOne(String id) {
        log.debug("Request to get Record : {}", id);
        return recordRepository.findOneWithEagerRelationships(id).map(recordMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Record : {}", id);
        recordRepository.deleteById(id);
    }

    @Override
    public com.mycompany.myapp.domain.Record save(com.mycompany.myapp.domain.Record record) {
        return this.recordRepository.save(record);
    }
}
