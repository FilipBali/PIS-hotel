package com.fit.vut.pis_hotel.domain.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            UserDO karol = new UserDO(
                    "Karol",
                    "Fit",
                    LocalDate.of(1998, Month.JUNE, 22),
                    "Bozetechova 2, Brno, CZ",
                    "E50241578",
                    "00420412345656",
                    "karol.fit@vut.cz"
            );
            UserDO admin = new UserDO(
                    "admin",
                    "admin",
                    LocalDate.of(2000, Month.JUNE, 20),
                    "Bozetechova 2, Brno, CZ",
                    "E50241541",
                    "00420659345656",
                    "admin.fit@vut.cz"
            );
            userRepository.saveAll(List.of(karol, admin));
        };
    }
}
