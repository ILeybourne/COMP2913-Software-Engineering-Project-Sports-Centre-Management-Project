package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTypeTest {
  MembershipType membershipType;
  Random random;
  BigDecimal testCost;

  @BeforeEach
  void setUp() {
    membershipType = new MembershipType();
    random = new Random();
    testCost = new BigDecimal(40);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getId() {
    long testIdSet = random.nextLong();
    membershipType.setId(testIdSet);
    long testIdGet = membershipType.getId();
    assertEquals(testIdSet, testIdGet);
  }


  @Test
  void setId() {
    long testIdSet = random.nextLong();
    membershipType.setId(testIdSet);
    long testIdGet = membershipType.getId();
    assertEquals(testIdSet, testIdGet);
  }

  @Test
  void getCreatedAt() {
    Date testDateSet = new Date(random.nextLong());
    membershipType.setCreatedAt(testDateSet);
    Date testDateGet = membershipType.getCreatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void setCreatedAt() {
    Date testDateSet = new Date(random.nextLong());
    membershipType.setCreatedAt(testDateSet);
    Date testDateGet = membershipType.getCreatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void getUpdatedAt() {
    Date testDateSet = new Date(random.nextLong());
    membershipType.setUpdatedAt(testDateSet);
    Date testDateGet = membershipType.getUpdatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void setUpdatedAt() {
    Date testDateSet = new Date(random.nextLong());
    membershipType.setUpdatedAt(testDateSet);
    Date testDateGet = membershipType.getUpdatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void getName() {
    membershipType.setName("Annual");
    String testGetName = membershipType.getName();
    assertEquals("Annual", testGetName);
  }

  @Test
  void setName() {
    membershipType.setName("Annual");
    String testGetName = membershipType.getName();
    assertEquals("Annual", testGetName);
  }

  @Test
  void getDuration() {
    membershipType.setDuration(20);
    int testGetDuration = membershipType.getDuration();
    assertEquals(20, testGetDuration);
  }

  @Test
  void setDuration() {
    membershipType.setDuration(20);
    int testGetDuration = membershipType.getDuration();
    assertEquals(20, testGetDuration);
  }

  @Test
  void getCost() {
    membershipType.setCost(testCost);
    BigDecimal testGetCost = membershipType.getCost();
    assertEquals(testCost, testGetCost);
  }

  @Test
  void setCost() {
    membershipType.setCost(testCost);
    BigDecimal testGetCost = membershipType.getCost();
    assertEquals(testCost, testGetCost);
  }
}