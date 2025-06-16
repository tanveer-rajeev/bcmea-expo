package com.betafore.evoting.ExpoServiceManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.Expo;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.RolePermissionManagement.PermissionEnumService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpoServiceDaoImpl implements ExpoServiceDao {

    private final ExpoServiceRepository expoServiceRepository;
    private final ExpoRepository expoRepository;
    private final PermissionEnumService permissionEnumService;

    @Override
    public List<ExpoService> all() {
        return expoServiceRepository.findAll();
    }

    public ExpoService findByName(String name) throws CustomException {
        Optional<ExpoService> optionalExpoService = expoServiceRepository.findByName(name);
        if (optionalExpoService.isEmpty()) {
            throw new CustomException("Expo Service not found by given name: " + name);
        }
        return optionalExpoService.get();
    }

    public ExpoService findEnableExpoServiceById(Long serviceId) throws CustomException {
        Optional<ExpoService> optionalExpoService = expoServiceRepository.findEnableServiceById(serviceId);
        if (optionalExpoService.isEmpty()) {
            throw new CustomException("Expo Service not enabled by given id: " + serviceId);
        }

        return optionalExpoService.get();
    }

    @Override
    public ExpoService findById(Long id) throws CustomException {
        Optional<ExpoService> optionalExpoService = expoServiceRepository.findById(id);
        if (optionalExpoService.isEmpty()) {
            throw new CustomException("Expo Service not found by given id: " + id);
        }
        return optionalExpoService.get();
    }

    public ExpoService findEnableServiceById(Long id) throws CustomException {
        Optional<ExpoService> optionalExpoService = expoServiceRepository.findEnableServiceById(id);
        if (optionalExpoService.isEmpty()) {
            throw new CustomException("Expo Service not enabled by given id" + id);
        }
        return optionalExpoService.get();
    }

    @Override
    public ExpoService save(ExpoService expoServiceDto) throws CustomException {
        Optional<ExpoService> optionalExpoService = expoServiceRepository.findByName(expoServiceDto.getName());
        if (optionalExpoService.isPresent()) {
            throw new CustomException("Expo service already present");
        }

        if (!isPermissionCreatedByServiceName(expoServiceDto.getName())) {
            throw new CustomException("Please create first permission as like service name or " +
                "permission prefix not match with expo service prefix : "+expoServiceDto.getName());
        }

        ExpoService expoService = ExpoService.builder().name(expoServiceDto.getName())
            .status(expoServiceDto.getStatus()).build();
        return expoServiceRepository.save(expoService);
    }

    public List<ExpoService> saveAll(ExpoServiceDto expoService) throws CustomException {
        List<ExpoService> expoServices = new ArrayList<>();
        for (ExpoService expoS : expoService.getExpoServices()) {
            expoServices.add(save(expoS));
        }
        return expoServices;
    }

    @Override
    @Transactional
    public void saveAndFlush(ExpoService expoServiceDto, Long id) throws CustomException {
        ExpoService expoService = findById(id);

        if (expoServiceDto.getName() != null && !expoServiceDto.getName().equals(expoService.getName())) {
            Optional<ExpoService> optionalExpoService = expoServiceRepository.findByName(expoServiceDto.getName());
            if (optionalExpoService.isPresent()) {
                throw new CustomException("Expo service already present");
            }
            if (!isPermissionCreatedByServiceName(expoServiceDto.getName())) {
                throw new CustomException("Please create first permission as like service name or permission prefix not match with expo service prefix");
            }
            expoService.setName(expoServiceDto.getName());
        }
        if (expoServiceDto.getStatus() != null && !expoServiceDto.getStatus().equals(expoService.getStatus())) {
            expoService.setStatus(expoServiceDto.getStatus());
        }
    }

    public boolean isPermissionCreatedByServiceName(String serviceName) {
        String[] split = serviceName.split(" ");
        Set<String> aSetOfPermissionEnumByPrefix = permissionEnumService.getASetOfPermissionEnumByPrefix(split[0]);
        return !aSetOfPermissionEnumByPrefix.isEmpty();
    }

    @Override
    public void removeById(Long id) throws CustomException {
        ExpoService expoService = findById(id);
        List<Expo> expoList = expoRepository.findAll();
        expoList.forEach(expo -> expo.getExpoServiceSet().remove(expoService));
        expoServiceRepository.deleteById(id);
    }

    @Override
    public void removeAll() {
        expoServiceRepository.deleteAll();
    }



}
