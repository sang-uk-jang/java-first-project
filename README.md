# 📚 Study Planner

> 날짜별 학습 계획과 기록을 체계적으로 관리하는 **Java 기반 개인 스터디 플래너**

## 📌 프로젝트 소개

종이 플래너는 학습 기록을 수정하거나 삭제하기 번거롭고, 지난 기록을 검색하거나 보관하기 어렵습니다. **Study Planner**는 캘린더를 중심으로 학습 기록을 생성·조회·수정·삭제하고, 로컬 파일로 저장·불러올 수 있도록 만드는 Java 프로그래밍 팀 프로젝트입니다.

- **개발 목적:** 날짜 중심의 편리한 학습 기록 관리
- **대상 사용자:** 개인 사용자 1명
- **개발 언어:** Java
- **개발 방법론:** **OOAD (Object-Oriented Analysis and Design, 객체지향 분석 및 설계)**
- **핵심 학습 목표:** 객체지향 설계, CRUD, Java Collections, 파일 입출력
- **진행 상태:** 기획 및 요구사항 정의 단계

## ✨ 주요 기능

| ID | 기능 | 설명 |
| --- | --- | --- |
| FR-01 | 🗓️ 캘린더 | 월별 달력을 표시하고 이전/다음 달로 이동합니다. |
| FR-02 | 📅 날짜 선택 | 날짜를 선택해 해당 날짜의 학습 기록을 확인합니다. |
| FR-03 | ✍️ 스터디 기록 생성 | 날짜별 학습 제목과 내용을 등록합니다. |
| FR-04 | 🔎 스터디 기록 조회 및 수정 | 특정 날짜의 기록을 조회하고 제목·내용·날짜를 수정합니다. |
| FR-05 | 🗑️ 스터디 기록 삭제 | 원하는 기록을 확인 후 삭제합니다. |
| FR-06 | 💾 학습 기록 저장 및 불러오기 | 기록을 로컬 파일에 저장하고 재실행 시 복원합니다. |
| FR-07 | 🔍 조건 검색 | 제목 키워드와 날짜 범위로 학습 기록을 검색합니다. |

### 기본 사용 흐름

```text
프로그램 실행
     ↓
저장된 학습 기록 불러오기
     ↓
캘린더 확인 → 날짜 선택
     ↓
기록 생성 / 조회 / 수정 / 삭제
     ↓
조건 검색 및 기록 확인
     ↓
파일 저장
```

## 🧩 OOAD 기반 설계

본 프로젝트는 **객체별 역할과 책임을 분석한 뒤 클래스를 설계**하는 OOAD 방법론을 적용합니다. 화면 처리, 기록 관리, 날짜 계산, 파일 저장 로직을 가능한 한 분리하여 유지보수성과 확장성을 높이는 것이 목표입니다.


### 구현 시 고려사항

- **캡슐화:** 객체의 상태는 적절한 접근 제어와 메서드를 통해 관리합니다.
- **책임 분리:** UI, 날짜 처리, 기록 관리, 저장 기능을 분리합니다.
- **추상화:** `RecordRepository` 인터페이스로 저장 방식 교체 가능성을 열어 둡니다.
- **컬렉션:** `List<StudyRecord>` 등을 활용해 다수 기록을 관리합니다.
- **파일 I/O:** 프로그램을 종료해도 기록이 유지되도록 로컬 파일에 저장합니다.
- **날짜:** `LocalDate`, `YearMonth` 등 `java.time` 사용을 검토합니다.

## 🛠️ 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Java |
| Design Methodology | OOAD |
| Data Structure | Java Collections Framework |
| Persistence | Java File I/O (로컬 파일 기반) |
| UI |  1차 : CLI,  2차 : GUI  |


## 🚀 실행 방법

현재는 요구사항 정의 단계이므로 **실행 명령 및 프로젝트 구조가 확정되지 않았습니다.** 구현 후 실제 빌드 도구, JDK 버전, 실행 진입점을 기준으로 아래 항목을 업데이트할 예정입니다.

1. 필요한 JDK 버전을 설치합니다. *(버전 추후 확정)*
2. 저장소를 복제합니다.

   ```bash
   git clone <REPOSITORY_URL>
   cd <REPOSITORY_DIRECTORY>
   ```

3. 프로젝트에 맞는 빌드·실행 명령으로 프로그램을 실행합니다. *(구현 후 기재)*

## 🤝 협업 방식 (제안)

기능별 브랜치와 Pull Request를 활용하여 개발합니다. 세부 운영 규칙은 팀 합의 후 확정합니다.

- `main`: 팀의 공통 통합 브랜치 (PR로만 변경 사항 병합)
- `feature/<기능명>`: `main`에서 분기하는 기능별 작업 브랜치 (예: `feature/calendar`, `feature/record-crud`)

```text
main → feature/*에서 개발 → Pull Request → 코드 리뷰 → main에 Merge
```

## ✅ 개발 체크리스트

- [ ] 캘린더 표시 및 월 이동 (FR-01)
- [ ] 유효한 날짜 선택 (FR-02)
- [ ] 스터디 기록 생성 (FR-03)
- [ ] 기록 조회 및 수정 (FR-04)
- [ ] 기록 삭제 및 삭제 확인 (FR-05)
- [ ] 로컬 파일 저장 및 불러오기 (FR-06)
- [ ] 제목 키워드 및 날짜 범위 검색 (FR-07)
- [ ] OOAD 상세 설계 및 클래스 역할 확정
- [ ] 기능별 테스트 및 통합 테스트

## 🧪 테스트 계획

다음 항목을 중심으로 검증할 예정입니다.

- 윤년과 월별 일수에 따른 달력 정확성
- 잘못된 날짜 또는 빈 제목 입력 시 예외 처리
- 여러 기록 생성 및 정확한 수정·삭제 대상 식별
- 저장 후 프로그램 재실행 시 데이터 복원
- 검색 조건에 맞는 기록만 조회되는지 확인
- 손상된 저장 파일 처리 시 정상 데이터 보호

## 🔭 향후 확장 가능 기능

아래 기능은 **이번 필수 구현 범위에 포함되지 않습니다.**

- 사용자별 로그인 및 기록 분리
- 학습 타이머



## 📝 Git Commit Convention

팀원들이 변경 사항을 쉽게 이해할 수 있도록 **Conventional Commits 기반**의 커밋 메시지 규칙을 사용합니다.

### 커밋 메시지 형식

```text
<type>: <변경 내용>
```

| Type | 의미 | 예시 |
| --- | --- | --- |
| `feat` | 새로운 기능 추가 | `feat: 날짜별 학습 기록 생성 기능 추가` |
| `fix` | 버그 수정 | `fix: 윤년 달력 날짜 계산 오류 수정` |
| `docs` | 문서 작성 및 수정 | `docs: README 협업 가이드 추가` |
| `refactor` | 기능 변경 없는 코드 구조 개선 | `refactor: 기록 검색 로직 분리` |
| `test` | 테스트 코드 추가 및 수정 | `test: 기록 삭제 테스트 추가` |
| `style` | 코드 포맷 변경(동작 변경 없음) | `style: 들여쓰기 정리` |
| `chore` | 설정·기타 유지보수 | `chore: gitignore 설정 추가` |

**작성 규칙**
- 한 커밋에는 가능하면 하나의 논리적 변경 사항만 담습니다.
- 변경 내용을 이해할 수 있도록 구체적으로 작성합니다.
- 커밋 메시지 언어는 **한국어**를 기본으로 합니다. `type`은 위 표의 영문 키워드를 사용합니다.
- 커밋 전에 `git status`와 `git diff`로 변경 사항을 확인합니다.

### 커밋 예시

```bash
git add src/
git commit -m "feat: 학습 기록 수정 기능 구현"
```

## 🌿 브랜치 생성 및 Push 방법

기능 개발은 최신 `main`에서 분기한 `feature/*` 브랜치에서 진행합니다. `main`에 직접 Push하지 않고 Pull Request(PR)를 통해 병합합니다.

### 1. 최초 1회: 저장소 복제

```bash
git clone <REPOSITORY_URL>
cd <REPOSITORY_DIRECTORY>
```

### 2. 최신 main 가져오기

```bash
git switch main
git pull origin main
```

### 3. 새 기능 브랜치 생성 및 이동

```bash
git switch -c feature/calendar
```

예시 브랜치 이름: `feature/calendar`, `feature/record-crud`, `feature/search`, `feature/file-io`.

### 4. 코드 수정 후 변경 사항 확인·커밋

```bash
git status
git add .
git commit -m "feat: 월별 캘린더 출력 기능 추가"
```

### 5. 원격 저장소에 브랜치 Push

```bash
git push -u origin feature/calendar
```

`-u`는 로컬 브랜치와 원격 브랜치의 추적 관계를 설정합니다. **해당 브랜치에서 최초 1회** 설정한 다음부터는 보통 아래처럼 Push하면 됩니다.

```bash
git add .
git commit -m "fix: 달력 월 이동 오류 수정"
git push
```

### 6. GitHub에서 Pull Request 생성

1. GitHub 저장소의 **Pull requests → New pull request**를 클릭합니다.
2. **base: `main`**, **compare: `feature/calendar`**로 선택합니다.
3. 변경 내용과 테스트 결과를 작성한 뒤 PR을 생성합니다.
4. 팀원의 코드 리뷰 및 승인 후 `main`에 Merge합니다.
5. 병합 후 필요하다면 작업이 끝난 원격 기능 브랜치를 삭제합니다.

### 7. 다음 기능을 시작할 때

```bash
git switch main
git pull origin main
git switch -c feature/search
```

**주의:** 기존 기능 브랜치에서 다음 기능 브랜치를 만들면 앞선 변경 사항까지 따라올 수 있으므로, 원칙적으로 최신 `main`에서 새 브랜치를 생성합니다.

### 협업 명령어 빠른 참고

| 명령어 | 용도 |
| --- | --- |
| `git branch` | 로컬 브랜치 확인 |
| `git switch <브랜치명>` | 다른 브랜치로 이동 |
| `git switch -c <브랜치명>` | 새 브랜치 생성 후 이동 |
| `git status` | 변경 파일 및 현재 상태 확인 |
| `git add <파일경로>` | 변경 사항 스테이징 |
| `git commit -m "메시지"` | 변경 사항 커밋 |
| `git push -u origin <브랜치명>` | 새 브랜치 최초 Push 및 추적 설정 |
| `git push` | 추적 브랜치로 후속 Push |
| `git pull origin main` | 원격 `main`의 최신 변경 사항 가져오기 |


---

**Study Planner** — Java Programming Team Project  

