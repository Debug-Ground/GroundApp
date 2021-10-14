# 👨‍🦱 Development People
|Developer|Tech Stack|
|------|---|
|Kim Minsu|Android
# ⭐ Getting Started

앱 실행하는 방법입니다.

1. 최신 안드로이드 스튜디오를 설치해줍니다.
2.  터미널에서 아래 코드를 사용하여 서버 파일을 다운받아줍니다.
   ```bash
   git clone https://github.com/Tamjiat/tamjiat_web.git 
   ```

3.  카카오 API 키를 입력합니다.
   ```bash
   manifests/AndroidManifest.xml
    * android:scheme="_" /> <!-- 카카오 번호 -->
    
   GlobalApplication.kt 
   KakaoSdk.init(this,"_"); //카카오 SDK 초기화(네이티브 앱 키)
   ```
   
4. 카카오디벨로퍼 설정을 합니다

5. 빌드합니다.
   
   
