### 제작 배경
- Spring Security 템플릿의 동작 원리를 이해해보고 싶은데 그냥 알아보기엔 심심하니, 템플릿을 만들어보면서 해보자! 하고 시작한 Repository

### 템플릿 설명
- Spring Security 프레임워크 (6.3.4) 를 이용한다.
- Front는 별도 존재함을 가정하고 API로써 동작하는 인증/인가 애플리케이션 제작에 활용되는 것을 목적으로 한다.
- 로그인을 성공하면 발급한 토큰 값과 연관된 정보를 json 형태로 응답 body에 세팅한다.
  - 응답 데이터 : access token, refresh token, type, expire 등의 필드들을 json body로 응답한다.
- 로그인한 유저는 발급받은 토큰 값을 Authorization 헤더 (value : "Bearer " + 토큰 값)를 이용해서 요청한다고 가정한다.
