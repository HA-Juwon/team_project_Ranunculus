const truncateForm = window.document.getElementById('truncateForm');
const infoForm = window.document.getElementById('infoForm');

const functions = {
    truncateCheck: (parmas) => {
        if (truncateForm['truncatePassword'].value === '') {
            truncateForm['truncatePassword'].focus();
            return;
        }

        if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(truncateForm['truncatePassword'].value)) {
            truncateForm['truncatePassword'].focusAndSelect();
            return;
        }
        // TODO : truncate js 작성
        // truncateForm[]
    },
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
    contactChange: (params) => {
        infoForm['oldContact'].classList.remove('visible');
        infoForm['contactChange'].classList.remove('visible');
        infoForm['newContact'].classList.add('visible');
        infoForm['contactAuthRequestButton'].classList.add('visible');
        infoForm['newContactAuthCode'].classList.add('visible');
        infoForm['contactAuthCheckButton'].classList.add('visible');
        infoForm['telecomValue'].removeAttribute('disabled');
    },
    requestContactAuthCode: (params) => {
        if (infoForm['newContact'].value === '') {
            editWarning.show('연락처를 입력해 주세요.');
            infoForm['newContact'].focus();
            return;
        }

        if (!new RegExp('^(\\d{8,12})$').test(infoForm['newContact'].value)) {
            editWarning.show('올바른 연락처를 입력해 주세요.');
            infoForm['newContact'].focus();
            return;
        }

        editWarning.hide();
        cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        xhr.open('GET', `./userContactAuth?contact=${infoForm['newContact'].value}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                            infoForm['newContactAuthSalt'].value = responseJson['salt'];
                            infoForm['newContact'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthRequestButton'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].removeAttribute('disabled');
                            infoForm['newContactAuthCode'].removeAttribute('disabled');
                            infoForm['newContactAuthCode'].focus();
                            break;
                        default:
                            alert('알 수 없는 이유로 문자를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                            infoForm['newContact'].focus();
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    infoForm['newContact'].focus();
                }
            }
        };
        xhr.send();
    },
    checkContactAuthCode: (params) => {
        if (infoForm['newContactAuthCode'].value === '') {
            editWarning.show('인증번호를 입력해 주세요.');
            infoForm['newContactAuthCode'].focus();
            return;
        }
        if (!new RegExp('^(\\d{6})$').test(infoForm['newContactAuthCode'].value)) {
            editWarning.show('올바른 인증번호를 입력해 주세요.');
            infoForm['newContactAuthCode'].focusAndSelect();
            return;
        }

        editWarning.hide();
        cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('contact', infoForm['newContact'].value);
        formData.append('code', infoForm['newContactAuthCode'].value);
        formData.append('salt', infoForm['newContactAuthSalt'].value);
        xhr.open('POST', './userContactAuth');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'expired':
                            alert('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                            infoForm['newContact'].removeAttribute('disabled');
                            infoForm['contactAuthRequestButton'].removeAttribute('disabled');
                            infoForm['newContactAuthCode'].value = '';
                            infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            infoForm['newContactAuthSalt'].value = '';
                            infoForm['newContact'].focusAndSelect();
                            break;
                        case 'success':
                            infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                            infoForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                            alert('연락처가 성공적으로 인증되었습니다.');
                            break;
                        default:
                            infoForm['newContactAuthCode'].focusAndSelect();
                            alert('입력한 인증번호가 올바르지 않습니다.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    infoForm['newContactAuthCode'].focusAndSelect();
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
    formData.append('oldPassword', infoForm['oldPassword'].value);

    if (infoForm['oldPassword'].value === "") {
        editWarning.show("회원정보 수정을 위해서 현재 비밀번호를 입력해 주세요.");
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
        formData.append('newAddressPrimary', infoForm['newAddressPrimary'].value);
        formData.append('newAddressSecondary', infoForm['newAddressSecondary'].value);
    }

    if (infoForm['contactAuthRequestButton'].disabled) {
        if (!infoForm['contactAuthCheckButton'].disabled || !infoForm['contactAuthRequestButton'].disabled) {
            editWarning.show('연락처 인증을 완료해 주세요.');
            return false;
        }
        formData.append('newTelecomValue', infoForm['telecomValue'].value);
        formData.append('newContact', infoForm['newContact'].value);
        formData.append('newContactAuthCode', infoForm['newContactAuthCode'].value);
        formData.append('newContactAuthSalt', infoForm['newContactAuthSalt'].value);
    }

    if (infoForm['newPassword'].value === "" &&
        infoForm['newAddressPostal'].value === "" &&
        infoForm['newAddressPrimary'].value === "" &&
        !infoForm['contactAuthRequestButton'].disabled) {
        editWarning.show('회원정보 수정 사항이 없습니다. 다시 시도해 주세요.');
        return false;
    }

    const xhr = new XMLHttpRequest();
    cover.show('회원정보 변경 사항을 적용중입니다.');
    xhr.open('POST', './userEditInfo');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        alert('회원정보 변경 사항이 적용되었습니다.');
                        window.location.reload();
                        break;
                    case 'duplicate' :
                        editWarning.show('현재 비밀번호와 동일한 신규 비밀번호를 입력하였습니다. 다시 입력해 주세요.');
                        infoForm['newPassword'].select();
                        break;
                    case 'expired' :
                        editWarning.show('올바른 연락처가 아니거나 연락처 인증을 미진행 하였습니다.');
                        break;
                    default :
                        editWarning.show('알 수 없는 이유로 회원정보를 변경하지 못하였습니다. 잠시 후 다시 시도해 주세요.')
                }
            } else {
                editWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};