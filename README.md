# jwp-chess
체스 게임 구현을 위한 저장소

#### 요구사항
- 스파크 애플리케이션을 유지한 상태에서 스프링 애플리케이션을 추가
- 도메인 객체가 아니며 재사용 할 객체를 스프링 빈을 활용하여 관리하기
#### 프로그래밍 요구사항
- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- 웹 UI를 적용할 때 도메인 객체의 변경을 최소화해야한다.
- 스프링 빈임을 나타내는 애너테이션(@Component, @Service 등)을 활용한다.
- 컴포넌트 스캔을 통해 빈 등록하여 사용한다.

