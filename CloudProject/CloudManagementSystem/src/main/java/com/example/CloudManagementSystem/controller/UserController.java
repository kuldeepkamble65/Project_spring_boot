package com.example.CloudManagementSystem.controller;

import com.example.CloudManagementSystem.DTO.LoginDto;
import com.example.CloudManagementSystem.DTO.ProductDto;
import com.example.CloudManagementSystem.entity.User;
import com.example.CloudManagementSystem.exception.*;
import com.example.CloudManagementSystem.service.SessionDetailsService;
import com.example.CloudManagementSystem.service.UserService;
import com.example.CloudManagementSystem.validation.Uservalidation;
import cookies.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
@Api(tags = "User Management")
@RequestMapping("/user")
@RestController
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController .class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionDetailsService sessionDetailsService;

    /**
     * endpoint to resister the user
     *
     * @param user The user object to be registered
     * @return ResponseEntity with appropriate message
     */
    @ApiOperation(value = "Register a new user")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        LOGGER.info("Received request to register a new user.");
            Uservalidation.validateUser(user);
            User user1 = userService.registerUser(user);
            return ResponseEntity.ok("User Registered Successfully...!");
        }

    /**
     * Endpoint to authenticate and login a user
     *
     * @param loginDto The DTO containing login credentials
     * @param response HttpServletResponse object to set cookie
     * @return ResponseEntity with appropriate message
     */
    @ApiOperation(value = "Login a user")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        LOGGER.info("Received request to login user with username: " + loginDto.getEmail());

            User user = userService.login(loginDto);

            if (user != null) {
                CookieUtil.addCookieForUser(response, user, sessionDetailsService);
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username / Password");
            }
    }

    /**
     * Endpoint to add products and subscriptions for a user
     *
     * @param userId         The ID of the user
     * @param productDtoList List of products to be added
     * @return ResponseEntity with appropriate message
     */
    @ApiOperation(value = "Add products and subscriptions for a user")
    @PostMapping(value = "/addProductsAndSubscriptions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProductsAndSubscriptions(@RequestHeader("userId") int userId,
                                                              @RequestBody List<ProductDto> productDtoList) {
        LOGGER.info("Received request to add products and subscriptions for user with ID: " + userId);
        try {
            userService.addProductsAndSubscriptions(userId, productDtoList);
            return ResponseEntity.ok("Products and subscriptions added successfully for user with ID: " + userId);
        } catch (UserNotFoundException | GstinNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding products and subscriptions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /**
     * Endpoint to log out a user
     *
     * @param response HttpServletResponse object to clear cookie
     * @return ResponseEntity with appropriate message
     */
    @ApiOperation(value = "Logout a user")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        LOGGER.info("Received request to logout user.");
        try {
            CookieUtil.clearCookies(response);
            return ResponseEntity.ok("Logout successfully..");
        } catch (Exception e) {
            LOGGER.error("Error occurred during logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /**
     * Endpoint to update subscription start date
     *
     * @param subcriptionId ID of the subscription to update
     * @param newstartDate  New start date for the subscription
     * @return ResponseEntity with appropriate message
     */
    @PostMapping(value = "/updateStartDate")
    public ResponseEntity<?> updateStartDate(@RequestHeader("subcriptionId") int subcriptionId,
                                             @RequestHeader("newStartDate") String newstartDate) {
        LOGGER.info("Received request to update subscription start date for ID: " + subcriptionId);
        try {
            LocalDate newSubStartDate = LocalDate.parse(newstartDate);
            userService.updateSubStartDate(subcriptionId, newSubStartDate);
            return ResponseEntity.ok("Subcription StartDate updated Successfully");
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating subscription start date: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    /**
     * Endpoint to update subscription end date
     *
     * @param subcriptionId ID of the subscription to update
     * @param newEndDate    New end date for the subscription
     * @return ResponseEntity with appropriate message
     */
    @PostMapping(value = "/updateEndDate")
    public ResponseEntity<?> updateEndDate(@RequestHeader("subcriptionId") int subcriptionId,
                                           @RequestHeader("newEndDate") String newEndDate) {
        LOGGER.info("Received request to update subscription end date for ID: " + subcriptionId);
        try {
            LocalDate newSubEndDate = LocalDate.parse(newEndDate);
            userService.updateSubEndDate(subcriptionId, newSubEndDate);
            return ResponseEntity.ok("Subscription end date updated successfully");
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating subscription end date: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateSubscriptionStatus() {
        try {
            userService.updateSubscriptionStatuses();
            LOGGER.info("Subscription statuses updated successfully.");
            return ResponseEntity.ok("Subscription statuses updated successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to update subscription statuses: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update subscription statuses: " + e.getMessage());
        }
    }
}
