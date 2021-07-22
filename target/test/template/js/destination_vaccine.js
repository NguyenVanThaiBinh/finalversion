// Create
function createNewDestination() {
    const nameDestination = document.getElementById("nameDestination").value;
    const wareHouseVaccine = document.getElementById("wareHouseVaccine").value;
    let wareHouseVaccineData = {
        id: wareHouseVaccine
    }
    let destinationData = {
        nameDestination: nameDestination,
        warehouseVaccine: wareHouseVaccineData
    };
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "post",
        data: JSON.stringify(destinationData),
        url: "/admin/destination/create",
        success(result) {
            console.log(result.nameDestination);
            swal.fire(
                "Thành công!",
                "Tạo địa điểm thành công!",
                "success"
            )
        },
        error() {
            swal.fire(
                "Cancelled",
                "Lỗi đã xảy ra :(",
                "error"
            )
        }
    })
}


//Delete

    function getDelete(value) {

        Swal.fire({
            title: 'Bạn chắc chắn muốn xoá?',
            text: "Nội dung xoá sẽ mất vĩnh viễn",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Vâng, tôi muốn xoá',
            cancelButtonText: 'Không, cám ơn'
        }).then(function (result) {
            if (result.isConfirmed) {

                $.ajax({
                    type: "post",
                    url: "/admin/destination/delete/" + value,
                    data: {'id': value},
                    success() {
                        swal.fire(
                            "Thành công!",
                            "Một địa điểm đã được xoá!",
                            "success"
                        )

                        $("#" + value + "-row").remove();
                    },
                    error() {
                        swal.fire(
                            "Cancelled",
                            "Lỗi đã xảy ra :(",
                            "error"
                        )
                    }
                });
            }

        })
    }
