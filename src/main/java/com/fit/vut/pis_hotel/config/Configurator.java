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
            UserDO rec = new UserDO(
                    "Mária",
                    "Kudláčková",
                    LocalDate.of(2000, Month.JUNE, 20),
                    "Rec 2, Brno, CZ",
                    "E50321123",
                    "00420123456123",
                    "rec.fit@vut.cz",
                    "rec"
            );

            UserDO chef = new UserDO(
                    "Ján Baltazár",
                    "Magin",
                    LocalDate.of(1999, Month.MAY, 13),
                    "Antonská 3, Praha, CZ",
                    "E9955131763",
                    "00420722356678",
                    "chef.fit@vut.cz",
                    "chef"
            );

            UserDO bowling = new UserDO(
                    "Amália",
                    "Brežná",
                    LocalDate.of(1998, Month.APRIL, 4),
                    "Malinovská 467, Bratislava, CZ",
                    "E9754043572",
                    "00424698723478",
                    "bowling.fit@vut.cz",
                    "bowling"
            );

            UserDO masseur = new UserDO(
                    "Ignác",
                    "Bajza",
                    LocalDate.of(1996, Month.NOVEMBER, 7),
                    "Hradská 467, Bratislava, CZ",
                    "E9611073439",
                    "00420613850765",
                    "masseur.fit@vut.cz",
                    "masseur"
            );

            userService.createUser(karol);
            userService.createUser(admin);
            userService.createUser(rec);
            userService.createUser(chef);
            userService.createUser(bowling);
            userService.createUser(masseur);

            HostDO host1 = new HostDO(
                    "Lukáš",
                    "Hostovič",
                    LocalDate.of(2002, Month.JUNE, 20),
                    "Joštova 4, Brno, CZ",
                    "P502888123",
                    "+420999422667",
                    "lukas@email.cz"
            );

            HostDO host2 = new HostDO(
                    "Peter",
                    "Petrovič",
                    LocalDate.of(1992, Month.AUGUST, 12),
                    "Česká 39, Praha, CZ",
                    "F108809444",
                    "+420422119766",
                    "peter.petrovic@vut.cz"
            );
            HostDO host3 = new HostDO(
                    "Mária Anna",
                    "Nováková",
                    LocalDate.of(1996, Month.NOVEMBER, 1),
                    "Malá strana 691, Praha, CZ",
                    "E402409856",
                    "+420123995467",
                    "majena.nov@vut.cz"
            );
            HostDO host4 = new HostDO(
                    "Hermína",
                    "Záborská",
                    LocalDate.of(1984, Month.SEPTEMBER, 25),
                    "Zámocká 32, Nové Zámky, SK",
                    "R892410857",
                    "+421566822366",
                    "zabor@uii.com"
            );
            HostDO host5 = new HostDO(
                    "Karolína",
                    "Malá",
                    LocalDate.of(1990, Month.OCTOBER, 8),
                    "Ku bráne 344, Komárno, SK",
                    "T896630822",
                    "+421332822466",
                    "mala.karolina@mail.sk"
            );
            HostDO host6 = new HostDO(
                    "Sára",
                    "Steinbecková",
                    LocalDate.of(1975, Month.JANUARY, 16),
                    "Gavlovičova, Bratislava, SK",
                    "B892256777",
                    "+421444588999",
                    "steins@gmail.com"
            );

            hostService.createHost(host1);
            hostService.createHost(host2);
            hostService.createHost(host3);
            hostService.createHost(host4);
            hostService.createHost(host5);
            hostService.createHost(host6);

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
            RoomDO room5 = new RoomDO(
                    5,
                    2,
                    cat_l
            );
            RoomDO room6 = new RoomDO(
                    6,
                    4,
                    cat_s
            );

            roomRepository.saveAll(List.of(room, room2, room3, room4,room5,room6));


            StayDO stay = new StayDO(
                    4,
                    LocalDate.of(2022, Month.FEBRUARY, 26),
                    LocalDate.of(2022, Month.MARCH, 1),
                    StateEnum.FINISHED,
                    BoardTypeEnum.FULLBOARD,
                    host1,
                    PaymentTypeEnum.CARD,
                    List.of(room, room2),
                    List.of(host1, host2)
            );
            StayDO stay2 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.APRIL, 22),
                    LocalDate.of(2022, Month.APRIL, 23),
                    StateEnum.CANCELED,
                    BoardTypeEnum.HALFBOARD,
                    host2,
                    PaymentTypeEnum.CASH,
                    List.of(room3)
            );
            StayDO stay3 = new StayDO(
                    4,
                    LocalDate.of(2022, Month.APRIL, 26),
                    LocalDate.of(2022, Month.MAY, 7),
                    StateEnum.ACTIVE,
                    BoardTypeEnum.FULLBOARD,
                    host3,
                    PaymentTypeEnum.CARD,
                    List.of(room4),
                    List.of(host3, host4)
            );
            StayDO stay4 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.APRIL, 16),
                    LocalDate.of(2022, Month.MAY, 23),
                    StateEnum.ACTIVE,
                    BoardTypeEnum.HALFBOARD,
                    host5,
                    PaymentTypeEnum.CASH,
                    List.of(room5)
            );
            StayDO stay5 = new StayDO(
                    2,
                    LocalDate.of(2022, Month.MAY, 20),
                    LocalDate.of(2022, Month.MAY, 23),
                    StateEnum.RESERVED,
                    BoardTypeEnum.FULLBOARD,
                    host6,
                    PaymentTypeEnum.CASH,
                    List.of(room4)
            );
            stayRepository.saveAll(List.of(stay, stay2, stay3, stay4, stay5));

            RoleDO roleUser = roleService.saveRole(new RoleDO("ROLE_USER"));
            RoleDO roleAdmin = roleService.saveRole(new RoleDO("ROLE_ADMIN"));
            RoleDO roleReceptionist = roleService.saveRole(new RoleDO("ROLE_RECEPTIONIST"));
            RoleDO roleCook = roleService.saveRole(new RoleDO("ROLE_COOK"));
            RoleDO roleMasseur = roleService.saveRole(new RoleDO("ROLE_MASSEUR"));
            RoleDO roleBowling = roleService.saveRole(new RoleDO("ROLE_BOWLING"));

            roleService.addRoleToUser(1L, "ROLE_USER");
            roleService.addRoleToUser(2L, "ROLE_ADMIN");
            roleService.addRoleToUser(3L, "ROLE_RECEPTIONIST");
            roleService.addRoleToUser(4L, "ROLE_COOK");
            roleService.addRoleToUser(5L, "ROLE_BOWLING");
            roleService.addRoleToUser(6L, "ROLE_MASSEUR");

            ServiceDO service1 = new ServiceDO(
                    LocalDateTime.of(2022, Month.FEBRUARY, 28, 14, 23),
                    LocalDateTime.of(2022, Month.FEBRUARY, 28, 15, 30),
                    ServiceTypeEnum.MASSAGE,
                    PaymentTypeEnum.CASH,
                    null,
                    StateEnum.FINISHED,
                    stay
            );

            ServiceDO service2 = new ServiceDO(
                    LocalDateTime.of(2022, Month.MAY, 4, 14, 23),
                    LocalDateTime.of(2022, Month.MAY, 4, 15, 30),
                    ServiceTypeEnum.MASSAGE,
                    PaymentTypeEnum.CASH,
                    null,
                    StateEnum.RESERVED,
                    stay3
            );

            ServiceDO service3 = new ServiceDO(
                    LocalDateTime.of(2022, Month.MAY, 6, 20, 0),
                    LocalDateTime.of(2022, Month.MAY, 6, 22, 30),
                    ServiceTypeEnum.BOWLING,
                    PaymentTypeEnum.CARD,
                    1,
                    StateEnum.RESERVED,
                    stay3
            );

            ServiceDO service4 = new ServiceDO(
                    LocalDateTime.of(2022, Month.MAY, 8, 14, 30),
                    LocalDateTime.of(2022, Month.MAY, 8, 15, 30),
                    ServiceTypeEnum.BOWLING,
                    PaymentTypeEnum.CARD,
                    1,
                    StateEnum.RESERVED,
                    stay4
            );

            ServiceDO service5 = new ServiceDO(
                    LocalDateTime.of(2022, Month.MAY, 9, 20, 0),
                    LocalDateTime.of(2022, Month.MAY, 9, 21, 0),
                    ServiceTypeEnum.MASSAGE,
                    PaymentTypeEnum.CARD,
                    null,
                    StateEnum.RESERVED,
                    stay4
            );

            stayService.createService(service1);
            stayService.createService(service2);
            stayService.createService(service3);
            stayService.createService(service4);
            stayService.createService(service5);
        };
    }
}
