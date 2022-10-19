const termsContainer = window.document.getElementById('termsContainer');
const registerForm = window.document.getElementById('registerForm');
const cancelButton = window.document.getElementById('cancelButton');
const nextButton = window.document.getElementById('nextButton');

cancelButton.addEventListener('click', () => {
   window.location.href = "/";
});

nextButton.addEventListener('click', e => {
   e.preventDefault();

   if (!termsContainer['agreeService'].checked) {
       alert('서비스 이용약관을 동의하지 않을시 회원가입 진행이 불가합니다.');
       termsContainer['agreeService'].focus();
       return false;
   }

   termsContainer.classList.remove('visible');
   registerForm.classList.add('visible');
});