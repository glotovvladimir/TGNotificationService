package org.kdk.tg.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.kdk.tg.notifications.config.TextTemplates.PING_MSG_ANSWER;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_FINISHED_WO_RESULTS;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_FINISHED_W_RESULTS;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_STARTED;

@Data
@Component
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageHandlerService {

    RequestProducerService reqProducer;

    @Autowired
    public MessageHandlerService(RequestProducerService reqProducer) {
        this.reqProducer = reqProducer;
    }

    @Autowired
    private RestTemplate restTemplate;

    public void sendPingMsg() {
        String message = PING_MSG_ANSWER.formatted(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("uuuu-MM-dd | HH:mm")));

        restTemplate.postForObject(
                reqProducer.getPath(),
                Map.of(
                        "chat_id", reqProducer.getChatId(),
                        "text", message),
                String.class);
        log.info("\n" + message);
    }

    @SneakyThrows
    public void sendTaskStartedMsg(String taskName, String branchName, String author, String commitMsg) {
        String message = TASK_STARTED.formatted(taskName, branchName, commitMsg, author);
        restTemplate.postForObject(
                reqProducer.getPath(),
                new ObjectMapper().writeValueAsString(Map.of("chat_id",  reqProducer.getChatId(),"text",message)),
                String.class);
        log.info("\n" + message);
    }

    @SneakyThrows
    public void sendTaskFinishedResultsMsg(String taskName, String branchName, String author, String resultsPath) {
        String message = TASK_FINISHED_W_RESULTS.formatted(taskName, branchName, author, resultsPath);
        restTemplate.postForObject(
                reqProducer.getPath(),
                new ObjectMapper().writeValueAsString(Map.of("chat_id", reqProducer.getChatId(),"text", message)),
                String.class);
        log.info("\n" + message);
    }

    @SneakyThrows
    public void sendTaskFinishedMsg(String taskName, String branchName, String author) {
        String message = TASK_FINISHED_WO_RESULTS.formatted(taskName, branchName, author);
        restTemplate.postForObject(
                reqProducer.getPath(),
                new ObjectMapper().writeValueAsString(Map.of("chat_id", reqProducer.getChatId(),"text", message)),
                String.class);
        log.info("\n" + message);
    }
}
