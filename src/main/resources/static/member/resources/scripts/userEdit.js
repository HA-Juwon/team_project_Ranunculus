const infoForm = window.document.getElementById('infoForm');

const functions = {
    closeAddressSearch: (params) => {
        window.document.body.classList.remove('searching');
    },
    openAddressSearch: (params) => {
        const searchContainer = window.document.body.querySelector(':scope > .address-search-container');
        const dialog = searchContainer.querySelector(':scope > .dialog');
        dialog.innerHTML = '';

        new daum.Postcode({
            oncomplete: (data) => {
                infoForm['oldAddressPostal'].classList.remove('visible');
                infoForm['oldAddressPrimary'].classList.remove('visible');
                infoForm['oldAddressSecondary'].classList.remove('visible');
                infoForm['newAddressPostal'].classList.add('visible');
                infoForm['newAddressPrimary'].classList.add('visible');
                infoForm['newAddressSecondary'].classList.add('visible');
                infoForm['newAddressPostal'].value = data.zonecode;
                infoForm['newAddressPrimary'].value = data.address;
                infoForm['newAddressSecondary'].value = "";
                infoForm['newAddressSecondary'].focus();
                infoForm['newAddressSecondary'].select();
                window.document.body.classList.remove('searching');
            }
        }).embed(dialog);

        window.document.body.classList.add('searching');
    },
    requestContactAuthCode: (params) => {
        if (infoForm['contact'].value === '') {
            editWarning.show('연락처를 입력해 주세요.');
            infoForm['contact'].focus();
            return;
        }

        if (!new RegExp('^(\\d{8,12})$').test(infoForm['contact'].value)) {
            editWarning.show('올바른 연락처를 입력해 주세요.');
            infoForm['contact'].focus();
            return;
        }

        editWarning.hide();
        cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        xhr.open('GET', `./userContactAuth?contact=${infoForm['contact'].value}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                            infoForm['contactAuthSalt'].value = responseJson['salt'];
                            infoForm['contact'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthRequestButton'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].removeAttribute('disabled');
                            infoForm['contactAuthCode'].removeAttribute('disabled');
                            infoForm['contactAuthCode'].focus();
                            break;
                        default:
                            alert('알 수 없는 이유로 문자를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            infoForm['contact'].focus();
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    infoForm['contact'].focus();
                }
            }
        };
        xhr.send();
    },
    checkContactAuthCode: (params) => {
        if (infoForm['contactAuthCode'].value === '') {
            editWarning.show('인증번호를 입력해 주세요.');
            infoForm['contactAuthCode'].focus();
            return;
        }
        if (!new RegExp('^(\\d{6})$').test(infoForm['contactAuthCode'].value)) {
            editWarning.show('올바른 인증번호를 입력해 주세요.');
            infoForm['contactAuthCode'].focusAndSelect();
            return;
        }

        editWarning.hide();
        cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', infoForm['contact'].value);
        formData.append('code', infoForm['contactAuthCode'].value);
        formData.append('salt', infoForm['contactAuthSalt'].value);
        xhr.open('POST', './userContactAuth');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'expired':
                            alert('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                            infoForm['contact'].removeAttribute('disabled');
                            infoForm['contactAuthRequestButton'].removeAttribute('disabled');
                            infoForm['contactAuthCode'].value = '';
                            infoForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthSalt'].value = '';
                            infoForm['contact'].focusAndSelect();
                            break;
                        case 'success':
                            infoForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            alert('연락처가 성공적으로 인증되었습니다.');
                            break;
                        default:
                            infoForm['contactAuthCode'].focusAndSelect();
                            alert('입력한 인증번호가 올바르지 않습니다.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    infoForm['contactAuthCode'].focusAndSelect();
                }
            }
        };
        xhr.send(formData);
    }
}

const editWarning = {
    getElement: () => window.document.getElementById('warning'),
    show: (text) => {
        editWarning.getElement().innerText = text;
        editWarning.getElement().classList.add('visible');
    },
    hide: () => {
        editWarning.getElement().classList.remove('visible');
    }
}

window.document.body.querySelectorAll('[data-func]').forEach(element => {
    element.addEventListener('click', event => {
        const dataFunc = element.dataset.func;
        if (typeof (dataFunc) === 'string' && typeof (functions[dataFunc]) === 'function') {
            functions[dataFunc]({
                element: element,
                event: event
            });
        }
    });
});

infoForm.onsubmit = e => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('email', infoForm['email'].value);
    formData.append('password', infoForm['oldPassword'].value);
    formData.append('name', infoForm['name'].value);
    formData.append('addressPostal', infoForm['oldAddressPostal'].value);
    formData.append('addressPrimary', infoForm['oldAddressPrimary'].value);
    formData.append('addressSecondary', infoForm['oldAddressSecondary'].value);


    if (infoForm['oldPassword'].value === "") {
        editWarning.show("개인정보 수정을 위해서 현재 비밀번호를 입력해 주세요.");
        return false;
    }

    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(infoForm['oldPassword'].value)) {
        editWarning.show('현재 비밀번호를 올바르게 입력해 주세요.');
        infoForm['oldPassword'].focusAndSelect();
        return false;
    }

    if (infoForm['newPassword'].value !== "") {
        if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(infoForm['newPassword'].value)) {
            editWarning.show('새로운 비밀번호를 올바르게 입력해 주세요.');
            infoForm['newPassword'].focusAndSelect();
            return false;
        }

        if (infoForm['newPassword'].value !== infoForm['newPasswordCheck'].value) {
            editWarning.show('새로운 비밀번호가 일치하지 않습니다. 다시 입력해 주세요.');
            infoForm['newPasswordCheck'].focusAndSelect();
            return false;
        }
        formData.append('newPassword', infoForm['newPassword'].value);
    }

    if (infoForm['newAddressPostal'].value !== "" &&
        infoForm['newAddressPrimary'].value !== "") {
        if (infoForm['newAddressSecondary'].value === "") {
            editWarning.show('상세 주소를 입력해 주세요.');
            infoForm['newAddressSecondary'].focus();
            return false;
        }
        formData.append('newAddressPostal', infoForm['newAddressPostal'].value);
        formData.append('newAddressPrimary', infoForm['newAddressPrimary'.value]);
        formData.append('newAddressSecondary', infoForm['newAddressSecondary'].value);
    }

    if (infoForm['contactAuthRequestButton'].disabled) {
        if (!infoForm['contactAuthCheckButton'].disabled || !infoForm['contactAuthRequestButton'].disabled) {
            editWarning.show('연락처 인증을 완료해 주세요.');
            return false;
        }
        formData.append('telecomValue', infoForm['telecomValue'].value);
        formData.append('contact', infoForm['contact'].value);
    }

    if (infoForm['newPassword'].value === "" &&
        infoForm['newAddressPostal'].value === "" &&
        infoForm['newAddressPrimary'].value === "" &&
        !infoForm['contactAuthRequestButton'].disabled) {
        editWarning.show('개인정보 수정 사항이 없습니다. 다시 시도해 주세요.');
        return false;
    }

    const xhr = new XMLHttpRequest();
    cover.show();
    xhr.open('POST', './userEdit?tab=info');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {

            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};