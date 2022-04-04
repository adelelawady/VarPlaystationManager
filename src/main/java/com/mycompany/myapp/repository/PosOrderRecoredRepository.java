package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OrderHandlerDomain;
import com.mycompany.myapp.domain.PosOrderRecord;
import com.mycompany.myapp.domain.Record;
import com.mycompany.myapp.domain.Table.TABLE_TYPE;
import com.mycompany.myapp.domain.TableRecord;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@Repository
public interface PosOrderRecoredRepository extends MongoRepository<PosOrderRecord, String> {
    List<PosOrderRecord> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    List<PosOrderRecord> findAllByTypeAndCreatedDateBetween(String type, Instant startDate, Instant endDate);

    Page<PosOrderRecord> findAllByTypeOrderByCreatedDateDesc(Pageable pageable, OrderHandlerDomain.TABLE_TYPE Type);

    // Page<PosOrderRecord> findAllByOrderByCreatedDateDesc(Pageable pageable, OrderHandlerDomain.TABLE_TYPE Type);

    List<PosOrderRecord> findAllByOrdersDataId(String orderId);

    List<PosOrderRecord> findAllByOrdersDataIdAndCreatedDateBetween(String orderId, Instant startDate, Instant endDate);

    @Aggregation(
        "{$match:" +
        "{" +
        "  " +
        "'created_date':{" +
        "    $gte:ISODate('?0')," +
        "    $lt:ISODate('?1')," +
        "  }" +
        "  " +
        " " +
        "}," +
        "$group{" +
        "  _id: null," +
        "  total: {" +
        "    $sum:{ $toInt: '$net_total_price' } " +
        "  }" +
        "} " +
        "}"
    )
    Long sumNetTotalByDate(String startDate, String endDate);
}
