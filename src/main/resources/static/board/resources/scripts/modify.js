const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
const modifyButton = window.document.getElementById('modifyButton');
const back = window.document.getElementById('back');

back.addEventListener('click', () => {
    window.location.href = `../qna`;
});

modifyButton.addEventListener('click', (e) => {
    e.preventDefault();
    if (!confirm('글을 수정 하시겠습니까?')) {
        return;
    }
    cover.show();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('writer', form['name'].value);
    formData.append('password', form['password'].value);
    formData.append('title', form['title'].value);
    formData.append('content', form['content'].value);
    xhr.open('POST', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('글을 성공적으로 수정하였습니다.');
                        window.location.href = `../read/${id}`;
                        break;
                    case 'k':
                        alert('비밀번호가 올바르지 않습니다.')
                        break;
                    default:
                        alert('알 수 없는 이유로 글을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
})