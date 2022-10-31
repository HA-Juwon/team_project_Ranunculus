const form = window.document.getElementById('form');

form['back'].addEventListener('click', () => {
    window.location.href = './qna';
});

form.onsubmit = e => {
    e.preventDefault();

    // warning.hide();
    if (form['name'].value === '') {
        alert('이름을 입력해 주세요')
        form['name'].focus();
        return false;
    }
    if (form['password'].value === '') {
        alert('비밀번호를 입력해 주세요')
        form['password'].focus();
        return false;
    }
    if (form['title'].value === '') {
        alert('제목을 입력해 주세요')
        form['title'].focus();
        return false;
    }
    cover.show();

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('name', form['name'].value);
    formData.append('password', form['password'].value);
    formData.append('title', form['title'].value);
    formData.append('content', form['content'].value);
    xhr.open('POST', './write');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('성공했어')
                        // const id = responseJson['id'];
                        // window.location.href = `./read/${id}`;
                        break;
                    default:
                        alert('알 수 없는 이유로 동행 글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};

ClassicEditor.create(form['content'], {
    simpleUpload: {
        uploadUrl: 'image'
    }
});







