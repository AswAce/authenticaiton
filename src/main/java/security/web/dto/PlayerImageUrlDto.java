package security.web.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerImageUrlDto {

    private String userUniqueId;
    private String imageUrl;
}
