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


	const form = document.querySelector('form');
	    form.addEventListener('submit', function(e) {
	        const pw = document.getElementById('teacherPassword').value;
	        const pwCheck = document.getElementById('teacherPasswordCheck').value;
			if (teacherDTO.getPassword() != null && !teacherDTO.getPassword().isEmpty()) {
			    if (!teacherDTO.getPassword().equals(teacherDTO.getPasswordCheck())) {
			        log.warn("[講師更新] パスワードが一致しません (ID: {})", teacher.getTeacherId());
			        throw new IllegalArgumentException("パスワードが一致しません。");
			    }
			    log.info("[講師更新] パスワードを更新します (ID: {})", teacher.getTeacherId());
			    teacher.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
			}
	    });
});