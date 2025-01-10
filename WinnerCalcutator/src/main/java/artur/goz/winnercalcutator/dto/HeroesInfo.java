package artur.goz.winnercalcutator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroesInfo implements Serializable {
    private String[] heroes;
    private String[] enemies;
}
