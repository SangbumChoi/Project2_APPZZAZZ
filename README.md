# Project2_APPZZAZZ
Final destination of making application ZZAZZ

## 주석
(필수) 주요 파일에는 주석이 적어도 description 형태로 한가지는 무조건 있어야함
주로 core에서 이루어지는 주석의 형태의 경우

    """
    Arguments:
        input (Tensor): input tensor
        shape List[int]: the new empty tensor shape

    Returns:
        output (Tensor)
        
    Descriptions:
        Blah-Blah
    """

이러한 형태로 input과 ouput의 형태를 살펴보고 description을 확인하여서 전체의 코드의 주요 개요 내용을 알 수 있게끔 해주는 방식으로 진행

(옵션) sub 주석으로는 줄마다 특별한 기능을 수행할 경우 아니면 스스로가 조금 더 잘 기억되기 위해서 정해주는 주석들이 존재한다.

Activity 파일에는 다음과같은 규격이 잘 맞지 않을 수 있다고 생각할 수 있지만 전반적으로 Activity 파일을 위한 function에 대한 파일들이 주를 이룰 것
이기 때문에 현재는 이러한 방식으로 진행한다.

## 앱 째즈 레퍼지토리 규약
    """
    com.example.myappliction
    --/ui
      --/ProjectActivity
      --/TrimActivity
    --/utils
    --/functions
    """

## push/pull
현재 app_ui branch가 master인 상황
모든 최신 업데이트의 경우 app_ui branch에서 merge하는 방식으로 push.
항상 자신의 feature branch의 이름의 경우 app_ui_project_tab 이러한 방식으로 자세하게 서술한다.

이외에 feature development에 있어서 개발이 안되는 사항에 대해서 

## pull request

(필수) Pull Request를 올릴 때 comment로 자신의 코드는 대략적으로 이렇고 build.gradle, implementation에서 새로운 API 기능들을 사용했을 경우 언급
을 해준다. 추가적으로 주요 핵심 코드에 대한 파일명을 언급해줘서 Reviewer입장에서 어떤 파일들을 확인해야하는지 빠르게 찾을 수 있도록 하게 해준다.

이후에 행해지는 액션들의 경우 모두 comment의 룰을 따르게 된다.

추가적으로 생기는 모든 conflict 상황에서 대해서는 CTO와 branch 주인이 fix하는 것을 원칙으로 한다.


## comment
(필수) PR에 대한 코멘트가 있을 경우 상세하게 답변
