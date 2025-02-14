package com.example.device.repository;

import com.example.device.model.Device;
import com.example.device.model.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByStatus(DeviceStatus status);
    List<Device> findByAssignedToId(Long employeeId);
} 