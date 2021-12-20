package security.web.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApprovalInfoDto {
    private String imageUrl;
    private String facebookName;
    private String email;
}
