const submitForm = window.document.getElementById('submitform');
const searchInput = window.document.getElementById('searchInput');

submitForm.onsubmit = e => {
    e.preventDefault()
    switch (submitForm['search'].value) {
        case 'name':
            break;
        case 'title':
            break;
        case 'content':
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

    const formData = new FormData();

    formData.append('search', submitForm['search'].value);
    formData.append('keyword', submitForm['keyword'].value);
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `./qna?search=${submitForm['search'].value}&keyword=${submitForm['keyword'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                alert('불러오기성공')
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}
