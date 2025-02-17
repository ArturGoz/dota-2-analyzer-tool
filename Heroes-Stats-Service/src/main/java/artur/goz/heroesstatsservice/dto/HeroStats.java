package artur.goz.heroesstatsservice.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeroStats {
    private Float winrate;
    private Integer matches;
}
