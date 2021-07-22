$(document).ready(function() {
    const monthNames = ["1", "2", "3", "4", "5", "6",
        "7", "8", "9", "10", "11", "12"
    ];
    let yobYears = 100;
    let selectYear= $("#year");
    let selectMonth = $("#month");
    let selectDay = $("#day");
    let currentYear = new Date().getFullYear();
    for (var y = 0; y < yobYears; y++) {
        let date = new Date(currentYear);
        let yearElem = document.createElement("option");
        yearElem.value = currentYear;
        yearElem.textContent = currentYear;
        selectYear.append(yearElem);
        currentYear--;
    }
    for (var m = 0; m < 12; m++) {
        let month = monthNames[m];
        let monthElem = document.createElement("option");
        monthElem.value = m;
        monthElem.textContent = month;
        selectMonth.append(monthElem);
    }
    var d = new Date();
    var month = d.getMonth();
    var year = d.getFullYear();
    var day = d.getDate();
    selectYear.val(year);
    selectYear.on("change", changeYear);
    selectMonth.val(month);
    selectMonth.on("change", changeMonth);
    // selectDay.on('change',function(){
    // 		let dayOC = this.value;
    // })
    AdjustDays();
    selectDay.val(day)
    function AdjustDays() {
        var year = selectYear.val();
        var month = parseInt(selectMonth.val()) + 1;
        selectDay.empty();
        //get the last day, so the number of days in that month
        var days = new Date(year, month,0).getDate();
        for (var d = 1; d <= days; d++) {
            var dayElem = document.createElement("option");
            dayElem.value = d;
            dayElem.textContent = d;
            selectDay.append(dayElem);
        }
    }
    function changeMonth() {
        let daytest = selectDay.val();
        let year = selectYear.val();
        let month = parseInt(selectMonth.val()) + 1;
        //
        //get the last day, so the number of days in that month
        let days = new Date(year, month,0).getDate();
        if(daytest>days){
            selectDay.empty();
            for (let d = 1; d <= days; d++) {
                let dayElem = document.createElement("option");
                dayElem.value = d;
                dayElem.textContent = d;
                selectDay.append(dayElem);
            }
        }
        else{
            selectDay.empty();
            for (let d = 1; d <= days; d++) {
                let dayElem = document.createElement("option");
                dayElem.value = d;
                dayElem.textContent = d;
                selectDay.append(dayElem);
            }
            selectDay.val(daytest);
        }
    }
    function changeYear(){
        let daytest = selectDay.val();
        let year = selectYear.val();
        let month = parseInt(selectMonth.val()) + 1;
        //
        //get the last day, so the number of days in that month
        let days = new Date(year, month,0).getDate();
        if(year%4==0 && year%100!=0){
            selectDay.empty();
            for (let d = 1; d <= days; d++) {
                let dayElem = document.createElement("option");
                dayElem.value = d;
                dayElem.textContent = d;
                selectDay.append(dayElem);
            }
            selectDay.val(daytest);
        }
        else{
            if(daytest>days){
                selectDay.empty();
                for (let d = 1; d <= days; d++) {
                    let dayElem = document.createElement("option");
                    dayElem.value = d;
                    dayElem.textContent = d;
                    selectDay.append(dayElem);
                }
            }
            else{
                selectDay.empty();
                for (let d = 1; d <= days; d++) {
                    let dayElem = document.createElement("option");
                    dayElem.value = d;
                    dayElem.textContent = d;
                    selectDay.append(dayElem);
                }
                selectDay.val(daytest);
            }
        }
    }
})
document.addEventListener("DOMContentLoaded",function() {
    var province = document.getElementById('province');
    window.onload = function(){
        $.ajax({
            url:'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
            headers:{
                'token':'6c596e03-d052-11eb-86d6-d6ee83481554',
                'Content-Type':'application/json'
            },
            method:'GET',
            dataType:'json',
            success:function(reponse){
                // console.log('success: ');
                // console.log(reponse.data);
                reponse.data[41].ProvinceName = "TT-Huế"
                var str="<option selected class='provinceId' data-province='"+reponse.data[41].ProvinceID+"'>"+
                    reponse.data[41].ProvinceName+"</option>"
                province.innerHTML = str;
                changeFunc();
            }
        })
    }
},true)
function changeFunc(){
    var selectBox = document.getElementById("province");
    var selectedValue = selectBox.options[selectBox.selectedIndex].getAttribute('data-province');
    var district = document.getElementById("district");
    $.ajax({
        url:'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district',
        headers:{
            'token':'6c596e03-d052-11eb-86d6-d6ee83481554',
            'Content-Type':'application/json'
        },
        method:'GET',
        dataType:'json',
        success:function(reponse){
            var str = "<option selected>Quận huyện</option>";
            for(var i=0;i<reponse.data.length;i++){
                if(reponse.data[i].ProvinceID==selectedValue){
                    str=str+"<option class='districtId' data-district='"+reponse.data[i].DistrictID+"'>"+
                        reponse.data[i].DistrictName+"</option>"
                }
            }
            district.innerHTML = str;
        }
    })
}
function changeFuncDistrict(){
    var selectBox = document.getElementById("district");
    var selectedValue = selectBox.options[selectBox.selectedIndex].getAttribute('data-district');
    var ward = document.getElementById("ward");
    $.ajax({
        url:'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id='+selectedValue,
        headers:{
            'token':'6c596e03-d052-11eb-86d6-d6ee83481554',
            'Content-Type':'application/json'
        },
        method:'GET',
        dataType:'json',
        success:function(reponse){
            var str = "<option selected>Phường xã</option>";
            for(var i=0;i<reponse.data.length;i++){
                str=str+"<option class='wardId' data-ward='"+reponse.data[i].WardCode+"'>"+
                    reponse.data[i].WardName+"</option>"
            }
            ward.innerHTML = str;
        }
    })
}