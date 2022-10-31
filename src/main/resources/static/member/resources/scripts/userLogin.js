const form = window.document.getElementById('form');


form.onsubmit = e => {
    e.preventDefault();

    if (form['email'].value === '') {
        alert('이메일을 입력해 주세요.');
        form['email'].focus();
        return false;
    }
    if (form['password'].value === '') {
        alert('비밀번호를 입력해 주세요.');
        form['password'].focus();
        return false;
    }
    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(form['email'].value)) {
        alert('올바른 이메일을 입력해 주세요.');
        form['email'].focus();
        form['email'].select();
        return false;
    }
    if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(form['password'].value)) {
        alert('올바른 비밀번호를 입력해 주세요.');
        form['email'].focus();
        form['email'].select();
        return false;
    }
    cover.show();

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    formData.append('password', form['password'].value);
    formData.append('autosign', form['autosign'].value);
    xhr.open('POST', './userLogin');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        // cover.show();
                        // cover.hide();
                        window.location.href = '/';
                        break;
                    case 'suspended':
                        alert('이용이 중지된 회원입니다.');
                    default:
                        alert('입력하신 이메일 혹은 비밀번호가 올바르지 않습니다.');
                        form['email'].focus();
                        form['email'].select();
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}