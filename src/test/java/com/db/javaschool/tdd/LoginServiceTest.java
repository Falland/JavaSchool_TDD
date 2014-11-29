package com.db.javaschool.tdd;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * User: Yury
 */
public class LoginServiceTest {

    LoginService service;
    IAccountRepository repo;
    IAccount acc;
    IAccount acc2;


    @Before
    public void init() {
        acc = mock(IAccount.class);
        doReturn(true).when(acc).passwordMatches("pass");
        doReturn("John").when(acc).getUserName();
        acc2 = mock(IAccount.class);
        doReturn(true).when(acc).passwordMatches("pass");
        doReturn("Johan").when(acc2).getUserName();
        repo = mock(IAccountRepository.class);
        doReturn(acc).when(repo).find("John");
        doReturn(acc2).when(repo).find("Johan");
        service = new LoginService(repo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLogin_shouldThrowException_whenNameIsBlank() {
         service.login("", "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLogin_shouldThrowException_whenNameIsNull() {
        service.login(null, "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLogin_shouldThrowException_whenPasswordIsBlank() {
        service.login("John", "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLogin_shouldThrowException_whenPasswordIsNull() {
        service.login("John", null);
    }

    @Test(expected=IllegalStateException.class)
    public void testLogin_shouldThrowException_whenAccountIsNotInRepo() {
        service.login("Ivan", "pass");
    }

    @Test
    public void testLogin_shouldSuccessfullyLogInUser_whenPasswordMatches() {
        service.login("John", "pass");

        verify(acc).passwordMatches("pass");
    }

    @Test
    public void testLogin_shouldSetLoggedIn_whenLoggedInSuccessfully() {
        service.login("John", "pass");

        verify(acc).passwordMatches("pass");
        verify(acc).setLoggedIn();
    }

    @Test
    public void testLogin_shouldNotSetLoggedIn_whenNotLoggedInSuccessfully() {
        service.login("John", "peas");

        verify(acc, never()).setLoggedIn();
    }

    @Test
     public void testLogin_shouldRevokeAccount_afterThreeWrongPasswordsProvided() {
        service.login("John", "peas");
        service.login("John", "peas");
        service.login("John", "peas");

        verify(acc).setRevoked();
    }

    @Test
     public void testLogin_shouldNotRevokeAccount_afterThreeWrongPasswordsProvidedForDifferentUsers() {
        service.login("John", "peas");
        service.login("Johan", "peas");
        service.login("John", "peas");

        verify(acc, never()).setRevoked();
        verify(acc2, never()).setRevoked();
    }

    @Test
    public void testLogin_shouldNotRevokeAccount_afterThreeNonConsecutiveFailedAttempts() {
        service.login("John", "peas");
        service.login("John", "pass");
        service.login("John", "peas");
        service.login("John", "peas");

        verify(acc, never()).setRevoked();
    }

    @Test(expected = IllegalStateException.class)
    public void testLogin_shouldThrowException_whenAccountIsRevoked() {
        doReturn(true).when(acc).isRevoked();
        service.login("John", "peas");
    }


}

