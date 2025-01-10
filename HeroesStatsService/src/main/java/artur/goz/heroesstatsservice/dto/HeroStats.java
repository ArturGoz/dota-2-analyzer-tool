package artur.goz.heroesstatsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroStats {
    private Float winrate;
    private Integer matches;
}
