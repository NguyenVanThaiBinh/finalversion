function deleteDestination(id) {
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
                url: '/admin/deleteDestination/' + id,
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
function editDestination() {
    let id = $('#cId').val();
    let name = $('#cName').val();
    let warehouseVaccine = {"id": $('#cWarehouse').val(),"warehouseName":$("#cWarehouse option:selected").text()};
    let newDestination = {
        nameDestination: name,
        warehouseVaccine: warehouseVaccine,
    }
    console.log(newDestination)
    if (id==0){
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            data: JSON.stringify(newDestination),
            url: "/admin/create-DS",
            success: function (destination) {
                $('#destinationList tbody').prepend(
                    ' <tr id="row'+ destination.id+'">\n' +
                    '      <td>' + destination.id + '</td>\n' +
                    '      <td>' + destination.nameDestination + '</td>\n' +
                    '      <td>' + destination.warehouseVaccine.warehouseName + '</td>\n' +
                    '      <td><button onclick="loadEditData('+ destination.id +')" type="button" class="btn btn-primary edit" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Sửa</i></a></button>' +
                    '<input type="hidden" id="id" value="' + destination.id + '"></td>\n' +
                    ' <td><button class="btn btn-danger" onclick="deleteDestination('+ destination.id+')">' +
                    'Xóa </button></td>' +
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
    }else{
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "PUT",
            data: JSON.stringify(newDestination),
            url: '/admin/editDs/' + id,
            success: function (destination) {
                console.log(destination);
                $('#row' + id + ' td').remove();
                $('#row' + id).html(`
                        <td>${destination.id}</td>
                        <td>${destination.nameDestination}</td>
                        <td>${destination.warehouseVaccine.warehouseName}</td>
                       <td><button onclick="loadEditData(${destination.id})" type="button" class="btn btn-primary edit" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Sửa</i></a></button><input type="hidden" id="id" value="' + customer.id + '"></td>
                      <td><button class="btn btn-danger" onclick="deleteDestination(${destination.id})">Xóa</button></td>`);
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
function loadEditData(id){
    $.ajax({
        type: 'GET',
        url: '/admin/apiDs/' + id,
        success: function (destination) {
            $('#exampleModalLabelSpan').text("sửa thông tin địa điểm tiêm chủng");
            $('#cId').val(destination.id);
            $('#cName').val(destination.nameDestination);
            $('#cWarehouse').val(destination.warehouseVaccine.id);
        }
    })
    //event.preventDefault();
}
function loadAddnew() {
    $('#exampleModalLabelSpan').text("Tạo thêm Điểm tiêm chủng");
    $('#cId').val(0);
    $('#cName').val("");
    $('#cWarehouse').val("--Chọn Kho Vaccine--");
}
function createWareHouse() {
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