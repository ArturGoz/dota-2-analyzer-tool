package artur.goz.heroesstatsservice.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroesInfo {
    private String[] heroes;
    private String[] enemies;
}
