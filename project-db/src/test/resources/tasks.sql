set foreign_key_checks = 0;

truncate table assignment;
truncate table task;
truncate table project;
truncate table member;

insert into member(name, login_id, password, role, active) values ('Ko Ko Oo', 'kokooo', 'kokooo', 'Admin', 1);
insert into member(name, login_id, password, role, active) values ('Aung Aung', 'aungaung', 'aungaung', 'Member', 1);
insert into member(name, login_id, password, role, active) values ('Aung Naing', 'aungnaing', 'aunaing', 'Member', 1);
insert into member(name, login_id, password, role, active) values ('Thiha', 'thiha', 'thiha', 'Member', 1);
insert into member(name, login_id, password, role, active) values ('San Lwin', 'sanlwin', 'sanlwin', 'Member', 1);
insert into member(name, login_id, password, role, active) values ('Moe Moe', 'moemoe', 'moemoe', 'Member', 1);


insert into project(name, description, manager, start, months) values ('Book Store', 'Shopping System', 2, '2022-05-10', 6);
insert into project(name, description, manager, start, months) values ('Project DB', 'Project Management System', 3, '2022-04-01', 12);
insert into project(name, description, manager, start, months) values ('Smart Kitchen', 'Restaurant Management System', 4, '2022-02-15', 9);
insert into project(name, description, manager, start, months) values ('Doctor Help', 'Clinick Management System', 2, '2022-05-10', 6);
insert into project(name, description, manager, start, months) values ('Order Me', 'Order Management System', 2, '2022-05-10', 18);
insert into project(name, description, manager, start, months) values ('The Movies', 'Movies Informations Provider', 3, '2022-05-10', 6);

insert into task(phase, name, start, days, project_id) values ('Analysis', 'Project DB', '2022-04-01', 35, 2);
insert into task(phase, name, start, days, project_id) values ('Design', 'Project DB', '2022-04-01', 60, 2);
insert into task(phase, name, start, days, project_id) values ('Coding', 'Project DB', '2022-04-01', 110, 2);
insert into task(phase, name, start, days, project_id) values ('Testing', 'Project DB', '2022-04-01', 70, 2);
insert into task(phase, name, start, days, project_id) values ('Release', 'Project DB', '2022-04-01', 90, 2);

insert into task(phase, name, start, days, project_id) values ('Analysis', 'The Movies', '2022-05-10', 30, 6);
insert into task(phase, name, start, days, project_id) values ('Design', 'The Movies', '2022-05-10', 30, 6);
insert into task(phase, name, start, days, project_id) values ('Coding', 'The Movies', '2022-05-10', 60, 6);

set foreign_key_checks = 1;
