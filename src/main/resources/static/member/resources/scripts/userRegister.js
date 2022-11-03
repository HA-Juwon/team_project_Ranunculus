const termsContainer = window.document.getElementById('termsContainer');
const registerForm = window.document.getElementById('registerForm');
const agreeService = window.document.getElementById('agreeService');
const agreePrivacy = window.document.getElementById('agreePrivacy');
const agreeAd = window.document.getElementById('agreeAd');

const functions = {
    termsAllCheck : (params) => {
        if (!agreeService.checked || !agreePrivacy.checked) {
            agreeService.checked = "true";
            agreePrivacy.checked = "true";
            agreeAd.checked = "true";
        } else {
            agreeService.checked = false;
            agreePrivacy.checked = false;
            agreeAd.checked = false;
        }
    },
    termsCancelButton : (params) => {
        if (window.confirm('정말 회원가입을 취소 하시겠어요?\n일부 서비스 이용이 불가 할 수 있습니다.')) {
            window.location.href = "/";
        }
    },
    termsNextButton : (params) => {
        if (!agreeService.checked || !agreePrivacy.checked) {
            alert('필수 이용약관을 동의하지 않을 시 \n회원가입 진행이 불가합니다.');
            return false;
        }

        termsContainer.classList.remove('visible');
        registerForm.classList.add('visible');
    },
    closeAddressSearch : (params) => {
        window.document.body.classList.remove('searching');
    },
    openAddressSearch : (params) => {
        const searchContainer = window.document.body.querySelector(':scope > .address-search-container');
        const dialog = searchContainer.querySelector(':scope > .dialog');
        dialog.innerHTML = '';

        new daum.Postcode({
            oncomplete: (data) => {
                registerForm['addressPostal'].value = data.zonecode;
                registerForm['addressPrimary'].value = data.address;
                registerForm['addressSecondary'].focus();
                registerForm['addressSecondary'].select();
                window.document.body.classList.remove('searching');
            }
        }).embed(dialog);

        window.document.body.classList.add('searching');
    },
    returnButton : (params) => {
        registerForm.reset();

        termsContainer.classList.add('visible');
        registerForm.classList.remove('visible');
    },
    requestContactAuthCode : (params) => {
        if (registerForm['contact'].value === '') {
            alert('연락처를 입력해 주세요.');
            registerForm['contact'].focus();
            return;
        }

        if (!new RegExp('^(\\d{8,12})$').test(registerForm['contact'].value)) {
            alert('올바른 연락처를 입력해 주세요.');
            registerForm['contact'].focus();
            return;
        }

        cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        xhr.open('GET', `./userRegisterAuth?contact=${registerForm['contact'].value}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                            registerForm['contactAuthSalt'].value = responseJson['salt'];
                            registerForm['contact'].setAttribute('disabled', 'disabled');
                            registerForm['contactAuthRequestButton'].setAttribute('disabled', 'disabled');
                            registerForm['contactAuthCheckButton'].removeAttribute('disabled');
                            registerForm['contactAuthCode'].removeAttribute('disabled');
                            registerForm['contactAuthCode'].focus();
                            break;
                        default:
                            alert('알 수 없는 이유로 문자를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            registerForm['contact'].focus();
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    registerForm['contact'].focus();
                }
            }
        };
        xhr.send();
    },
    checkContactAuthCode : (params) => {
        if (registerForm['contactAuthCode'].value === '') {
            alert('인증번호를 입력해 주세요.');
            registerForm['contactAuthCode'].focus();
            return;
        }
        if (!new RegExp('^(\\d{6})$').test(registerForm['contactAuthCode'].value)) {
            alert('올바른 인증번호를 입력해 주세요.');
            registerForm['contactAuthCode'].focusAndSelect();
            return;
        }
        cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', registerForm['contact'].value);
        formData.append('code', registerForm['contactAuthCode'].value);
        formData.append('salt', registerForm['contactAuthSalt'].value);
        xhr.open('POST', './userRegisterAuth');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'expired':
                            alert('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                            registerForm['contact'].removeAttribute('disabled');
                            registerForm['contactAuthRequestButton'].removeAttribute('disabled');
                            registerForm['contactAuthCode'].value = '';
                            registerForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                            registerForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            registerForm['contactAuthSalt'].value = '';
                            registerForm['contact'].focusAndSelect();
                            break;
                        case 'success':
                            registerForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                            registerForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            alert('연락처가 성공적으로 인증되었습니다.');
                            break;
                        default:
                            registerForm['contactAuthCode'].focusAndSelect();
                            alert('입력한 인증번호가 올바르지 않습니다.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    registerForm['contactAuthCode'].focusAndSelect();
                }
            }
        };
        xhr.send(formData);
    }
};

const registerWarning = {
    getElement : () => window.document.getElementById('registerWarning'),
    show : (text) => {
        registerWarning.getElement().innerText = text;
        registerWarning.getElement().classList.add('visible');
    }
}

window.document.body.querySelectorAll('[data-func]').forEach(element => {
    element.addEventListener('click', event => {
        const dataFunc = element.dataset.func;
        if (typeof(dataFunc) === 'string' && typeof(functions[dataFunc]) === 'function') {
            functions[dataFunc] ({
                element : element,
                event : event
            });
        }
    });
});

let emailChecked = false;

registerForm['email'].addEventListener('focusout', () => {
    if (registerForm['email'].value === '' || !new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(registerForm['email'].value)) {
        registerForm['email'].focusAndSelect();
        registerWarning.show('올바른 이메일을 입력해 주세요.');
        emailChecked = false;
        return false;
    }
    cover.show();
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./userEmailCheck?email=${registerForm['email'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        registerForm['password'].focus();
                        emailChecked = true;
                        break;
                    case 'duplicate':
                        alert('입력하신 이메일은 이미 사용 중입니다.');
                        emailChecked = false;
                        break;
                    default:
                        alert('알 수 없는 이유로 이메일 중복 검사를 완료하지 못 하였습니다. 잠시 후 다시 시도해 주세요.');
                        emailChecked = false;
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                emailChecked = false;
            }
        }
    };
    xhr.send();
});

registerForm.onsubmit = e => {
    e.preventDefault();

    if (registerForm['email'].value === "") {
        registerWarning.show('이메일을 입력해 주세요.');
        registerForm['email'].focus();
        return false;
    }

    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(registerForm['email'].value)) {
        registerWarning.show('올바른 이메일 주소를 입력해 주세요.');
        registerForm['email'].focusAndSelect();
        return false;
    }

    if (!emailChecked) {
        registerWarning.show('이메일 중복 검사가 완료되지 않았습니다.');
        return false;
    }

    if (registerForm['password'].value === "") {
        registerWarning.show('비밀번호를 입력해 주세요.');
        registerForm['password'].focus();
        return false;
    }

    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(registerForm['password'].value)) {
        registerWarning.show('올바른 비밀번호를 입력해 주세요.');
        form['password'].focusAndSelect();
        return false;
    }

    if (registerForm['passwordCheck'].value === "") {
        registerWarning.show('비밀번호를 입력해 주세요.');
        registerForm['passwordCheck'].focus();
        return false;
    }

    if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
        registerWarning.show('비밀번호가 일치하지 않습니다.');
        registerForm['passwordCheck'].focusAndSelect();
        return false;
    }

    if (registerForm['name'].value === "") {
        registerWarning.show('이름을 입력해 주세요.');
        registerForm['name'].focus();
        return false;
    }

    if (!RegExp("^([가-힣]{2,5})$").test(registerForm['name'].value)) {
        registerWarning.show('올바른 이름을 입력해 주세요.');
        registerForm['name'].focusAndSelect();
        return false;
    }

    if (registerForm['addressPrimary'].value === "") {
        registerWarning.show('주소 찾기를 진행해 주세요.');
        return false;
    }

    if (registerForm['addressSecondary'].value === "") {
        registerWarning.show('상세주소를 입력해 주세요.');
        registerForm['addressSecondary'].focus();
        return false;
    }

    if (registerForm['telecomValue'].value === '-1') {
        registerWarning.show('통신사를 선택해 주세요.');
        return false;
    }

    if (registerForm['contact'].value === "") {
        registerWarning.show('연락처를 입력해 주세요.');
        registerForm['contact'].focus();
        return false;
    }

    if (!new RegExp('^(\\d{8,12})$').test(registerForm['contact'].value)) {
        registerWarning.show('올바른 연락처를 입력해 주세요.');
        registerForm['contact'].focusAndSelect();
        return false;
    }

    if (!registerForm['contactAuthCheckButton'].disabled || !registerForm['contactAuthRequestButton'].disabled) {
        alert('연락처 인증을 완료해 주세요.');
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    formData.append('password', registerForm['password'].value);
    formData.append('name', registerForm['name'].value);
    formData.append('addressPostal', registerForm['addressPostal'].value);
    formData.append('addressPrimary', registerForm['addressPrimary'].value);
    formData.append('addressSecondary', registerForm['addressSecondary'].value);
    formData.append('telecomValue', registerForm['telecomValue'].value);
    formData.append('contact', registerForm['contact'].value);
    formData.append('policyMarketing', agreeAd.checked);

    cover.show();
    xhr.open('POST', './userRegister');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        window.location.href = "./userRegisterDone";
                        break;
                    default :
                        alert('알 수 없는 이유로 회원가입을 실패했습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};