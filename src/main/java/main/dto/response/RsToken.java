package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsToken extends RsBasic {

  private String token;

  public static RsToken getSuccessInstance(String token) {
    RsToken rs = new RsToken();
    rs.setResult(true);
    rs.setToken(token);
    return rs;
  }

  public static RsToken getUnSuccessInstance(Object description) {
    RsToken rs = new RsToken();
    rs.setResult(false);
    rs.setDescription(description);
    return rs;
  }

}
