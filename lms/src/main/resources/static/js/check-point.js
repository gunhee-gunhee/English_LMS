/*
 生徒を選択し、授業タイプ（補講または無料体験）をクリックした際に、対応するポイントがない場合はアラートを表示
*/

document.querySelectorAll('input[name="classType"]').forEach(radio => {
    radio.addEventListener('change', function() {
        // 1. 選択された学生
        const selectedStudentRadio = document.querySelector('.ListCheckbox:checked');
        if (!selectedStudentRadio) {
            alert('生徒を選択してください。');
            return;
        }

        // 2. 学生の <tr> をさがす。
        const tr = selectedStudentRadio.closest('tr');

        // 3. 補講・無料体験のポイントを抽出
        const additionalPoint = parseInt(tr.dataset.additional);
		console.log("additionalPoint" , additionalPoint);
        const freePoint = parseInt(tr.dataset.free);
		console.log("freePoint" , freePoint);

        // 4. 授業タイプごとにポイントを確認
        const classType = this.value; // "補講" or "無料体験"
        if (classType === 'additional' && additionalPoint === 0) {
            alert('選択された学生には補講用のポイントがありません。');
        } else if (classType === 'free' && freePoint === 0) {
            alert('選択された学生には無料体験用のポイントがありません。');
        }
    });
});
