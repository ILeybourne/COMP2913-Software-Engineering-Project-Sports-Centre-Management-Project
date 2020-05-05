/* Minimum Facilities */
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22000, '2020-05-01 00:00:01', 'Swimming Pool', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22001, '2020-05-01 00:00:01', 'Fitness Room', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22002, '2020-05-01 00:00:01', 'Squash Court 1', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22003, '2020-05-01 00:00:01', 'Squash Court 2', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22004, '2020-05-01 00:00:01', 'Squash Court 3', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22005, '2020-05-01 00:00:01', 'Squash Court 4', '2020-05-01 00:00:01');
INSERT INTO sports_centre_management.resource(id, created_at, name, updated_at) values (22006, '2020-05-01 00:00:01', 'Sports Hall', '2020-05-01 00:00:01');
/* Minimum Activities that occur at the facilities */
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22000, 'General Use', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 32, 5, 22000);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22001, 'Lane Swimming', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 32, 5, 22000);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22002, 'Lessons', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 32, 9, 22000);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22003, 'Team Events', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 32, 20, 22000);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22004, 'General Use', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 25, 10.50, 22001);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22005, '1 Hour Session', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 7.50, 22002);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22006, 'Team Events', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 20, 22002);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22007, '1 Hour Session', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 7.50, 22003);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22008, 'Team Events', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 20, 22003);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22009, '1 Hour Session', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 7.50, 22004);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22010, 'Team Events', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 20, 22004);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22011, '1 Hour Session', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 7.50, 22005);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22012, 'Team Events', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 4, 20, 22005);
INSERT INTO sports_centre_management.activity_type(id, name, created_at, updated_at, total_capacity, cost, resource_id) values (22013, '1 Hour Session', '2020-05-01 00:00:01', '2020-05-01 00:00:01', 20, 11, 22006);
/* Membership Types */
INSERT INTO sports_centre_management.membership_type(id, name, created_at, updated_at, duration, cost) VALUES (22000, 'Annual', '2020-05-01 00:00:01', '2020-05-01 00:00:02', 365, 200);
INSERT INTO sports_centre_management.membership_type(id, name, created_at, updated_at, duration, cost) VALUES (22001, 'Monthly', '2020-05-01 00:00:01', '2020-05-01 00:00:02', 30, 20);


