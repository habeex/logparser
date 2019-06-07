package com.habeex.logparser;

import com.habeex.logparser.Service.LogService;
import com.habeex.logparser.dto.AccessLogDto;
import com.habeex.logparser.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class AppRunner implements ApplicationRunner {

    @Autowired
    LogService logService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        AccessLogDto accessLogDto = new AccessLogDto(args);

        if (args.containsOption("accesslog")){
            logService.saveLog(accessLogDto.getAccessLog());
        }

        if (accessLogDto.isPresent()){
            logService.getLogs(accessLogDto);
        }else {
            System.out.println("Usage: java -cp \"parser.jar\" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
        }

        List<Log> logs = logService.findByIp("192.168.169.194");
        System.out.println(logs.size() + " size");

    }
}
