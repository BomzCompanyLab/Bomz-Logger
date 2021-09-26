# bomz-logger

**BOMZ Public Project Logger 1.1**

자바 기반의 Logger 구현체입니다


> Log Level

   - TRACE
   - DEBUG
   - INFO
   - WARN
   - ERROR
   - FATAL
   - OFF


> Appender

   - File
      로그 내용을 파일로 저장하며 시간/일 단위로 로그 파일 분할 저장 및 파일당 최대 크기를 지정할 수 있습니다
`<logger name="root" type="file" level="warn"><br>`
`    <pattern name="dir">./log/my.log</pattern>`
`    <pattern name="size">50</pattern>`
`    <pattern name="period">(DAY | HOUR)</pattern>`
`</logger>`





MIT License
Copyright (c) 2021 BOMZ
