# ShootingTest
## Description
GunsCore 플러그인의 무기를 기반으로 한 사격장 플러그인입니다.

### Use Paper Version:
1.18.2
### Tested Paper Version:
1.18.2
### Required Plugin:
- GunsCore
- Citizens
- WorldGuard
- Multiverse-Core (Optional)
## Install Guide
1. 최신 버전의 플러그인 파일을 다운로드합니다.
2. 다운로드한 *.jar 파일을 플러그인 디렉토리에 저장합니다.
## Feature

### 사격장 지역 기능
본 사격장 플러그인은 'ShootingTest' 라는 이름에 걸맞게 사격장과 관련된 기능들을 제공하고 있습니다.  
사격장을 이용하기에 앞서, 여러 사격장 기능을 이용하기 위해서는 사격장 지역을 설정하여야만 합니다.  
사격장 지역 설정 방법은 다음과 같습니다.:
1. [ /st wand ] 명령어를 입력하여, 지역 설정에 필요한 좌표 1, 2를 선택하는데 필요한 아이템을 인벤토리에 지급받습니다.
2. 사격장으로 이용하고자 하는 지역을 아이템 좌클릭, 우클릭을 통해 설정합니다.
3. 두 개의 좌표를 선택한 후, [ /st region create {name} ] 명령어를 입력하여 사격장 지역을 새로 생성합니다.  

사격장 지역 제거는 [ /st region remove {name} ] 명령어를 통해 쉽게 제거할 수 있습니다.

사격장은 개수 제한 없이, 여러 개의 사격장을 생성할 수 있습니다.  
여러 개의 사격장을 생성하였을 경우에 사격장 목록을 확인하고 싶을 경우, 
[ /st region see list ] 명령어를 입력하여 서버에 생성된 사격장 지역 목록을 확인할 수 있습니다.  
또한, 사격장의 좌표 정보를 알고 싶을 경우, [ /st region see positions {name} ] 명령어를 통해 확인할 수 있습니다.

### 훈련용 타겟(Dummy) 기능
훈련용 타겟을 대상으로 GunsCore 플러그인에서 제공하는 무기의 성능을 직접 체험해보실 수 있습니다.  
또한, 훈련용 타겟의 체력과 리스폰 시간을 'config.yml' 파일에서 커스텀할 수 있습니다.  
타겟을 생성 및 제거 하는 방법은 아래와 같습니다.  
- /st dummy create {name}: {name} 이름을 가진 훈련용 타겟을 생성합니다.
- /st dummy remove {name}: {name} 이름을 가진 훈련용 타겟을 제거합니다.

훈련용 타겟 생성 및 제거 외 다른 기능들:
- /st dummy list: 훈련용 타겟 목록을 확인합니다.
- /st dummy tp {name}: {name} 이름을 가진 훈련용 타겟에게로 텔레포트합니다."

### 무기 선택 기능
플레이어가 사격장 지역 내로 입장하면, 사격장 전용 인벤토리로 업데이트 됩니다.  
사격장 전용 인벤토리에는 '사격장 메뉴'라는 아이템이 있으며, 해당 아이템을 통해 무기를 선택하여 사용해보실 수 있습니다.

### 플러그인 리로드 기능
[ /st reload ] 명령어를 사용하여 플러그인을 리로드할 수 있습니다.


## Commands
```yaml
commands:
  st:
    permission: st.manage
```
## Permissions
```yaml
permissions:
  st.manage:
    default: op
```