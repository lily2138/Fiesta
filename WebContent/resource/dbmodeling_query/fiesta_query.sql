-- 테이블 전체 삭제
DROP TABLE wish;
DROP TABLE review;
DROP TABLE orderdetail;
DROP TABLE service;
DROP TABLE answer;
DROP TABLE company;
DROP TABLE comcategory;
DROP TABLE custorder;
DROP TABLE question;
DROP TABLE customer;


-- 테이블 생성
-- comcategory
CREATE TABLE comcategory(
	comCategory_code INT NOT NULL PRIMARY KEY,
    comCategory_desc VARCHAR(45)
);

-- company
CREATE TABLE company(
	com_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    com_pass VARCHAR(45),
    com_id VARCHAR(45),
    com_name VARCHAR(45) NOT NULL,
    com_tel VARCHAR(45),
    com_addr VARCHAR(45),
    com_img VARCHAR(45),
    com_desc VARCHAR(45),
    comCategory_code INT NOT NULL
);
ALTER TABLE company ADD constraint fk_company foreign key(comCategory_code) references comcategory(comCategory_code) on delete cascade;

-- service
CREATE TABLE service(
	service_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(45),
    service_desc VARCHAR(45),
    service_img VARCHAR(45),
    service_tag VARCHAR(45),
    com_code INT
);
ALTER TABLE service ADD constraint fk_service foreign key(com_code) references company(com_code) on delete cascade;

-- customer
CREATE TABLE customer(
	cust_id VARCHAR(45) NOT NULL PRIMARY KEY,
    cust_name VARCHAR(45) NOT NULL,
    cust_pass VARCHAR(45) NOT NULL,
    cust_tel VARCHAR(45) NOT NULL,
    cust_email VARCHAR(45) NOT NULL,
    cust_group VARCHAR(45) NOT NULL
);

-- custorder
CREATE TABLE custorder(
	order_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_sysdate DATE NOT NULL,
    order_revdate VARCHAR(45) NOT NULL,
    order_place VARCHAR(45) NOT NULL,
    order_budget INT,
    order_require VARCHAR(45),
    order_condition VARCHAR(45) NOT NULL,
    cust_id VARCHAR(45) NOT NULL
);
ALTER TABLE custorder ADD constraint fk_custorder foreign key(cust_id) references customer(cust_id) on delete cascade;

-- orderdetail
CREATE TABLE orderdetail(
	detail_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    detail_totalprice INT NOT NULL,
    detail_desc VARCHAR(45),
    service_code INT NOT NULL,
    com_code INT NOT NULL,
    order_code INT NOT NULL
);
ALTER TABLE orderdetail ADD constraint fk_orderdetail_service_code foreign key(service_code) references service(service_code) on delete cascade;
ALTER TABLE orderdetail ADD constraint fk_orderdetail_com_code foreign key(com_code) references company(com_code) on delete cascade;
ALTER TABLE orderdetail ADD constraint fk_orderdetail_order_code foreign key(order_code) references custorder(order_code) on delete cascade;

-- review
CREATE TABLE review(
	review_code VARCHAR(45) NOT NULL PRIMARY KEY,
    review_score INT,
    review_img VARCHAR(45),
    review_desc VARCHAR(45) NOT NULL,
    service_code INT NOT NULL,
    com_code INT NOT NULL,
    cust_id VARCHAR(45) NOT NULL
);
ALTER TABLE review ADD constraint fk_review_service_code foreign key(service_code) references service(service_code) on delete cascade;
ALTER TABLE review ADD constraint fk_review_com_code foreign key(com_code) references company(com_code) on delete cascade;
ALTER TABLE review ADD constraint fk_review_cust_id foreign key(cust_id) references customer(cust_id) on delete cascade;

-- wish
CREATE TABLE wish(
	wish_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    service_code INT NOT NULL,
    com_code INT NOT NULL,
    cust_id VARCHAR(45) NOT NULL
);
ALTER TABLE wish ADD constraint fk_wish_service_code foreign key(service_code) references service(service_code) on delete cascade;
ALTER TABLE wish ADD constraint fk_wish_com_code foreign key(com_code) references company(com_code) on delete cascade;
ALTER TABLE wish ADD constraint fk_wish_cust_id foreign key(cust_id) references customer(cust_id) on delete cascade;

-- question
CREATE TABLE question(
	q_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    q_date DATE NOT NULL,
    q_desc VARCHAR(45),
    cust_id VARCHAR(45)
);
ALTER TABLE question ADD constraint fk_question foreign key(cust_id) references customer(cust_id) on delete cascade;

-- answer
CREATE TABLE answer(
	a_code INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    a_date DATE NOT NULL,
    a_desc VARCHAR(45) NOT NULL,
    com_code INT NOT NULL,
    q_code INT NOT NULL
);
ALTER TABLE answer ADD constraint fk_answer_com_code foreign key(com_code) references company(com_code) on delete cascade;
ALTER TABLE answer ADD constraint fk_answer_q_code foreign key(q_code) references question(q_code) on delete cascade;


-- company, review 부분 수정 어디인지 확인하기
-- INSERT 부분
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(01, '연예기획사');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(02, '숙소');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(03, '주류/렌탈');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(04, '버스');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(05, '음향조명');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(06, '보험회사');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(07, '푸드트럭');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(08, '의류');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(09, '현수막');
INSERT INTO comcategory(comCategory_code, comCategory_desc) VALUES(10, '협찬');

INSERT INTO company(com_pass, com_id, com_name, com_tel, com_addr, com_img, com_desc, comcategory_code) 
VALUES('2222','house1','숙소1','02-1111-5964','가평군','./resource/img/house1.jpg','최대 300명 수용 숙소',02);
INSERT INTO service(service_name, service_desc, service_img, service_tag, com_code)
VALUES('300명 숙소','다양한 강당, 편안한 시설','./resource/img/house1.jpg','#숙소', 1);
INSERT INTO customer(cust_id, cust_name, cust_pass, cust_tel, cust_email, cust_group)
VALUES('java','java','1234','010-5043-5765','encore@gmail.com','한양대 사회과학대학 학생회');