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
  
INSERT INTO `webmoonya`.`pointlog` (`pointLogNo`, `pointwhen`, `pointwho`, `pointwhy`, `pointscore`) VALUES ('2', '2025-02-05 14:25:44', 'subAdmin', '회원가입', '100');


  
  