/**
 * 授業開始日（inputStartDate）をユーザーが選択すると、
 * 自動的にその月の最終日が終了日（inputEndDate）に入力されるスクリプト
 * 
 * - 日付は「YYYY-MM-DD」形式で自動入力
 * - 開始日が変更されるたびに即時に動作
 * - 例：2025-07-08を選択 → 終了日は2025-07-31に自動設定
 */


window.addEventListener("DOMContentLoaded", function() {
  const startDate = document.getElementById("inputStartDate");
  const endDate = document.getElementById("inputEndDate");

  //// 指定された日付の月末日を返す関数
  function calculateLastDay(dateStr){
	// 選択された授業開始日
	const date = new Date(startDate.value);
	// 翌月の1日
	const nextMonth = new Date(date.getFullYear(), date.getMonth() + 1, 1);
    // 翌月の1日 - 一日 = 該当月の最後の日
	const lastDayOfMonth = new Date(nextMonth - 1);  
	
	// yyyy-MM-dd形式にフォーマットする
	const yyyy = lastDayOfMonth.getFullYear();
	const mm = String(lastDayOfMonth.getMonth() + 1).padStart(2, '0');
	const dd = String(lastDayOfMonth.getDate()).padStart(2, '0');		             
  
	return `${yyyy}-${mm}-${dd}`;
}
  
  
  //初期ロード時に値が入ってたら終了日も自動入力
  if (startDate && endDate) {
 	endDate.value = calculateLastDay(startDate.value);
  }
  
  //授業開始日を変更した時の処理
  startDate.addEventListener("change", function() {
	if(startDate.value) {
		endDate.value = calculateLastDay(startDate.value);
	}
  })
});
