const infoForm = window.document.getElementById('infoForm');

const functions = {
    closeAddressSearch: (params) => {
        window.document.body.classList.remove('searching');
    },
    openAddressSearch: (params) => {
        const searchContainer = window.document.body.querySelector(':scope > .address-search-container');
        const dialog = searchContainer.querySelector(':scope > .dialog');
        dialog.innerHTML = '';

        new daum.Postcode({
            oncomplete: (data) => {
                registerForm['addressPostal'].value = data.zonecode;
                registerForm['addressPrimary'].value = data.address;
                registerForm['addressSecondary'].focus();
                registerForm['addressSecondary'].select();
                window.document.body.classList.remove('searching');
            }
        }).embed(dialog);

        window.document.body.classList.add('searching');
    },

}

window.document.body.querySelectorAll('[data-func]').forEach(element => {
    element.addEventListener('click', event => {
        const dataFunc = element.dataset.func;
        if (typeof (dataFunc) === 'string' && typeof (dataFunc) === 'function') {
            functions[dataFunc]({
                element: element,
                event: event
            });
        }
    });
});

infoForm.onsubmit = () => {

};