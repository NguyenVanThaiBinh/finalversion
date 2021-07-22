$(document).ready(function (){
    $('#search').on('input',function (event){
        let searchCMND = $('#search').val();
        let page = $('#page').val();
        let date = $('#dateUrl').val().trim();
        if(date==""){
            let datenow = new Date().toISOString().split('T')[0].split("-");
            date = datenow[2]+"-"+datenow[1]+"-"+datenow[0];
        }
        $.ajax({
            type:'GET',
            url:'/doctor/api/'+date+'/'+searchCMND,
            success:successHandler(date,searchCMND)
        })
        event.preventDefault();
    })
})
function getUser(employee,page) {
    let a = page;
    let check = $('#check').val();
    let checkTime = $('#checkTime').val();
    if(check==0 && checkTime==1){
        if(employee.checkStatus==0){
            return `<tr>
                <th>${employee.id}</th>
                <td>${employee.userName}</td>
                <td>${employee.phoneNumber}</td>
                <td>${employee.email}</td>
                <td>${employee.cmnd}</td>
                <td>${employee.dateVaccine}</td>
                <td>${employee.timeVaccine}</td>
                <td class="button"><button class="btn btn-success" onclick="check(${employee.id},${a})">Check</button></td>
            </tr>
        `;
        }
        else{
            return `<tr>
                <th>${employee.id}</th>
                <td>${employee.userName}</td>
                <td>${employee.phoneNumber}</td>
                <td>${employee.email}</td>
                <td>${employee.cmnd}</td>
                <td>${employee.dateVaccine}</td>
                <td>${employee.timeVaccine}</td>
                <td class="button"><button class="btn btn-danger" onclick="uncheck(${employee.id},${a})">Uncheck</button></td>
            </tr>
        `;
        }
    }
    else{
        if(employee.checkStatus==0){
            return `<tr>
                <th>${employee.id}</th>
                <td>${employee.userName}</td>
                <td>${employee.phoneNumber}</td>
                <td>${employee.email}</td>
                <td>${employee.cmnd}</td>
                <td>${employee.dateVaccine}</td>
                <td>${employee.timeVaccine}</td>
                <td class="button"><button class="btn btn-success" disabled>Check</button></td>
            </tr>
        `;
        }
        else{
            return `<tr>
                <th>${employee.id}</th>
                <td>${employee.userName}</td>
                <td>${employee.phoneNumber}</td>
                <td>${employee.email}</td>
                <td>${employee.cmnd}</td>
                <td>${employee.dateVaccine}</td>
                <td>${employee.timeVaccine}</td>
                <td class="button"><button class="btn btn-danger" disabled>Uncheck</button></td>
            </tr>
        `;
        }
    }
}
function successHandler(date,searchCMND) {
    let url = "/doctor/api/"+date+'/'+searchCMND;
    if(searchCMND==""){
        url='/doctor/list/'+date;
        $('.active').toggleClass('active ""');
        $('span').first().addClass("active");
    }
    $.ajax({
        type: "GET",
        //tên API
        url: url,
        //xử lý khi thành công
        success: function (data) {
            // hien thi danh sach o day
            let content = '    <tr>\n' +
                '        <td>#</td>\n' +
                '        <td>FullName</td>\n' +
                '        <td>Phone</td>\n' +
                '        <td>Email</td>\n' +
                '        <td>CMND</td>\n' +
                '        <td>Date</td>\n' +
                '        <td>Time</td>\n' +
                '    </tr>';
            for (let i = 0; i < data.content.length; i++) {
                content += getUser(data.content[i]);
            }
            if(searchCMND==""){
                $('#page').show();
            }else{
                $('#page').hide();
            }
            document.getElementById('orderItems').innerHTML = content;
        }
    });
}
function check(id,page){
    let search = $('#search').val();
    $.ajax({
        type:'GET',
        url:'/doctor/check/'+id,
        success:function success(id){
            if(search==""){
                $.ajax({
                    type: "GET",
                    //tên API
                    url:'/doctor/list?page='+page,
                    //xử lý khi thành công
                    success: function (data) {
                        console.log(data.number);
                        // data = data.toArray();
                        // hien thi danh sach o day
                        let content = '    <tr>\n' +
                            '        <td>#</td>\n' +
                            '        <td>FullName</td>\n' +
                            '        <td>Phone</td>\n' +
                            '        <td>Email</td>\n' +
                            '        <td>CMND</td>\n' +
                            '        <td>Date</td>\n' +
                            '        <td>Time</td>\n' +
                            '    </tr>';
                        for (let i = 0; i < data.content.length; i++) {
                            content += getUser(data.content[i],page);
                        }
                        document.getElementById('orderItems').innerHTML = content;
                    }
                });
            }
            else{
                url = "/doctor/api/"+search;
                $.ajax({
                    type: "GET",
                    //tên API
                    url:url,
                    //xử lý khi thành công
                    success: function (data) {
                        // hien thi danh sach o day
                        let content = '    <tr>\n' +
                            '        <td>#</td>\n' +
                            '        <td>FullName</td>\n' +
                            '        <td>Phone</td>\n' +
                            '        <td>Email</td>\n' +
                            '        <td>CMND</td>\n' +
                            '        <td>Date</td>\n' +
                            '        <td>Time</td>\n' +
                            '    </tr>';
                        for (let i = 0; i < data.content.length; i++) {
                            content += getUser(data.content[i],page);
                        }
                        document.getElementById('orderItems').innerHTML = content;
                    }
                });
            }
        }
    })
}
function uncheck(id,page){
    let search = $('#search').val();
    $.ajax({
        type:'GET',
        url:'/doctor/unchecked/'+id,
        success:function success(id){
            if(search==""){
                $.ajax({
                    type: "GET",
                    //tên API
                    url:'/doctor/list?page='+page,
                    //xử lý khi thành công
                    success: function (data) {
                        console.log(data.number);
                        // data = data.toArray();
                        // hien thi danh sach o day
                        let content = '    <tr>\n' +
                            '        <td>#</td>\n' +
                            '        <td>FullName</td>\n' +
                            '        <td>Phone</td>\n' +
                            '        <td>Email</td>\n' +
                            '        <td>CMND</td>\n' +
                            '        <td>Date</td>\n' +
                            '        <td>Time</td>\n' +
                            '    </tr>';
                        for (let i = 0; i < data.content.length; i++) {
                            content += getUser(data.content[i],page);
                        }
                        document.getElementById('orderItems').innerHTML = content;
                    }
                });
            }
            else{
                url = "/doctor/api/"+search;
                $.ajax({
                    type: "GET",
                    //tên API
                    url:url,
                    //xử lý khi thành công
                    success: function (data) {
                        // hien thi danh sach o day
                        let content = '    <tr>\n' +
                            '        <td>#</td>\n' +
                            '        <td>FullName</td>\n' +
                            '        <td>Phone</td>\n' +
                            '        <td>Email</td>\n' +
                            '        <td>CMND</td>\n' +
                            '        <td>Date</td>\n' +
                            '        <td>Time</td>\n' +
                            '    </tr>';
                        for (let i = 0; i < data.content.length; i++) {
                            content += getUser(data.content[i],page);
                        }
                        document.getElementById('orderItems').innerHTML = content;
                    }
                });
            }
        }
    })
}
$(document).ready(function (){
    $('#date').on('input',function (){
        let date1 = $('#date').val()
        let arr = date1.toString().split("-");
        let date = arr[2]+"-"+arr[1]+"-"+arr[0];
        let datenow = new Date().toISOString().split('T')[0];
        if(date1 == datenow){
            window.location.href="/doctor";
        }
        else{
            window.location.href="/doctor/"+date;
        }
    })
})