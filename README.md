# SeoulTech AR - 건물 인식 앱

서울과학기술대학교 캠퍼스 건물을 카메라로 인식하여 정보를 보여주는 Android 앱입니다.

## 📱 주요 기능

- **실시간 카메라 인식**: CameraX를 활용한 실시간 영상 처리
- **건물 정보 표시**: 인식된 건물의 이름과 설명 제공
- **AR 애니메이션**: 건물 인식 시 시각적 피드백

##  기술 스택

| 구분 | 기술 |
|------|------|
| Language | Kotlin |
| Camera | Android CameraX |
| UI | Android View System |
| Animation | XML Animator |

## 프로젝트 구조
```
app/src/main/
├── java/com/example/itscartest/
│   ├── MainActivity.kt      # 메인 카메라 화면
│   ├── Building.kt          # 건물 데이터 클래스
│   └── TextAnalyzer.kt      # 이미지 분석
└── res/
    ├── drawable/            # 건물 이미지
    ├── animator/            # 애니메이션
    └── layout/              # UI 레이아웃
```

##  실행 방법

1. Android Studio에서 프로젝트 열기
2. Gradle Sync 실행
3. 에뮬레이터 또는 실제 기기에서 실행

## 스크린샷

(추후 추가 예정)

##  개발자

- GitHub: [@AIN108](https://github.com/AIN108)
