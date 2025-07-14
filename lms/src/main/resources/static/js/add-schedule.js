
// ==========================
// スケジュール追加ボタン
// ==========================
let scheduleCount = document.querySelectorAll('.schedule-item').length || 0;


document.getElementById('addScheduleBtn').onclick = function() {
  // 新しいスケジュール行を生成
  const box = document.createElement('div');
  box.className = "card mb-3 schedule-item";
  box.innerHTML = `
    <div class="card-body">
      <div class="form-group">
        <label>授業曜日</label>
        <div class="btn-group btn-group-toggle d-flex flex-wrap" data-toggle="buttons">
          ${['日','月','火','水','木','金','土'].map(day => `
            <label class="btn btn-outline-primary m-1">
              <input type="checkbox" name="schedules[${scheduleCount}].weekdays" value="${day}" autocomplete="off">${day}
            </label>
          `).join('')}
        </div>
      </div>
      <div class="form-group d-flex flex-wrap align-items-center">
        <label class="mr-2">開始</label>
        <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].startHour" required>
          <option value="">--</option>
          ${[...Array(24).keys()].map(h => `<option value="${h}">${h.toString().padStart(2,"0")}</option>`).join("")}
        </select>時
        <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].startMinute" required>
          <option value="">--</option>
          ${[0,10,20,30,40,50].map(m => `<option value="${m}">${m === 0 ? '00' : m}</option>`).join("")}
        </select>分
        <span class="mx-2">〜</span>
        <label class="mr-2">終了</label>
        <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].endHour" required>
          <option value="">--</option>
          ${[...Array(24).keys()].map(h => `<option value="${h}">${h.toString().padStart(2,"0")}</option>`).join("")}
        </select>時
        <select class="form-control mx-1" style="width: 80px;" name="schedules[${scheduleCount}].endMinute" required>
          <option value="">--</option>
          ${[0,10,20,30,40,50].map(m => `<option value="${m}">${m === 0 ? '00' : m}</option>`).join("")}
        </select>分
        <button type="button" class="btn btn-sm btn-danger removeScheduleBtn d-block w-100 mt-2">スケジュール削除</button>
      </div>
    </div>
  `;
  document.getElementById('scheduleBoxList').appendChild(box);
  scheduleCount++;
  applyScheduleRowFeatures(); // 新規追加行にも機能を適用
};

// ==========================
// 全スケジュール行に削除・トグル機能付与
// ==========================
function applyScheduleRowFeatures() {
  // 1. 曜日トグル（Bootstrapボタン風）の設定
  document.querySelectorAll('.schedule-item .btn-group-toggle label').forEach(function(label) {
    const checkbox = label.querySelector('input[type="checkbox"]');
    if (checkbox) {
      // 最初からcheckedならactiveクラス付与
      label.classList.toggle('active', checkbox.checked);
      // クリックでトグル
      label.onclick = function(event) {
        event.preventDefault();
        checkbox.checked = !checkbox.checked;
        label.classList.toggle('active');
      };
    }
  });

  // 2. 削除ボタン（rowごと削除）
  document.querySelectorAll('.schedule-item .removeScheduleBtn').forEach(function(btn) {
    btn.onclick = function() {
      btn.closest('.schedule-item').remove();
      reindexScheduleRows(); // インデックス再採番
    };
  });
}

// ==========================
// スケジュールインデックスを再採番
// ==========================
function reindexScheduleRows() {
  document.querySelectorAll('#scheduleBoxList .schedule-item').forEach(function(row, idx) {
    // 曜日
    row.querySelectorAll('input[type="checkbox"][name*="weekdays"]').forEach(function(chk) {
      chk.name = `schedules[${idx}].weekdays`;
    });
    // 開始・終了時刻
    row.querySelectorAll('select[name*="startHour"]').forEach(function(sel) {
      sel.name = `schedules[${idx}].startHour`;
    });
    row.querySelectorAll('select[name*="startMinute"]').forEach(function(sel) {
      sel.name = `schedules[${idx}].startMinute`;
    });
    row.querySelectorAll('select[name*="endHour"]').forEach(function(sel) {
      sel.name = `schedules[${idx}].endHour`;
    });
    row.querySelectorAll('select[name*="endMinute"]').forEach(function(sel) {
      sel.name = `schedules[${idx}].endMinute`;
    });
  });
  scheduleCount = document.querySelectorAll('#scheduleBoxList .schedule-item').length;
}

// ==========================
// フォーム送信前のバリデーション
// ==========================
document.addEventListener('DOMContentLoaded', function() {
  applyScheduleRowFeatures(); // 初期（既存）行にも機能を適用

  // フォーム送信時、空のrow除去＋再インデックス
  document.querySelector('form').addEventListener('submit', function(e) {
    document.querySelectorAll('#scheduleBoxList .schedule-item').forEach(function(row) {
      const hasDay = row.querySelectorAll('input[type="checkbox"]:checked').length > 0;
      const startH = row.querySelector('select[name*="startHour"]').value;
      const startM = row.querySelector('select[name*="startMinute"]').value;
      const endH = row.querySelector('select[name*="endHour"]').value;
      const endM = row.querySelector('select[name*="endMinute"]').value;
      if (!hasDay || !startH || !startM || !endH || !endM) {
        row.remove();
      }
    });
    reindexScheduleRows();
  });
});
