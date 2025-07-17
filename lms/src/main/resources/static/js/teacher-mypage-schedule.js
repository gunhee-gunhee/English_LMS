// 日付を「yyyy-mm-dd」形式にフォーマット（Date → 文字列）
function formatDateObjToString(dateObj) {
    return [
        dateObj.getFullYear(),
        ('0' + (dateObj.getMonth() + 1)).slice(-2),
        ('0' + dateObj.getDate()).slice(-2)
    ].join('-');
}

// 「yyyy-mm-dd」形式からDateオブジェクトに変換
function parseDate(str) {
    const [year, month, day] = str.split('-').map(Number);
    return new Date(year, month - 1, day);
}

$(document).ready(function() {
    // 今日の日付で初期化
    const today = new Date();
    let selectedDate = formatDateObjToString(today);
    $('#selected-date-label').text(selectedDate);

    // flatpickr：非表示inputにカレンダーを適用（ポップアップ型）
    const calendar = flatpickr("#calendar-date", {
        defaultDate: selectedDate,
        dateFormat: "Y-m-d",
        clickOpens: false,
        allowInput: false,
        position: "below",
        onChange: function(selectedDates, dateStr) {
            if (dateStr) {
                selectedDate = dateStr;
                $('#selected-date-label').text(selectedDate);
                fetchAndRender(selectedDate);
            }
            calendar.close();
        }
    });

    // 日付部分（span）をクリックでカレンダーを表示
    $('#selected-date-label').on('click', function() {
        calendar.open();
    });

    // ←→ボタンで1日ずつ正確に移動
    $('#prev-day').on('click', function() {
        let d = parseDate(selectedDate);
        d.setDate(d.getDate() - 1);
        selectedDate = formatDateObjToString(d);
        $('#selected-date-label').text(selectedDate);
        calendar.setDate(selectedDate, true);
        fetchAndRender(selectedDate);
    });
    $('#next-day').on('click', function() {
        let d = parseDate(selectedDate);
        d.setDate(d.getDate() + 1);
        selectedDate = formatDateObjToString(d);
        $('#selected-date-label').text(selectedDate);
        calendar.setDate(selectedDate, true);
        fetchAndRender(selectedDate);
    });

    // ページ初期表示時
    fetchAndRender(selectedDate);

    // テーブルにスケジュールデータを描画する本体ロジック
	function fetchAndRender(date) {
	    fetch(`/api/teacher-mypage/${teacherNum}/schedule?date=${date}`)
	        .then(res => res.json())
	        .then(list => {
	            const tbody = $('#schedule-table tbody');
	            tbody.empty();
	            if (!list || list.length === 0) {
	                tbody.append('<tr><td colspan="6" class="text-center no-lesson">No classes</td></tr>');
	                return;
	            }
				list.forEach(row => {
					let studentLabel =
					    `<a href="/student/detail/${row.studentNum}" class="fw-bold text-primary text-decoration-underline">
					        ${row.studentName}${row.studentNameJp ? ` (${row.studentNameJp})` : ""}
					    </a>`;
				    let detailBtn = `<a href="/teacher/day-class/${row.dayClassNum}" class="btn btn-small btn-comment">Detail</a>`;
				    let classTypeLabel = "";
				    if (row.classType === "free") {
				        classTypeLabel = `<span class="no-lesson">Free class</span>`;
				    } else if (row.classType === "additional") {
				        classTypeLabel = `<span class="no-lesson">Additional class</span>`;
				    } else {
				        classTypeLabel = `<span class="no-lesson">Regular class</span>`;
				    }
				    let absentLabel = "";
				    if (row.absent) {
				        const now = new Date();
				        let startDate = new Date(date + "T" + row.startTime);
				        let absentType = startDate < now ? "Absent confirmed" : "Planned absence";
				        absentLabel = `<span class="btn btn-small ${absentType === "Absent confirmed" ? "btn-absent" : "btn-absent-cancel"}">${absentType}</span>`;
				    }
				    let zoomBtn = row.zoomLink
				        ? `<a href="${row.zoomLink}" target="_blank" class="btn btn-small btn-join">Join</a>`
				        : `<button class="btn btn-small btn-join" disabled>Join</button>`;

				    $('#schedule-table tbody').append(`
				        <tr>
				            <td>${row.startTime} ~ ${row.endTime}</td>
				            <td>${studentLabel}</td>
				            <td>${detailBtn}</td>
				            <td>${classTypeLabel}</td>
				            <td>${absentLabel}</td>
				            <td>${zoomBtn}</td>
				        </tr>
				    `);
				});

	        });
	}

});
