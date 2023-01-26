const submitForm = window.document.getElementById('submitForm');
const searchInput = window.document.getElementById('searchInput');
const list1 = window.document.getElementById('list1')
const list2 = window.document.getElementById('list2')
const list3 = window.document.getElementById('list3')
const list4 = window.document.getElementById('list4')
const list5 = window.document.getElementById('list5')
const list6 = window.document.getElementById('list6')

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
            return false;

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
    xhr.open('POST', `./qna`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                alert('불러오기성공');
                console.log(submitForm['keyword'].value)

            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}


