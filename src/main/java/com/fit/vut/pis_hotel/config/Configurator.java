package com.fit.vut.pis_hotel.config;

import com.fit.vut.pis_hotel.domain.role.RoleDO;
import com.fit.vut.pis_hotel.domain.role.RoleService;
import com.fit.vut.pis_hotel.domain.room.RoomDO;
import com.fit.vut.pis_hotel.domain.room.RoomRepository;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryDO;
import com.fit.vut.pis_hotel.domain.roomCategory.RoomCategoryRepository;
import com.fit.vut.pis_hotel.domain.roomCategory.enums.RoomTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.StayDTO;
import com.fit.vut.pis_hotel.domain.stay.StayRepository;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class Configurator {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, StayRepository stayRepository, RoleService roleService,
                                        StayService stayService, RoomRepository roomRepository, RoomCategoryRepository roomCategoryRepository) {
        return args -> {
            UserDO karol = new UserDO(
                    "Karol",
                    "Fit",
                    LocalDate.of(1998, Month.JUNE, 22),
                    "Bozetechova 2, Brno, CZ",
                    "E50241578",
                    "00420412345656",
                    "karol.fit@vut.cz",
                    "1234"
            );
            UserDO admin = new UserDO(
                    "admin",
                    "admin",
                    LocalDate.of(2000, Month.JUNE, 20),
                    "Bozetechova 2, Brno, CZ",
                    "E50241541",
                    "00420659345656",
                    "admin.fit@vut.cz",
                    "1234"
            );
            userService.createUser(karol);
            userService.createUser(admin);

            StayDO stay = new StayDO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StayStateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    karol,
                    PaymentTypeEnum.CARD
            );
            StayDO stay2 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.FEBRUARY, 22),
                    LocalDate.of(2022, Month.FEBRUARY, 23),
                    StayStateEnum.CANCELED,
                    BoardTypeEnum.HALFBOARD,
                    admin,
                    PaymentTypeEnum.CASH
            );
            stayRepository.saveAll(List.of(stay, stay2));

            StayDTO stay3 = new StayDTO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StayStateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    karol.getId(),
                    PaymentTypeEnum.CARD
            );

            stayService.createStayWithCreatorId(stay3);

            RoleDO roleUser = roleService.saveRole(new RoleDO("ROLE_USER"));
            RoleDO roleAdmin = roleService.saveRole(new RoleDO("ROLE_ADMIN"));

            roleService.addRoleToUser(1L, "ROLE_USER");
            roleService.addRoleToUser(2L, "ROLE_ADMIN");

            RoomCategoryDO cat_s = new RoomCategoryDO(
                    RoomTypeEnum.STANDARD,
                    "posteľ, nočný stolík, skriňa, písací stôl, zrkadlo a vešiaková stena, kúpelňa, TV, telefón, WIFI",
                    44.25

            );

            RoomCategoryDO cat_a = new RoomCategoryDO(
                    RoomTypeEnum.APARTMENT,
                    "posteľ, nočný stolík, skriňa, písací stôl, zrkadlo a vešiaková stena, väčšia kúpeľňa, TV," +
                            "telefón, WIFI, kuchynský kút, riady a príbor, izba pre deti",
                    78.45

            );

            RoomCategoryDO cat_l = new RoomCategoryDO(
                    RoomTypeEnum.LUXURY,
                    "posteľ, nočný stolík, skriňa, písací stôl, zrkadlo a vešiaková stena, väčšia kúpeľňa s výrivkou, TV," +
                            "telefón, WIFI, kuchynský kút, riady a príbor, bar, stolný futbal, izba pre deti",
                    102.50

            );

            roomCategoryRepository.saveAll(List.of(cat_s, cat_a, cat_l));

            RoomDO room = new RoomDO(
                    1,
                    2,
                    cat_s
            );

            RoomDO room2 = new RoomDO(
                    2,
                    4,
                    cat_s
            );

            RoomDO room3 = new RoomDO(
                    3,
                    2,
                    cat_a
            );

            RoomDO room4 = new RoomDO(
                    4,
                    4,
                    cat_l
            );

            roomRepository.saveAll(List.of(room, room2, room3, room4));

        };
    }
}
