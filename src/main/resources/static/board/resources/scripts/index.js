const main = window.document.getElementById('main');
const form = main.querySelector(':scope > .form');
const searchForm = window.document.getElementById('search-form')
const writerRegex = new RegExp('^([가-힣]{2,5})$');

form.onsubmit = () => {
    if (!writerRegex.test(form['writer'].value)) {
        form['writer'].classList.add('warned');
        form['writer'].focus();
        return false;
    }
    if (form['content'].value === '') {
        form['content'].classList.add('warned');
        form['content'].focus();
        return false;
    }
    return true;
};

form['writer'].addEventListener('keydown', () => {
    if (form['writer'].classList.contains('warned')) {
        form['writer'].classList.remove('warned');
    }
});

form['content'].addEventListener('keydown', () => {
    if (form['content'].classList.contains('warned')) {
        form['content'].classList.remove('warned');
    }
});

searchForm['reset-button'].addEventListener('click', () => {
    window.location.href = '/';
})