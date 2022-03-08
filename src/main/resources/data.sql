insert into batches (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     batch_end_date, batch_start_date, batch_status, name, notes)
values (now(), 1, false, now(), 1, '2021-12-30', '2021-06-30', 'COMPLETED', 'EU-1', 'EU only'),
       (now(), 1, false, now(), 1, '2022-12-30', '2021-06-26', 'COMPLETED', 'EU-2', 'EU only'),
       (now(), 1, false, now(), 1, '2023-01-15', '2022-03-07', 'INPROGRESS', 'EU-3', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-28', 'PLANNED', 'EU-4', 'EU only'),
       (now(), 1, false, now(), 1, '2021-12-30', '2021-06-29', 'PLANNED', 'EU-5', 'EU only');

insert into user_roles (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name)
values (now(), 1, false, now(), 1, 'Admin'),
       (now(), 1, false, now(), 1, 'Instructor'),
       (now(), 1, false, now(), 1, 'Cydeo Mentor'),
       (now(), 1, false, now(), 1, 'Alumni Mentor'),
       (now(), 1, false, now(), 1, 'Student');

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, student_status, group_id, user_role_id, batch_id, user_name)
values (now(), 1, false, now(), 1, '1980-12-30', 'USA', 'mike@ct.com', true, 'Mike', 'MALE', 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5412589874', null, null, 1, null, 'mike@ct.com'),
       (now(), 1, false, now(), 1, '1975-01-12', 'TURKEY', 'omer@ct.com', true, 'Omer', 'MALE', 'Bright', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5325698754', null, null, 2, null, 'omer@ct.com'),
       (now(), 1, false, now(), 1, '1965-02-23', 'UK', 'ozzy@ct.com', true, 'Ozzy', 'MALE', 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5052569847', null, null, 2, null, 'ozzy@ct.com'),
       (now(), 1, false, now(), 1, '1986-05-25', 'FRANCE', 'jamal@ct.com', true, 'Jamal', 'MALE', 'Orange', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5336549855', null, null, 2, null, 'jamal@ct.com'),
       (now(), 1, false, now(), 1, '1981-04-20', 'GERMANY', 'akbar@ct.com', true, 'Akbar', 'MALE', 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5054523311', null, null, 2, null, 'akbar@ct.com'),
       (now(), 1, false, now(), 1, '1999-11-01', 'ITALY', 'baha@ct.com', true, 'Baha', 'MALE', 'Darkblue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5425213654', null, null, 3, null, 'baha@ct.com'),
       (now(), 1, false, now(), 1, '2000-10-05', 'NETHERLAND', 'cihat@ct.com', true, 'Cihat', 'MALE', 'Green', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423625411', null, null, 3, null, 'cihat@ct.com'),
       (now(), 1, false, now(), 1, '1996-08-30', 'USA', 'osman@ct.com', true, 'Osman', 'MALE', 'White', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5329885588', null, null, 4, null, 'osman@ct.com'),
       (now(), 1, false, now(), 1, '1994-07-21', 'UK', 'pelin@ct.com', true, 'Pelin', 'FEMALE', 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5302556633', null, null, 4, null, 'pelin@ct.com');

insert into groups  (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, name, batch_id, cydeo_mentor_id, alumni_mentor_id)
values (now(), 1, false, now(), 1, 'Group-1', 3, 6, 8),
       (now(), 1, false, now(), 1, 'Group-2', 3, 7, 9),
       (now(), 1, false, now(), 1, 'Group-3', 3, 6, 9),
       (now(), 1, false, now(), 1, 'Group-4', 3, 7, 8),
       (now(), 1, false, now(), 1, 'Group-5', 3, 6, 8),
       (now(), 1, false, now(), 1, 'Group-6', 3, 7, 8),
       (now(), 1, false, now(), 1, 'Group-7', 3, 7, 8),
       (now(), 1, false, now(), 1, 'Group-8', 3, 7, 8),
       (now(), 1, false, now(), 1, 'Group-9', 3, 7, 8),
       (now(), 1, false, now(), 1, 'Group-10', 3, 7, 8);

insert into users (insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, birthday,
                   country, email, enabled, first_name, gender, last_name, password, phone, student_status, group_id, user_role_id, batch_id, user_name)
values (now(), 1, false, now(), 1, '1983-09-16', 'FRANCE', 'messi@ct.com', true, 'Messi', 'MALE', 'Purple', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5312147789', 'NEW', 1, 5, 3, 'messi@ct.com'),
       (now(), 1, false, now(), 1, '1981-12-08', 'ITALY', 'ronaldo@ct.com', true, 'Ronaldo', 'MALE', 'Pink', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423665523', 'NEW', 1, 5, 3, 'ronaldo@ct.com'),
       (now(), 1, false, now(), 1, '1988-11-17', 'USA', 'iniesta@ct.com', true, 'Iniesta', 'MALE', 'Brown', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5054587895', 'RETURNING', 2, 5, 3, 'iniesta@ct.com'),
       (now(), 1, false, now(), 1, '2005-01-06', 'TURKEY', 'falcao@ct.com', true, 'Falcao', 'MALE', 'Gray', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5056669568', 'NEW', 2, 5, 3, 'falcao@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'FRANCE', 'sneider@ct.com', true, 'Sneider', 'MALE', 'Yellow', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'sneider@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'FRANCE', 'maradona@ct.com', true, 'Maradona', 'MALE', 'Blue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'maradona@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'FRANCE', 'ronaldinho@ct.com', true, 'Ronaldinho', 'MALE', 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'ronaldinho@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'FRANCE', 'metin@ct.com', true, 'Metin', 'MALE', 'Pink', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'metin@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'FRANCE', 'ali@ct.com', true, 'Ali', 'MALE', 'Brown', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'ali@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'feyyaz@ct.com', true, 'Feyyaz', 'MALE', 'Purple', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'feyyaz@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'schumaher@ct.com', true, 'Schumaher', 'MALE', 'Gray', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'schumaher@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'marcao@ct.com', true, 'Marcao', 'MALE', 'White', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'marcao@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'nelson@ct.com', true, 'Nelson', 'MALE', 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'NEW', 2, 5, 3, 'nelson@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'kerem@ct.com', true, 'Kerem', 'MALE', 'Red', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'RETURNING', 2, 5, 3, 'kerem@ct.com'),
       (now(), 1, false, now(), 1, '2000-03-13', 'UK', 'alex@ct.com', true, 'Alex', 'MALE', 'Blue', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5423124655', 'RETURNING', 2, 5, 3, 'alex@ct.com'),
       (now(), 1, false, now(), 1, '1980-05-10', 'GERMANY', 'tugay@ct.com', true, 'Tugay', 'MALE', 'Black', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', '5325123655', 'NEW', 2, 5, 3, 'tugay@ct.com');

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
values (now(), 1, false, now(), 1, '2022-03-07', '2022-03-07', 'Day-01 Intro to Java', 'RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-08', '2022-03-07', 'Day-02 Variables','RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-09', '2022-03-19', 'Quiz-01 Variables','QUIZ', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-10', '2022-03-10', 'Day-03 Data Types','RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-11', '2022-03-20', 'Assessment-01','QUIZ', 3, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-12', '2022-03-12', 'Day-01 SDLC','RECORDING', 3, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-13', '2022-03-13', 'Day-02 STLC','RECORDING', 3, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-14', '2022-03-14', 'Week-01 TAY','FLIPGRID', 6, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-15', '2022-03-15', 'Day-03 Intro to Testing','RECORDING', 3, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-16', '2022-03-16', 'Week02','WEEKLY_MENTOR_MEETING', 7, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-17', '2022-03-17', 'Day-04 Methods','RECORDING', 1, 3, 'PLANNED'),
       (now(), 1, false, now(), 1, '2022-03-17', '2022-03-21', 'Day-05 Class&Objects','RECORDING', 1, 3, 'PLANNED');

-- insert into student_task (task_id, student_id)
-- values (1, 1),(2, 1),(3, 1),(4, 1),(5, 1),(6, 1),(7, 1),(8, 1),(9, 1),(10, 1),(11, 1);
