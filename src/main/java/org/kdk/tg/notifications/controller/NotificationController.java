package org.kdk.tg.notifications.controller;

import lombok.extern.slf4j.Slf4j;
import org.kdk.tg.notifications.service.MessageHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api/v1")
@RestController
public class NotificationController {

    MessageHandlerService msgCompiler;

    @Autowired
    public NotificationController(MessageHandlerService msgCompiler) {
        this.msgCompiler = msgCompiler;
    }

    @GetMapping(path = "/ping")
    public void pingServiceMsg() {
        msgCompiler.sendPingMsg();
        log.info("\nPing message sent");
    }

    @PostMapping(path = "/taskStarted")
    public void taskStartedMsg(@RequestBody Map<String, String> data) {
        msgCompiler.sendTaskStartedMsg(
                data.get("taskName"), data.get("branchName"), data.get("author"), data.get("commitMsg"));
        log.info("\nTask started message sent for data: " + data);
    }

    @PostMapping(path = "/taskFinishedWResults")
    public void taskFinishedResultsMsg(@RequestBody Map<String, String> data) {
        msgCompiler.sendTaskFinishedResultsMsg(
                data.get("taskName"), data.get("branchName"), data.get("author"), data.get("resultsPath"));
        log.info("\nTask finished with results message sent for data: " + data);
    }

    @PostMapping(path = "/taskFinished")
    public void taskFinishedMsg(@RequestBody Map<String, String> data) {
        msgCompiler.sendTaskFinishedMsg(
                data.get("taskName"), data.get("branchName"), data.get("author"));
        log.info("\nTask finished without results message sent for data: " + data);
    }
}
