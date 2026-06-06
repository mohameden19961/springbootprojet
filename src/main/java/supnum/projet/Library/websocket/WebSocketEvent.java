package supnum.projet.Library.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class WebSocketEvent {
    private String type;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public WebSocketEvent(String type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
