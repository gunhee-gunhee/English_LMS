$(document).ready(function() {

	console.log('customtables.js loaded!');

	// 既存の講師リストを一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#teacherTable')) {
		$('#teacherTable').DataTable().destroy();
	}
	let teacherTable = $('#teacherTable').DataTable({
		paging: false,                       // ページネーションを有効にする
		searching: false,                    // テーブル内の検索機能を有効にする
		lengthChange: true,                // 表示件数の選択メニューを非表示にする
		info: false,                        // "Showing 1 to X..."などの情報を非表示にする  
		scrollY: "320px",
		scrollCollapse: true,
		language: {
			paginate: {
				previous: "<",              // 「前へ」ボタン
				next: ">"                   // 「次へ」ボタン
			}
		},
		columnDefs: [
			{ orderable: false, targets: 0 } // 0番目の列（チェックボックス）は並び替えを無効にする
		]
	});
	// 既存の学生リストを一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#studentTable')) {
		$('#studentTable').DataTable().destroy();
	}
	let studentTable = $('#studentTable').DataTable({
		paging: false,                       // ページネーションを有効にする
		searching: false,                    // テーブル内の検索機能を有効にする
		lengthChange: true,                // 表示件数の選択メニューを非表示にする
		info: false,                        // "Showing 1 to X..."などの情報を非表示にする  
		scrollY: "320px",
		scrollCollapse: true,
		language: {
			paginate: {
				previous: "<",              // 「前へ」ボタン
				next: ">"                   // 「次へ」ボタン
			}
		},
		columnDefs: [
			{ orderable: false, targets: 0 } // 0番目の列（チェックボックス）は並び替えを無効にする
		]
	});

	$('#regularClass, #irregularClass').on('shown.bs.collapse', function() {
		teacherTable.columns.adjust();
		studentTable.columns.adjust();
	});


	setTimeout(function() {
		$('#teacherTable, #studentTable').css('visibility', 'visible');
	}, 50);

	teacherTable.order([1, 'asc']).draw();
	studentTable.order([1, 'asc']).draw();

	teacherTable.on('draw', function() {
		$('#teacherTable th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! teacherTable current order:', teacherTable.order());
	});

	studentTable.on('draw', function() {
		$('#studentTable th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! studentTable current order:', studentTable.order());
	});
});




