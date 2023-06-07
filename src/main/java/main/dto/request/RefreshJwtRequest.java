package main.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshJwtRequest {

  public String refreshToken;

}
