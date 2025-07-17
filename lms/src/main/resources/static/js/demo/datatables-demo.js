// Call the dataTables jQuery plugin
$(document).ready(function() {
  let dt = $('#dataTable').DataTable({
	order: [[1, 'asc']],
	initComplete: function() {
	    console.log('DataTable INIT!');
	    $('#dataTable thead th').each(function(idx, th) {
	      console.log('th', idx, th.className);
	    });
	  }
  })
  
  
});
