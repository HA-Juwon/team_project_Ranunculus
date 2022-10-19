const termsContainer = window.document.getElementById('termsContainer');

const functions = {
    agreeAll: () => {
        if (termsContainer['agreeService'].checked === true) {
            termsContainer['agreeService'].prop("checked", false);
        } else {
            termsContainer['agreeService'].prop("checked", true);
        }

    }
};
window.document.body.querySelectorAll('[data-func]').forEach(x => {
    x.addEventListener('click', e => {
        const dataFuncValue = x.dataset.func;
        if (typeof (dataFuncValue) === 'string' && typeof (functions[dataFuncValue]) === 'function') {
            functions[dataFuncValue]({
                element: x,
                event: e
            });
        }
    });
});