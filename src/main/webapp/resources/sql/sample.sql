-- 스키마 사용
use webmoonya; 
-- 회원 테이블 생성
CREATE TABLE `member` (
  `userId` varchar(8) NOT NULL,
  `userPwd` varchar(200) NOT NULL,
  `userName` varchar(12) DEFAULT NULL,
  `mobile` varchar(13) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `registerDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `userImg` varchar(50) DEFAULT 'memberimg/avatar100.png',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원 테이블';

ALTER TABLE `webmoonya`.`member` 
ADD COLUMN `userpoint` INT NULL DEFAULT 0 AFTER `userImg`;


-- 게시글 테이블 생성
CREATE TABLE `webmoonya`.`hboard` (
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
  
  -- 회원-계층형게시판 테이블 관계 설정_fk
  ALTER TABLE `webmoonya`.`hboard` 
ADD INDEX `hboard_member_fk_idx` (`writer` ASC) VISIBLE;
;
ALTER TABLE `webmoonya`.`hboard` 
ADD CONSTRAINT `hboard_member_fk`
  FOREIGN KEY (`writer`)
  REFERENCES `webmoonya`.`member` (`userId`)
  ON DELETE SET NULL
  ON UPDATE NO ACTION;
  
INSERT INTO `webmoonya`.`hboard` (`title`, `content`, `writer`) VALUES ('날씨', '춥다', 'admin');

SELECT * FROM hboard order by boardNo desc;

-- 포인트 정보 테이블 생성
CREATE TABLE `webmoonya`.`pointinfo` (
  `pointcontent` VARCHAR(20) NOT NULL,
  `pointscore` INT NOT NULL,
  PRIMARY KEY (`pointcontent`));

INSERT INTO `webmoonya`.`pointinfo` (`pointcontent`, `pointscore`) VALUES ('회원가입', '100');
INSERT INTO `webmoonya`.`pointinfo` (`pointcontent`, `pointscore`) VALUES ('게시글작성', '5');
INSERT INTO `webmoonya`.`pointinfo` (`pointcontent`, `pointscore`) VALUES ('로그인', '1');
INSERT INTO `webmoonya`.`pointinfo` (`pointcontent`, `pointscore`) VALUES ('답글/댓글작성', '2');
INSERT INTO `webmoonya`.`pointinfo` (`pointcontent`, `pointscore`) VALUES ('게시글신고', '-10');

-- 포인트 지급 내역 테이블 생성
CREATE TABLE `webmoonya`.`pointlog` (
  `pointLogNo` INT NOT NULL AUTO_INCREMENT,
  `pointwhen` DATETIME NOT NULL DEFAULT now(),
  `pointwho` VARCHAR(8) NOT NULL,
  `pointwhy` VARCHAR(20) NOT NULL,
  `pointscore` INT NOT NULL,
  PRIMARY KEY (`pointLogNo`))
COMMENT = '포인트 지급 내역';

ALTER TABLE `webmoonya`.`pointlog` 
ADD INDEX `pointlog_pointwho_fk_idx` (`pointwho` ASC) VISIBLE,
ADD INDEX `pointlog_pointwhy_fk_idx` (`pointwhy` ASC) VISIBLE;
;
ALTER TABLE `webmoonya`.`pointlog` 
ADD CONSTRAINT `pointlog_pointwho_fk`
  FOREIGN KEY (`pointwho`)
  REFERENCES `webmoonya`.`member` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `pointlog_pointwhy_fk`
  FOREIGN KEY (`pointwhy`)
  REFERENCES `webmoonya`.`pointinfo` (`pointcontent`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
  
INSERT INTO pointlog (`pointwho`, `pointwhy`, `pointscore`) VALUES ('subAdmin', '회원가입', (
select `pointscore` from pointinfo where pointcontent = '회원가입'
));

UPDATE member SET `userpoint` = `userpoint` + ? WHERE (`userId` = 'admin');

-- 게시판 파일 업로드용 테이블
CREATE TABLE `webmoonya`.`boardupfiles` (
  `boardUpfileNo` INT NOT NULL AUTO_INCREMENT,
  `originalFileName` VARCHAR(50) NOT NULL,
  `newFileName` VARCHAR(100) NOT NULL,
  `ext` VARCHAR(4) NULL,
  `size` INT NULL,
  `base64Image` LONGTEXT NULL,
  `boardNo` INT NOT NULL,
  PRIMARY KEY (`boardUpfileNo`))
COMMENT = '게시글에 업로드되는 파일';
-- hboard 참조
ALTER TABLE `webmoonya`.`boardupfiles` 
ADD INDEX `boardupFiles_boardNo_fk_idx` (`boardNo` ASC) VISIBLE;
;
ALTER TABLE `webmoonya`.`boardupfiles` 
ADD CONSTRAINT `boardupFiles_boardNo_fk`
  FOREIGN KEY (`boardNo`)
  REFERENCES `webmoonya`.`hboard` (`boardNo`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;
ALTER TABLE `webmoonya`.`boardupfiles` 
ADD COLUMN `thumbFileName` VARCHAR(100) NULL AFTER `newFileName`,
CHANGE COLUMN `ext` `ext` VARCHAR(50) NULL DEFAULT NULL ;

INSERT INTO boardupfiles (`originalFileName`, `newFileName`, `thumbFileName`, `fileType`, `ext`, `size`, `base64Image`, `boardNo`)
VALUES ('2', '3', '4', '5', '6', '7', '8', '9');

SELECT * FROM hboard where (`boardNo` = 11);

select * from member where userid = (select writer from hboard where boardNo = 15);

SELECT
		 h.*, f.*,
		 m.userId, m.userName, m.email, m.userImg
		FROM hboard h left outer join boardupfiles f 
		on h.boardNo = f.boardNo inner join member m on h.writer = m.userId 
		where (h.boardNo = 13);
        
-- 조회수 증가용 읽은 기록 테이블 작성
CREATE TABLE `webmoonya`.`boardreadlog` (
  `boardReadNo` INT NOT NULL AUTO_INCREMENT,
  `readWho` VARCHAR(50) NULL,
  `readWhen` DATETIME NULL DEFAULT now(),
  `readboardNo` INT NULL,
  PRIMARY KEY (`boardReadNo`),
  INDEX `readlog_boardNo_fk_idx` (`readboardNo` ASC) VISIBLE,
  CONSTRAINT `readlog_boardNo_fk`
    FOREIGN KEY (`readboardNo`)
    REFERENCES `webmoonya`.`hboard` (`boardNo`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
    
select ifnull( (SELECT`readboardNo` 
FROM `webmoonya`.`boardreadlog`
WHERE `readWho` = '127'
  AND `readboardNo` = 1 
  AND `readWhen` >= NOW() - INTERVAL 1 DAY), -1) as timediff;
  
select ifnull(timestampdiff(hour,  (select max(readWhen)
from boardreadlog
where readWho = '127' and readBoardNo = 1), now()),-1) as timediff;

select ifnull(timestampdiff(hour, 
(select readWhen from boardreadlog where readWho = 'readWho' and readBoardNo = 'boardNo'), now()), -1) as timediff;

update hboard
set readCount = readCount + 1
where boardNo = ?;

INSERT INTO boardreadlog (`readWho`, `readboardNo`) VALUES ('127', '2');

------------------------------------------------------------------------------------------------------
-- 답글 기능 구현 + 계층형 게시판 구현
-- 0) 게시판의 정렬기준을 아래의 정렬기준으로 바꾼다 (ref : 부모글의 글번호
select * from hboard order by ref desc, refOrder asc;


-- 1) ref : 기존 게시글의 ref 컬럼 값을 boardNo값으로 update(ref : 부모글의 글번호, refOrder : 부모글과 답글이 보여지는 순서)
--  게시글이 insert된후 useGeneratedKeys속성에 의해 얻어진 boardNo를 ref 컬럼에 update
update hboard
set ref = ?
where boardNo = ?;

-- 2) 답글을 저장할 때,  ref : 부모글의 ref, step : 부모글의 step +1, refOrder : 부모글의 refOrder + 1로 저장
insert into hboard(title, writer, content, ref, step, refOrder)
values (?, ?, ?, ?, ? + 1, ? + 1);

select refOrder from hboard where ref = 3 and refOrder >= 2 ;

UPDATE hboard 
SET refOrder = refOrder + 1 
WHERE ref = 3 AND refOrder > 2;
	
    
    
DELETE FROM hboard WHERE (`boardNo` = '8');

select boardNo from hboard where boardNo = boardNo and writer = userId;

select userId from member where userId = userId;

INSERT INTO `webmoonya`.`member` (`userId`, `userPwd`, `userName`, `mobile`, `email`, `gender`, `job`, `hobbys`, `postZip`, `addr`)
VALUES ('dooly',  sha1(md5(?)), '둘리', '010-1234-5678', 'dooly@dooly.com', 'F', 'a', 'a', '14325', 'asdf');

select * from member where userId = 'aba4114' and userPwd = sha1(md5('1234'));

SELECT * FROM webmoonya.pointlog;

-- 24시간 포인트지급_24시간 이내 기록있으면 지난시간 0~24 int반환 없으면 -1반환
select ifnull(timestampdiff(hour,  (select max(pointwhen)
from pointlog
where pointwho = 'aba4114' and pointwhy = '로그인'), now()),-1) as timediff;

UPDATE hboard SET `boardNo` = '22'
-- if title != null
, `title` = '?'
-- if content != null
, `content` = '?'
WHERE (`boardNo` = '22');

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
set title = ?, content = ?
where boardNo = ?;

select * from boardupfiles where boardNo = 16;
-- 위의 3가지 쿼리문이 필요 할 수 있다...
-- 동적 쿼리문으로 만들 수 있긴 하지만, 먼저, 수정할 게시글을 출력해 놓는다면... 
-- 유저가 변경한 부분 + 변경 하지 않은 부분의 값이 그대로 모두 update 될 수 있기 때문에 
-- "게시글 내용과 제목을 모두 수정할 때"의 쿼리문만 필요하게된다..

select writer from hboard where boardNo = ?;

DELETE FROM boardupfiles WHERE (boardNo = ?);

------------------
 -- 게시글 삭제 기능 구현
 
 --  게시글 삭제 시  ref, step, refOrder 값은 그대로 남겨 두는 것으로 처리 한다.
 --  게시글 삭제시 첨부파일이 있다면 첨부 파일은 먼저 삭제 되어야 한다.
 
 --- 게시글 삭제 기능 구현을 위해 테이블 수정
 ALTER TABLE hboard
ADD COLUMN `isDelete` VARCHAR(1) NULL DEFAULT 'N' AFTER `refOrder`;

UPDATE hboard SET `title` = '삭제된글입니다', `content` = '', `writer` = null , `isDelete` = 'Y' WHERE (`boardNo` = '6');


select h.*, f.*, m.userId, m.userName, m.email, m.userImg
      from hboard h left outer join boardupfiles f
      on h.boardNo = f.boardNo
      inner join member m
      on h.writer = m.userId
      where h.boardNo = 6;
      
SELECT h.*, f.*, m.userId, m.userName, m.email, m.userImg
	FROM hboard h LEFT OUTER JOIN boardupfiles f
    ON h.boardNo = f.boardNo
	LEFT OUTER JOIN member m
    ON h.writer = m.userId
	WHERE h.boardNo = 6;

 -- 게시글 삭제 기능 구현 쿼리문
 update hboard
 set title = '삭제된 글입니다', content = null, writer = null, isDelete = 'Y'
 where boardNo = ?;
 

 use webshjin;
 
  -- 게시글 첨부 파일 삭제 쿼리문
 delete from boardupfiles
 where boardNo = ?;


-- 자동 로그인 구현
-- 1) 유저가 로그인 할 때, Remember me에 체크되어 있다면
--    로그인 성공했을 때(/member/login (postHandle)의 세션 아이디를 얻어와 유저의 컴퓨터에 저장(쿠키를 이용해), db에도 저장

-- 2) 유저가 로그인 하려 할때(/member/login  (preHandle), 쿠키에 세션아이디가 저장되어 있다면, 그 값이 db에 저장된 세션 아이디와 같은지 비교
--     같다면, 바로 로그인 시킴


-- 3)  유저가 로그인 하려 할 때 쿠키에 세션 아이디가 없다면(자동로그인 x, 쿠키가 만료되어 사라진) ... 그냥 일반 로그인

-- 자동로그인 구현을 위해 테이블 수정
ALTER TABLE member
ADD COLUMN `sessionID` VARCHAR(50) NULL AFTER `isAdmin`;















-- 스키마 사용
use webmoonya;