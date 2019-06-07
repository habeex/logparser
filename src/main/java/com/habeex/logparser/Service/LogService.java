package com.habeex.logparser.Service;

import com.habeex.logparser.dto.AccessLogDto;
import com.habeex.logparser.model.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogService {


    void saveLog(String file);

    void getLogs(AccessLogDto accessLogDto);

    List<Log> findByIp(String ip);
}
