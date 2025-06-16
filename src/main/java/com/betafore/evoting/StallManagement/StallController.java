package com.betafore.evoting.StallManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.HallManagement.HallService;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.QuestionMangement.QuestionRepository;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import com.betafore.evoting.response.MessageResponse;
import com.betafore.evoting.response.ResultResponse;
import com.betafore.evoting.security_config.CustomHasAuthority;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stalls")
public class StallController {

    private final StallService service;
    private final HallService hallService;
    private final FileStorageServiceImp fileStorageServiceImp;
    private final ExpoRepository expoRepository;
    private final QuestionRepository questionRepository;

    private String makeSlug(String name) {
        return String.join("-", name.toLowerCase().split(" "));
    }

    @Operation(summary = "Get all stall")
    @CustomHasAuthority(authorities = PermissionEnum.STALL_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ResultResponse> list(@PathVariable Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<Stall> stalls = service.getAll(expoId);
        ResultResponse response = new ResultResponse(new StallResourceMapper().collection(stalls));
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create stall")
    @CustomHasAuthority(authorities = PermissionEnum.STALL_CREATE)
    @PostMapping(path = "/{expoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> save(@Valid @ModelAttribute StallRequest request,
                                                @PathVariable Long expoId) throws CustomException {

        checkValidExpoId(expoId);
        Optional<Hall> optional = hallService.getByIdAndExpoId(request.getHallId(), expoId);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Optional<Stall> byNameAndExpoId = service.findByNameAndExpoId(request.getName(), expoId);
        if (byNameAndExpoId.isPresent()) {
            throw new CustomException("Stall name  is already present in the expo");
        }

        String slug = makeSlug(request.getName());
        Stall stall = new Stall(expoId, optional.get(), request.getName(), slug, null, null, null, request.getIdentity());


        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            String filename = fileStorageServiceImp.saveFile(request.getLogo());
            stall.setLogo(filename);
        }

        if (request.getBackground() != null && !request.getBackground().isEmpty()) {
            String filename = fileStorageServiceImp.saveFile(request.getBackground());
            stall.setBackground(filename);
        }

        MessageResponse response = new MessageResponse("A stall has been created successfully.");
        service.save(stall);
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get stall by id")
    @CustomHasAuthority(authorities = PermissionEnum.STALL_READ)
    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Stall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching stall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ResultResponse response = new ResultResponse(new StallResourceMapper().single(optional.get()));
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update stall by id")
    @CustomHasAuthority(authorities = PermissionEnum.STALL_UPDATE)
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute StallRequest request) throws CustomException {
        Optional<Stall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        MessageResponse response = new MessageResponse("The stall has been updated successfully.");
        Stall stall = optional.get();
        if (request.getName() != null && !request.getName().isEmpty() && !request.getName().equals(stall.getName())) {
            Optional<Stall> byNameAndExpoId = service.findByNameAndExpoId(request.getName(), stall.getExpoId());
            if (byNameAndExpoId.isPresent()) {
                throw new CustomException("Stall name  is already present in the expo");
            }
            stall.setName(request.getName());
            stall.setSlug(makeSlug(request.getName()));
        }

        if (request.getHallId() != null) {
            Optional<Hall> optionalHall = hallService.getById(request.getHallId());

            if (optionalHall.isEmpty()) {
                response = new MessageResponse("Sorry, we couldn't find any matching hall.");
                response.setStatus("error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            stall.setHall(optionalHall.get());
        }

        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            if (stall.getLogo() != null && !stall.getLogo().isEmpty() && fileStorageServiceImp.fileDoesExist(stall.getLogo())) {
                fileStorageServiceImp.deleteFile(stall.getLogo());
            }

            String filename = fileStorageServiceImp.saveFile(request.getLogo());
            stall.setLogo(filename);
        }

        if (request.getBackground() != null && !request.getBackground().isEmpty()) {
            if (stall.getBackground() != null && !stall.getBackground().isEmpty() && fileStorageServiceImp.fileDoesExist(stall.getBackground())) {
                fileStorageServiceImp.deleteFile(stall.getBackground());
            }

            String filename = fileStorageServiceImp.saveFile(request.getBackground());
            stall.setBackground(filename);
        }

        response.setStatus("success");
        service.save(stall);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete stall by id")
    @CustomHasAuthority(authorities = PermissionEnum.STALL_DELETE)
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        Optional<Stall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching stall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Stall stall = optional.get();

        if (stall.getLogo() != null && !stall.getLogo().isEmpty() && fileStorageServiceImp.fileDoesExist(stall.getLogo())) {
            fileStorageServiceImp.deleteFile(stall.getLogo());
        }
        List<Question> questionList = questionRepository.findAllByExpoId(stall.getExpoId());

        for (Question question : questionList) {
            for (Stall stallFromQuestion : question.getStalls()) {
                if (stallFromQuestion.getId().equals(id)) {
                    question.getStalls().remove(stall);
                    break;
                }
            }
        }

        service.deleteById(id);
        MessageResponse response = new MessageResponse("The stall has been deleted successfully.");
        response.setStatus("success");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Get stall logo")
    @GetMapping("/logo/{id}")
    public ResponseEntity<?> getLogo(@PathVariable Long id) throws IOException {
        Optional<Stall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching stall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Stall stall = optional.get();
        if (stall.getLogo() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
            fileStorageServiceImp.loadFile(stall.getLogo())
        );
    }

    @Operation(summary = "Get stall background")
    @GetMapping("/background/{id}")
    public ResponseEntity<?> getBackground(@PathVariable Long id) throws IOException {
        Optional<Stall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching stall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Stall stall = optional.get();
        if (stall.getBackground() == null) {
            return ResponseEntity.ok().body(ApiResponse.builder().message("No image found")
                .build());
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(
            fileStorageServiceImp.loadFile(stall.getBackground())
        );
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
