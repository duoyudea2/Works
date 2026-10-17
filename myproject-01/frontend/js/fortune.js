window.onload = async function () {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
        return;
    }
    const res = await fetch("http://localhost:8080/api/auth/me", {
        headers: { "Authorization": "Bearer " + token }
    });
    const json = await res.json();
    if (json.code !== 200) {
        window.location.href = "login.html";
        return;
    }
    document.getElementById("userInfo").innerHTML =
        `<b>用户名：</b>${json.username}<br>` +
        `<b>出生地：</b>${json.placeOfBirth}<br>` +
        `<b>出生时间：</b>${json.birthDatetime}<br>` +
        `<b>你的八字：</b>${json.bazi}`;
};

async function doFortune() {
    const btn = document.getElementById("btn");
    btn.innerText = "正在解签，请稍候…";
    btn.disabled = true;

    const interests = document.querySelector('input[name="interests"]:checked').value;
    const token = localStorage.getItem("token");
    try {
        const res = await fetch("http://localhost:8080/api/fortune/get", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({ interests })
        });
        const json = await res.json();

        if (json.code === 200) {
            sessionStorage.setItem("fortuneResult", JSON.stringify(json));
            window.location.href = "result.html";
        } else {
            alert(json.msg);
            btn.innerText = "开始求签";
            btn.disabled = false;
        }
    } catch (e) {
        alert("网络错误：" + e.message);
        btn.innerText = "开始求签";
        btn.disabled = false;
    }
}

async function doLogout() {
    const token = localStorage.getItem("token");  
    await fetch("http://localhost:8080/api/auth/logout", { headers: { "Authorization": "Bearer " + token } });
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    window.location.href = "login.html";
}