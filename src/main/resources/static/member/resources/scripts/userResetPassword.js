const resetPasswordForm = window.document.getElementById('resetPasswordForm');

resetPasswordForm.onsubmit = e => {
    e.preventDefault();

    if (resetPasswordForm['password'].value === '') {
        alert('새로운 비밀번호를 입력해 주세요.');
        // warning.show('새로운 비밀번호를 입력해 주세요.');
        resetPasswordForm['password'].focus();
        return false;
    }
    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(resetPasswordForm['password'].value)) {
        alert('올바른 비밀번호를 입력해 주세요.');
        // warning.show('올바른 비밀번호를 입력해 주세요.');
        resetPasswordForm['password'].focusAndSelect();
        return false;
    }
    if (resetPasswordForm['passwordCheck'].value === '') {
        alert('새로운 비밀번호를 다시 입력해 주세요.');
        // warning.show('새로운 비밀번호를 다시 입력해 주세요.');
        resetPasswordForm['passwordCheck'].focus();
        return false;
    }
    if (resetPasswordForm['password'].value !== resetPasswordForm['passwordCheck'].value) {
        alert('비밀번호가 일치하지 않습니다.');
        // warning.show('비밀번호가 일치하지 않습니다.');
        resetPasswordForm['passwordCheck'].focusAndSelect();
        return false;
    }
    // cover.show('비밀번호를 다시 설정하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('password', resetPasswordForm['password'].value);
    xhr.open('POST', './userResetPassword');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        resetPasswordForm.classList.remove('visible');
                        window.document.getElementById('result').classList.add('visible');
                        break;
                    default:
                        alert('알 수 없는 이유로 비밀번호를 재설정하지 못하였습니다.')
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};