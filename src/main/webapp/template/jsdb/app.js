
class App {
    static BASE_URL_CUSTOMER = "http://localhost:8089/api/customers";
    static BASE_URL_PROVINCE = "http://localhost:8089/api/provinces";

    static showDeleteConfirmDialog() {
        return Swal.fire({
            icon: 'warning',
            text: 'Are you sure you want to delete the selected data ?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it !',
            cancelButtonText: 'Cancel',
        })
    }

    static showSuccessAlert(t) {
        Swal.fire({
            icon: 'success',
            title: t,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500
        })
    }

    static showErrorAlert(t) {
        Swal.fire({
            icon: 'error',
            title: 'Warning',
            text: t,
        })
    }
}

class Customer {
    constructor(id, fullName, email, phone, province) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.province = province;
    }
}
