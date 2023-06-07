package main.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

  private String text;

  private String title;

  private Date date;

  private MultipartFile photo;

}
