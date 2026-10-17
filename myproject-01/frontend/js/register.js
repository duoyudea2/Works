async function doRegister() {
    const btn = document.getElementById("btn");
    const msg = document.getElementById("msg");
    msg.innerText = "";

    const data = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        placeOfBirth: document.getElementById("placeOfBirth").value,
        birthDatetime: document.getElementById("birthDatetime").value
    };

    if (!data.username || !data.password) {
        msg.innerText = "用户名和密码不能为空";
        return;
    }

    btn.innerText = "正在生成八字，请稍候…";
    btn.disabled = true;

    try {
        const res = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        const json = await res.json();

        if (json.code === 200) {
            alert("注册成功！");
            window.location.href = "login.html";
        } else {
            msg.innerText = json.msg;
            btn.innerText = "注册";
            btn.disabled = false;
        }
    } catch (e) {
        msg.innerText = "网络错误：" + e.message;
        btn.innerText = "注册";
        btn.disabled = false;
    }
}