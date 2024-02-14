package org.kdk.tg.notifications.config;

public interface TextTemplates {

    String PING_MSG_ANSWER = "Service is up and able to send messages\nService date and time: %s";
    String TASK_STARTED = "Task '%s' started \non branch:    %s\ndescription:  %s\nauthor:       %s";
    String TASK_FINISHED_WO_RESULTS = "Task '%s' finished \non branch:    %s\nauthor:       %s";
    String TASK_FINISHED_W_RESULTS = "Task '%s' finished \non branch:    %s\nauthor:       %s\nresults:  %s";
}
