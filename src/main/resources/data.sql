insert into batches (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     batch_start_date, batch_end_date, batch_status, name, notes)
values (now(), 1, false, now(), 1, '2020-01-01', '2020-06-30', 3, 'EU-1', 'EU only'),
       (now(), 1, false, now(), 1, '2021-01-01', '2021-12-31', 3, 'EU-2', 'EU only'),
       (now(), 1, false, now(), 1, '2022-01-01', '2022-06-30', 2, 'EU-3', 'EU only'),
       (now(), 1, false, now(), 1, '2022-06-06', '2022-12-31', 1, 'EU-4', 'EU only'),
       (now(), 1, false, now(), 1, '2022-06-30', '2022-12-12', 1, 'EU-5', 'EU only');

insert into user_roles (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name)
values (now(), 1, false, now(), 1, 'Admin'),
       (now(), 1, false, now(), 1, 'Instructor'),
       (now(), 1, false, now(), 1, 'Cydeo Mentor'),
       (now(), 1, false, now(), 1, 'Alumni Mentor'),
       (now(), 1, false, now(), 1, 'Student');

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, student_status, user_role_id, user_name, current_batch_id, current_group_id)
values (now(), 1, false, now(), 1, '1980-12-30', 1, 'mike@ct.com', true, 'Mike', 1, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5412589874', null, 1, 'mike@ct.com', null, null),
       (now(), 1, false, now(), 1, '1975-01-12', 5, 'omer@ct.com', true, 'Omer', 1, 'Bright', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5325698754', null, 2, 'omer@ct.com', null, null),
       (now(), 1, false, now(), 1, '1965-02-23', 2, 'ozzy@ct.com', true, 'Ozzy', 1, 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5052569847', null, 2, 'ozzy@ct.com', null, null),
       (now(), 1, false, now(), 1, '1986-05-25', 6, 'jamal@ct.com', true, 'Jamal', 1, 'Orange', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5336549855', null, 2, 'jamal@ct.com', null, null),
       (now(), 1, false, now(), 1, '1981-04-20', 3, 'akbar@ct.com', true, 'Akbar', 1, 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5054523311', null, 2, 'akbar@ct.com', null, null),
       (now(), 1, false, now(), 1, '1999-11-01', 8, 'baha@ct.com', true, 'Baha', 1, 'Darkblue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5425213654', null, 3, 'baha@ct.com', null, null),
       (now(), 1, false, now(), 1, '2000-10-05', 7, 'cihat@ct.com', true, 'Cihat', 1, 'Green', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423625411', null, 3, 'cihat@ct.com', null, null),
       (now(), 1, false, now(), 1, '1996-08-30', 1, 'osman@ct.com', true, 'Osman', 1, 'White', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5329885588', null, 4, 'osman@ct.com', null, null),
       (now(), 1, false, now(), 1, '1994-07-21', 2, 'pelin@ct.com', true, 'Pelin', 0, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5302556633', null, 4, 'pelin@ct.com', null, null);

insert into groups  (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name, cydeo_mentor_id, alumni_mentor_id)
values (now(), 1, false, now(), 1, 'Group-1', 6, 8),
       (now(), 1, false, now(), 1, 'Group-2', 7, 9),
       (now(), 1, false, now(), 1, 'Group-1', 6, 9),
       (now(), 1, false, now(), 1, 'Group-2', 7, 8),
       (now(), 1, false, now(), 1, 'Group-1', 6, 8),
       (now(), 1, false, now(), 1, 'Group-2', 7, 8),
       (now(), 1, false, now(), 1, 'Group-1', 7, 8),
       (now(), 1, false, now(), 1, 'Group-2', 7, 8),
       (now(), 1, false, now(), 1, 'Group-1', 7, 8),
       (now(), 1, false, now(), 1, 'Group-2', 7, 8),
       (now(), 1, false, now(), 1, 'Group-3', 7, 8),
       (now(), 1, false, now(), 1, 'Group-4', 7, 8);

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, student_status, user_role_id, user_name, current_batch_id, current_group_id)
values (now(), 1, false, now(), 1, '1983-09-16', 6, 'messi@ct.com', true, 'Messi', 1, 'Purple', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5312147789', 2, 5, 'messi@ct.com', 3, 5),
       (now(), 1, false, now(), 1, '1981-12-08', 8, 'ronaldo@ct.com', true, 'Ronaldo', 1, 'Pink', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423665523', 2, 5, 'ronaldo@ct.com', 3, 5),
       (now(), 1, false, now(), 1, '1988-11-17', 1, 'iniesta@ct.com', true, 'Iniesta', 1, 'Brown', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5054587895', 2, 5, 'iniesta@ct.com', 3, 6),
       (now(), 1, false, now(), 1, '2005-01-06', 5, 'falcao@ct.com', true, 'Falcao', 1, 'Gray', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5056669568', 2, 5, 'falcao@ct.com', 3, 6),
       (now(), 1, false, now(), 1, '2000-03-13', 6, 'sneider@ct.com', true, 'Sneider', 1, 'Yellow', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'sneider@ct.com', 3, 6),
       (now(), 1, false, now(), 1, '2000-03-13', 6, 'maradona@ct.com', true, 'Maradona', 1, 'Blue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'maradona@ct.com', 3, 6),
       (now(), 1, false, now(), 1, '2000-03-13', 6, 'ronaldinho@ct.com', true, 'Ronaldinho', 1, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'ronaldinho@ct.com', 3, 6),
       (now(), 1, false, now(), 1, '2000-03-13', 6, 'metin@ct.com', true, 'Metin', 1, 'Pink', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 3, 5, 'metin@ct.com', null, null),
       (now(), 1, false, now(), 1, '2000-03-13', 6, 'ali@ct.com', true, 'Ali', 1, 'Brown', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'ali@ct.com', 4, 7),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'feyyaz@ct.com', true, 'Feyyaz', 1, 'Purple', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'feyyaz@ct.com', 4, 7),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'schumaher@ct.com', true, 'Schumaher', 1, 'Gray', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'schumaher@ct.com', 4, 8),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'marcao@ct.com', true, 'Marcao', 1, 'White', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'marcao@ct.com', 4, null),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'nelson@ct.com', true, 'Nelson', 1, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 2, 5, 'nelson@ct.com', 4, null),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'kerem@ct.com', true, 'Kerem', 1, 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 1, 5, 'kerem@ct.com', 5, 9),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'alex@ct.com', true, 'Alex', 1, 'Blue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 1, 5, 'alex@ct.com', 5, 9),
       (now(), 1, false, now(), 1, '1980-05-10', 3, 'tugay@ct.com', true, 'Tugay', 1, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5325123655', 2, 5, 'tugay@ct.com', 5, 9),
       (now(), 1, false, now(), 1, '2000-03-13', 2, 'pedri@ct.com', true, 'Pedri', 1, 'Blue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 1, 5, 'pedri@ct.com', 5, 9),
       (now(), 1, false, now(), 1, '1980-05-10', 3, 'mbappe@ct.com', true, 'Mbappe', 1, 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5325123655', 1, 5, 'mbappe@ct.com', 5, 9);

insert into batch_group_student (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, batch_id, group_id, student_id)
values (now(), 1, false, now(), 1, 1, 1, 24),
       (now(), 1, false, now(), 1, 1, 1, 25),
       (now(), 1, false, now(), 1, 1, 2, 26),
       (now(), 1, false, now(), 1, 1, 2, 10),

       (now(), 1, false, now(), 1, 2, 3, 17),
       (now(), 1, false, now(), 1, 2, 3, 18),
       (now(), 1, false, now(), 1, 2, 3, 19),
       (now(), 1, false, now(), 1, 2, 4, 20),
       (now(), 1, false, now(), 1, 2, 4, 11),

       (now(), 1, false, now(), 1, 3, 5, 10),
       (now(), 1, false, now(), 1, 3, 5, 11),
       (now(), 1, false, now(), 1, 3, 6, 12),
       (now(), 1, false, now(), 1, 3, 6, 13),
       (now(), 1, false, now(), 1, 3, 6, 14),
       (now(), 1, false, now(), 1, 3, 6, 15),
       (now(), 1, false, now(), 1, 3, 6, 16),

       (now(), 1, false, now(), 1, 4, 7, 18),
       (now(), 1, false, now(), 1, 4, 7, 19),
       (now(), 1, false, now(), 1, 4, 8, 20),
       (now(), 1, false, now(), 1, 4, null, 21),
       (now(), 1, false, now(), 1, 4, null, 22),

       (now(), 1, false, now(), 1, 5, 9, 23),
       (now(), 1, false, now(), 1, 5, 9, 24),
       (now(), 1, false, now(), 1, 5, 9, 25),
       (now(), 1, false, now(), 1, 5, 9, 26),
       (now(), 1, false, now(), 1, 5, 9, 27),
       (now(), 1, false, now(), 1, 5, 10, null),
       (now(), 1, false, now(), 1, 5, 11, null),
       (now(), 1, false, now(), 1, 5, 12, null);

insert into lessons (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name)
values (now(), 1, false, now(), 1,'Java'),
       (now(), 1, false, now(), 1,'Selenium'),
       (now(), 1, false, now(), 1,'SDLC'),
       (now(), 1, false, now(), 1,'API'),
       (now(), 1, false, now(), 1,'Git/GitHub'),
       (now(), 1, false, now(), 1,'Flipgrid'),
       (now(), 1, false, now(), 1,'Mentor Session');

insert into instructor_lesson (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, lesson_id, instructor_id)
values (now(), 1, false, now(), 1, 1, 3),
       (now(), 1, false, now(), 1, 2, 4),
       (now(), 1, false, now(), 1, 3, 3),
       (now(), 1, false, now(), 1, 4, 3),
       (now(), 1, false, now(), 1, 4, 4),
       (now(), 1, false, now(), 1, 1, 2),
       (now(), 1, false, now(), 1, 5, 2),
       (now(), 1, false, now(), 1, 5, 3);

insert into tasks (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                   publishing_date, due_date, name, task_type, lesson_id, batch_id, task_status)
values (now(), 1, false, now(), 1, '2022-03-07', '2022-03-07', 'Day-01 Intro to Java', 1, 1, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-08', '2022-03-07', 'Day-02 Variables', 1, 1, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-09', '2022-03-19', 'Quiz-01 Variables', 2, 1, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-10', '2022-03-10', 'Day-03 Data Types', 1, 1, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-11', '2022-03-20', 'Assessment-01', 2, 3, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-12', '2022-03-12', 'Day-01 SDLC', 1, 3, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-13', '2022-03-13', 'Day-02 STLC', 1, 3, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-14', '2022-03-14', 'Week-01 TAY', 3, 6, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-15', '2022-03-15', 'Day-03 Intro to Testing', 1, 3, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-16', '2022-03-16', 'Week02', 6, 7, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-17', '2022-03-17', 'Day-04 Methods', 1, 1, 3, 1),
       (now(), 1, false, now(), 1, '2022-03-17', '2022-03-21', 'Day-05 Class&Objects', 1, 1, 4, 1);

-- insert into student_task (task_id, student_id)
-- values (1, 1),(2, 1),(3, 1),(4, 1),(5, 1),(6, 1),(7, 1),(8, 1),(9, 1),(10, 1),(11, 1);
