

-- 회원 테이블 생성
CREATE TABLE `member` (
  `userId` varchar(8) NOT NULL,
  `userPwd` varchar(200) NOT NULL,
  `userName` varchar(12) DEFAULT NULL,
  `mobile` varchar(13) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `registerDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `userImg` varchar(50) DEFAULT 'memberImg/avatar100.png',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원 테이블';

-- 계층형 게시판 테이블 생성
CREATE TABLE `hboard` (
  `boardNo` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `content` VARCHAR(4000) NULL,
  `writer` VARCHAR(8) NULL,
  `postDate` DATETIME NULL DEFAULT now(),
  `readCount` INT NULL DEFAULT 0,
  `ref` INT NULL DEFAULT 0,
  `step` INT NULL DEFAULT 0,
  `refOrder` INT NULL DEFAULT 0,
  PRIMARY KEY (`boardNo`));

-- 회원테이블과 계층형 게시판 테이블 관계 설정
ALTER TABLE `hboard` 
ADD INDEX `hboard_member_fk_idx` (`writer` ASC) VISIBLE;
;
ALTER TABLE `hboard` 
ADD CONSTRAINT `hboard_member_fk`
  FOREIGN KEY (`writer`)
  REFERENCES `webshjin`.`member` (`userId`)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;
  
-- 게시물 작성
INSERT INTO `hboard` (`boardNo`, `title`, `content`, `writer`, `postDate`, `readCount`, `ref`, `step`, `refOrder`) 
VALUES ('1', '게시판이 생성되었습니다', '많은 이용 바랍니다', 'admin', '2025-02-06 10:44:00', '0', '0', '0', '0');
  
-- 전체 게시글 조회 기능
select * from hboard order by boardNo desc;


-- 게시글 작성 쿼리문
insert into hboard(title, content, writer) 
values(?, ?, ?);

-- 포인트정보 테이블 생성
CREATE TABLE `pointinfo` (
  `pointcontent` VARCHAR(20) NOT NULL,
  `pointscore` INT NOT NULL,
  PRIMARY KEY (`pointcontent`));
  
-- 포인트 지급 내역 테이블 생성
CREATE TABLE `pointlog` (
  `pointLogNo` INT NOT NULL AUTO_INCREMENT,
  `pointwhen` DATETIME NOT NULL DEFAULT now(),
  `pointwho` VARCHAR(8) NOT NULL,
  `pointwhy` VARCHAR(20) NOT NULL,
  `pointscore` INT NOT NULL,
  PRIMARY KEY (`pointLogNo`))
COMMENT = '포인트 지급 내역';

-- 포인트 지급 내역 테이블 fk 설정
ALTER TABLE `pointlog` 
ADD CONSTRAINT `pointlog_pointwho_fk`
  FOREIGN KEY (`pointwho`)
  REFERENCES `member` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `pointlog_pointwhy_fk`
  FOREIGN KEY (`pointwhy`)
  REFERENCES `pointinfo` (`pointcontent`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
  
-- 회원 테이블에 포인트 컬럼 추가
ALTER TABLE `member` 
ADD COLUMN `userpoint` INT NULL DEFAULT 0 AFTER `userImg`;


-- pointlog테이블에 포인트 지급 내역 insert 쿼리문
insert into pointlog(pointwho, pointwhy, pointscore)
values(?, ?, (select pointscore from pointinfo where pointcontent=?));

-- member의 userpoint update 쿼리문
update member
set userpoint = userpoint + (select pointscore from pointinfo where pointcontent=?)
where userId = ?;

-- 게시판 파일 업로드를 위한boardupfiles 테이블 생성
CREATE TABLE `boardupfiles` (
  `boardUpFileNo` INT NOT NULL AUTO_INCREMENT,
  `originalFileName` VARCHAR(50) NOT NULL,
  `newFileName` VARCHAR(100) NOT NULL,
  `ext` VARCHAR(4) NULL,
  `size` INT NULL,
  `base64Image` TEXT NULL,
  `boardNo` INT NOT NULL,
  PRIMARY KEY (`boardUpFileNo`))
COMMENT = '게시글에 업로드되는 파일';

-- 파일업로드 테이블과 게시판 테이블 fk 관계 설정
ALTER TABLE `boardupfiles` 
ADD INDEX `boardupFiles_boardNo_fk_idx` (`boardNo` ASC) VISIBLE;
;
ALTER TABLE `boardupfiles` 
ADD CONSTRAINT `boardupFiles_boardNo_fk`
  FOREIGN KEY (`boardNo`)
  REFERENCES `webshjin`.`hboard` (`boardNo`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

-- 파일 업로드 테이블 썸네일파일이름 컬럼 추가
ALTER TABLE `boardupfiles` 
ADD COLUMN `thumbFileName` VARCHAR(100) NULL AFTER `newFileName`,
CHANGE COLUMN `ext` `ext` VARCHAR(20) NULL DEFAULT NULL ;

-- 파일 업로드 테이블 구조 변경
ALTER TABLE `boardupfiles` 
ADD COLUMN `fileType` VARCHAR(20) NULL AFTER `thumbFileName`,
CHANGE COLUMN `ext` `ext` VARCHAR(5) NULL DEFAULT NULL ;

-- boardupfiles 테이블에 업로드된 파일을 저장하는 쿼리문
insert into boardupfiles(originalFileName, newFileName, thumbFileName, fileType, ext, size, base64Image, boardNo)
values(?, ?, ?, ?,?,?, ?, ?);

-- 게시글 상세 조회 하는 쿼리문 1 : 2번의 select
select * from hboard where boardNo = 15;
select * from boardupfiles where boardNo = 15;
select * from member where userid = (select writer from hboard where boardNo = 15);

-- 게시글 상세 조회 하는 쿼리문 2 : 조인 이용(여기에서는 첨부파일이 없는 게시글도 나와야 하기 때문에 outer join을 사용)
select h.*, f.*, m.userId, m.userName, m.email, m.userImg 
from hboard h left outer join boardupfiles f
on h.boardNo = f.boardNo
inner join member m 
on h.writer = m.userId
where h.boardNo = 15;  

-- boardupfiles테이블의 fileType 컬럼 길이 수정
ALTER TABLE `boardupfiles` 
CHANGE COLUMN `fileType` `fileType` VARCHAR(50) NULL DEFAULT NULL ;

-- 조회수 증가 처리를 위한 게시글 조회 기록 테이블 생성
CREATE TABLE `boardreadlog` (
  `boardReadNo` INT NOT NULL AUTO_INCREMENT,
  `readWho` VARCHAR(50) NOT NULL,
  `readWhen` DATETIME NOT NULL DEFAULT now(),
  `readBoardNo` INT NOT NULL,
  PRIMARY KEY (`boardReadNo`),
  INDEX `readlog_boardNo_fk_idx` (`readBoardNo` ASC) VISIBLE,
  CONSTRAINT `readlog_boardNo_fk`
    FOREIGN KEY (`readBoardNo`)
    REFERENCES `webshjin`.`hboard` (`boardNo`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

use webshjin;
------------------------------------------------------------------------------------------------------
-- "?" ip주소를 가진 유저가 ?번글을 1일 이내에 조회한 기록이 있는지 없는지 체크 하는 쿼리문
select datediff(now(), (select readWhen
from boardreadlog
where readWho = '127.0.0.1' and readBoardNo = 15));

-- "?" ip주소를 가진 유저가 ?번글을 24시간 이내에 조회한 기록이 있는지 없는지 체크 하는 쿼리문
-- 이 값이 24보다 크거나 같으면 조회수 증가 -> insert(24시간 이내에 조회한 기록이 없다?)
-- 이 값이 -1이면 조회한 기록이 없는 것이므로 조회수 증가 -> insert
-- 같은 유저가 같은 글을 조회한 기록이 1개 이상일 수도 있기 때문에 다중행 서브쿼리가 실행되지 않도록 하기 위해
--  가장 최근의 조회기록만 가져오도록 max(readWhen)로 수정
select ifnull(timestampdiff(hour,  (select max(readWhen)
from boardreadlog
where readWho = '0:0:0:0:0:0:0:1' and readBoardNo = 20), now()), -1) as timediff; 

-- 조회수 증가 쿼리문
update hboard
set readCount = readCount + 1
where boardNo = ?;

-- 조회기록 insert
insert into boardreadlog(readWho, readBoardNo)
values(?, ?);

------------------------------------------------------------------------------------------------------
-- 답글 기능 구현 + 계층형 게시판 구현
-- 0) 게시판의 정렬기준을 아래의 정렬기준으로 바꾼다 (ref : 부모글의 글번호
select * from hboard order by ref desc, refOrder asc;


-- 1) ref : 기존 게시글의 ref 컬럼 값을 boardNo값으로 update(ref : 부모글의 글번호, refOrder : 부모글과 답글이 보여지는 순서)
--  게시글이 insert된후 useGeneratedKeys속성에 의해 얻어진 boardNo를 ref 컬럼에 update
update hboard
set ref = ?
where boardNo = ?;

-- 2) 부모글에 대한 다른 답글이 있는 상태에서, 부모글의 새로운 답글이 추가된경우 
-- 새로운 답글이 출력될 공간을 확보한다는 의미에서 기존의 답글에 대해 refOrder값을 1씩 증가시킴
update hboard
set refOrder = refOrder + 1
where ref = ? and refOrder > ?;

-- 3) 답글을 저장할 때,  ref : 부모글의 ref, step : 부모글의 step +1, refOrder : 부모글의 refOrder + 1로 저장
insert into hboard(title, writer, content, ref, step, refOrder)
values (?, ?, ?, ?, ? + 1, ? + 1); 

----------------------------------------------------------------------------------------------------------------
use webshjin;

-- 회원 가입을 위한 member 테이블 수정
ALTER TABLE `member` 
ADD COLUMN `gender` VARCHAR(1) NULL AFTER `email`,
ADD COLUMN `job` VARCHAR(45) NULL AFTER `gender`,
ADD COLUMN `hobbies` VARCHAR(50) NULL AFTER `job`;

ALTER TABLE `member` 
CHANGE COLUMN `email` `email` VARCHAR(50) NOT NULL ;

ALTER TABLE `member` 
ADD COLUMN `postZip` VARCHAR(7) NULL AFTER `hobbies`;

ALTER TABLE `member` 
ADD COLUMN `addr` VARCHAR(150) NULL AFTER `postZip`;

-- 유저아이디가 중복되는지 안되는지 검사
select userId from member where userId = ?;

-- 회원가입 시키는 쿼리문
-- 유저 이미지가 있는 경우
insert into member(userId, userPwd, userName, mobile, email, gender, job, hobbies, postZip, addr, userImg) 
value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

-- 유저 이미지가 없는 경우
insert into member(userId, userPwd, userName, mobile, email, gender, job, hobbies, postZip, addr) 
value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);


-- 로그인 성공/실패 쿼리문
use webshjin;
select * from member where userId=? and userPwd=sha1(md5(?));

-- 관리자 페이지 구현을 위해 테이블 수정
ALTER TABLE `member` 
ADD COLUMN `isAdmin` VARCHAR(1) NULL DEFAULT 'N' AFTER `userpoint`;

-- 게시글 수정 쿼리문 (제목, 내용 수정 가능)
-- 게시글 제목을 수정한다면..
update hboard
set title = ?
where boardNo = ?;

-- 게시글 내용을 수정한다면..
update hboard
set content = ?
where boardNo = ?;

-- 게시글 내용과 제목을 모두 수정할 때
update hboard
set title = ? , content = ?
where boardNo = ?;

-- 위의 3가지 쿼리문이 필요 할 수 있다...
-- 동적 쿼리문으로 만들 수 있긴 하지만, 먼저, 수정할 게시글을 출력해 놓는다면... 
-- 유저가 변경한 부분 + 변경 하지 않은 부분의 값이 그대로 모두 update 될 수 있기 때문에 
-- "게시글 내용과 제목을 모두 수정할 때"의 쿼리문만 필요하게된다..

 -- 게시글의 작성자를 확인 하는 쿼리문
 select writer from hboard where boardNo = ?;
 
-- 게시글의 파일을 수정하는 경우
-- 1) 기존의 파일을 삭제 할 수 있다
-- 2) 기존의 게시글에 파일을 추가 할 수 있다
 
 -- 게시글 수정시 기존 파일을 삭제하는 쿼리문
 delete from boardupfiles where boardUpFileNo = ?;
 
 ----------------------------------------------------------------------------
 -- 게시글 삭제 기능 구현
 
 --  게시글 삭제 시  ref, step, refOrder 값은 그대로 남겨 두는 것으로 처리 한다.
 --  게시글 삭제시 첨부파일이 있다면 첨부 파일은 먼저 삭제 되어야 한다.
 
 --- 게시글 삭제 기능 구현을 위해 테이블 수정
 ALTER TABLE `hboard` 
ADD COLUMN `isDelete` VARCHAR(1) NULL DEFAULT 'N' AFTER `refOrder`;
 
 -- 게시글 삭제 기능 구현 쿼리문
 update hboard
 set title = '삭제된 글입니다', content = null, writer = null, isDelete = 'Y'
 where boardNo = ?;
 

 use webshjin;
 
  -- 게시글 첨부 파일 삭제 쿼리문
 delete from boardupfiles
 where boardNo = ?;

--------------------------------------------------------------------------------------------
-- 자동 로그인 구현
-- 1) 유저가 로그인 할 때, Remember me에 체크되어 있다면
--    로그인 성공했을 때(/member/login (postHandle)의 세션 아이디를 얻어와 유저의 컴퓨터에 저장(쿠키를 이용해), db에도 저장

-- 2) 유저가 로그인 하려 할때(/member/login  (preHandle), 쿠키에 세션아이디가 저장되어 있다면, 그 값이 db에 저장된 세션 아이디와 같은지 비교
--     같다면, 바로 로그인 시킴


-- 3)  유저가 로그인 하려 할 때 쿠키에 세션 아이디가 없다면(자동로그인 x, 쿠키가 만료되어 사라진) ... 그냥 일반 로그인

-- 자동로그인 구현을 위해 테이블 수정
ALTER TABLE member
ADD COLUMN `sessionID` VARCHAR(50) NULL AFTER `isAdmin`;

-- 세션아이디를 저장하는 쿼리문
update member
set sessionID=? 
where userId=?;

-- 쿠키와 저장된 세션ID를 검사하는 쿼리문
select * from member
where sessionID = ?;

---------------------------------------------------------------------------------------------------------------------
-- 마이 페이지 기능
-- 기본 유저의 정보(비밀번호는 보여주지않음) + 수정(비밀번호 확인) , 회원탈퇴, 활동 내역(내가 쓴글, 내가 좋아요(찜)한 글, 포인트 적립내역)

---------------------------------------------------------------------------------------------------------------------
-- 좋아요  기능

------------------------------------------------------------------------------------------------------------
-- 홈 (인기글 10개 출력, 최신글 5개 출력)
-- 최신글 5개 가져오기
select * from hboard order by postDate desc limit 5;

-- limit 출력하기 시작하는 row index번호(default 0), 출력할 갯수
--------------------------------------------------------------------------------------------------
-- 페이징 (paging) 구현
-- 페이징 : 많은 데이터를 일정 양 단위로 나누어 조회되도록 하는 방법
-- 페이징은 단순히 유저에게 편의성을 주는 것이 아니라, 많은 데이터를 한꺼번에 출력하지 않고, 데이터를 끊어서 출력하여 부하를 분산한다는 의미가 강하다

-- mysql에서는 페이징을 limit 키워드를 사용하여 작성 할 수 있다

-- select * from hboard order by ref desc, refOrder asc limit 페이지에서출력하기시작할row의 index번호, 한페이지당 출력할 row의 수;

-- 1) 전체 데이터 갯수
-- select count(*) from hboard;   -- 228
-- 2) pageNo : 유저가 pagination에서 클릭한 페이지의 번호
-- 3) 한페이지당 출력할 row의 수 : 10, 유저에게 선택된 값

-- 4) 전체 페이지 수 
-- 한페이지당 출력할 row의 수 : 10개라고 가정한다면...
-- 1)번에서 나온 갯수(전체 데이터 갯수) / 10 하여, 나누어 떨어지면 그 값이 전체 페이지수가 되고, 나누어 떨어지지않으면 +1 한값이 전체 페이지수가 된다.
-- 228 / 10 = 22.8 => 23

-- 5) 페이지에서출력하기시작할row의 index번호
 -- select * from hboard order by ref desc, refOrder asc limit 0, 10;  -- 1페이지
-- select * from hboard order by ref desc, refOrder asc limit 10, 10;  -- 2페이지
-- select * from hboard order by ref desc, refOrder asc limit 20, 10;  -- 3페이지
-- select * from hboard order by ref desc, refOrder asc limit 30, 10;  -- 4페이지
-- select * from hboard order by ref desc, refOrder asc limit 40, 10;  -- 5페이지

-- 만약 한페이지당 출력할 row의 수 : 5
-- select * from hboard order by ref desc, refOrder asc limit 0, 5  -- 1페이지
-- select * from hboard order by ref desc, refOrder asc limit 5, 5;  -- 2페이지
-- select * from hboard order by ref desc, refOrder asc limit 10, 5;  -- 3페이지

-- 만약 한페이지당 출력할 row의 수 : 3
-- select * from hboard order by ref desc, refOrder asc limit 0, 3  -- 1페이지
-- select * from hboard order by ref desc, refOrder asc limit 3, 3;  -- 2페이지

-- 페이지에서출력하기시작할row의 index번호 = (현재페이지번호 - 1) * 한페이지당 출력할 row의 수

------------------ 페이징 블럭 구현
-- 한 블럭당 몇개 페이지를 보여줄 것인가 ? 10개 (default)
-- 1) 현재 페이지가 몇번 블럭에 있는지?
--  현재페이지번호 / 한 블럭당 보여줄 페이지 해봐서 나누어 떨어지지 않으면 +1

--  4 / 10 =>  1번블럭
-- 7 / 10 => 1번블럭a
-- 17 / 10 => 2번블럭
-- 30 / 10 => 3번블럭

-- 2) 그 블럭의 시작 페이지번호?
-- 1번블럭 -> 1
-- 2번블럭 -> 11
-- 5번블럭 -> 41

-- 그 블럭의 시작 페이지 = ((현재 페이징 블럭 번호 - 1) *  한 블럭당 보여줄 페이지) + 1

-- 3) 그 블럭의 끝 페이지번호
-- 1번블럭 -> 10
-- 2번블럭 -> 20
-- 5번블럭 -> 50
 
-- 그 블럭의 끝 페이지번호 = 현재 페이지member 블럭번호 * 한 블럭당 보여줄 페이지


-- csv to table 해보기 위해 테이블 생성
CREATE TABLE seoultemp (
  `areaId` INT NOT NULL,
  `areaName` VARCHAR(45) NULL,
  `observerDate` DATETIME NULL,
  `avgTemp` FLOAT NULL,
  `avgMaxTemp` FLOAT NULL,
  `maxTemp` FLOAT NULL,
  `maxTempDate` DATETIME NULL,
  `avgMinTemp` FLOAT NULL,
  `minTemp` FLOAT NULL,
  `minTempDate` DATETIME NULL);
  
  
 
  
  ----------------------------------------------------------- 게시판 검색 기능
  
  -- 제목, 작성자, 내용으로 like 검색
  select * from hboard 
  where title like '%%'
  order by ref desc, refOrder asc limit 0, 10;  -- 제목
  
  select * from hboard 
  where writer like '%ti%'
  order by ref desc, refOrder asc limit 0, 10;  -- 작성자
  
   select * from hboard 
  where content like '%%'
  order by ref desc, refOrder asc limit 0, 10;  -- 내용
  
  -- 검색을 했을 때도 결과가 페이징 되어야 한다.
  -- 검색 했을 때 검색된 row의 갯수를 다시 얻어와야 한다(결과의 갯수가 다르기 때문)
   select count(*) from hboard 
  order by ref desc, refOrder asc limit 0, 10;  -- 228
  
   select count(*) from hboard 
   where title like '%준봉%'
  order by ref desc, refOrder asc limit 0, 10;  -- 1


 CREATE TABLE rboard (
   `boardNo` INT NOT NULL AUTO_INCREMENT,
   `title` VARCHAR(50) NOT NULL,
   `content` VARCHAR(4000) NULL,
   `writer` VARCHAR(8) NULL,
   `postDate` DATETIME NULL DEFAULT now(),
   `readCount` INT NULL DEFAULT 0,  -- 여기에 쉼표 추가
   PRIMARY KEY (`boardNo`)
);

ALTER TABLE rboard 
CHANGE COLUMN `content` `content` LONGTEXT NULL DEFAULT NULL ;
  
  -- 스키마 사용
use webmoonya;