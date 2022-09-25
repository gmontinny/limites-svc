package br.com.coffeeandit;

import br.com.coffeeandit.entity.LimiteDiario;
import br.com.coffeeandit.repository.LimiteDiarioRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = LimiteDiarioRepository.class)
@EntityScan(basePackageClasses = LimiteDiario.class)
public class LimitesSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitesSvcApplication.class, args);
    }

}
