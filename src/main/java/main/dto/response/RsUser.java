package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsUser extends RsBasic {

  private List<UserDto> listUser;

  public static RsUser getSuccessInstance(List<UserDto> listUser) {
    RsUser rs = new RsUser();
    rs.setResult(true);
    rs.setListUser(listUser);
    return rs;
  }

  public static RsUser getUnSuccessInstance(Object description) {
    RsUser rs = new RsUser();
    rs.setResult(false);
    rs.setDescription(description);
    return rs;
  }

}
