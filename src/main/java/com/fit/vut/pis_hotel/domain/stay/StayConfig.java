package com.fit.vut.pis_hotel.domain.stay;

import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import com.fit.vut.pis_hotel.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StayConfig {

    UserRepository userRepository;

    @Autowired
    public StayConfig(StayRepository stayRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner commandLineRunnerStay(StayRepository stayRepository) {
        return args -> {
            StayDO stay = new StayDO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StayStateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    null,
                    PaymentTypeEnum.CARD
            );
            StayDO stay2 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.FEBRUARY, 22),
                    LocalDate.of(2022, Month.FEBRUARY, 23),
                    StayStateEnum.CANCELED,
                    BoardTypeEnum.HALFBOARD,
                    null,
                    PaymentTypeEnum.CASH
            );
            stayRepository.saveAll(List.of(stay, stay2));
        };
    }
}
