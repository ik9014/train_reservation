/*비밀번호 확인 js*/
document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.getElementById('signupForm');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const passwordError = document.getElementById('passwordError');
    const confirmPasswordError = document.getElementById('confirmPasswordError');

    signupForm.addEventListener('submit', function (e) {
        let valid = true;

        // 비밀번호 확인
        if (password.value !== confirmPassword.value) {
            confirmPasswordError.textContent = '비밀번호가 일치하지 않습니다.';
            confirmPasswordError.style.display = 'block';
            confirmPassword.classList.add('error');
            valid = false;
        } else {
            confirmPasswordError.style.display = 'none';
            confirmPassword.classList.remove('error');
        }

        // 비밀번호 조건 확인
        if (password.value.length < 8 || password.value.length > 16) {
            passwordError.textContent = '비밀번호는 8글자 이상 16자리 이하여야합니다.';
            passwordError.style.display = 'block';
            password.classList.add('error');
            valid = false;
        } else {
            passwordError.style.display = 'none';
            password.classList.remove('error');
        }

        // 폼이 유효하지 않으면 제출을 막음
        if (!valid) {
            e.preventDefault();
        }
    });

});
/*핸드폰 번호 {010}-{4자리}-{4자리} 형식 js*/
document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.getElementById('signupForm');
    const phone = document.getElementById('phone');
    const phoneError = document.getElementById('phoneError');

    signupForm.addEventListener('submit', function (e) {
        let valid = true;

        // 핸드폰 번호 유효성 검사 (정규식)
        const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
        if (!phoneRegex.test(phone.value)) {
            phoneError.textContent = '전화번호 형식에 맞추어 입력해주세요 010-****-****';
            phoneError.style.display = 'block';
            phone.classList.add('error');
            valid = false;
        } else {
            phoneError.style.display = 'none';
            phone.classList.remove('error');
        }

        // 폼이 유효하지 않으면 제출을 막음
        if (!valid) {
            e.preventDefault();
        }
    });

    // 핸드폰 번호 포맷팅 (자동으로 - 추가)
    phone.addEventListener('input', function () {
        let phoneValue = phone.value.replace(/[^\d]/g, '');  // 숫자만 남기고 제거
        if (phoneValue.length < 4) {
            phone.value = phoneValue;
        } else if (phoneValue.length < 7) {
            phone.value = phoneValue.slice(0, 3) + '-' + phoneValue.slice(3);
        } else if (phoneValue.length < 11) {
            phone.value = phoneValue.slice(0, 3) + '-' + phoneValue.slice(3, 7) + '-' + phoneValue.slice(7);
        } else {
            phone.value = phoneValue.slice(0, 3) + '-' + phoneValue.slice(3, 7) + '-' + phoneValue.slice(7, 11);
        }
    });
});
