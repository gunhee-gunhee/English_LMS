document.querySelectorAll('.fetch-teachers-btn').forEach(function(btn) {

	btn.addEventListener('click', function() {

		const parentForm = btn.closest('form');
		console.log('[debug] parentForm:', parentForm);
		//　定期/非定期
		let type;
		if (parentForm.id === 'regularClassForm') {
			type = 'regular';
		} else if (parentForm.id === 'irregularClassForm') {
			type = 'irregular';
		}


		let params = new URLSearchParams();

		// タイプごとにパラメータを設定
		if (type === 'regular') {
			// ---- 定期授業用のパラメータ ----


			// 曜日チェックボックスの値を取得
			// 1. チェックされた曜日チェックボックス（NodeList）をすべて取得
			const checkedWeekDayNodes = parentForm.querySelectorAll('input[name="weekDays"]:checked');

			// 2. NodeListを通常の配列に変換（配列メソッドを使えるように！）
			const checkedWeekDayArray = Array.from(checkedWeekDayNodes);

			// 3. 各チェックボックスのvalueだけを取り出して曜日配列を作成
			const weekDays = checkedWeekDayArray.map(dayBtn => dayBtn.value);

			// 時間の値を取得
			// 入力された時・分を2桁に整形し、"HH:mm" 形式の文字列を作成
			//開始時間
			const startHour = parentForm.querySelector('#startHour').value.padStart(2, '0');
			const startMinute = parentForm.querySelector('#startMinute').value.padStart(2, '0');
			//終了時間
			const endHour = parentForm.querySelector('#endHour').value.padStart(2, '0');
			const endMinute = parentForm.querySelector('#endMinute').value.padStart(2, '0');

			const startTime = `${startHour}:${startMinute}`;
			const endTime = `${endHour}:${endMinute}`;

			const startDate = parentForm.querySelector('#startDate').value;

			//paramsに曜日を追加
			// weekDays 配列（選択された曜日）をループしながら、
			// 各曜日（day）を 'weekDays' パラメータとして params に追加する
			// ※ day は weekDays 配列の各要素（例："月", "水", "金"）を指す
			weekDays.forEach(day => params.append('weekDays', day));

			//paramsに授業時間を追加
			params.append('startTime', startTime);
			params.append('endTime', endTime);
			params.append('startDate', startDate);

		} else if (type === 'irregular') {
			const selectedDate = parentForm.querySelector('#selectedDate').value;
			const selectedTime = parentForm.querySelector('#selectedTime').value;
			params.append('date', selectedDate);
			params.append('time', selectedTime);
		}

		// 共通のfetch URLにリクエスト送信！
		// params をクエリ文字列に変換してサーバーに GET リクエストを送信
		console.log('[debug] params string:', params.toString());
		fetch('/admin/available-teacher-list?' + params.toString())
			// レスポンスデータを JSON に変換
			.then(response => response.json())
			// 取得したデータでテーブルを更新
			.then(data => {
				//let dt = window.teacherTableRegular;
				console.log('講師のデータ:', data);

				let tableElem = parentForm.querySelector('.available-teacherlist');
				if (!tableElem) {
					console.log('テーブルを見つかりません。');
					return;
				}

				let tableId = tableElem.id;
				console.log('tableId　:', tableId); // ex) "teacherTableRegular" or "teacherTableIrregular"


				let dt;
				if (tableId === 'teacherTableRegularClone') {
					dt = window.teacherTableRegular;
				} else if (tableId === 'teacherTableIrregularClone') {
					dt = window.teacherTableIrregular;
				} else {
					alert('知らないテーブル!');
					return;
				}

				dt.clear();  // 既存のテーブル内容をクリア

				// データが存在しない場合、メッセージを表示して終了
				if (data.length === 0) {
					dt.row.add([
						'', '', '利用可能な講師がいません。', ''
					]).draw();
				} else {
					// APIから取得したデータを1件ずつ DataTable に追加する
					data.forEach(teacher => {
						// 各列にラジオボタン、ID、ニックネーム、zoomId（未設定なら空欄）を挿入
						dt.row.add([
							`<input type="radio" name="teacherNum" value="${teacher.teacherNum}" class="ListCheckbox" required>`,
							teacher.id,
							teacher.nickname,
							teacher.zoomId ?? ''
						]);
					});
					dt.draw();
				}
			})
			.catch(() => {
				dt.clear();
				// エラー時は「講師取得失敗」のメッセージを表示
				dt.row.add(['', '', '講師照会の失敗', '']).draw();
			});

	});
});
