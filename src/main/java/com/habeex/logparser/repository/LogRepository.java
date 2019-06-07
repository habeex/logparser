package com.habeex.logparser.repository;

import com.habeex.logparser.model.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<Log, Long> {

    @Query("SELECT a FROM Log a WHERE a.date BETWEEN :startDate AND :endDate GROUP BY a.ip HAVING count(a.ip) >= :threshold")
    List<Log> findByIdAndDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("threshold") long threshold);

    @Query("SELECT a FROM Log a WHERE a.ip =:ip")
    List<Log> findByIp(@Param("ip") String ip);
}
