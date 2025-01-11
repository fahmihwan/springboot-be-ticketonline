package ticket_online.ticket_online.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void setUp() {
//        mockUser = new User();
//        mockUser.setFull_name("Test User");
//        mockUser.setEmail("fahmiiwan86@gmail.com");
//        mockUser.setPassword("qweqwe123");
//        mockUser.setRole("user");
//        mockUser.setBirth_date(LocalDateTime.now());
//        mockUser.setPhone_number("082343432323");
    }

    @Test
    public void testCreateUser() {
//        when(userRepository.save(mockUser)).thenReturn(mockUser);
//
//        User createdUser = userService.createUser(mockUser);
//        assertNotNull(createdUser);
//        assertEquals("Test User", createdUser.getFull_name());
//        assertTrue(createdUser.getIa());
    }
}