document.getElementById('fetch-teachers-btn').addEventListener('click', function() {
	// 曜日チェックボックスの値を取得
	// 1. チェックされた曜日チェックボックス（NodeList）をすべて取得
	const checkedWeekDayNodes = document.querySelectorAll('input[name="weekDays"]:checked');

	// 2. NodeListを通常の配列に変換（配列メソッドを使えるように！）
	const checkedWeekDayArray = Array.from(checkedWeekDayNodes);

	// 3. 各チェックボックスのvalueだけを取り出して曜日配列を作成
	const weekDays = checkedWeekDayArray.map(dayBtn => dayBtn.value);

	// 時間の値を取得
	// 入力された時・分を2桁に整形し、"HH:mm" 形式の文字列を作成
	//開始時間
	const startHour = document.getElementById('startHour').value.padStart(2, '0');
	const startMinute = document.getElementById('startMinute').value.padStart(2, '0');
	//終了時間
	const endHour = document.getElementById('endHour').value.padStart(2, '0');
	const endMinute = document.getElementById('endMinute').value.padStart(2, '0');

	const startTime = `${startHour}:${startMinute}`;
	const endTime = `${endHour}:${endMinute}`;
	
	const startDate = document.getElementById('startDate').value;  


	let params = new URLSearchParams();

	//paramsに曜日を追加
	// weekDays 配列（選択された曜日）をループしながら、
	// 各曜日（day）を 'weekDays' パラメータとして params に追加する
	// ※ day は weekDays 配列の各要素（例："月", "水", "金"）を指す
	weekDays.forEach(day => params.append('weekDays', day));

	//paramsに授業時間を追加
	params.append('startTime', startTime);
	params.append('endTime', endTime);
	params.append('startDate', startDate);

	let table = $('#teacherTable').DataTable();

	// params をクエリ文字列に変換してサーバーに GET リクエストを送信
	fetch('/admin/available-teacher-list?' + params.toString())
		// レスポンスデータを JSON に変換
		.then(response => response.json())
		// 取得したデータでテーブルを更新
		.then(data => {
			console.log('받아온 데이터:', data);
			table.clear();  // 既存のテーブル内容をクリア

			// データが存在しない場合、メッセージを表示して終了
			if (data.length === 0) {
				table.row.add([
					'', '', '利用可能な講師がいません。', ''
				]).draw(); 
			} else {
				// APIから取得したデータを1件ずつ DataTable に追加する
				data.forEach(teacher => {
					// 各列にラジオボタン、ID、ニックネーム、zoomId（未設定なら空欄）を挿入
					table.row.add([
						`<input type="radio" name="teacherNum" value="${teacher.teacherNum}" class="ListCheckbox" required>`,
						teacher.id,
						teacher.nickname,
						teacher.zoomId ?? ''
					]);
				});
				table.draw();
			}
		})
		.catch(() => {
			table.clear();
			// エラー時は「講師取得失敗」のメッセージを表示
			table.row.add(['', '', '講師照会の失敗', '']).draw();
		});


}
)