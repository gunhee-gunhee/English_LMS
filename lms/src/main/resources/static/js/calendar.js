
// ==========================
// calendar　
// ==========================
let selectedDate = null;
let selectedTime = null;
flatpickr("#calendar", {
    locale: "ja",
    dateFormat: "Y-m-d",
    inline: true,
    onChange: function(selectedDates, dateStr) {
        selectedDate = dateStr;
        document.getElementById('selectedDate').value = dateStr;
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // 時間選択ボタン：単一選択
    document.querySelectorAll('.time-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.time-btn').forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            selectedTime = this.innerText;
            document.getElementById('selectedTime').value = selectedTime;
        });
    });

    // 申請ボタンをクリックすると confirm
    const applyBtn = document.getElementById("applyBtn");
    const applyForm = document.getElementById("applyForm");

    applyBtn.addEventListener('click', function() {
        if (!document.getElementById('selectedDate').value) {
            alert("日付を選択してください。");
            return;
        }
        if (!document.getElementById('selectedTime').value) {
            alert("時間を選択してください.");
            return;
        }
        if (confirm("申請しますか？")) {
            applyForm.submit();
			window.location.href = '/student/thanks';
        }
    });
});