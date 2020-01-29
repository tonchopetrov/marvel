package ms_common.messaging.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class MessageDTO {

    private String language;
    private String text;
    private MessageStatus status;

    @Override
    public String toString() {
        return "MessageDTO{" +
                "language='" + language + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public enum MessageStatus{
        SUCCESS,ERROR;
    }
}
