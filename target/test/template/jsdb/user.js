function deleteUser() {
    let page = $('#page').val();
    let userId = $(this).parent().find("#idU").val();
    let a = '#row' + userId;
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    })
    swalWithBootstrapButtons.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "DELETE",
                url: '/admin/' + userId,
                //xử lý khi thành công
                success: function (data) {
                    $.ajax({
                        type:'GET',
                        url:'/admin',
                        success:function (data){
                            if(data.totalElements>10){
                                $.ajax({
                                    type:'GET',
                                    url:'/admin?page='+page,
                                    success:function (data2){
                                        $(a).remove();
                                        swalWithBootstrapButtons.fire(
                                            'Deleted!',
                                            'Your file has been deleted.',
                                            'success'
                                        );
                                        $('#userList').append(getUser(data2.content[data2.content.length-1]));
                                    }
                                })
                            }
                            else{
                                $(a).remove();
                                $('#nav').hide();
                                swalWithBootstrapButtons.fire(
                                    'Deleted!',
                                    'Your file has been deleted.',
                                    'success'
                                );
                            }
                        }
                    })
                    setInfo();
                }
            });
            //chặn sự kiện mặc định của thẻ
        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelled',
                'Your imaginary file is safe :)',
                'error'
            )
        }
    })
    event.preventDefault();
}
function edit(){
    let id = $(this).parent().find("#idU").val();
    $.ajax({
        type: 'GET',
        url: '/admin/apiID/' + id,
        success: function (user) {
            $('#edName').val(user.userName);
            $('#edEmail').val(user.email);
            $('#edCMND').val(user.cmnd);
            $('#edPhone').val(user.phoneNumber);
            $('#edId').val(id);
        }
    })
}
function editUser() {
    let id = $('#edId').val();
    let name = $('#edName').val();
    let cmnd = $('#edCMND').val();
    let phone = $('#edPhone').val();
    let email = $('#edEmail').val();
    let User = {
        userName: name,
        email: email,
        cmnd: cmnd,
        phoneNumber: phone
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "PUT",
        data: JSON.stringify(User),
        url: '/admin/edit/' + id,
        success:successHandler,
    })
}
function successHandler() {
    let search = $('#search').val();
    let page = $('#page').val();
    if(search==""){
        $.ajax({
            type: 'GET',
            url: '/admin/api-doctor?page='+page,
            success: function (data) {
                let content = '<tr class="tr">\n' +
                    '<td>Id</td>\n' +
                    '<td>Tên Khách hàng</td> \n' +
                    '<td>CMND</td> \n' +
                    '<td>Email</td>\n' +
                    '<td>Ngày tiêm chủng\t</td>\n' +
                    '<td>giờ tiêm chủng</td>\n' +
                    '</tr>';
                for (let i = 0; i < data.content.length; i++) {
                    content += getUser(data.content[i]);
                }
                document.getElementById("userList").innerHTML = content;
                $('.close-modal').click();
            }
        })
    }
    else{
        $.ajax({
            type: 'GET',
            url: '/admin/api/'+search,
            success: function (data) {
                let content = '<tr class="tr">\n' +
                    '<td>Id</td>\n' +
                    '<td>Tên Khách hàng</td> \n' +
                    '<td>CMND</td> \n' +
                    '<td>Email</td>\n' +
                    '<td>Ngày tiêm chủng\t</td>\n' +
                    '<td>giờ tiêm chủng</td>\n' +
                    '</tr>';
                for (let i = 0; i < data.content.length; i++) {
                    content += getUser(data.content[i]);
                }
                document.getElementById("userList").innerHTML = content;
                $('.close-modal').click();
            }
        })
    }
}
setInterval(function (){
    let search = $('#search').val();
    let page = $('#page').val();
    if(search==""){
        $.ajax({
            type: 'GET',
            url: '/admin/api-doctor?page='+page,
            success: function (data) {
                let content = '<tr class="tr">\n' +
                    '<td>Id</td>\n' +
                    '<td>Tên </td> \n' +
                    '<td>CMND</td> \n' +
                    '<td>Email</td>\n' +
                    '<td>Ngày tiêm \t</td>\n' +
                    '<td>Giờ tiêm </td>\n' +
                    '</tr>';
                for (let i = 0; i < data.content.length; i++) {
                    content += getUser(data.content[i]);
                }
                document.getElementById("userList").innerHTML = content;
            }
        })
        numberPage();
    }
    else{
        $.ajax({
            type: 'GET',
            url: '/admin/api/'+search,
            success: function (data) {
                let content = '<thead><tr>\n' +
                    '<td>Id</td>\n' +
                    '<td>Tên Khách hàng</td> \n' +
                    '<td>CMND</td> \n' +
                    '<td>Email</td>\n' +
                    '<td>Ngày tiêm chủng\t</td>\n' +
                    '<td>giờ tiêm chủng</td>\n' +
                    '</tr></thead><tbody>';
                for (let i = 0; i < data.content.length; i++) {
                    content += getUser(data.content[i]);
                }
                content+='</tbody>';
                document.getElementById("userList").innerHTML = content;
            }
        })
    }
    setInfo();
},7000);
function numberPage(){
    $.ajax({
        type:'GET',
        url:'/admin/api-doctor',
        success:function (data){
            if(data.totalElements>=11){
                $('#nav').show();
            }
        }
    })
}
function setInfo(){
    let page = $('#page').val();
    $.ajax({
        type:'GET',
        url:'/admin/api-doctor?page='+page,
        success:function (data){
            let from = data.number * data.size +1;
            let to = from + data.content.length-1;
            let total = data.totalElements;
            // alert("Showing "+from+" to "+to+" of "+total+" users");
            $('#infoPage').html("Showing "+from+" to "+to+" of "+total+" users");
        }
    })
}
function getUser(employee) {
    if(employee.dateVaccine==null){
        employee.dateVaccine="";
    }
    if(employee.timeVaccine==null){
        employee.timeVaccine="";
    }
    return `<tr id='row${employee.id}'>
        <td>${employee.id}</td>
        <td>${employee.userName}</td>
        <td>${employee.cmnd}</td>
        <td>${employee.email}</td>
        <td>${employee.dateVaccine}</td>
        <td>${employee.timeVaccine}</td>
       <td>
       <button type="button" class="btn btn-primary edit"
                data-toggle="modal" data-target="#exampleModal"
                data-whatever="@mdo">Edit
        </button>
        <input type="hidden" id="idU" value="${employee.id}">
<!--        <button class="btn btn-outline-danger delete"><i-->
<!--                    class="fas fa-trash-alt"></i>Delete-->
<!--            </button>-->
        </td>
    </tr>
`;
}
function search(){
    let search = $('#search').val();
    $.ajax({
        type:'GET',
        url:'/admin/api/'+search,
        success:successHandler2(search)
    })
    event.preventDefault();
}
function successHandler2(searchCMND) {
    let url = "/admin/api/"+searchCMND;
    if(searchCMND==""){
        url='/admin/api-doctor';
    }
    $.ajax({
        type: "GET",
        //tên API
        url: url,
        //xử lý khi thành công
        success: function (data) {
            // hien thi danh sach o day
            let content = '<tr class="tr">\n' +
                '<td>Id</td>\n' +
                '<td>Tên Khách hàng</td> \n' +
                '<td>CMND</td> \n' +
                '<td>Email</td>\n' +
                '<td>Ngày tiêm chủng\t</td>\n' +
                '<td>giờ tiêm chủng</td>\n' +
                '</tr>';
            for (let i = 0; i < data.content.length; i++) {
                content += getUser(data.content[i]);
            }
            if(searchCMND==""){
                $('#nav').show();
            }
            else{
                $('#nav').hide();
            }
            document.getElementById('userList').innerHTML = content;
        }
    });
}
function test(){
    $('body').on('click','.delete',deleteUser);
    $('body').on('click','.edit',edit);
    $('body').on('input','#search',search);
}
test();
setInfo();