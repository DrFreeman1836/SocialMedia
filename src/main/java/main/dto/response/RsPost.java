package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import main.dto.request.PostDto;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsPost extends RsBasic {

  private List<PostDto> listPost;

  public static RsPost getSuccessInstance(List<PostDto> listPost) {
    RsPost rs = new RsPost();
    rs.setResult(true);
    rs.setListPost(listPost);
    return rs;
  }

  public static RsPost getUnSuccessInstance(Object description) {
    RsPost rs = new RsPost();
    rs.setResult(false);
    rs.setDescription(description);
    return rs;
  }

}
