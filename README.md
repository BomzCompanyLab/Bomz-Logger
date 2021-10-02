# bomz-logger

**BOMZ Public Project Logger 1.1**

자바 기반의 Logger 구현체


**Observe**
   - 로거 설정파일의 변경 여부 실시간 감시
   - 'on' or 'off'

**Log Level**
   - TRACE
   - DEBUG
   - INFO
   - WARN
   - ERROR
   - FATAL
   - OFF



**Appender**
   - Console
      - 콘솔상에 로그 데이터 출력   
   - File
      - 로그 내용을 파일로 저장하며 시간/일 단위로 로그 파일 분할 저장 및 파일당 최대 크기를 지정 가능
   - TCP
      - 설정된 IP:PORT 로 접속하여 TCP 방식으로 로그 데이터 전송
   - Http 
      - 지정된 URL 과 Http Method (GET or POST) 방식으로 접속하여 로그 데이터 전송
   - Custom
      - 사용자가 직접 구현한 구현체로 로그 데이터 처리



**Date Pattern**
   - G : Era designator
   - y : Year
   - M : Month
   - w : Week in year
   - W : Week in Month
   - D : Day in year
   - d : Day in month
   - F : Day of week in month
   - E : Day in week
   - H : Hour 0 ~ 23
   - k : Hour 1 ~ 24
   - h : Hour 1 ~ 12
   - m : Minute
   - s : Second
   - S : Millisecond
   - z : Time zone (General)
   - Z : Time zone (RFC 822)
   - a : am / pm


**Date Locale**
   - KO : KOREA
   - US : USA
   - JA : JAPAN
   - ZH : CHINA
   - FR : FRANCE


**Date Time Zone**
   - Asia/Seoul
   - America/Los_Angeles
   - Etc..


**Example**
   - Log Level
      - info :: info, warn, error and fatal
      - fatal :: only fatal
      - (trace|warn) :: trace and warn
      - (debug|error|fatal) :: debug, error and fatal
      - (warn) :: only warn
  




MIT License
Copyright (c) 2021 BOMZ
