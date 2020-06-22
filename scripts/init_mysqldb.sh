# character 설정 확인
show variables like 'c%';

# 수동으로 character 변경 필요함
alter database app_quotes
CHARACTER SET 'utf8mb4'
COLLATE = 'utf8mb4_general_ci';

# 변경 확인
show variables like 'c%';

# timezeon 확인
select @@time_zone, now();

# 한글 테스트
CREATE TABLE test (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    content varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

insert into test(content) values('테스트');
select * from test;