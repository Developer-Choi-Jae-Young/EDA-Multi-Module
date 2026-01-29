### Phase 1. Structural Evolution

#### 🌿 v1 - 단일 프로젝트 + 레이어드 구조 (Layered Architecture)
가장 표준적인 **Controller -> Service -> Repository -> DB** 흐름을 가진 구조입니다.

* **특징**:
    * Spring Boot의 가장 기본적인 3-Tier Architecture를 따릅니다.
    * 구조가 직관적이며 초기 개발 속도가 매우 빠르고 구현이 간편합니다.
* **한계점 및 학습 포인트**:
    * **강한 결합도(Tight Coupling)**: 서비스 레이어 내에서 여러 도메인 객체 간의 의존성이 강하게 형성됩니다.
    * **유지보수 저하**: 특정 코드 변경이나 에러 발생 시 의존하고 있는 다른 로직에 사이드 이펙트(Side Effect)가 전파될 가능성이 높습니다.
    * **확장성 부족**: 프로젝트 규모가 커질수록 서비스 클래스가 비대해지는 'Fat Service' 현상이 발생하기 쉽습니다.
