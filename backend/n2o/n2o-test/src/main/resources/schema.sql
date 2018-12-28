CREATE TABLE IF NOT EXISTS pim_individual (id int primary key auto_increment, surname char(50), name char(50), patr_name char(50),
  birth_dt date, has_citizenship boolean, gender_id int, nationality_id int, vip boolean, ethnic_group_id int, social_group_id int);
-- CREATE TABLE IF NOT EXISTS IF NOT EXISTS pci_patient (id int primary key auto_increment, vip boolean, ethnic_group_id int, social_group_id int);
CREATE TABLE IF NOT EXISTS pim_gender (id int primary key auto_increment, name char(255), to_dt date);
CREATE TABLE IF NOT EXISTS md_ethnic_group (id int primary key auto_increment, name char(250));
CREATE TABLE IF NOT EXISTS md_soc_group (id int primary key auto_increment, name char(250));
CREATE TABLE IF NOT EXISTS pim_nationality (id int primary key auto_increment, name char(255));
CREATE TABLE IF NOT EXISTS pim_room (id int primary key auto_increment, name char(255), department_id int);
CREATE TABLE IF NOT EXISTS pim_department(id int primary key auto_increment, name char(255), org_id int);
CREATE TABLE IF NOT EXISTS pim_organization (id int primary key auto_increment, short_name char(250));
CREATE TABLE IF NOT EXISTS address_element (id int primary key auto_increment, name char(255), parent_id int, level_id int, type_id int, display_format_id int);
CREATE TABLE IF NOT EXISTS address_element_level (id int primary key auto_increment, code char(255));
CREATE TABLE IF NOT EXISTS address_element_type (id int primary key auto_increment, name char(255), short_name char (255));
CREATE TABLE IF NOT EXISTS pci_benefit_type (id int primary key auto_increment, name char(255));
CREATE TABLE IF NOT EXISTS pim_individual_doc (id int PRIMARY KEY auto_increment, indiv_id int, series char (20), number char(40), type_id int, is_active boolean,
name char(50), surname char(50), patr_name char(50));
CREATE TABLE IF NOT EXISTS pim_doc_type (id int PRIMARY KEY auto_increment, code char(255), name char(200));
CREATE TABLE IF NOT EXISTS pim_indiv_doc_detail (id int PRIMARY KEY auto_increment, doc_id int);
CREATE TABLE IF NOT EXISTS pim_indiv_contact (id int PRIMARY KEY  auto_increment, indiv_id int, type_id int, value char(255));
CREATE TABLE IF NOT EXISTS pim_party_contact_type (id int PRIMARY KEY auto_increment, name char(255));
CREATE TABLE IF NOT EXISTS check_paging (id int PRIMARY KEY auto_increment, VAL char(10));