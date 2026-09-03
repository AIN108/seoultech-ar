# Asset and Third-Party Notice

이 저장소의 애플리케이션 소스 코드는 루트 `LICENSE`의 MIT License를 따릅니다. 다만 외부 라이브러리와 이미지 자산은 각각 별도의 권리와 라이선스가 적용될 수 있습니다.

## 외부 라이브러리

현재 `app/build.gradle.kts`에 포함된 주요 외부 구성요소는 다음과 같습니다.

- AndroidX / AppCompat / Lifecycle / ConstraintLayout
- Android CameraX
- Google Material Components
- Google ML Kit Text Recognition
- JUnit / AndroidX Test / Espresso

각 라이브러리는 해당 프로젝트의 라이선스 및 배포 조건을 따릅니다. 루트 MIT License가 이들 외부 라이브러리의 라이선스를 대체하지 않습니다.

## 캠퍼스 건물 이미지

`app/src/main/res/drawable/`에는 다음과 같은 서울과학기술대학교 캠퍼스 건물 이미지가 포함되어 있습니다.

- `building_1.jpg`
- `building_2.png`
- `building_3.jpg`
- `building_7.*`
- `building_34.jpg`
- `building_39.jpg`
- `building_51.jpg`
- `building_60.jpg`
- `building_62.*`

현재 저장소 기록만으로는 각 이미지의 원 촬영자, 최초 게시 위치 또는 재배포 조건을 모두 확정할 수 없습니다. 따라서 이러한 이미지 파일은 루트 MIT License가 자동으로 적용되는 코드 자산으로 간주하지 않습니다.

향후 출처를 확인할 수 있다면 이미지별로 원 촬영자/출처 URL/사용 조건을 이 문서에 추가하는 것이 좋습니다. 직접 촬영한 이미지라면 그 사실을 명시하면 됩니다.

## Android 기본 템플릿 자산

앱 아이콘, 테마, 기본 테스트 파일 및 일부 XML 리소스에는 Android Studio 프로젝트 템플릿에서 생성된 기본 구성요소가 포함될 수 있습니다. 해당 구성요소는 Android/AndroidX 프로젝트의 관련 이용 조건을 따릅니다.
