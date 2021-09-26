# bomz-logger

**BOMZ Public Project Logger 1.1**

자바 기반의 Logger 구현체입니다



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
      콘솔상에 로그 데이터 출력   
   - File
      로그 내용을 파일로 저장하며 시간/일 단위로 로그 파일 분할 저장 및 파일당 최대 크기를 지정 가능
   - TCP
      설정된 IP:PORT 로 접속하여 TCP 방식으로 로그 데이터 전송
   - Http 
      지정된 URL 과 Http Method 방식으로 접속하여 로그 데이터 전송
   - Custom
      사용자가 직접 구현한 구현체로 로그 데이터 처리



**Date Pattern**

   - 예시)
      설정 : <pattern name="date">[yyyy.MM.dd hh:mm:ss]</pattern>
      결과 : [2021.09.26 13:09:20]


MIT License
Copyright (c) 2021 BOMZ
