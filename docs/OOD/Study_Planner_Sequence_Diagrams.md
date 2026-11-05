# Study Planner — Sequence Diagrams

## 작성 기준

- `alt`: 조건별 성공·실패 분기, `opt`: 선택적 기능, `loop`: 반복 검사.
- **SRS 확정 사항**: 필수 제목, 유효 날짜, 고유 ID, 삭제 확인, 파일 오류, 키워드·날짜 범위 AND 검색.
- **구체화한 설계 선택**: UI가 입력을 받아 `StudyPlanner`에 위임하고, 저장은 `RecordRepository`를 경유하며, 파일 오류 시 정상 기록을 보존하는 호출 구조. 실제 UI 형태, 직렬화 포맷, 자동 저장은 아직 미확정입니다.
- `FileRecordRepository`는 `RecordRepository`의 실제 구현체입니다. 저장·불러오기 시퀀스에서 인터페이스와 구현체를 함께 그린 화살표는 **추상 호출과 실제 구현 실행을 설명하기 위한 논리적 표현**입니다. 런타임에서는 보통 구현체 객체 하나가 호출을 받습니다.

## UC-01 — 월별 캘린더 조회 및 이동 (FR-01)

```plantuml
@startuml
title UC-01 - 월별 캘린더 조회 및 이동
autonumber
actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
U -> UI: 캘린더 열기
UI -> CAL: getMonthDays(yearMonth)
alt 연월이 유효함
    CAL --> UI: List<LocalDate>
    UI -> U: showCalendar(yearMonth, days)
else 연월이 유효하지 않음
    CAL --> UI: 유효하지 않은 연월 오류
    UI -> U: showError(message)
end
opt 사용자가 이전 달/다음 달 선택
    U -> UI: 월 이동(direction)
    UI -> CAL: moveMonth(yearMonth, direction)
    alt 이동할 연월이 유효함
        CAL --> UI: newYearMonth
        UI -> CAL: getMonthDays(newYearMonth)
        CAL --> UI: days
        UI -> U: showCalendar(newYearMonth, days)
    else 지원 범위를 벗어남
        CAL --> UI: 오류
        UI -> U: showError(message) / 현재 달 유지
    end
end
@enduml
```

## UC-02 — 날짜 선택 (FR-02)

```plantuml
@startuml
title UC-02 - 날짜 선택
autonumber
actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL
U -> UI: 날짜 선택(year, month, day)
UI -> CAL: validateDate(year, month, day)
alt 유효한 날짜
    CAL --> UI: selectedDate: LocalDate
    UI -> UI: 선택 날짜 갱신
    UI -> PL: findByDate(selectedDate)
    PL --> UI: List<StudyRecord>
    UI -> U: 선택 날짜 및 기록 목록 표시
else 잘못된 날짜 (예: 2월 30일)
    CAL --> UI: 날짜 오류
    UI -> U: showError(message) / 선택 날짜 유지
end
@enduml
```

## UC-03 — 학습 기록 등록 (FR-03)

```plantuml
@startuml
title UC-03 - 학습 기록 등록
autonumber
actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL
entity "StudyRecord" as REC
U -> UI: 기록 등록(date, title, content)
UI -> CAL: validateDate(date)
alt 날짜가 유효하지 않음
    CAL --> UI: 날짜 오류
    UI -> U: showError(message)
else 날짜가 유효함
    CAL --> UI: validDate
    UI -> PL: addRecord(date, title, content)
    PL -> PL: validateTitle(title)
    alt 제목이 공백임
        PL --> UI: 입력 오류 (변경 없음)
        UI -> U: showError(message)
    else 제목이 유효함
        PL -> PL: generateUniqueId()
        create REC
        PL -> REC: new StudyRecord(id, date, title, content)
        PL -> PL: records.add(REC)
        PL --> UI: 생성 결과 (id)
        UI -> PL: findByDate(date)
        PL --> UI: 갱신된 기록 목록
        UI -> U: showRecords() / 생성 성공 표시
    end
end
@enduml
```

## UC-04 — 학습 기록 조회 (FR-04)

```plantuml
@startuml
title UC-04 - 학습 기록 조회
autonumber
actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL
U -> UI: 특정 날짜의 기록 조회(date)
UI -> CAL: validateDate(date)
alt 날짜 유효
    CAL --> UI: validDate
    UI -> PL: findByDate(date)
    PL --> UI: List<StudyRecord>
    alt 기록이 있음
        UI -> U: showRecords(records)
    else 기록이 없음
        UI -> U: 빈 목록 표시
    end
else 날짜 무효
    CAL --> UI: 날짜 오류
    UI -> U: showError(message)
end
@enduml
```

## UC-05 — 학습 기록 수정 (FR-04)

```plantuml

@startuml
title UC-05 - 학습 기록 수정
autonumber

actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL
entity "StudyRecord" as REC

' 1. 날짜 입력
U -> UI: 수정할 기록의 날짜 입력(date)
UI -> CAL: validateDate(date)

alt 날짜 무효
    CAL --> UI: 날짜 오류
    UI -> U: showError(message)
else 날짜 유효
    CAL --> UI: validDate

    ' 2. 해당 날짜의 기록 조회
    UI -> PL: findByDate(date)
    PL --> UI: List<StudyRecord>

    alt 해당 날짜에 기록 없음
        UI -> U: showMessage("기록 없음")
    else 기록 존재
        UI -> U: 날짜별 기록 목록 표시(ID, 제목, 내용)

        ' 3. 사용자가 ID 선택
        U -> UI: 수정할 기록 ID 선택(id)
        UI -> PL: findById(id)

        alt ID가 없음
            PL --> UI: 조회 실패
            UI -> U: showError(message)
        else ID 존재
            PL --> UI: 기존 StudyRecord
            UI -> U: 현재 값 표시

            ' 4. 수정할 내용 입력
            U -> UI: 수정 제출(id, newDate, title, content)
            UI -> CAL: validateDate(newDate)

            alt 수정 날짜 무효
                CAL --> UI: 날짜 오류
                UI -> U: showError(message) / 수정 없음
            else 수정 날짜 유효
                CAL --> UI: validDate
                UI -> PL: updateRecord(id, newDate, title, content)
                PL -> PL: validateTitle(title)

                alt 제목 공백 또는 ID 사라짐
                    PL --> UI: 수정 실패 (변경 없음)
                    UI -> U: showError(message)
                else 입력 유효
                    PL -> REC: update(newDate, title, content)
                    REC --> PL: 수정 완료
                    PL --> UI: 수정 완료

                    UI -> PL: findById(id)
                    PL --> UI: 변경된 StudyRecord
                    UI -> U: 수정된 기록 표시
                end
            end
        end
    end
end
@enduml

```

## UC-06 — 학습 기록 삭제 (FR-05)

```plantuml

@startuml
title UC-06 - 학습 기록 삭제
autonumber

actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL

' 1. 날짜 입력
U -> UI: 삭제할 기록의 날짜 입력(date)
UI -> CAL: validateDate(date)

alt 날짜 무효
    CAL --> UI: 날짜 오류
    UI -> U: showError(message)
else 날짜 유효
    CAL --> UI: validDate

    ' 2. 해당 날짜의 기록 조회
    UI -> PL: findByDate(date)
    PL --> UI: List<StudyRecord>

    alt 해당 날짜에 기록 없음
        UI -> U: showMessage("기록 없음")
    else 기록 존재
        UI -> U: 날짜별 기록 목록 표시(ID, 제목, 내용)

        ' 3. 사용자가 삭제할 ID 선택
        U -> UI: 삭제할 기록 ID 선택(id)
        UI -> PL: findById(id)

        alt ID가 없음
            PL --> UI: 조회 실패
            UI -> U: showError(message)
        else ID 존재
            PL --> UI: 삭제 대상 정보
            UI -> U: 삭제 대상 표시 및 확인 요청

            ' 4. 삭제 확인 또는 취소
            U -> UI: 확인 또는 취소

            alt 삭제 확인
                UI -> PL: deleteRecord(id)
                PL -> PL: records.remove(id)
                PL --> UI: 삭제 성공

                ' 5. 갱신된 목록 조회
                UI -> PL: findByDate(date)
                PL --> UI: 갱신된 List<StudyRecord>
                UI -> U: 성공 메시지 / 갱신 목록

            else 삭제 취소
                UI -> U: 취소 안내 / 데이터 유지
            end
        end
    end
end

@enduml
```

## UC-07 — 학습 기록 파일 저장 (FR-06)

```plantuml

@startuml
title UC-07 - 학습 기록 파일 저장
autonumber

actor User as U
boundary "PlannerUI" as UI
control "StudyPlanner" as PL
participant "RecordRepository" as REPO
participant "FileRecordRepository" as FILE
collections "로컬 파일 시스템" as FS

U -> UI: 종료
UI -> PL: saveRecords()
PL -> REPO: saveAll(records)
REPO -> FILE: saveAll(records)
FILE -> FILE: 직렬화 및 안전한 파일 기록
FILE -> FS: 파일 쓰기

alt 쓰기 성공
    FS --> FILE: 완료
    FILE --> REPO: 성공
    REPO --> PL: 성공
    PL --> UI: 저장 완료
    UI -> U: 성공 안내

else 권한 오류 / I/O 실패
    FS --> FILE: IOException
    FILE --> REPO: 저장 실패
    REPO --> PL: 저장 실패
    PL --> UI: 저장 실패 (기존 정상 데이터 보호)
    UI -> U: showError(message)
end
@enduml

```

## UC-08 — 학습 기록 파일 불러오기 (FR-06)

```plantuml

@startuml
title UC-08 - 학습 기록 파일 불러오기
autonumber

actor User as U
boundary "PlannerUI" as UI
control "StudyPlanner" as PL
participant "repo:RecordRepository" as REPO
collections "로컬 파일 시스템" as FS

note over REPO
실제 객체는 FileRecordRepository
end note

U -> UI: 프로그램 시작 또는 불러오기 요청
UI -> PL: loadRecords()
PL -> REPO: findAll()
REPO -> FS: 파일 읽기

alt 파일이 존재하고 정상임
    FS --> REPO: 저장된 데이터
    REPO -> REPO: 역직렬화 및 유효성 검증
    REPO --> PL: List<StudyRecord>
    PL -> PL: records 교체
    PL --> UI: 불러오기 성공
    UI -> U: showRecords(records)

else 최초 실행 / 파일 없음
    FS --> REPO: 파일 없음
    REPO --> PL: 빈 목록
    PL -> PL: records를 빈 목록으로 초기화
    PL --> UI: 빈 목록 준비
    UI -> U: 초기 화면 표시

else 손상된 파일 또는 읽기 오류
    FS --> REPO: 읽기 실패 또는 손상 데이터
    REPO -> REPO: 오류 처리
    REPO --> PL: 로드 실패
    PL -> PL: 기존 정상 기록 유지
    PL --> UI: 오류 (자동 덮어쓰기 금지)
    UI -> U: showError(message)
end

@enduml

```

## UC-09 — 조건 검색 (FR-07)

```plantuml

@startuml
title UC-09 - 조건 검색
autonumber

actor User as U
boundary "PlannerUI" as UI
control "CalendarService" as CAL
control "StudyPlanner" as PL

U -> UI: 검색(keyword?, startDate?, endDate?)

opt 날짜 범위가 입력됨
    UI -> CAL: validateDateRange(startDate, endDate)
    CAL --> UI: 유효성 검증 결과
end

alt 날짜 형식 오류 또는 시작일이 종료일보다 늦음
    UI -> U: showError(message) / 검색 취소

else 검색 조건 유효
    UI -> PL: search(keyword, startDate, endDate)

    loop 각 StudyRecord 반복
        PL -> PL: 제목 부분 일치 검사 (keyword 입력 시)
        PL -> PL: 날짜 범위 포함 검사 (범위 입력 시)

        note right of PL
            여러 조건이 있으면 AND 결합
            날짜 범위 양 끝 포함
        end note
    end

    PL --> UI: 일치하는 기록 목록

    alt 일치하는 기록 존재
        UI -> U: showRecords(results)
    else 결과 없음
        UI -> U: 검색 결과 없음 표시
    end
end

@enduml

```

