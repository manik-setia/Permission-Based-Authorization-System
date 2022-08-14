package com.test.device.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceApi {

    @Autowired
    DeviceRepository deviceRepository;

    @GetMapping
    @RolesAllowed({"ROLE_CUSTOMER", "ROLE_EDITOR"})
    public List<Device> list(){
        return deviceRepository.findAll();
    }

    @PostMapping
    @RolesAllowed({"ROLE_EDITOR"})
    public ResponseEntity<Device> create(@RequestBody @Valid Device device){
        Device savedDevice=deviceRepository.save(device);
        URI deviceURI= URI.create("/devices/"+savedDevice.getId());

        return ResponseEntity.created(deviceURI).body(savedDevice);
    }
}
