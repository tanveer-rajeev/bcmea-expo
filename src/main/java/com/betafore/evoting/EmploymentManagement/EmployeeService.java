package com.betafore.evoting.EmploymentManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.ParticipantManagement.Participant;
import com.betafore.evoting.ParticipantManagement.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmployeeService implements EmployeeDao {

    private final EmployeeRepository employeeRepository;
    private final ParticipantRepository participantRepository;
    private final ExpoRepository expoRepository;

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    @Override
    public List<Employee> allEmployeeByParticipant(Long participantId) throws CustomException {

        return participantRepository.findById(participantId)
            .orElseThrow(() -> new CustomException("Participant not found by given id: " + participantId))
            .getEmployeeList();
    }

    public List<Employee> allByExpoId(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return employeeRepository.findAllByExpoId(expoId);
    }

    @Override
    public Employee findById(Long id) throws CustomException {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeById(id);
        if (optionalEmployee.isEmpty())
            throw new CustomException("Employee not created by the given id: " + id);

        return optionalEmployee.get();
    }

    @Override
    @Transactional
    public Employee save(Employee employee, Long participantId, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Participant participant = participantRepository.findByExpoIdAndId(expoId, participantId).orElseThrow(() -> new CustomException("Participant not found"));
        if (!participant.getExpoId().equals(expoId)) {
            throw new CustomException("The participant not from this expo");
        }

        Optional<Employee> optionalEmployee = employeeRepository.findByExpoIdAndPhoneNumberOrEmail(expoId, employee.getPhoneNumber(), employee.getEmail());
        if (optionalEmployee.isPresent()) {
            throw new CustomException("Email or phone number already present ");
        }
        try {
            employee.setExpoId(expoId);
            employee.setParticipant(participant);
            participantRepository.save(participant);
            return employeeRepository.save(employee);
        } catch (Exception e) {
            throw new CustomException("Error while saving employee: " + e.getMessage());
        }

    }

    @Transactional
    public void updateEmployee(Long id, Employee employeeDto) throws CustomException {
        Employee employee = findById(id);
        if (employeeDto.getName() != null && !employeeDto.getName().isEmpty() && !employeeDto.getName().equals(employee.getName()))
            employee.setName(employeeDto.getName());
        if (employeeDto.getPhoneNumber() != null && !employeeDto.getPhoneNumber().isEmpty() && !employeeDto.getPhoneNumber().equals(employee.getPhoneNumber())) {
            Optional<Employee> optionalEmployee = employeeRepository.findByPhoneNumberAndExpoId(employeeDto.getPhoneNumber(), employee.getExpoId());
            if (optionalEmployee.isPresent()) {
                throw new CustomException("Phone number already present, try new one");
            }
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
        }
        if (employeeDto.getEmail() != null && !employeeDto.getEmail().isEmpty() && !employeeDto.getEmail().equals(employee.getEmail())) {
            Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmailAndExpoId(employeeDto.getEmail(), employee.getExpoId());
            if (employeeByEmail.isPresent()) {
                throw new CustomException("Email already present, try new one");
            }
            employee.setEmail(employeeDto.getEmail());
        }
        if (employeeDto.getCompany() != null && !employeeDto.getCompany().isEmpty() && !employeeDto.getCompany().equals(employee.getCompany()))
            employee.setCompany(employeeDto.getCompany());
        if (employeeDto.getDesignation() != null && !employeeDto.getDesignation().isEmpty() && !employeeDto.getDesignation().equals(employee.getDesignation()))
            employee.setDesignation(employeeDto.getDesignation());
        if (employeeDto.getAddress() != null && !employeeDto.getAddress().isEmpty() && !employeeDto.getAddress().equals(employee.getAddress()))
            employee.setAddress(employeeDto.getAddress());

    }

    @Override
    public void removeById(Long id) throws CustomException {
        employeeRepository.findById(id).orElseThrow(() -> new CustomException("Employee not found"));
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        employeeRepository.deleteAllByExpoId(expoId);
    }
}
