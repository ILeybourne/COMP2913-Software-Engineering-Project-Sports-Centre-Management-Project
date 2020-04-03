package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {

    Membership membership;
    MembershipType membershipType;
    Account account;
    Random random;

    @BeforeEach
    void setUp() {
        membership = new Membership();
        membershipType = new MembershipType();
        account = new Account();
        random = new Random();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setCreatedAt(testDateSet);
        Date testDateGet = membership.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setCreatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setCreatedAt(testDateSet);
        Date testDateGet = membership.getCreatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setUpdatedAt(testDateSet);
        Date testDateGet = membership.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void setUpdatedAt() {
        Date testDateSet = new Date(random.nextLong());
        membership.setUpdatedAt(testDateSet);
        Date testDateGet = membership.getUpdatedAt();
        assertEquals(testDateSet, testDateGet);
    }

    @Test
    void getAccount() {
        membership.setAccount(account);
        Account testGetAccount = membership.getAccount();
        assertEquals(account, testGetAccount);
    }

    @Test
    void setAccount() {
        membership.setAccount(account);
        Account testGetAccount = membership.getAccount();
        assertEquals(account, testGetAccount);
    }

    @Test
    void getMembershipType() {
        membership.setMembershipType(membershipType);
        MembershipType testGetMembershipType = membership.getMembershipType();
        assertEquals(membershipType, testGetMembershipType);
    }

    @Test
    void setMembershipType() {
        membership.setMembershipType(membershipType);
        MembershipType testGetMembershipType = membership.getMembershipType();
        assertEquals(membershipType, testGetMembershipType);
    }

    @Test
    void getStartDate() {
        Date testDateSet = new Date(random.nextLong());
        membership.setStartDate(testDateSet);
        Date testGetStartDate = membership.getStartDate();
        assertEquals(testDateSet, testGetStartDate);
    }
    @Test
    void setStartDate() {
        Date testDateSet = new Date(random.nextLong());
        membership.setStartDate(testDateSet);
        Date testGetStartDate = membership.getStartDate();
        assertEquals(testDateSet, testGetStartDate);
    }

    @Test
    void setEndDate() {
        Date testDateSet = new Date(random.nextLong());
        membership.setEndDate(testDateSet);
        Date testGetEndDate = membership.getEndDate();
        assertEquals(testDateSet, testGetEndDate);
    }
    @Test
    void endEndDate() {
        Date testDateSet = new Date(random.nextLong());
        membership.setEndDate(testDateSet);
        Date testGetEndDate = membership.getEndDate();
        assertEquals(testDateSet, testGetEndDate);
    }


    @Test
    @Disabled
    void makePayment() {
    }

    @Test
    @Disabled
    void emailReceipt() {
    }

    @Test
    @Disabled
    void printReceipt() {
    }

    @Test
    @Disabled
    void storeReceipt() {
    }
}