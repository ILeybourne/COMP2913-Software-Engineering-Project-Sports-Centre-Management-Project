package uk.ac.leeds.comp2913.api.Domain.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTypeTest {
  ActivityType activityType;
  Random random;
  BigDecimal testCost;
  Resource resource;


  @BeforeEach
  void setUp() {
    activityType = new ActivityType();
    random = new Random();
    testCost = new BigDecimal(40);
    resource = new Resource();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getId() {
    long testIdSet = random.nextLong();
    activityType.setId(testIdSet);
    long testIdGet = activityType.getId();
    assertEquals(testIdSet, testIdGet);
  }


  @Test
  void setId() {
    long testIdSet = random.nextLong();
    activityType.setId(testIdSet);
    long testIdGet = activityType.getId();
    assertEquals(testIdSet, testIdGet);
  }

  @Test
  void getName() {
    activityType.setName("Swimming Pool");
    String testGetName = activityType.getName();
    assertEquals("Swimming Pool", testGetName);
  }

  @Test
  void setName() {
    activityType.setName("Swimming Pool");
    String testGetName = activityType.getName();
    assertEquals("Swimming Pool", testGetName);
  }

  @Test
  void getCost() {
    activityType.setCost(testCost);
    BigDecimal testGetCost = activityType.getCost();
    assertEquals(testCost, testGetCost);
  }

  @Test
  void setCost() {
    activityType.setCost(testCost);
    BigDecimal testGetCost = activityType.getCost();
    assertEquals(testCost, testGetCost);
  }

  @Test
  void getTotalCapacity() {
    activityType.setTotalCapacity(20);
    int testGetTotalCapacity = activityType.getTotalCapacity();
    assertEquals(20, testGetTotalCapacity);
  }

  @Test
  void setTotalCapacity() {
    activityType.setTotalCapacity(20);
    int testGetTotalCapacity = activityType.getTotalCapacity();
    assertEquals(20, testGetTotalCapacity);
  }

  @Test
  void getResource() {
    activityType.setResource(resource);
    Resource testGetResource = activityType.getResource();
    assertEquals(resource, testGetResource);
  }

  @Test
  void setResource() {
    activityType.setResource(resource);
    Resource testGetResource = activityType.getResource();
    assertEquals(resource, testGetResource);
  }

  @Test
  void getCreatedAt() {
    Date testDateSet = new Date(random.nextLong());
    activityType.setCreatedAt(testDateSet);
    Date testDateGet = activityType.getCreatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void setCreatedAt() {
    Date testDateSet = new Date(random.nextLong());
    activityType.setCreatedAt(testDateSet);
    Date testDateGet = activityType.getCreatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void getUpdatedAt() {
    Date testDateSet = new Date(random.nextLong());
    activityType.setUpdatedAt(testDateSet);
    Date testDateGet = activityType.getUpdatedAt();
    assertEquals(testDateSet, testDateGet);
  }

  @Test
  void setUpdatedAt() {
    Date testDateSet = new Date(random.nextLong());
    activityType.setUpdatedAt(testDateSet);
    Date testDateGet = activityType.getUpdatedAt();
    assertEquals(testDateSet, testDateGet);
  }
}
