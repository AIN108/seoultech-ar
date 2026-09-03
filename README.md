# SeoulTech AR - 캠퍼스 건물 정보 오버레이 앱

서울과학기술대학교 캠퍼스에서 카메라로 건물 번호를 읽고, 일치하는 건물의 이름·이미지·설명을 카메라 화면 위 UI로 표시하는 Android 프로젝트입니다.

> 프로젝트 이름에는 `AR`이 포함되어 있지만 현재 구현은 ARCore/3D 공간추적 기반 AR이 아닙니다. CameraX의 실시간 프레임을 Google ML Kit Text Recognition으로 분석해 건물 번호를 인식하고, Android View UI를 카메라 프리뷰 위에 표시하는 방식입니다.

## 주요 기능

- CameraX 기반 후면 카메라 실시간 프리뷰
- ML Kit Text Recognition 기반 숫자/OCR 인식
- 등록된 건물 번호와 OCR 결과 매칭
- 건물 이름·이미지·설명 카드 표시
- 카메라 프리뷰 위 시각적 타깃/UI 오버레이

현재 등록된 건물에는 대학본부(1), 다산관(2), 창학관(3), 테크노파크(7), 도서관(34), 다빈치관(39), 100주년기념관(51), 미래관(60), 테크노큐브(62)가 포함되어 있습니다.

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Kotlin |
| Camera | AndroidX CameraX |
| Recognition | Google ML Kit Text Recognition |
| UI | Android View System / ViewBinding |
| Animation | XML Animator |
| Build | Gradle Kotlin DSL |

주요 라이브러리 버전은 `app/build.gradle.kts`에서 관리합니다.

## 프로젝트 구조

```text
app/src/main/
├── java/com/example/itscartest/
│   ├── MainActivity.kt      # CameraX 프리뷰, 건물 매칭, UI 제어
│   ├── Building.kt          # 건물 데이터 클래스
│   └── TextAnalyzer.kt      # ML Kit OCR 이미지 분석
└── res/
    ├── drawable/            # 건물 이미지 및 UI 리소스
    ├── animator/            # 애니메이션
    └── layout/              # UI 레이아웃
```

## 실행 방법

1. Android Studio에서 프로젝트를 엽니다.
2. Gradle Sync를 실행합니다.
3. 카메라 권한을 사용할 수 있는 Android 기기 또는 에뮬레이터를 준비합니다.
4. 앱을 빌드·실행하고 카메라 권한을 허용합니다.

현재 설정은 `compileSdk 34`, `targetSdk 34`, `minSdk 24`입니다.

## 인식 방식

현재 코드는 건물 자체의 외형을 이미지 분류 모델로 판별하지 않습니다.

```text
CameraX 프레임
      ↓
ML Kit Text Recognition
      ↓
프레임에서 숫자 문자열 추출
      ↓
등록된 건물 번호와 비교
      ↓
일치하면 건물 정보 카드 표시
```

따라서 건물 번호가 카메라에서 충분히 읽힐 수 있는 크기·초점·조명이어야 합니다.

## 외부 라이브러리와 출처

- Android CameraX: https://developer.android.com/media/camera/camerax
- Google ML Kit Text Recognition: https://developers.google.com/ml-kit/vision/text-recognition/v2/android
- AndroidX / Material Components: 각 Android/Google 프로젝트의 라이선스를 따릅니다.

자세한 외부 구성요소 및 이미지 자산 주의사항은 [`ASSET_NOTICE.md`](./ASSET_NOTICE.md)를 참고하세요.

## 이미지 자산

`app/src/main/res/drawable/`에 캠퍼스 건물 이미지가 포함되어 있습니다. 현재 저장소 기록만으로 모든 이미지의 원 촬영자·최초 게시처·재배포 조건을 확정할 수 없어, 이미지 파일에는 루트 MIT License가 자동으로 적용된다고 간주하지 않습니다.

향후 출처를 확인할 수 있다면 `ASSET_NOTICE.md`에 이미지별 출처를 추가하면 됩니다.

## Git / Android Studio 파일

`.gitignore`는 Android Studio의 `.idea/`, Gradle/build 산출물, 로컬 SDK 설정, 키스토어 등 새로 생성되는 로컬 파일이 추가되지 않도록 정리했습니다.

과거 커밋에 이미 추적된 IDE 파일은 프로젝트 기록 보존을 위해 이번 정리에서 강제로 삭제하지 않았습니다.

## License

AIN108이 작성한 프로젝트 소스 코드는 루트 [`LICENSE`](./LICENSE)의 MIT License를 따릅니다. 외부 라이브러리 및 출처가 별도로 확인되어야 하는 이미지 자산은 각자의 이용 조건을 따릅니다.

## 개발자

- GitHub: [@AIN108](https://github.com/AIN108)
