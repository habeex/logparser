package com.habeex.logparser.Service.Impl;

import com.habeex.logparser.Service.LogService;
import com.habeex.logparser.dto.AccessLogDto;
import com.habeex.logparser.model.CommentLogs;
import com.habeex.logparser.model.Log;
import com.habeex.logparser.repository.CommentLogsRepository;
import com.habeex.logparser.repository.LogRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logRepository;

    @Autowired
    CommentLogsRepository commentLogsRepository;

    @Override
    public void saveLog(String file) {

       try {
           List<Log> logs = new ArrayList<>();
           Reader in = new FileReader(file);
           Iterable<CSVRecord> records = CSVFormat.RFC4180.withDelimiter('|').parse(in);
           for (CSVRecord record : records) {
               Log log = new Log();
               DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
               String dateString = record.get(0);
               dateString = dateString.replace("\uFEFF", ""); // remove UTF BOM
               Date date = formatter.parse(dateString);
               log.setDate(date);

               log.setIp(record.get(1));
               log.setMethod(record.get(2));
               log.setResponse(record.get(3));
               log.setUserAgent(record.get(4));
               logs.add(log);
           }

           logRepository.saveAll(logs);

       } catch (ParseException e) {
           e.printStackTrace();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void getLogs(AccessLogDto accessLogDto) {
        List<Log> logs = logRepository.findByIdAndDateBetween(
                accessLogDto.getStartDate(),
                accessLogDto.getEndDate(),
                accessLogDto.getThreshold());

        List<CommentLogs> commentLogs = new ArrayList<>();
        logs.forEach(log -> {
            CommentLogs commentLog = new CommentLogs();
            commentLog.setCreatedDate(new Date());
            commentLog.setIp(log.getIp());
            commentLog.setComment("The IP: " + log.getIp() + " has reached more than " +  accessLogDto.getThreshold().toString() +
                    " requests between " +  accessLogDto.getStartDate().toString() + " and " +  accessLogDto.getEndDate().toString());
            commentLogs.add(commentLog);

            System.out.println("The IP: " + log.getIp() + " has reached more than " + accessLogDto.getThreshold().toString() +
                    " requests between " + accessLogDto.getStartDate().toString() + " and " + accessLogDto.getEndDate().toString());
        });

        if (commentLogs.size() > 0){
            commentLogsRepository.saveAll(commentLogs);
        }else {
            System.out.println("commentLogs.size() " + commentLogs.size());
        }
    }

    @Override
    public List<Log> findByIp(String ip) {
        try {
            return logRepository.findByIp(ip);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
