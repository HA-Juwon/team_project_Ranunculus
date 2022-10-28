//이름과 전화번호 인증을 통해서 확인

const recoverPasswordForm = window.document.getElementById('recoverPasswordForm');
const contactAuthRequestButton=window.document.getElementById('contactAuthRequestButton');
const contactAuthCheckButton=window.document.getElementById('contactAuthCheckButton');

//인증번호 요청 버튼을 누르면
contactAuthRequestButton.addEventListener('click',()=>{

    if(recoverPasswordForm['email'].value===''){
        alert('이메일을 입력해 주세요.');
        recoverPasswordForm['email'].focus();
        return;
    }

    if (!new RegExp("^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$").test(recoverPasswordForm['email'].value)) {
        alert('올바른 이메일을 입력해 주세요.');
        recoverPasswordForm['email'].focus();
        return;
    }

    if (recoverPasswordForm['name'].value === '') {
        alert('이름을 입력해 주세요.');
        recoverPasswordForm['name'].focus();
        return;
    }

    if (!new RegExp('^([가-힣]{2,5})$').test(recoverPasswordForm['name'].value)) {
        alert('올바른 이름을 입력해 주세요.');
        recoverPasswordForm['name'].focus();
        return;
    }

    if (recoverPasswordForm['contact'].value === '') {
        alert('연락처를 입력해 주세요.');
        recoverPasswordForm['contact'].focus();
        return;
    }

    if (!new RegExp('^(\\d{8,12})$').test(recoverPasswordForm['contact'].value)) {
        alert('올바른 연락처를 입력해 주세요.');
        recoverPasswordForm['contact'].focus();
        return;
    }

    //커버 아직 안 만듦
    // cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./userRecoverAuth?name=${recoverPasswordForm['name'].value}&contact=${recoverPasswordForm['contact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson);
                switch (responseJson['result']) {
                    case 'success':
                        // console.log(responseJson['salt']);
                        alert('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                        recoverPasswordForm['contactAuthSalt'].value = responseJson['salt'];
                        recoverPasswordForm['name'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contact'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contactAuthRequestButton'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contactAuthCheckButton'].removeAttribute('disabled');
                        recoverPasswordForm['contactAuthCode'].removeAttribute('disabled');
                        recoverPasswordForm['contactAuthCode'].focus();
                        break;
                    default:
                        alert('입력하신 이름과 연락처가 일치하는 회원이 없습니다.');
                        recoverPasswordForm['contact'].focus();
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                recoverPasswordForm['contact'].focus();
            }
        }
    };
    xhr.send();
});


//인증번호 확인 버튼을 누르면
contactAuthCheckButton.addEventListener("click", ()=>{
    console.log('인증번호 확인 버튼 작동');
    if (recoverPasswordForm['contactAuthCode'].value === '') {
        alert('인증번호를 입력해 주세요.');
        recoverPasswordForm['contactAuthCode'].focus();
        return;
    }
    if (!new RegExp('^(\\d{6})$').test(recoverPasswordForm['contactAuthCode'].value)) {
        alert('올바른 인증번호를 입력해 주세요.');
        recoverPasswordForm['contactAuthCode'].focusAndSelect();
        return;
    }
    // cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('contact', recoverPasswordForm['contact'].value);
    formData.append('code', recoverPasswordForm['contactAuthCode'].value);
    formData.append('salt', recoverPasswordForm['contactAuthSalt'].value);
    xhr.open('POST', './userRecoverAuth');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // cover.hide();
            console.log("작동됨");
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson);
                switch (responseJson['result']) {
                    case 'expired':
                        alert('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                        recoverPasswordForm['contact'].removeAttribute('disabled');
                        recoverPasswordForm['contactAuthRequestButton'].removeAttribute('disabled');
                        recoverPasswordForm['contactAuthCode'].value = '';
                        recoverPasswordForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contactAuthSalt'].value = '';
                        recoverPasswordForm['contact'].focusAndSelect();
                        break;
                    case 'success':
                        recoverPasswordForm['contactAuthCode'].setAttribute('disabled', 'disabled');
                        recoverPasswordForm['contactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        alert('연락처가 성공적으로 인증되었습니다.');
                        break;
                    default:
                        recoverPasswordForm['contactAuthCode'].focusAndSelect();
                        alert('입력한 인증번호가 올바르지 않습니다.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                recoverPasswordForm['contactAuthCode'].focusAndSelect();
            }
        }
    };
    xhr.send(formData);
});

recoverPasswordForm.onsubmit = e =>{
    e.preventDefault();
    //contactAuthCheckButton가 비활성화 상태거나(인증번호 전송 버튼을 누른 적이 없으면)
    if (!recoverPasswordForm['contactAuthCheckButton'].disabled || !recoverPasswordForm['contactAuthRequestButton'].disabled) {
        alert('연락처 인증을 완료해 주세요.');
        return false;
    }
    // 아직 커버 안만듦
    // cover.show('이메일을 찾고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email',
        recoverPasswordForm['email'].value);
    formData.append('name',
        recoverPasswordForm['name'].value);
    formData.append('contact', recoverPasswordForm['contact'].value);
    formData.append('code', recoverPasswordForm['contactAuthCode'].value);
    formData.append('salt', recoverPasswordForm['contactAuthSalt'].value);
    xhr.open('POST', './userRecoverPassword');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            //아직 커버 없음
            // cover.hide();
            // console.log('되나?');
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href = "./userResetPassword";
                       break;
                    default:
                        alert('알 수 없는 이유로 비밀번호를 찾지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}