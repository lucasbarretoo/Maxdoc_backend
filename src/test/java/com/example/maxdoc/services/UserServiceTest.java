package com.example.maxdoc.services;

import com.example.maxdoc.entities.Document;
import com.example.maxdoc.entities.User;
import com.example.maxdoc.respositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Nested
    class saveUser {

        @Test
        @DisplayName("Deve salvar usuário com sucesso")
        void shouldSaveAUser() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            User newUser = userService.save(user);

            assertNotNull(newUser);

            User userSaved = userArgumentCaptor.getValue();

            assertEquals(user.getId(), userSaved.getId());
            assertEquals(user.getName(), userSaved.getName());
            assertEquals(user.getPassword(), userSaved.getPassword());
            assertEquals(user.getEmail(), userSaved.getEmail());

        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            User user = new User(
                random.nextInt() * 10,
                "user name",
                "12345678",
                "email@teste.com.br"
            );

            doThrow(new RuntimeException()).when(userRepository).save(any());

            assertThrows(RuntimeException.class, () -> userService.save(user));
        }
    }

    @Nested
    class updateUser {

        @Test
        @DisplayName("Deve atualizar usuário com sucesso")
        void shouldUpdateAUser() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );


            doReturn(Optional.of(user)).when(userRepository).findById(integerArgumentCaptor.capture());

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            User userUpdated = userService.update(user.getId(), user);

            assertNotNull(userUpdated);

            assertEquals(user.getId(), userUpdated.getId());

        }

        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );

            doReturn(Optional.of(user)).when(userRepository).findById(integerArgumentCaptor.capture());

            doThrow(new RuntimeException()).when(userRepository).save(userArgumentCaptor.capture());

            assertThrows(RuntimeException.class, () -> userService.update(user.getId(), user));

        }
    }

    @Nested
    class deleteUser {

        @Test
        @DisplayName("Deve remover documento quando existir")
        void shouldDeleteADocumentWhenExist() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );


            doReturn(true).when(userRepository).existsById(integerArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(integerArgumentCaptor.capture());

            userService.delete(user.getId());
            List<Integer> idList = integerArgumentCaptor.getAllValues();

            assertEquals(user.getId(), idList.get(0));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Não deve remover usuario quando não existir")
        void shouldNotDeleteAUserWhenNotExist() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );
            doReturn(false).when(userRepository).existsById(integerArgumentCaptor.capture());

            assertThrows(ResponseStatusException.class, () -> userService.delete(user.getId()));

            assertEquals(user.getId(), integerArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(integerArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());

        }
    }

    @Nested
    class findUserById{

        @Test
        @DisplayName("Deve retornar usuário pelo id")
        void shouldReturnUserById() {

            Random random = new Random();

            User user = new User(
                random.nextInt() * 10,
                "user name",
                "12345678",
                "email@teste.com.br"
            );

            doReturn(Optional.of(user)).when(userRepository).findById(integerArgumentCaptor.capture());

            User u = userService.findById(user.getId());
            assertNotNull(u);
            assertEquals(user.getId(), u.getId());
            assertEquals(user.getName(), u.getName());
            assertEquals(user.getPassword(), u.getPassword());
            assertEquals(user.getEmail(), u.getEmail());
        }


        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );

            doThrow(new RuntimeException()).when(userRepository).findById(any());

            assertThrows(RuntimeException.class, () -> userService.findById(user.getId()));
        }

    }
    @Nested
    class findUserByEmail {

        @Test
        @DisplayName("Deve retornar usuário pelo email")
        void shouldReturnUserByEmail() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );

            doReturn(Optional.of(user)).when(userRepository).findByEmail(stringArgumentCaptor.capture());

            User u = userService.findByEmail(user.getEmail());
            assertNotNull(u);
            assertEquals(user.getId(), u.getId());
            assertEquals(user.getName(), u.getName());
            assertEquals(user.getPassword(), u.getPassword());
            assertEquals(user.getEmail(), u.getEmail());
        }


        @Test
        @DisplayName("Deve lançar exceção quando ocorrer erro")
        void shouldThrowExceptionWhenErroOccours() {

            Random random = new Random();

            User user = new User(
                    random.nextInt() * 10,
                    "user name",
                    "12345678",
                    "email@teste.com.br"
            );

            doThrow(new RuntimeException()).when(userRepository).findByEmail(any());

            assertThrows(RuntimeException.class, () -> userService.findByEmail(user.getEmail()));
        }


        @Nested
        class findAllUsers {

            @Test
            @DisplayName("Deve retornar todos os usuários")
            void shouldReturnAllUsers() {
                Random random = new Random();

                User user = new User(
                        random.nextInt() * 10,
                        "user name",
                        "12345678",
                        "email@teste.com.br"
                );

                List<User> users = List.of(user);

                doReturn(users).when(userRepository).findAll();

                List<User> userList = userService.findAll();

                assertNotNull(userList);
                assertEquals(users.size(), userList.size());

            }


            @Test
            @DisplayName("Deve lançar exceção quando ocorrer erro")
            void shouldThrowExceptionWhenErroOccours() {

                doThrow(new RuntimeException()).when(userRepository).findAll();

                assertThrows(RuntimeException.class, () -> userService.findAll());
            }
        }
    }


}