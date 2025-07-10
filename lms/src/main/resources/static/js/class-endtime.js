//開始の時・分を選択すると、終了の時・分が自動的に20分後に設定される 

$(function() {
    $('#startHour, #startMinute').on('change' , function() {
       // <select>のvalueは文字列（String）なので、数値に変換が必要
       // parseInt(..., 10) を使って10進数として変換する

        const hour = parseInt($('#startHour').val(), 10);
        const minute = parseInt($('#startMinute').val(), 10);

        // 両方選ばれている時だけ動く
        if(!isNaN(hour) && !isNaN(minute)) {
            //20分後　計算
            let newMinute = minute + 20;
            let newHour = hour;
            if(newMinute >= 60 ){
                newHour += Math.floor(newMinute / 60);
                newMinute = newMinute % 60;
            }
            // 23時50分以降は23時50分までしか選べない
            if(newHour > 23){
                newHour = 23;
                newMinute =50;
            }

            // 値をセットする
            $('#endHour').val(newHour);
            $('#endMinute').val(newMinute);
        }
    })
})
