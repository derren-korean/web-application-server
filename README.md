# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* HTTP의 request의 정보를 가져옵니다. [HTTP 스팩 문서](https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html)
* request-line의 파일 경로를 이용하여 파일을 읽어옵니다.
* 해당 파일을 byte로 변환하여 client로 반환합니다.

### 요구사항 2 - get 방식으로 회원가입
#### 해당 request가 query라면, 해당 업무를 진행 후 index.html로 응답한다.
* 자바 외부 라이브러리를 이용하지 않고, 쿼리가 있는 경우와 없는 경우 단순한 2개로만 나누어 진행한다.
* request의 query를 model.User로 파싱한다.
* index.html로 응답한다.

### 요구사항 3 - post 방식으로 회원가입
* get이외에도 post방식으로 가능하도록 변경한다.
* post는 해더 마지막에 바디로써 쿼리가 추가된다.
* befferedReader가 다 읽히면(EOF) 소켓의 연결이 끊어짐으로 주의한다.
 
### 요구사항 4 - redirect 방식으로 이동
* header write가 순차 진행 가능하도록 한다.
* get과 post의 경우 다르게 작성되어야 한다.
* 자세한 내용은 3xx status code

### 요구사항 5 - cookie
* 로그인 성공 - redirect to index.html & set Cookie = logined=true
* 로그인 실패 - redirect to login_failed.html & set Cookie = logine=false

### 요구사항 6 - 사용자 리스트
* 로그인 쿠키에 사용자 로그인이 되었다면, 보내준다.
* 로그인이 안되어 있는 경우, login 페이지로 넘긴다. 

### 요구사항 7 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 