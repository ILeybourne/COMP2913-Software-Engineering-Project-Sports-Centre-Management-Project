package uk.ac.leeds.comp2913.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.leeds.comp2913.api.Domain.Model.Activity;
import uk.ac.leeds.comp2913.api.Domain.Model.Resource;


@RestController
@RequestMapping("/timetable")
public class TimetableController {
    @GetMapping("")
    public List<Resource> get(@RequestParam(value = "name", defaultValue = "World") String name) {
        Resource r1 = new Resource();
        r1.setName("Squash Court");
        List<Activity> list1 = new ArrayList<>();
        Activity a1 = new Activity();
        a1.setId(1l);
        a1.setTotal_capacity(4);
        a1.setName("Squash Court 1");
        a1.setStart_time(new Date());
        a1.setEnd_time(new Date());
        list1.add(a1);
        Activity a2 = new Activity();
        a2.setId(1l);
        a2.setTotal_capacity(4);
        a2.setName("Squash Court 2");
        a2.setStart_time(new Date());
        a2.setEnd_time(new Date());
        list1.add(a2);
        Activity a3 = new Activity();
        a3.setId(1l);
        a3.setTotal_capacity(4);
        a3.setName("Squash Court 3");
        a3.setStart_time(new Date());
        a3.setEnd_time(new Date());
        list1.add(a3);
        r1.setActivities(list1);


        Resource r2 = new Resource();
        r2.setName("Tennis Court");
        List<Activity> list2 = new ArrayList<>();
        Activity a4 = new Activity();
        a4.setId(1l);
        a1.setTotal_capacity(4);
        a4.setName("Tennis Court 1");
        a4.setStart_time(new Date());
        a4.setEnd_time(new Date());
        list2.add(a4);
        Activity a5 = new Activity();
        a5.setId(1l);
        a5.setTotal_capacity(4);
        a5.setName("Tennis Court 2");
        a5.setStart_time(new Date());
        a5.setEnd_time(new Date());
        list2.add(a5);
        Activity a6 = new Activity();
        a6.setId(1l);
        a6.setTotal_capacity(4);
        a6.setName("Tennis Court 3");
        a6.setStart_time(new Date());
        a6.setEnd_time(new Date());
        list2.add(a6);
        r2.setActivities(list2);
        List<Resource> l3 = new ArrayList<>();
        l3.add(r1);
        l3.add(r2);

        a1.getResource();
        return l3;
    }
}
