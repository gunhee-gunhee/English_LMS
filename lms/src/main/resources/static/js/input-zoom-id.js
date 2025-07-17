/*
	管理者用の講師登録フォームで、zoomId を選択しなかった場合にエラーメッセージを表示する。
*/



function validateZoomId() {
	const zoomErrorDiv = document.getElementById('zoomIdError');
	zoomErrorDiv.style.display = 'none';
	zoomErrorDiv.textContent = '';

	const zoomId = document.getElementById('inputZoomId').value;
	if (!zoomId || zoomId.trim() === "") {
		zoomErrorDiv.textContent = "Zoomアカウントを選択してください。";
		zoomErrorDiv.style.display = 'block';
		zoomErrorDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
		return false;
	}
	return true;
}

function validateWeekday() {
	const errorDiv = document.getElementById('weekdayError');
	
	let isValid = true; 
	
	document.querySelectorAll('#scheduleBoxList .schedule-item').forEach(function(item) {
		const checkboxes = item.querySelectorAll('input[type="checkbox"][name*=".weekdays"]');
		//some() : 配列の中に「条件OK」が1つでもあれば true
		const isChecked = Array.from(checkboxes).some(box => box.checked);
		
		if(!isChecked){
			isValid = false;
		}
	});
	
	if(!isValid){
		errorDiv.textContent = "各スケジュールで、少なくとも1つの曜日を選んでください。";
		errorDiv.style.display = 'block';
		errorDiv.scrollIntoView({behavior:'smooth', block: 'start'});
	}else{
		errorDiv.textContent = '';
		errorDiv.style.display= 'none';
	}
  
		return isValid;
    }
	
	function registerConfirm() {
	    
	    if (!validateZoomId() || !validateWeekday() || !validateTimeError()) {
	   
	        return false;
	    }
	  
	    return confirm('会員登録しますか？');
	}
   
   
