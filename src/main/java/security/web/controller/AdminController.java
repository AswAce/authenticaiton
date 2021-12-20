package security.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.exceptions.UniquePlayerIdExistException;
import security.exceptions.UserNotFoundException;
import security.service.AdminService;
import security.web.dto.admin.ApprovalInfoDto;
import security.web.dto.admin.ApprovedUserDto;

import java.util.List;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/for-approval")
    public List<ApprovalInfoDto> forApproval() {
        return this.adminService.getUsersForApproval();
    }

    @PostMapping("/approve")
    public ResponseEntity approve(@RequestBody ApprovedUserDto body) {

        try {
            this.adminService.approveUser(body);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UniquePlayerIdExistException | UserNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);

        }


    }
}
