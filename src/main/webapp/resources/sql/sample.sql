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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원 테이블'

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
