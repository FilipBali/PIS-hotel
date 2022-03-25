package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.StayDTO;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StayStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/stay")
public class StayController {

    StayService stayService;

    @Autowired
    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @GetMapping
    public List<StayDO> getStays() {
        return stayService.getStays();
    }

    @GetMapping(value = "/{id}")
    public StayDO getStay(@PathVariable("id") Long id) {
        return stayService.getStay(id);
    }

    @GetMapping(value = "/simple/{id}")
    public StayDTO getSimpleStay(@PathVariable("id") Long id) {
        return stayService.getSimpleStay(id);
    }

    @GetMapping(value = "/simple")
    public List<StayDTO> getSimpleStays() {
        return stayService.getSimpleStays();
    }

    @PostMapping
    public void createStay(@RequestBody StayDO stay) {
        stayService.createStay(stay);
    }

    @PostMapping(value = "/creatorId")
    public void createStayWithCreatorId(@RequestBody StayDTO stay) {
        stayService.createStayWithCreatorId(stay);
    }

    @DeleteMapping(value = "{id}")
    public void deleteStay(@PathVariable Long id) {
        stayService.deleteStay(id);
    }

    @PutMapping(path = {"{id}"})
    public void updateStay(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Integer accommodatedNumber,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) StayStateEnum state,
            @RequestParam(required = false) BoardTypeEnum boardType,
            @RequestParam(required = false) PaymentTypeEnum paymentType) {
        stayService.updateStay(id, accommodatedNumber, dateFrom, dateTo, state, boardType, paymentType);
    }

}
