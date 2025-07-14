document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('search-btn').addEventListener('click', function() {
        const date = document.getElementById('date-input').value;
        const teacherNum = 1; // ログイン教師の番号に動的に変更

        fetch(`/api/teacher-mypage/${teacherNum}/schedule?date=${date}`)
            .then(res => res.json())
            .then(list => {
                const tbody = document.querySelector('#schedule-table tbody');
                tbody.innerHTML = '';
                if (!list || list.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="7" class="text-center">授業がありません</td></tr>';
                    return;
                }
                list.forEach(row => {
                    tbody.innerHTML += `
                        <tr>
                            <td>${row.startTime} ~ ${row.endTime}</td>
                            <td>${row.studentName}${row.studentNameJp ? '（'+row.studentNameJp+'）' : ''}</td>
                            <td>${row.className || ''}</td>
                            <td>${row.classType || ''}</td>
                            <td>${row.attendance ? '○' : ''}</td>
                            <td>${row.absent || ''}</td>
                            <td><button class="btn btn-sm btn-info">詳細</button></td>
                        </tr>
                    `;
                });
            });
    });
});
