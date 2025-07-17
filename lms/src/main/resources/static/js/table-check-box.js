/*listのチェックボックス機能*/

document.addEventListener("DOMContentLoaded", function() {
    
    //thのchekbox
    const allCheckbox = document.getElementById("selectAllCheckbox");
    
    //tdのchekboxes
    const checkboxes = document.querySelectorAll(".ListCheckbox");

    // allCheckbox がチェックされたら、すべてのチェックボックスが選択される。
    allCheckbox.addEventListener("change", function(){
        checkboxes.forEach(function(box) {
            box.checked = allCheckbox.checked;
        })
    })

    // 個別のチェックを外すと、allCheckbox も自動的に解除される。
    checkboxes.forEach(function(box){
    box.addEventListener("change",function(){
        if(!box.checked){
            allCheckbox.checked = false;
        }
    })     


    })
      
})