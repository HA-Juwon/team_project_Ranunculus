const termsContainer = window.document.getElementById('termsContainer');
const registerForm = window.document.getElementById('registerForm');
const cancelButton = window.document.getElementById('cancelButton');
const nextButton = window.document.getElementById('nextButton');
const returnButton = window.document.getElementById('returnButton');

cancelButton.addEventListener('click', () => {
   if (window.confirm('정말 회원가입을 취소 하시겠어요?\n일부 서비스 이용이 불가 할 수 있습니다.')) {
   window.location.href = "/";
   }
});

nextButton.addEventListener('click', e => {
   e.preventDefault();

   // if (!termsContainer['agreeService'].checked) {
   //     alert('서비스 이용약관을 동의하지 않을시 회원가입 진행이 불가합니다.');
   //     termsContainer['agreeService'].focus();
   //     return false;
   // }

   termsContainer.classList.remove('visible');
   registerForm.classList.add('visible');
});

returnButton.addEventListener('click', () => {

   termsContainer.classList.add('visible');
   registerForm.classList.remove('visible');
});