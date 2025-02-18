function startTimer() {
    let timer = 10; // 3분
    let timerInterval = setInterval(displayTime, 1000);

    function displayTime() {
        if (timer < 0) {
            alert('time is up!');
            timer = 0;
            clearInterval(timerInterval); // 타이머 취소(setInterval로 호출한 값 취소)
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