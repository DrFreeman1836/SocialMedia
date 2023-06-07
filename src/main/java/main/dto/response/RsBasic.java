package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsBasic {

  protected Boolean result;

  protected Object description;

  public static RsBasic getSuccessInstance() {
    RsBasic rs = new RsBasic();
    rs.setResult(true);
    return rs;
  }

  public static RsBasic getUnSuccessInstance(Object description) {
    RsBasic rs = new RsBasic();
    rs.setResult(false);
    rs.setDescription(description);
    return rs;
  }

}
