-- ================================================================
-- 관리자계정 spring계정 생성
-- ================================================================
alter session set "_oracle_script" = true;  -- 일본사용자 c## 접두어 없이 계정생성

create user spring
identified by spring
default tablespace users;

alter user spring quota unlimited on users;

grant connect, resource to spring;


-- ================================================================
-- spring계정
-- ================================================================
-- demo 테이블
create table dev (
    no number,
    name varchar2(50) not null,
    career number not null,
    email varchar2(200) not null,
    gender char(1),
    lang varchar2(100) not null,     -- vo에서는 String[ ]  <---->  varchar2 'java, c, js'  변환작업 필요
    constraint pk_dev_no primary key(no),
    constraint ck_dev_gender check(gender in ('M', 'F'))
);
create sequence seq_dev_no;
commit;

select * from dev;



-- 회원테이블 생성
-- member_role 권한정보는 별도의 테이블에서 관리(spring-security)
create table member(
    id varchar2(15),
    password varchar2(300) not null,
    name varchar2(256) not null,
    gender char(1),
    birthday date,
    email varchar2(256),
    phone char(11) not null,
    address varchar2(512),
    hobby varchar2(256),
    enroll_date date default sysdate,
    enabled number default 1, -- 회원활성화여부  1:활성화됨 / 0:비활성화(spring-security)
    constraint pk_member_id primary key(id),
    constraint ck_member_gender check(gender in ('M', 'F')),
    constraint ck_member_enablec check(enabled in (1, 0))
);

select * from member;


update member set password = '$2a$10$Uu4o8Iw0rPUFJA1nj/BIV.pmkZYLTc3ecNXmk4fsH7i6tD8.2IRJ6' where id = 'admin';

--insert into spring.member values ('abcde','1234','아무개','M',to_date('88-01-25','rr-mm-dd'),'abcde@naver.com','01012345678','서울시 강남구','운동,등산,독서',default,default);
--insert into spring.member values ('qwerty','1234','김말년','F',to_date('78-02-25','rr-mm-dd'),'qwerty@naver.com','01098765432','서울시 관악구','운동,등산',default,default);
--insert into spring.member values ('admin','1234','관리자','F',to_date('90-12-25','rr-mm-dd'),'admin@naver.com','01012345678','서울시 강남구','독서',default,default);




-- ==================================================================
-- memo테이블 생성
-- ==================================================================
create table memo (
    no number,
    memo varchar2(2000),
    password char(4) not null,
    reg_date date default sysdate,
    constraint pk_memo_no primary key(no)
);

create sequence seq_memo_no;

insert into memo values(seq_memo_no.nextval, '안녕하세요~ 반갑습니다.', '1234', default);
insert into memo values(seq_memo_no.nextval, '파이널프로젝트 화이팅!', '1234', default);
insert into memo values(seq_memo_no.nextval, '여보세요~', '1234', default);

select * from memo;

commit;








































