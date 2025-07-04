document.addEventListener('DOMContentLoaded', function () {
	    const dayInputs = document.querySelectorAll("input[name='days']");

	    dayInputs.forEach(input => {
	      const label = document.querySelector("label[for='" + input.id + "']");

	      if (input.checked) {
	        label.classList.add('active');
	      }

	      label.addEventListener('click', function (e) {
	        e.preventDefault();
	        input.checked = !input.checked;
	        label.classList.toggle('active', input.checked);
	      });
	    });
	  });