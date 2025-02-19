function startTimer() {
    let timer = 10; // 3분
    let timerInterval = setInterval(displayTime, 1000);

    function displayTime() {
        if (timer < 0) {
            timer = 0;
            clearInterval(timerInterval); // 타이머 취소(setInterval로 호출한 값 취소)

            $('.authArea').empty();
            $.ajax({
                url : '/member/invalidAuthCode',
                type : 'post', // 이진 데이터를 보낼 때는 post
                dataType : 'json', // 수신받을 데이터 타입
                async: false,
                success : function(data) {
                    console.log(data);
                    if (data.code == '200' && $('#emailAuth').val() != 'true') {
                        $('.modal-body').html(
                            '인증코드가 만료되었습니다. 다시 인증을 해주세요');
                        $('#myModal').show();
                        $('#email').val('');
                        $('#email').focus();
                    } 
                },
                error : function(err) {
                    // ResponseEntity객체의 HttpStatus(통신상태)를 받아 에러가 발생하면 아래의 함수가 호출됨
                    console.log(err.responseJSON);
                },
            });

        } else {
            // 타이머가 흐르도록...
            let min = Math.floor(timer / 60);
            let sec = String(timer % 60).padStart(2, '0');
            let remainTime = min + ':' + sec;
            $('.timer').html(remainTime);
            --timer;
        }
    }
}