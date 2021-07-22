function deleteWareHouse(id){
    let a = '#row'+id;
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
                url: '/admin/delete-warehouse/'+id,
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
function editWareHouse() {
    let id = $('#kId').val();
    let name = $('#kName').val();
    let address = $('#kAddress').val();
    let amount = $('#kAmount').val();
    let newWareHouse = {
        warehouseName: name,
        warehouseAddress: address,
        amountVaccine: amount,
    }
    if (id==0){
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            data: JSON.stringify(newWareHouse),
            url: "/admin/create-W",
            success: function (houses) {
                $('#wareHouseList tbody').prepend(' <tr id="row' + houses.id + '">\n' +
                    '      <td>' + houses.id + '</td>\n' +
                    '      <td>' + houses.warehouseName + '</td>\n' +
                    '      <td>' + houses.warehouseAddress + '</td>\n' +
                    '      <td>' + houses.amountVaccine + '</td>\n' +
                    '      <td><button onclick="loadEditData('+houses.id+')" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Edit</button>' +
                    '<input type="hidden" id="id" value="' + houses.id + '"></td>\n' +
                    ' <td><button onclick="deleteWareHouse('+houses.id+')" class="btn btn-outline-danger" ><i class="fas fa-trash-alt"></i>Delete</button>    </td>' +
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
        });
    }else{
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "PUT",
            data: JSON.stringify(newWareHouse),
            url: '/admin/editW/' + id,
            success: function (house) {
                console.log(house);
                $('#row' + id + ' td').remove();
                $('#row' + id).html(`
                        <td>${house.id}</td>
                        <td>${house.warehouseName}</td>
                        <td>${house.warehouseAddress}</td>
                        <td>${house.amountVaccine}</td>
                       <td><button onclick="loadEditData(${house.id})" type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Edit</button>
                       <input type="hidden" id="id" value="' + customer.id + '"></td>
                      <td><button onclick="deleteWareHouse(${house.id})" class="btn btn-outline-danger" ><i class="fas fa-trash-alt"></i>Delete</button></td>`);
                $('.close-modal').click();
                Swal.fire({
                    icon: 'success',
                    title: 'You have changed successfull',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        });
    }
}
function loadEditData(id){
    $.ajax({
        type: 'GET',
        url: '/admin/apiIdW/' + id,
        success: function (house) {
            $('#exampleModalLabelSpan').text("Chỉnh sửa kho vaccine");
            $('#kId').val(house.id);
            $('#kName').val(house.warehouseName);
            $('#kAddress').val(house.warehouseAddress);
            $('#kAmount').val(house.amountVaccine);
        }
    })
}
function loadAddnew() {
    $('#exampleModalLabelSpan').text("Tạo thêm kho Vaccine");
    $('#kId').val(0);
    $('#kName').val("");
    $('#kAddress').val("");
    $('#kAmount').val("");
}
function  createWareHouse(){
    let name = $('#kName').val();
    let address = $('#kAddress').val();
    let amount = $('#kAmount').val();
    let newWareHouse = {
        warehouseName: name,
        warehouseAddress: address,
        amountVaccine: amount,
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(newWareHouse),
        url: "/admin/create-W",
        success: function (house) {
            $('#wareHouseList tbody').prepend(' <tr id="row' + house.id + '">\n' +
                '      <td>' + house.id + '</td>\n' +
                '      <td>' + house.warehouseName + '</td>\n' +
                '      <td>' + house.warehouseAddress + '</td>\n' +
                '      <td>' + house.amountVaccine + '</td>\n' +
                '      <td><button type="button" class="btn btn-primary edit" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Edit</button>' +
                '<input type="hidden" id="id" value="' + house.id + '"></td>\n' +
                ' <td><button class="btn btn-outline-danger" ><i class="fas fa-trash-alt"></i>Delete</button></td>' +
                ' </tr>');
            //sư kiện nào thực hiện Ajax
            $('.close-modal').click();
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Your work has been saved',
                showConfirmButton: false,
                timer: 1500
            })
        }
    });
}