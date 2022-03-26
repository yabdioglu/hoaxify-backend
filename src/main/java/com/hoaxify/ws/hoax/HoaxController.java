package com.hoaxify.ws.hoax;

import com.hoaxify.ws.hoax.vm.HoaxVM;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {

    @Autowired
    HoaxService hoaxService;

    @PostMapping("/hoaxes")
    public GenericResponse saveHoax(@Valid @RequestBody Hoax hoax, @CurrentUser User user) {
        hoaxService.save(hoax, user);
        return new GenericResponse("Hoax is saved");
    }

    @GetMapping("/hoaxes")
    public Page<HoaxVM> getHoaxes(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
        return hoaxService.getHoaxes(pageable).map(HoaxVM::new);
    }

    @GetMapping({"/hoaxes/{id:[0-9]+}", "/users/{username}/hoaxes/{id:[0-9]+}"})
    public ResponseEntity<?> getHoaxesRelative(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
                                               @PathVariable long id,
                                               @PathVariable(required = false) String username,
                                               @RequestParam(name = "count", required = false, defaultValue = "false") boolean count,
                                               @RequestParam(name = "direction", defaultValue = "before") String direction) {
        if (count) {
            long newHoaxCount = hoaxService.getNewHoaxesCount(id, username);
            // { count : 8 } Java karşlığı map
            Map<String, Long> response = new HashMap<>();
            response.put("count", newHoaxCount);
            return ResponseEntity.ok(response);
        }
        if (direction.equals("after")) {
            List<HoaxVM> newHoaxs = hoaxService.getNewHoaxes(id, username, pageable.getSort())
                    .stream().map(HoaxVM::new).collect(Collectors.toList());
            return ResponseEntity.ok(newHoaxs);
        }
        return ResponseEntity.ok(hoaxService.getOldHoaxes(id, username, pageable).map(HoaxVM::new));
    }

    @GetMapping("/users/{username}/hoaxes")
    public Page<HoaxVM> getUserHoaxes(@PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
        return hoaxService.getUserHoaxes(username, pageable).map(HoaxVM::new);
    }

}
