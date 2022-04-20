package com.fit.vut.pis_hotel.controller;

import com.fit.vut.pis_hotel.domain.host.HostDO;
import com.fit.vut.pis_hotel.domain.host.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/host")
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;

    @GetMapping
    public List<HostDO> getHosts() {
        return hostService.getHosts();
    }

    @GetMapping(value = "/{id}")
    public HostDO getHost(@PathVariable("id") Long id) {
        return hostService.getHost(id);
    }

    @PostMapping
    public void createHost(@RequestBody HostDO Host) {
        hostService.createHost(Host);
    }

    @DeleteMapping(path = "{id}")
    public void deleteHost(@PathVariable("id") Long id) {
        hostService.deleteHost(id);
    }

    @PutMapping(path = {"{id}"})
    public void updateHost(
            @PathVariable("id") Long id, @RequestBody HostDO host) {
        hostService.updateHost(id, host);
    }

}
