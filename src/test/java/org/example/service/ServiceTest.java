package org.example.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kdk.tg.notifications.App;
import org.kdk.tg.notifications.config.AppConfig;
import org.kdk.tg.notifications.service.RequestProducerService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.kdk.tg.notifications.config.TextTemplates.PING_MSG_ANSWER;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_FINISHED_WO_RESULTS;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_FINISHED_W_RESULTS;
import static org.kdk.tg.notifications.config.TextTemplates.TASK_STARTED;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = App.class)
public class ServiceTest extends BaseTest {

    @Autowired
    AppConfig appConfig;

    @MockBean
    RestTemplate restTemplateMock;

    @Autowired
    @InjectMocks
    RequestProducerService requestProducerService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void requestStringTest() {
        Assertions
                .assertEquals(requestProducerService.getPath(), "https://api.telegram.org/botsecret/sendMessage");
    }

    @Test
    @SneakyThrows
    public void pingMsgTest() {
        Map<String, String> map = Map.of(
                "chat_id", requestProducerService.getChatId(),
                "text", PING_MSG_ANSWER.formatted(LocalDateTime.now()
                        .truncatedTo(ChronoUnit.MINUTES)
                        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd | HH:mm"))));

        when(restTemplateMock.postForObject(startsWith("https://api.telegram.org/bot"), eq(map), eq(String.class)))
                .thenReturn("success");

        mockMvc.perform(get("/api/v1/ping")).andExpect(status().isOk());

        verify(restTemplateMock, times(1))
                .postForObject(requestProducerService.getPath(), map, String.class);
    }

    @Test
    @SneakyThrows
    public void taskStartedMsgTest() {
        Map<String, String> map = Map.of(
                "commitMsg", "msg",
                "author", "author",
                "taskName", "name",
                "branchName", "branch"
        );

        when(restTemplateMock.postForObject(startsWith("https://api.telegram.org"), eq(asJsonString(map)), eq(String.class)))
                .thenReturn("success");

        mockMvc.perform(
                post("/api/v1/taskStarted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(map)))
                .andExpect(status().isOk());

        verify(restTemplateMock, times(1))
                .postForObject(eq(requestProducerService.getPath()),
                        eq(asJsonString(Map.of(
                        "chat_id", requestProducerService.getChatId(),
                        "text", TASK_STARTED
                                        .formatted(
                                                map.get("taskName"),
                                                map.get("branchName"),
                                                map.get("commitMsg"),
                                                map.get("author"))))),
                        eq(String.class));
    }

    @Test
    @SneakyThrows
    public void taskFinishedWOResultsTest() {
        Map<String, String> map = Map.of(
                "author", "author",
                "taskName", "name",
                "branchName", "branch"
        );

        when(restTemplateMock.postForObject(startsWith("https://api.telegram.org"), eq(asJsonString(map)), eq(String.class)))
                .thenReturn("success");

        mockMvc.perform(
                        post("/api/v1/taskFinished")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(map)))
                .andExpect(status().isOk());

        verify(restTemplateMock, times(1))
                .postForObject(eq(requestProducerService.getPath()),
                        eq(asJsonString(Map.of(
                                "chat_id", requestProducerService.getChatId(),
                                "text", TASK_FINISHED_WO_RESULTS
                                        .formatted(
                                                map.get("taskName"),
                                                map.get("branchName"),
                                                map.get("author"))))),
                        eq(String.class));
    }

    @Test
    @SneakyThrows
    public void taskFinishedWResultsTest() {
        Map<String, String> map = Map.of(
                "path", "path",
                "author", "author",
                "taskName", "name",
                "branchName", "branch"
        );

        when(restTemplateMock.postForObject(startsWith("https://api.telegram.org"), eq(asJsonString(map)), eq(String.class)))
                .thenReturn("success");

        mockMvc.perform(
                        post("/api/v1/taskFinishedWResults")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(map)))
                .andExpect(status().isOk());

        verify(restTemplateMock, times(1))
                .postForObject(eq(requestProducerService.getPath()),
                        eq(asJsonString(Map.of(
                                "chat_id", requestProducerService.getChatId(),
                                "text", TASK_FINISHED_W_RESULTS
                                        .formatted(
                                                map.get("taskName"),
                                                map.get("branchName"),
                                                map.get("author"),
                                                map.get("resultsPath"))))),
                        eq(String.class));

    }
}
