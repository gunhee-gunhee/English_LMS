$(document).ready(function() {
  $('.datatable').each(function() {
    $(this).DataTable({
      order: [[1, 'asc']],
      initComplete: function() {
        console.log(this.api().table().node().id + ' INIT!');
      }
    });
  });
});
