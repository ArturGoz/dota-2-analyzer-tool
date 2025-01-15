package artur.goz.heroesstatsservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
@EnableRabbit
public class HeroesStatsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeroesStatsServiceApplication.class, args);
    }

}
