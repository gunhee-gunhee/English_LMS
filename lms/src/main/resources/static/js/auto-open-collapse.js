/*
フォーム送信後にエラーや成功メッセージを表示するためにページをリダイレクトする際、
送信されたフォームを自動的に開くための処理。
 */

document.addEventListener('DOMContentLoaded', function () {
    const targetDiv = document.getElementById('formOpenTarget');
    if (!targetDiv) return;

    const openForm = targetDiv.dataset.target;
    if (!openForm) return;

    const collapseElement = document.getElementById(openForm + 'Register');
    if (collapseElement) {
        new bootstrap.Collapse(collapseElement, { toggle: true });
    }
});
