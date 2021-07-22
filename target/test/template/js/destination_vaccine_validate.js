$(() => {
    $("#destination_validate").validate({
        onkeyup: function (element) {
            $(element).valid()
        },
        errorElement: 'div',
        onclick: false,
        rules: {
            userName: {
                required: true,
                minlength: 5,
                maxlength: 50,
                validateName: true,
            }
        },
        messages: {
            userName: {
                required: "Bắt buộc nhập tên đầy đủ",
                minlength: "Hãy nhập tối thiểu 5 ký tự",
                maxlength: "Hãy nhập tối đa 50 ký tự"
            }
        },
        submitHandler: function () {
            createNewDestination();
            $("#createModel").modal('hide');

        }
    });
    $.validator.addMethod("validateName", function (str, element) {
        str = str.trim();
        str = str.toLowerCase();
        str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
        str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
        str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
        str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
        str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
        str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
        str = str.replace(/đ/g, "d");
        let str2 = Array.from(str.split(" "));
        let nguyenam = ['a', 'e', 'u', 'i', 'o', 'y'];
        let phuamDau = ['b', 'c', 'ch', 'd', 'g', 'gh', 'gi', 'h', 'k', 'kh', 'l', 'm', 'n', 'nh', 'ng', 'ngh', 'ph', 'q', 'u', 'r', 's', 't', 'th', 'tr', 'v', 'x'];
        let phuamcuoi = ['p', 't', 'c', 'ch', 'm', 'n', 'ng', 'nh']
        for (let i = 0; i < str2.length; i++) {
            let checkNguyenam = 0;
            let checkAmcuoi = 0;
            let checkAmDau = 0;
            if (str2[i].length > 1) {
                let strSmall = Array.from(str2[i]);
                for (let j = 0; j < strSmall.length; j++) {
                    for (let k = 0; k < nguyenam.length; k++) {
                        if (strSmall[j] == nguyenam[k]) {
                            checkNguyenam++;
                        }
                        for (let h = 0; h < phuamcuoi.length; h++) {
                            if ((strSmall[strSmall.length - 1] == phuamcuoi[h] && strSmall[strSmall.length - 2] == nguyenam[k]) ||
                                (strSmall[strSmall.length - 1] == nguyenam[k] && checkNguyenam == 1) || (strSmall[strSmall.length - 2] + strSmall[strSmall.length - 1] == phuamcuoi[h] && strSmall[strSmall.length - 3] == nguyenam[k])) {
                                checkAmcuoi++;
                            }
                        }
                        for (let m = 0; m < phuamDau.length; m++) {
                            if ((strSmall[0] == phuamDau[m] && strSmall[1] == nguyenam[k]) || strSmall[0] == nguyenam[k] || (strSmall[0] + strSmall[1] == phuamDau[m] && strSmall[2] == nguyenam[k])) {
                                checkAmDau++;
                            }
                        }
                    }
                }
                if (checkNguyenam == 0 || checkAmcuoi == 0 || checkAmDau == 0) {
                    return this.optional(element);
                }
            }
        }
        return /^[^\-\!\[\]\{\}\"\'\>\<\%\^\*\?\/\\\|\,\;\:\+\=\(\)\@\$\&\!\.\#\_0-9]*$/g.test(str);
    }, "Hãy nhập tên hợp lệ!!!");
});



// $(() => {
//     $("#set_password").validate({
//         onkeyup: function (element) {
//             $(element).valid()
//         },
//         errorElement: 'div',
//         onclick: false,
//             rules: {
//                 password: {
//                     required: true,
//                     equalTo: "#userPass2",
//                     validatePassword: true,
//                     minlength: 5
//                 },
//                 userPass2: {
//
//                 },
//
//             },
//             messages: {
//
//                 password: {
//                     required: "Bắt buộc nhập mật khẩu",
//                     equalTo: "Hai mật khẩu phải giống nhau",
//                     minlength: "Hãy nhập ít nhất 5 ký tự"
//                 },
//             },
//     });
//
//         $.validator.addMethod("validatePassword", function (value, element) {
//             return this.optional(element) || /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$/i.test(value);
//         }, "Mật khẩu quá đơn giản!");
//
// });

