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
