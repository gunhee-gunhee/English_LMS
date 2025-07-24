$(document).ready(function() {

	console.log('customtables.js loaded!');

	// 既存の DataTable を一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#dataTable')) {
		$('#dataTable').DataTable().destroy();
	}

	let table = $('.datatable').DataTable({
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
		$('.datatable').DataTable().columns.adjust();
	});


	setTimeout(function() {
		$('.datatable').css('visibility', 'visible');
	}, 50);

	table.order([1, 'asc']).draw();

	table.on('draw', function() {
		$('#dataTable th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');

		console.log('draw! current order:', table.order());
	});
	console.log('Current order:', table.order());
});






