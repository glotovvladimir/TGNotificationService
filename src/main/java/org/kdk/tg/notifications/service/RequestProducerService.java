package org.kdk.tg.notifications.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestProducerService {

    @Value("${tg.bot.token}")
    private String botToken;

    @Value("${tg.chat.id}")
    private String chatId;

    public String getPath() {
        return "https://api.telegram.org/bot%s/sendMessage".formatted(botToken);
    }
}
