window.teacherTableRegular = null;
window.teacherTableIrregular = null;

$(document).ready(function() {

	console.log('customtables.js loaded!');
	//定期授業： 既存の講師リストを一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#teacherTableRegular')) {
		$('#teacherTableRegular').DataTable().destroy();
	}
	window.teacherTableRegular = $('#teacherTableRegular').DataTable({
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
		],
		drawCallback: function(settings) {
				        // 가짜 테이블에 id 추가!
				        // 기존 id가 있다면 중복 방지로 삭제 먼저
				        $('#teacherTableRegularClone').removeAttr('id');
				        $('#teacherTableRegular').closest('.dataTables_wrapper')
						.find('table.dataTable')
						    .not('#teacherTableRegular') // 진짜 테이블 제외
						    .attr('id', 'teacherTableRegularClone');
				    }
	});
	
	
	//  非定期授業： 既存の講師リストを一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#teacherTableIrregular')) {
		$('#teacherTableIrregular').DataTable().destroy();
	}
	window.teacherTableIrregular = $('#teacherTableIrregular').DataTable({
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
		],
		drawCallback: function(settings) {
		        // 가짜 테이블에 id 추가!
		        // 기존 id가 있다면 중복 방지로 삭제 먼저
		        $('#teacherTableIrregularClone').removeAttr('id');
		        $('#teacherTableIrregular').closest('.dataTables_wrapper')
				.find('table.dataTable')
				    .not('#teacherTableIrregular') // 진짜 테이블 제외
				    .attr('id', 'teacherTableIrregularClone');
		    }
	});
	// 定期授業：　既存の学生リストを一度破棄してから再初期化する。
	if ($.fn.DataTable.isDataTable('#studentTableRegular')) {
		$('#studentTableRegular').DataTable().destroy();
	}
	let studentTableRegular = $('#studentTableRegular').DataTable({
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
	
	// 非定期授業：　既存の学生リストを一度破棄してから再初期化する。
		if ($.fn.DataTable.isDataTable('#studentTableIrregular')) {
			$('#studentTableIrregular').DataTable().destroy();
		}
		let studentTableIrregular = $('#studentTableIrregular').DataTable({
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
		teacherTableRegular.columns.adjust();
		teacherTableIrregular.columns.adjust();
		studentTableRegular.columns.adjust();
		studentTableIrregular.columns.adjust();
	});


	setTimeout(function() {
		$('#teacherTableRegular,#teacherTableIrregular, #studentTableIrregular, #studentTableRegular').css('visibility', 'visible');
	}, 50);

	teacherTableRegular.order([1, 'asc']).draw();
	teacherTableIrregular.order([1, 'asc']).draw();
	studentTableRegular.order([1, 'asc']).draw();
	studentTableIrregular.order([1, 'asc']).draw();

	teacherTableRegular.on('draw', function() {
		$('#teacherTableRegular th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! teacherTableRegular current order:', teacherTableRegular.order());
	});

	teacherTableIrregular.on('draw', function() {
		$('#teacherTableIrregular th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! teacherTableIrregular current order:', teacherTableIrregular.order());
	});
	
	studentTableRegular.on('draw', function() {
		$('#studentTableRegular th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! studentTableRegular current order:', studentTableRegular.order());
	});
	
	studentTableIrregular.on('draw', function() {
		$('#studentTableIrregular th:first-child')
			.removeClass('sorting sorting_asc sorting_desc')
			.addClass('sorting_disabled')
			.removeAttr('aria-label')
			.removeAttr('aria-sort');
		console.log('draw! studentTableIrregular current order:', studentTableIrregular.order());
	});
});




