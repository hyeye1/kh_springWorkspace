package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * * 롬복(lombok)
 * - 필드에 대한 getter/setter, toString, 생성자 등등 어노테이션 기술만으로 자동으로 만들어주는 라이브러리
 * => 필드 수정할 때마다 내가 일일히 getter/setter, 생성자 수정할 필요 없음!!
 * 
 *  1. Maven을 통해 lombok라이브러리 dependency로 추가
 *  2. 해당 jar파일이 위치해있는 폴더 찾아가서 .jar 파일 실행해서 설치했음
 *     내가 적용시키고자 하는 IDE 선택해서 설치
 *  3. IDE 재부팅
 *  
 *  4. 어노테이션으로 기술하면서 사용하면됨!!
 *  @ToString : 모든 필드 하나의 문자열로 합쳐서 반환시켜주는 toString메소드 자동생성
 *  @Setter/@Getter : 모든 필드에 대한 getter/setter 메소드 자동생성
 *  @NoArgsConstructor : 인자 없는 생성자 (기본생성자) 자동생성
 *  @AllArgsConstructor : 모든 인자를 가진 생성자(매개변수 생성자) 자동생성
 *  @EqualsAndHashCode : equals()와 hashCode() 메소드 자동생성
 *  등등..
 *  
 *  @Data : 위의 모든것들을 싹다 만들어주는 (매개변수 생성자 제외).. => 지양 
 *  
 *  
 *  * 주의사항
 *    => 필드명 지을때 적어도 소문자 두개 이상으로 시작하는 이름으로 지을것!!
 *       ex) pName, pCode => 이런걸로 하지마셈!! => productName, productCode 이런식으로!!
 *  
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Member {

	private String userId;
	private String userPwd;
	private String userName;
	private String email;
	private String gender;
	private String age;
	private String phone;
	private String address;
	private Date enrollDate;
	private Date modifyDate;
	private String status;
	
	//private String pName;  // jsp에서 ${ 객체.pName }로 접근하면 내부적으로 getpName() 메소드 찾음
							 // 근데 롬복을 이용해서 만들어진 메소드보면 getPName() 메소드가 만들어짐.. => 못찾음
	
}
