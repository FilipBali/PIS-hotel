package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.stay.StayDO;
import com.fit.vut.pis_hotel.domain.stay.StayDTO;
import com.fit.vut.pis_hotel.domain.stay.StayService;
import com.fit.vut.pis_hotel.domain.stay.enums.BoardTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.PaymentTypeEnum;
import com.fit.vut.pis_hotel.domain.stay.enums.StateEnum;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDO;
import com.fit.vut.pis_hotel.domain.stay.service.ServiceDTO;
import com.fit.vut.pis_hotel.domain.stay.service.enums.ServiceTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/stay")
public class StayController {

    private final StayService stayService;

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
            @RequestParam(required = false) StateEnum state,
            @RequestParam(required = false) BoardTypeEnum boardType,
            @RequestParam(required = false) PaymentTypeEnum paymentType) {
        stayService.updateStay(id, accommodatedNumber, dateFrom, dateTo, state, boardType, paymentType);
    }

    @GetMapping(value = "/service")
    public List<ServiceDO> getServices() {
        return stayService.getServices();
    }

    @GetMapping(value = "/service/{id}")
    public ServiceDO getService(@PathVariable("id") Long id) {
        return stayService.getService(id);
    }

    @GetMapping(value = "/service/simple/{id}")
    public ServiceDTO getSimpleService(@PathVariable("id") Long id) {
        return stayService.getSimpleService(id);
    }

    @GetMapping(value = "/service/simple")
    public List<ServiceDTO> getSimpleServices() {
        return stayService.getSimpleServices();
    }

    @GetMapping(value = "/{stayId}/service")
    public List<ServiceDO> getStayServices(@PathVariable("stayId") Long stayId) {
        return stayService.getStayServices(stayId);
    }

    @PostMapping(value = "/service/simple")
    public void createAndAssignService(@RequestBody ServiceDTO service) {
        stayService.createAndAssignService(service);
    }

    @PostMapping(value = "/service")
    public void createService(@RequestBody ServiceDO service) {
        stayService.createService(service);
    }

    @DeleteMapping(value = "/service/{id}")
    public void deleteService(@PathVariable Long id) {
        stayService.deleteService(id);
    }

    @PutMapping(value = "/service/{id}")
    public void updateService(
            @PathVariable("id") Long id,
            @RequestParam(required = false) LocalDateTime timeFrom,
            @RequestParam(required = false) LocalDateTime timeTo,
            @RequestParam(required = false) ServiceTypeEnum serviceType,
            @RequestParam(required = false) PaymentTypeEnum paymentType,
            @RequestParam(required = false) Integer bowlingTracks,
            @RequestParam(required = false) StateEnum state) {
        stayService.updateService(id, timeFrom, timeTo, serviceType, paymentType, bowlingTracks, state);
    }

}
