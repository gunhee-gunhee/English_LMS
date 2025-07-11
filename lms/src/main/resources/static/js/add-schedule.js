let scheduleCount = 1;
document.getElementById('addScheduleBtn').onclick = function() {
  const box = document.createElement('div');
  box.className = "card mb-3 schedule-item";
  box.innerHTML = `
    <div class="card-body">
      <div class="form-group">
        <label>授業曜日</label>
        <div class="btn-group btn-group-toggle d-flex flex-wrap">
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="日" autocomplete="off">日
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="月" autocomplete="off">月
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="火" autocomplete="off">火
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="水" autocomplete="off">水
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="木" autocomplete="off">木
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="金" autocomplete="off">金
          </label>
          <label class="btn btn-outline-primary m-1">
            <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="土" autocomplete="off">土
          </label>
        </div>
      </div>
      <div class="form-group">
        <label>授業時間</label>
        <div class="d-flex align-items-center flex-wrap">
          <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].startHour" required>
            <option value="">--</option>
            ${[...Array(24).keys()].map(h => `<option value="${h}">${h.toString().padStart(2,"0")}</option>`).join("")}
          </select>時
          <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].startMinute" required>
            <option value="">--</option>
            ${[0,10,20,30,40,50].map(m => `<option value="${m}">${m === 0 ? '00' : m}</option>`).join("")}
          </select>分
          ~
          <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].endHour" required>
            <option value="">--</option>
            ${[...Array(24).keys()].map(h => `<option value="${h}">${h.toString().padStart(2,"0")}</option>`).join("")}
          </select>時
          <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].endMinute" required>
            <option value="">--</option>
            ${[0,10,20,30,40,50].map(m => `<option value="${m}">${m === 0 ? '00' : m}</option>`).join("")}
          </select>分
        </div>
      </div>
      <button type="button" class="btn btn-sm btn-danger removeScheduleBtn">削除</button>
    </div>
  `;
  document.getElementById('scheduleBoxList').appendChild(box);
  scheduleCount++;

  // 削除ボタンの機能
  box.querySelector('.removeScheduleBtn').onclick = function() {
    box.remove();
  };
  
  //曜日toggle機能
  
  // 新しく追加されたスケジュールボックス（box）の中にある曜日ボタンの<label>要素をすべて取得する。
  // Bootstrapのトグル機能は<label>に「active」クラスを付けて動作するため、<label>を対象にする。
  const labels = box.querySelectorAll('.btn-group-toggle label');
  
  // 各<label>ボタンに対して、クリックイベントを1つずつ手動で設定する
  labels.forEach(label => {
	
	// 各<label>の中にある<input type="checkbox">要素を取得する。
	// 実際の選択状態（チェック済みかどうか）は、このcheckboxの「checked」プロパティで判断する。
	const checkbox = label.querySelector('input[type="checkbox"]');
	
	// checkboxがすでにチェックされている場合は、<label>に「active」クラスをあらかじめ追加する。
	 // なぜなら、Bootstrapはこの「active」クラスがあるとボタンを青く表示してくれるから！
	if(checkbox.checked){
		// <label>がクリックされたときの動作を定義するイベントリスナーを追加する
		label.classList.add('active');
	}
	
	 label.addEventListener('click', function (event) {
		  event.preventDefault();
	      checkbox.checked = !checkbox.checked;
	      label.classList.toggle('active');
	    });
	  });
	};
