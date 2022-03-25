package com.hoaxify.ws.hoax;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HoaxService {

    HoaxRepository hoaxRepository;

    UserService userService;

    public HoaxService(HoaxRepository hoaxRepository, UserService userService) {
        this.hoaxRepository = hoaxRepository;
        this.userService = userService;
    }

    public void save(Hoax hoax, User user) {
        hoax.setTimestamp(new Date());
        hoax.setUser(user);
        hoaxRepository.save(hoax);
    }

    public Page<Hoax> getHoaxes(Pageable pageable) {
        return hoaxRepository.findAll(pageable);
    }

    public Page<Hoax> getUserHoaxes(String username, Pageable pageable) {
        User inDB = userService.getByUsername(username);
        return hoaxRepository.findByUser(inDB, pageable);
    }

    public Page<Hoax> getOldHoaxes(long id, Pageable pageable) {
        return hoaxRepository.findByIdLessThan(id,pageable);
    }

    public Page<Hoax> getOldHoaxesOfUser(long id, String username, Pageable pageable) {
        User inDB = userService.getByUsername(username);
        return hoaxRepository.findByIdLessThanAndUser(id, inDB, pageable);
    }

    public long getNewHoaxesCount(long id) {
        return hoaxRepository.countByIdGreaterThan(id);
    }
}
