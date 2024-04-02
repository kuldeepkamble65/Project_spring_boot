package com.example.CloudManagementSystem.service;

import com.example.CloudManagementSystem.DTO.LoginDto;
import com.example.CloudManagementSystem.DTO.ProductDto;
import com.example.CloudManagementSystem.DTO.SubscriptionDataDto;
import com.example.CloudManagementSystem.controller.UserController;
import com.example.CloudManagementSystem.entity.*;
import com.example.CloudManagementSystem.exception.*;
import com.example.CloudManagementSystem.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class UserSeriveImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubcriptionRepository subscriptionRepository;

    @Autowired
    private GstinRepository gstinRepository;

    @Autowired
    private UserProductGstinMappingRepository mappingRepository;
    /**
     * Registers a new user
     *
     * @param user The user object to be registered
     * @return The registered user object
     */
    @Override
    public User registerUser(User user) {

            if (userRepository.findByEmail(user.getEmail()) != null) {
                throw new EmailNotFoundException("Email already exists. Choose another one.");
            }

            String hashedPassword = encryptionService.encryptPassword(user.getPassword());

            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(hashedPassword);
            LOGGER.info("Registering new user: {}", newUser.getEmail());
            return userRepository.save(newUser);
        }

    /**
     * Logs in a user
     *
     * @param loginDto The DTO containing login credentials
     * @return The logged-in user object
     */
    @Override
    public User login(LoginDto loginDto) {

            User user = userRepository.findByEmail(loginDto.getEmail());
            if (user != null && encryptionService.matches(loginDto.getPassword(), user.getPassword())) {
                LOGGER.info("User logged in: {}", user.getEmail());
                return user;
            } else {
                throw new UserNotFoundException("Invalid username/ password");
            }

    }
    /**
     * Updates subscription start date
     *
     * @param subcriptionId ID of the subscription to update
     * @param newSubStartDate New start date for the subscription
     */
    @Override
    public void updateSubStartDate(int subcriptionId, LocalDate newSubStartDate) {
        try{
            Subscription subscription = subscriptionRepository.findById(subcriptionId);
            if(subscription == null){
                throw new SubscriptionNotFoundException("subcription not found with id " +subcriptionId);
            }
            if (newSubStartDate.isBefore(LocalDate.now())) {
                throw new InvalidDateException("New start date cannot be in the past");
            }
            subscription.setStartDate(newSubStartDate);
            subscriptionRepository.save(subscription);
            LOGGER.info("Subscription start date updated successfully: {}", subcriptionId);
        }catch (SubscriptionNotFoundException | InvalidDateException e){
            LOGGER.error("Error updating subscription start date: {}", e.getMessage());
            throw e;
        }
    }
    /**
     * Updates subscription end date
     *
     * @param subcriptionId ID of the subscription to update
     * @param newSubEndDate New end date for the subscription
     */
    @Override
    public void updateSubEndDate(int subcriptionId, LocalDate newSubEndDate) {
        try{
            Subscription subscription = subscriptionRepository.findById(subcriptionId);
            if(subscription == null){
                throw new SubscriptionNotFoundException("Subcription not found with id "+ subcriptionId);
            }
            if(subscription.getStartDate().isAfter(newSubEndDate)){
                throw new InvalidDateException("Start date cannot be after the end date");
            }
            subscription.setEndDate(newSubEndDate);
            subscriptionRepository.save(subscription);
            LOGGER.info("Subscription end date updated successfully: {}", subcriptionId);
        }catch (SubscriptionNotFoundException | InvalidDateException e){
            LOGGER.error("Error updating subscription end date: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDto> getProductsByUserId(int userId) {
        return null;
    }


    @Override
    public void addProductsAndSubscriptions(int userId, List<ProductDto> productDtoList) {
        try {
            User user = userRepository.findByuserId(userId);

            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }

            for (ProductDto productDto : productDtoList) {
                Product product = productRepository.findByProductName(productDto.getProductName());
                if (product == null) {
                    System.out.println("Product not found for name: " + productDto.getProductName());
                    throw new ProductNotFoundException("Product not found with name: " + productDto.getProductName());
                }

                SubscriptionDataDto subscriptionDataDto = productDto.getSubscriptionData();
                Integer gstinId = subscriptionDataDto.getGstinId();
                Gstin gstin = getOrCreateGstin(gstinId, generateRandomPan());

                Subscription subscription = new Subscription();
                subscription.setStartDate(LocalDate.now());
                subscription.setEndDate(LocalDate.now().plusMonths(1));
                subscription.setPlan(subscriptionDataDto.getPlan());
                subscription.setPrice(subscriptionDataDto.getPrice());
                subscription.setPaymentStatus(true);
                subscription.setGstin(gstin);
                subscription.setProduct(product);
                subscriptionRepository.save(subscription);

                UserProductGstinMapping mapping = new UserProductGstinMapping();
                mapping.setUser(user);
                mapping.setProduct(product);
                mapping.setGstin(gstin);
                mappingRepository.save(mapping);
            }
        } catch (UserNotFoundException e) {
            throw e;
        }catch (ProductNotFoundException e) {
            throw e;
        }
    }
    public Gstin getOrCreateGstin(Integer gstinId, String panNo) {
        if (gstinId != null) {
            Gstin existingGstin = gstinRepository.findByGstinId(gstinId);
            if (existingGstin != null) {
                return existingGstin;
            }
        }

        Gstin newGstin = generateNewGstin(panNo);
        gstinRepository.save(newGstin);
        return newGstin;
    }

    private Gstin generateNewGstin(String panNo) {
        Gstin newGstin = new Gstin();
        newGstin.setGstinNo(generateRandomGstin(panNo));
        return newGstin;
    }

    public static String generateRandomGstin(String panNo) {
        Random random = new Random();
        StringBuilder gstinBuilder = new StringBuilder();

        int stateCode = random.nextInt(37) + 1;
        if (stateCode < 10) {
            gstinBuilder.append("0").append(stateCode);
        } else {
            gstinBuilder.append(stateCode);
        }

        gstinBuilder.append(panNo);

        int randomDigit = random.nextInt(10);
        gstinBuilder.append(randomDigit).append('Z');

        int randomDigit2 = random.nextInt(10);
        gstinBuilder.append(randomDigit2);

        return gstinBuilder.toString();
    }

    public static String generateRandomPan() {
        Random random = new Random();
        StringBuilder panBuilder = new StringBuilder();

        char[] uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        for (int i = 0; i < 5; i++) {
            char randomLetter = uppercaseLetters[random.nextInt(26)];
            panBuilder.append(randomLetter);
        }

        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10);
            panBuilder.append(randomNumber);
        }

        char lastLetter = uppercaseLetters[random.nextInt(26)];
        panBuilder.append(lastLetter);

        return panBuilder.toString();
    }
    /**
     * Updates subscription statuses if they expire
     */
    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void updateSubscriptionStatuses() {

            List<Subscription> subscriptions = subscriptionRepository.findAll();
            for (Subscription subscription : subscriptions) {
                if (isSubscriptionExpired(subscription)) {
                    subscription.setPaymentStatus(false);
                    subscriptionRepository.save(subscription);
                }
            }
            LOGGER.info("Subscription statuses updated successfully.");

    }
    private boolean isSubscriptionExpired(Subscription subscription) {
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate);
        return subscription.getEndDate().isBefore(currentDate);
    }
}
