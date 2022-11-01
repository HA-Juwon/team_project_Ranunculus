const submitForm = window.document.getElementById('submitform');
const searchName = window.document.getElementById('searchName');
const searchTitle = window.document.getElementById('searchTitle');
const searchContent = window.document.getElementById('searchContent');
const searchInput = window.document.getElementById('searchInput');

submitForm.onsubmit = e => {
    e.preventDefault()
    switch (submitForm['search'].value) {
        case 'name':
            alert('네임선택')
            break;
        case 'title':
            alert('제목선택')
            break;
        case 'content':
            alert('내용선택')
            break;
        default:
            alert('기준을 선택해주세요.')
            break;

    }
    if (submitForm['keyword'].value === '') {
        alert('찾을 내용을 입력하세요.');
        submitForm['searchInput'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./qna?search=${submitForm[''].value}&keyword=${submitForm['keyword'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        break;
                    default:
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
}

