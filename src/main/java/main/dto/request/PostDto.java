package main.dto.request;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

  private String text;

  private String title;

  private Date date;

}
