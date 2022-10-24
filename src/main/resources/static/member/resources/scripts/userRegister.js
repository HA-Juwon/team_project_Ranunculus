const termsContainer = window.document.getElementById('termsContainer');
const registerForm = window.document.getElementById('registerForm');
const cancelButton = window.document.getElementById('cancelButton');
const nextButton = window.document.getElementById('nextButton');
const returnButton = window.document.getElementById('returnButton');
const agreeAll = window.document.getElementById('agreeAll');
const agreeService = window.document.getElementById('agreeService');
const agreePrivacy = window.document.getElementById('agreePrivacy');
const agreeAd = window.document.getElementById('agreeAd');
const addressButton = window.document.getElementById('addressButton');

registerForm.focusAndSelect = (name) => {
    registerForm[name].focus();
    registerForm[name].select();
};

agreeAll.addEventListener('click', () => {
    if (!agreeService.checked || !agreePrivacy.checked) {
   agreeService.checked = "true";
    agreePrivacy.checked = "true";
    agreeAd.checked = "true";
    } else{
    agreeService.checked = false;
    agreePrivacy.checked = false;
    agreeAd.checked = false;
    }
});

cancelButton.addEventListener('click', () => {
   if (window.confirm('정말 회원가입을 취소 하시겠어요?\n일부 서비스 이용이 불가 할 수 있습니다.')) {
   window.location.href = "/";
   }
});

nextButton.addEventListener('click', e => {
   e.preventDefault();

   if (!agreeService.checked){
        alert('서비스 이용약관을 동의하지 않을시 회원가입 진행이 불가합니다.');
        agreeService.focus();
        return false;
   }

    if (!agreeService.checked || !agreePrivacy.checked){
        alert('필수 이용약관을 동의하지 않을 시 \n회원가입 진행이 불가합니다.');
        return false;
    }

   termsContainer.classList.remove('visible');
   registerForm.classList.add('visible');
});

addressButton.addEventListener('click', () => {
    const addressSearchContainer = window.document.getElementById('addressSearchContainer');
    const dialog = addressSearchContainer.querySelector(':scope > .dialog');
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
});

// addressButton.addEventListener('focusout', () => {
//     window.document.body.classList.remove('searching');
// });

returnButton.addEventListener('click', () => {
    registerForm.reset();

   termsContainer.classList.add('visible');
   registerForm.classList.remove('visible');
});

registerForm.onsubmit = e => {
  // e.preventDefault();

    if (registerForm['email'].value === "") {
        alert('이메일을 입력해 주세요.');
        registerForm['email'].focus();
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
      return ['name'].focus();
      return false;
  }

  if (!RegExp("^([가-힣]{2,5})$").test(registerForm['name'].value)) {
      alert('올바른 이름을 입력해 주세요.');
      registerForm['name'].focusAndSelect();
      return false;
  }

    if (registerForm['addressPrimary'].value === "") {
        alert('주소 찾기를 진행해주세요.');
        registerForm['addressPrimary'].focus();
        return false;
    }

    if (registerForm['contact'].value === "") {
        alert('연락처를 입력해 주세요.');
        registerForm['contact'].focus();
        return false;
    }

    // if ()

};