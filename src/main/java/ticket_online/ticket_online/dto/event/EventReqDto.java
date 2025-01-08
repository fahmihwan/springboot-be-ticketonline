package ticket_online.ticket_online.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventReqDto {

    @JsonProperty("event_title") // Penyesuaian nama dengan frontend (snake_case)
    private String eventTitle;
    private MultipartFile image;
    private String schedule;
    private String venue;

    private String slug;
    private String description;

    @JsonProperty("admin_id")
    private Long adminId;

//      formData.append('event_title', payload.event_title)
//            formData.append('image', payload.image)
//            formData.append('schedule', payload.schedule)
//            formData.append('venue', payload.venue)
//            formData.append('slug', payload.slug)
//            formData.append('description', payload.description)
//            formData.append('admin_id', payload.admin_id)

}
