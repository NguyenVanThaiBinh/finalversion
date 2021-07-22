function editHopital() {
    let id = $('#cId').val();
    let name = $('#cName').val();
    let cmnd = $('#cCMND').val();
    let pass = $('#cPass').val();
    // let doctor = $('#cDoctor').val();
    let newDoctor = {
        userName: name,
        cmnd: cmnd,
        password: pass,
        // checkDoctor:doctor
    }
    console.log(newDoctor)
    if (id == 0) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            data: JSON.stringify(newDoctor),
            url: "/admin/createDoctor",
            success: function (doctor) {
                $('#doctorList tbody').prepend(' <tr id="row' + doctor.id + '">\n' +
                    '      <td>' + doctor.id + '</td>\n' +
                    '      <td>' + doctor.userName + '</td>\n' +
                    '      <td>' + doctor.cmnd + '</td>\n' +
                    // '      <td>' + doctor + '</td>\n' +
                    '      <td><button onclick="loadEditData(' + doctor.id + ')" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Edit</button>' +
                    '<input type="hidden" id="id" value="' + doctor.id + '"></td>\n' +
                    ' <td><button onclick="deleteDoctor(' + doctor.id + ')" class="btn btn-danger" >Delete</button>    </td>' +
                    ' </tr>');
                //sư kiện nào thực hiện Ajax
                $('.close-modal').click();
                Swal.fire({
                    icon: 'success',
                    title: 'Your work has been saved',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    } else {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "PUT",
            data: JSON.stringify(newDoctor),
            url: '/admin/edit-D/' + id,
            success: function (doctor) {
                $('#row' + id + ' td').remove();
                $('#row' + id).html(`
                        <td>${doctor.id}</td>
                        <td>${doctor.userName}</td>
                        <td>${doctor.cmnd}</td>
                       <td><button onclick="loadEditData(${doctor.id})" type="button" class="btn btn-primary edit" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Edit</i></a></button><input type="hidden" id="id" value="' + customer.id + '"></td>
                      <td><button class="btn btn-danger" onclick="deleteDoctor(${doctor.id})">Delete</button></td>`);
                $('.close-modal').click();
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'You have changed successfull',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        });
    }
}
function loadEditData(id) {
    console.log(id)
    $.ajax({
        type: "GET",
        url: "/admin/apiID/" + id,
        success: function (doctor) {
            $('#exampleModalLabelSpan').text("Chỉnh sửa thông tin");
            $('#cId').val(doctor.id);
            $('#cName').val(doctor.userName);
            $('#cCMND').val(doctor.cmnd);
            $('#cPass').val("")

        }
    })
}
function loadAddNew() {
    $('#exampleModalLabelSpan').text("Tạo tài khoản cho điểm tiêm chủng");
    $('#cId').val(0);
    $('#cName').val("");
    $('#cCMND').val("");
    $('#cPass').val("");

}
function deleteDoctor(id) {
    let a = '#row' + id;
    Swal.fire({
        title: 'Bạn chắc chắn muốn xoá?',
        text: "Nội dung xoá sẽ mất vĩnh viễn",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Vâng, tôi muốn xoá',
        cancelButtonText: 'Không, cám ơn'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "DELETE",
                url: '/admin/deleteDoctor/' + id,
                //xử lý khi thành công
                success: function (data) {
                    $(a).remove();
                    swal.fire(
                        "Thành công!",
                        "Một kho đã được xoá!",
                        "success"
                    )
                }
            });
            //chặn sự kiện mặc định của thẻ
        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swal.fire(
                "Cancelled",
                "Lỗi đã xảy ra :(",
                "error"
            )
        }
    })
}