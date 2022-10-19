const termsContainer = window.document.getElementById('termsContainer');
const registerForm = window.document.getElementById('registerForm');
const cancelButton = window.document.getElementById('cancelButton');
const nextButton = window.document.getElementById('nextButton');
const returnButton = window.document.getElementById('returnButton');

registerForm.focusAndSelect = (name) => {
    registerForm[name].focus();
    registerForm[name].select();
};

cancelButton.addEventListener('click', () => {
   if (window.confirm('정말 회원가입을 취소 하시겠어요?\n일부 서비스 이용이 불가 할 수 있습니다.')) {
   window.location.href = "/";
   }
});

nextButton.addEventListener('click', e => {
   e.preventDefault();

   // TODO: 이거 수정해야해 ㅠ 체크여부 반환이 안되넹;
   // if (!termsContainer['agreeService'].checked) {
   //     alert('서비스 이용약관을 동의하지 않을시 회원가입 진행이 불가합니다.');
   //     termsContainer['agreeService'].focus();
   //     return false;
   // }

   termsContainer.classList.remove('visible');
   registerForm.classList.add('visible');
});

returnButton.addEventListener('click', () => {
    registerForm['memberId'].innerText = "";
    registerForm['password'].innerText = "";
    registerForm['passwordCheck'].innerText = "";
    registerForm['name'].innerText = "";
    registerForm['streetAddress'].innerText = "";
    registerForm['streetAddress2'].innerText = "";
    registerForm['contact'].innerText = "";
    registerForm[''].innerText = "";
    registerForm[''].innerText = "";

   termsContainer.classList.add('visible');
   registerForm.classList.remove('visible');
});

registerForm.onsubmit = e => {
  e.preventDefault();

  if (registerForm['memberId'].value === "") {
      // TODO: common.css & common.js warning, cover 만들어서 수정 필요!
      alert('아이디를 입력해 주세요.');
      registerForm['memberId'].focus();
      return false;
  }

  if (registerForm['password'].value === "") {
      alert('비밀번호를 입력해 주세요.');
      registerForm['password'].focus();
      return false;
  }

  if (registerForm['passwordCheck'].value === "") {
      alert('비밀번호를 입력해 주세요.');
      registerForm['passwordCheck'].focus();
      return false;
  }

  if (registerForm['password'].value !== registerForm['passwordCheck'].value) {
      alert('비밀번호가 일치하지 않습니다.');
      registerForm['passwordCheck'].focusAndSelect();
      return false;
  }

  if (registerForm['name'].value === "") {
      alert('이름을 입력해 주세요.');
      return false;
  }

  if (!RegExp("^([가-힣]{2,5})$").test(registerForm['name'].value)) {
      alert('올바른 이름을 입력해 주세요.');
      return false;
  }


};