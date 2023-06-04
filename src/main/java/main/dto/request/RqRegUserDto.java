package main.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RqRegUserDto {

  private String name;

  private String password;

  private String email;

}
