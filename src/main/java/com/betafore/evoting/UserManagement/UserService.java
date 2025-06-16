package com.betafore.evoting.UserManagement;

import com.betafore.evoting.EmailConfig.EmailSenderServiceImpl;
import com.betafore.evoting.EmailConfig.SendEmailDto;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.GiftManagement.Gift;
import com.betafore.evoting.GiftManagement.GiftCount;
import com.betafore.evoting.GiftManagement.GiftCountService;
import com.betafore.evoting.GiftManagement.GiftService;
import com.betafore.evoting.SeminarManagement.Seminar;
import com.betafore.evoting.SeminarManagement.SeminarService;
import com.betafore.evoting.SmsConfig.SMS_SendServiceImpl;
import com.betafore.evoting.security_config.CustomCipherService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService implements UserDao {

    private final UserRepository userRepository;
    private final SMS_SendServiceImpl sms_sendServiceImpl;
    private final GiftCountService giftCountService;
    private final SeminarService seminarService;
    private final GiftService giftService;
    private final ExpoRepository expoRepository;
    private final CustomCipherService customCipherService;
    private final EmailSenderServiceImpl emailSenderServiceImpl;

    @Override
    public User findById(Long id) throws CustomException {
        return userRepository.findById(id)
            .orElseThrow(() -> new CustomException("User not found: " + id));
    }

    public Set<Seminar> getSeminarListFromUser(Long id) throws CustomException {
        return findById(id).getSeminars();
    }

    @Override
    @Transactional(rollbackFor = CustomException.class)
    public User registration(UserRequestDto userDto, Long expoId) throws CustomException {

        try {
            User saved = saveUser(userDto, expoId);
            if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()){
                sendEmail(saved.getEmail(), expoId, saved.getId());
            }
//            sendSms(saved.getPhoneNumber(), expoId, saved.getId());
            return saved;

        } catch (CustomException exception) {
            throw new CustomException(exception.getMessage());
        }
    }

    @Transactional(rollbackFor = CustomException.class)
    public User createUser(UserRequestDto userDto, Long expoId, boolean sendEmail, boolean sendSms) throws CustomException {

        try {
            User saved = saveUser(userDto, expoId);
            if (sendEmail) sendEmail(saved.getEmail(), expoId, saved.getId());
            if (sendSms) sendSms(saved.getPhoneNumber(), expoId, saved.getId());
            return saved;

        } catch (CustomException exception) {
            throw new CustomException(exception.getMessage());
        }
    }

    @Transactional(rollbackFor = CustomException.class)
    public User saveUser(UserRequestDto userDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);

        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty() && userRepository.findUserByEmailAndExpoId(userDto.getEmail(), expoId).isPresent())
            throw new CustomException("Email already taken : " + userDto.getEmail());

        if (userRepository.findUserByPhoneNumberAndExpoId(userDto.getPhoneNumber(), expoId).isPresent())
            throw new CustomException("Phone number already taken : " + userDto.getPhoneNumber());

        User userBuilder = User.builder()
            .expoId(expoId)
            .name(userDto.getName())
            .phoneNumber(userDto.getPhoneNumber())
            .email(userDto.getEmail())
            .address(userDto.getAddress())
            .company(userDto.getCompany())
            .country(userDto.getCountry())
            .profession(userDto.getProfession())
            .build();

        return userRepository.save(userBuilder);
    }

    public void sendEmail(String email, Long expoId, Long userId) throws CustomException {
        try {
            String message = generateUserScanURL(userId);
            SendEmailDto sendEmailDto = SendEmailDto.builder().to(email).body(message).subject("Expo Entry Link").build();
            emailSenderServiceImpl.sendEmail(sendEmailDto, expoId);
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public void sendSms(String phoneNumber, Long expoId, Long userId) throws CustomException {
        try {
            String message = generateUserScanURL(userId);
            sms_sendServiceImpl.sendSms(expoId, phoneNumber, message);
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public String generateUserScanURL(Long userId) {
        String urlEncoded = URLEncoder.encode(customCipherService.encodeLong(userId), StandardCharsets.UTF_8);
        return "http://baseUrl/download/id-card/" + urlEncoded;
    }

    public User userScan(String userId) throws CustomException {
        CharSequence charSequence = userId;
        return findById(customCipherService.decodeLong(charSequence));
    }

    @Transactional(rollbackFor = CustomException.class)
    public void updateByStaff(Long userId, UserConfirmationDto userConfirmationDto) throws CustomException {

        User user = findById(userId);
        int totalEligibility = 0;
        Set<Seminar> seminarList = new HashSet<>();
        Set<Gift> giftList = new HashSet<>();

        if (userConfirmationDto.getSeminarList() != null && !userConfirmationDto.getSeminarList().isEmpty()) {
            user.setInterestedInSeminar(true);

            if (!user.getSeminars().isEmpty()) {
                user.getSeminars().clear();
            }
            for (Long seminarId : userConfirmationDto.getSeminarList()) {
                Seminar seminar = seminarService.findById(seminarId);
                if (!seminar.getExpoId().equals(user.getExpoId())) {
                    throw new CustomException("Seminar is not present in the current expo");
                }
                if (!user.getSeminars().isEmpty() && user.getSeminars().contains(seminar)) {
                    continue;
                }
                seminar.setNumOfGuest(seminar.getNumOfGuest() + 1);
                seminarList.add(seminar);
            }
            if (!seminarList.isEmpty()) user.addSeminarList(seminarList);
        } else if (userConfirmationDto.getSeminarList() != null) {
            user.getSeminars().clear();
        }

        if (userConfirmationDto.getGiftList() != null && !userConfirmationDto.getGiftList().isEmpty()) {
            if (!user.getGifts().isEmpty()) {
                user.getGifts().clear();
            }
            for (Long giftId : userConfirmationDto.getGiftList()) {
                Gift gift = giftService.findById(giftId);
                if (!gift.getExpoId().equals(user.getExpoId())) {
                    throw new CustomException("Gift is not present in the current expo");
                }
                if (!user.getGifts().isEmpty() && user.getGifts().contains(gift)) {
                    continue;
                }
                giftList.add(gift);
                GiftCount giftCount = GiftCount.builder()
                    .userId(userId)
                    .giftId(gift.getId())
                    .eligibility(gift.getEligibility())
                    .build();
                giftCountService.deleteByGiftIdAndUserId(giftId, userId);
                giftCountService.save(giftCount);
                if (user.getTotalGiftCount() == null) {
                    user.setTotalGiftCount(0);
                }
                totalEligibility += gift.getEligibility();
            }
            user.setTotalGiftCount(totalEligibility);
            if (!giftList.isEmpty()) user.addGiftList(giftList);
        } else if (userConfirmationDto.getGiftList() != null) {
            user.getGifts().clear();
            user.setTotalGiftCount(0);
            giftCountService.deleteAllByUserId(userId);
        }

        if (userConfirmationDto.getIsInvolvedWithCeramic() != null && !userConfirmationDto.getIsInvolvedWithCeramic().isEmpty()) {
            boolean isInvolved = Boolean.parseBoolean(userConfirmationDto.getIsInvolvedWithCeramic());

            if (isInvolved) {
                user.setInvolvedWithCeramic(true);
                if (userConfirmationDto.getDesignation() == null || userConfirmationDto.getDesignation().isEmpty()) {
                    throw new CustomException("Please fill up designation");
                }
                if (userConfirmationDto.getWorkAddress() == null || userConfirmationDto.getWorkAddress().isEmpty()) {
                    throw new CustomException("Please fill up work address");
                }
                user.setDesignation(userConfirmationDto.getDesignation());
                user.setWorkAddress(userConfirmationDto.getWorkAddress());
            } else {
                user.setInvolvedWithCeramic(false);
                user.setDesignation(null);
                user.setWorkAddress(null);
            }
        }
        user.setAttendExpo(true);
    }


    public User findUserByPhoneNumber(String phoneNumber) throws CustomException {
        return userRepository.findUserByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new CustomException("Phone number not found"));
    }

    @Override
    public List<User> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return userRepository.findAllByExpoId(expoId);
    }

    @Override
    @Transactional
    public User updateByUser(UserRequestDto userDto, Long id) throws CustomException {

        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found by given id"));

        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty() && !userDto.getEmail().equals(user.getEmail())) {

            if (userRepository.findUserByEmail(userDto.getEmail()).isPresent())
                throw new CustomException("Email already taken : " + userDto.getEmail());

            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty() && !userDto.getName().equals(user.getName())) {
            user.setName(userDto.getName());
        }
        if (userDto.getAddress() != null && !userDto.getAddress().isEmpty() && !userDto.getAddress().equals(user.getAddress())) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getCompany() != null && !userDto.getCompany().isEmpty() && !userDto.getCompany().equals(user.getCompany())) {
            user.setCompany(userDto.getCompany());
        }
        if (userDto.getCountry() != null && !userDto.getCountry().isEmpty() && !userDto.getCountry().equals(user.getCountry())) {
            user.setCountry(userDto.getCountry());
        }
        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty() && !userDto.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.findUserByPhoneNumber(userDto.getPhoneNumber()).isPresent())
                throw new CustomException("Phone number already taken : " + userDto.getPhoneNumber());
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (userDto.getProfession() != null && !userDto.getProfession().isEmpty() && !userDto.getProfession().equals(user.getProfession())) {
            user.setProfession(userDto.getProfession());
        }
        return user;
    }

    @Override
    @Transactional
    public void removeById(Long id) throws CustomException {
        userRepository.findById(id).orElseThrow(() -> new CustomException("User not found by given id"));
        giftCountService.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<User> users = userRepository.findAllByExpoId(expoId);
        for (User user : users) {
            giftCountService.deleteAllByUserId(user.getId());
            userRepository.deleteById(user.getId());
        }

    }

    @Transactional
    public void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

}
