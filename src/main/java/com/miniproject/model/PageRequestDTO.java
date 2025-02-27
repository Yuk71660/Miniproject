package com.miniproject.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @date 2025. 2. 27.
 * @packagename com.miniproject.model
 * @typeName PageRequestDTO
 * @todo 페이징에 필요한 매개변수를 가지고 있는 DTO클래스
 * 
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PageRequestDTO {
   private int pageNo = 1;  // 현재 페이지 번호
   private int rowCntPerPage = 10;  // 1페이지당 보여줄 row의 수

}
