package com.fit.vut.pis_hotel.config;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.host.HostService;
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
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDTO;
import com.fit.vut.pis_hotel.domain.stay.service.enums.ServiceTypeEnum;
import com.fit.vut.pis_hotel.domain.user.UserDO;
import com.fit.vut.pis_hotel.domain.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Configuration
public class Configurator {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, HostService hostService, StayRepository stayRepository, RoleService roleService,
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

            HostDO host1 = new HostDO(
                    "Lukas",
                    "Hostovic",
                    LocalDate.of(2002, Month.JUNE, 20),
                    "Hostova 4, Brno, CZ",
                    "E502415556",
                    "00420987345656",
                    "lukas.fit@vut.cz"
            );

            HostDO host2 = new HostDO(
                    "Peter",
                    "Petrovic",
                    LocalDate.of(1992, Month.AUGUST, 12),
                    "Ceska 39, Praha, CZ",
                    "E502409856",
                    "004209854535656",
                    "peter.petrovic@vut.cz"
            );

            hostService.createHost(host1);
            hostService.createHost(host2);

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


            StayDO stay = new StayDO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    host1,
                    PaymentTypeEnum.CARD,
                    List.of(room, room2)
            );
            StayDO stay2 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.MARCH, 22),
                    LocalDate.of(2022, Month.MARCH, 23),
                    StateEnum.CANCELED,
                    BoardTypeEnum.HALFBOARD,
                    host2,
                    PaymentTypeEnum.CASH,
                    List.of(room3)
            );
            stayRepository.saveAll(List.of(stay, stay2));

            StayDTO stay3 = new StayDTO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    host1.getId(),
                    PaymentTypeEnum.CARD,
                    List.of(3L)
            );

            stayService.createStayWithCreatorId(stay3);

            RoleDO roleUser = roleService.saveRole(new RoleDO("ROLE_USER"));
            RoleDO roleAdmin = roleService.saveRole(new RoleDO("ROLE_ADMIN"));
            RoleDO roleReceptionist = roleService.saveRole(new RoleDO("ROLE_RECEPTIONIST"));
            RoleDO roleCook = roleService.saveRole(new RoleDO("ROLE_COOK"));
            RoleDO roleMasseur = roleService.saveRole(new RoleDO("ROLE_MASSEUR"));
            RoleDO roleBowling = roleService.saveRole(new RoleDO("ROLE_BOWLING"));

            roleService.addRoleToUser(1L, "ROLE_USER");
            roleService.addRoleToUser(2L, "ROLE_ADMIN");

            ServiceDO service1 = new ServiceDO(
                    LocalDateTime.of(2022, Month.FEBRUARY, 27, 14, 30),
                    LocalDateTime.of(2022, Month.FEBRUARY, 27, 15, 30),
                    ServiceTypeEnum.BOWLING,
                    PaymentTypeEnum.CARD,
                    1,
                    StateEnum.RESERVED,
                    stay
            );

            ServiceDTO serviceDTO2 = new ServiceDTO(
                    LocalDateTime.of(2022, Month.MARCH, 22, 14, 30),
                    LocalDateTime.of(2022, Month.MARCH, 22, 15, 30),
                    ServiceTypeEnum.MASSAGE,
                    PaymentTypeEnum.CASH,
                    null,
                    StateEnum.RESERVED,
                    2L
            );

            stayService.createService(service1);
            stayService.createAndAssignService(serviceDTO2);
        };
    }
}
