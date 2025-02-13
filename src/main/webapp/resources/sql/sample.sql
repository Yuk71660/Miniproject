-- 스키마 사용
use webmoonya;-- 
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