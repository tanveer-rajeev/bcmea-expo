package com.betafore.evoting.HallManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.QuestionMangement.QuestionRepository;
import com.betafore.evoting.StallManagement.StallRepository;
import com.betafore.evoting.response.MessageResponse;
import com.betafore.evoting.response.ResultResponse;
import com.betafore.evoting.QuestionMangement.QuestionService;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/halls")
public class HallController {

    private final HallService service;
    private final ExpoRepository expoRepository;

    @GetMapping("/getQuestions/{id}")
    public ResponseEntity<ResultResponse> getQuestion(@PathVariable Long id) throws CustomException {
        Hall hall = getHallById(id);
        ResultResponse response = new ResultResponse();
        response.setResults(hall.getQuestions());
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get hall")
    private Hall getHallById(Long id) throws CustomException {
        Optional<Hall> optionalHall = service.getById(id);
        if (optionalHall.isEmpty()) {
            throw new CustomException("Hall not found by given id: " + id);
        }

        return optionalHall.get();
    }

    @Operation(summary = "Get all hall by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.HALL_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ResultResponse> list(@RequestParam(required = false) String with,
                                               @PathVariable Long expoId) throws CustomException {
        checkValidExpoId(expoId);

        List<Hall> halls = service.getAll(expoId);
        ResultResponse response = new ResultResponse();

        if (with != null && with.equals("stalls")) {
            response.setResults(new HallResourceMapper().collection(halls));
        } else {
            response.setResults(halls);
        }

        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    private String makeSlug(String name) {
        return String.join("-", name.toLowerCase().split(" "));
    }


    @Operation(summary = "Create hall")
    @CustomHasAuthority(authorities = PermissionEnum.HALL_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<MessageResponse> save(@RequestBody HallRequest request,
                                                @PathVariable Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Optional<Hall> bySlugAndExpoId = service.getByNameAndExpoId(request.getName(), expoId);
        if (bySlugAndExpoId.isPresent()) {
            throw new CustomException("Slug name already present in the expo");
        }
        String slug = makeSlug(request.getName());
        Hall hall = new Hall(expoId, request.getName(), slug, null, null, null);
        MessageResponse response = new MessageResponse("A hall has been created successfully.");
        service.save(hall);
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get hall by id")
    @CustomHasAuthority(authorities = PermissionEnum.HALL_READ)
    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id, @RequestParam(required = false) String with) {
        Optional<Hall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall by given id.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ResultResponse response = new ResultResponse();
        response.setResults(optional.get());

        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update hall by id")
    @CustomHasAuthority(authorities = PermissionEnum.HALL_UPDATE)
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody HallRequest request) throws CustomException {
        Optional<Hall> optional = service.getById(id);

        if (optional.isEmpty()) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        MessageResponse response = new MessageResponse("The hall has been updated successfully.");
        Hall hall = optional.get();
        Optional<Hall> bySlugAndExpoId = service.getByNameAndExpoId(request.getName(), hall.getExpoId());
        if (bySlugAndExpoId.isPresent()) {
            throw new CustomException("Hall name already present in the expo");
        }

        hall.setName(request.getName());
        hall.setSlug(makeSlug(request.getName()));
        response.setStatus("success");
        service.save(hall);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Delete hall by id")
    @CustomHasAuthority(authorities = PermissionEnum.HALL_DELETE)
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) throws CustomException {
        try {
            service.deleteById(id);
            MessageResponse response = new MessageResponse("The hall has been deleted successfully.");
            response.setStatus("success");
            return ResponseEntity.ok().body(response);
        } catch (EmptyResultDataAccessException e) {
            MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
