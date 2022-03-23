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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {

    @Autowired
    HoaxService hoaxService;

    @PostMapping("/hoaxes")
    public GenericResponse saveHoax(@Valid @RequestBody Hoax hoax, @CurrentUser User user){
        hoaxService.save(hoax, user);
        return new GenericResponse("Hoax is saved");
    }

    @GetMapping("/hoaxes")
    public Page<HoaxVM> getHoaxes(@PageableDefault(sort = "id" , direction = Direction.DESC) Pageable pageable){
        return hoaxService.getHoaxes(pageable).map(HoaxVM::new);
    }

    @GetMapping("/hoaxes/{id:[0-9]+}")
    public Page<HoaxVM> getHoaxesRelative(@PageableDefault(sort = "id" , direction = Direction.DESC) Pageable pageable, @PathVariable long id){
        return hoaxService.getOldHoaxes(id, pageable).map(HoaxVM::new);
    }

    @GetMapping("/users/{username}/hoaxes")
    public Page<HoaxVM> getUserHoaxes(@PathVariable String username, @PageableDefault(sort = "id" , direction = Direction.DESC) Pageable pageable){
        return hoaxService.getUserHoaxes(username, pageable).map(HoaxVM::new);
    }

    @GetMapping("/users/{username}/hoaxes/{id:[0-9]+}")
    public Page<HoaxVM> getUserHoaxesRelative(@PathVariable long id, @PathVariable String username, @PageableDefault(sort = "id" , direction = Direction.DESC) Pageable pageable){
        return hoaxService.getOldHoaxesOfUser(id, username, pageable).map(HoaxVM::new);
    }
}
