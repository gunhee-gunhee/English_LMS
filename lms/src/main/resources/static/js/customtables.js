$(document).ready(function () {
    // 既存の DataTable を一度破棄してから再初期化する。
    $('#dataTable').DataTable().destroy();

    $('#dataTable').DataTable({
        paging: false,                       // ページネーションを有効にする
        searching: false,                    // テーブル内の検索機能を有効にする
        lengthChange: true,                // 表示件数の選択メニューを非表示にする
        info: false,                        // "Showing 1 to X..."などの情報を非表示にする
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

    table.on('draw', function () {
    $('#dataTable th:first-child')
        .removeClass('sorting sorting_asc sorting_desc')
        .addClass('sorting_disabled')
        .removeAttr('aria-label')  // 보조기능도 꺼버리자
        .removeAttr('aria-sort');
});

});






