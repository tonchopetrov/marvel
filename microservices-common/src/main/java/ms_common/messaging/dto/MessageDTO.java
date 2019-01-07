package ms_common.messaging.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private String language;
    private String text;

    @Override
    public String toString() {
        return "MessageDTO{" +
                "language='" + language + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
