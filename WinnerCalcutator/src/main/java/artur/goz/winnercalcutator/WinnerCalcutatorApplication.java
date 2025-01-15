package artur.goz.winnercalcutator;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
@EnableRabbit
public class WinnerCalcutatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinnerCalcutatorApplication.class, args);
    }

}
