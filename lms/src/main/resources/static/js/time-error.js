
function validateAndConfirm() {
    // 에러 메시지 숨기기
    const errorDiv = document.getElementById('timeError');
    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const startHour = parseInt(document.getElementById('startHour').value);
    const startMinute = parseInt(document.getElementById('startMinute').value);
    const endHour = parseInt(document.getElementById('endHour').value);
    const endMinute = parseInt(document.getElementById('endMinute').value);

    const startTime = startHour * 60 + startMinute;
    const endTime = endHour * 60 + endMinute;

    if (endTime <= startTime) {
        errorDiv.textContent = "終了時間は開始時間より後にしてください。";
        errorDiv.style.display = 'block';
		
		errorDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
		
        return false;
    }

    return confirm('会員登録しますか？');
}

