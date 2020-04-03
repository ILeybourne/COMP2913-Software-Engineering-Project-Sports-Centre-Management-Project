package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CentreTest {

    Centre centre;
    Random random;

    @BeforeEach
    void setUp() {
        centre = new Centre();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        centre.setCreatedAt(testDateSet);
        Date testDateGet = centre.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        centre.setCreatedAt(testDateSet);
        Date testDateGet = centre.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        centre.setUpdatedAt(testDateSet);
        Date testDateGet = centre.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        centre.setUpdatedAt(testDateSet);
        Date testDateGet = centre.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getName() {
        String testNameSet = Integer.toString(random.nextInt());
        centre.setName(testNameSet);
        String testNameGet = centre.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void setName() {
        String testNameSet = Integer.toString(random.nextInt());
        centre.setName(testNameSet);
        String testNameGet = centre.getName();
        assertEquals(testNameSet, testNameGet);
    }

    @Test
    void getAccounts() {
        List<Account> testAccountsSet = new ArrayList<Account>();
        Account testAccount = new Account();
        testAccountsSet.add(testAccount);
        centre.setAccounts(testAccountsSet);
        List<Account> testAccountsGet = centre.getAccounts();
        assertEquals(testAccountsSet, testAccountsSet);
    }

    @Test
    void setAccounts() {
        List<Account> testAccountsSet = new ArrayList<Account>();
        Account testAccount = new Account();
        testAccountsSet.add(testAccount);
        centre.setAccounts(testAccountsSet);
        List<Account> testAccountsGet = centre.getAccounts();
        assertEquals(testAccountsSet, testAccountsSet);
    }

    @Test
    void getResources() {
        List<Resource> testResourceSet = new ArrayList<Resource>();
        Resource testResource = new Resource();
        testResourceSet.add(testResource);
        centre.setResources(testResourceSet);
        List<Resource> testResourceGet = centre.getResources();
        assertEquals(testResourceGet, testResourceSet);
    }

    @Test
    void setResources() {
        List<Resource> testResourceSet = new ArrayList<Resource>();
        Resource testResource = new Resource();
        testResourceSet.add(testResource);
        centre.setResources(testResourceSet);
        List<Resource> testResourceGet = centre.getResources();
        assertEquals(testResourceGet, testResourceSet);
    }
}