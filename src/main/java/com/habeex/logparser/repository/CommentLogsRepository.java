package com.habeex.logparser.repository;

import com.habeex.logparser.model.CommentLogs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLogsRepository extends CrudRepository<CommentLogs, Long> {



}

